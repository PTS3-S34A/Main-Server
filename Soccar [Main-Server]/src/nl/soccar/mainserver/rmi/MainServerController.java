package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.soccar.exception.RogueGameServerException;
import nl.soccar.library.SessionData;
import nl.soccar.library.enumeration.BallType;
import nl.soccar.library.enumeration.Duration;
import nl.soccar.library.enumeration.MapType;
import nl.soccar.mainserver.data.context.StatisticsMySqlContext;
import nl.soccar.mainserver.data.context.UserMySqlContext;
import nl.soccar.mainserver.data.repository.StatisticsRepository;
import nl.soccar.mainserver.data.repository.UserRepository;
import nl.soccar.mainserver.util.DatabaseUtilities;
import nl.soccar.rmi.RmiConstants;
import nl.soccar.rmi.interfaces.IGameServerForMainServer;

/**
 * Abstract class that serves as base for all MainServerController
 * implementations. It provides the data repositories that are used for
 * manipulation of data in the used persistency service.
 *
 * @author PTS34A
 */
public final class MainServerController {

    private static final Logger LOGGER = Logger.getLogger(MainServerController.class.getSimpleName());

    private static final Random RANDOM = new Random();

    private static final int PING_INTERVAL = 10000; // in milliseconds

    private Timer timer;
    private Registry registry;
    private MainServerForClient mainServerForClient;
    private MainServerForGameServer mainServerForGameServer;

    private final List<IGameServerForMainServer> gameServers;
    private final Map<IGameServerForMainServer, List<SessionData>> sessions;
    private final UserRepository userRepository;
    private final StatisticsRepository statisticsRepository;

    /**
     * Constructor user for instantiation of a MainServerController object. The
     * database connection is initialized, the sessions collection, user
     * repository and statistics repository are being instantiated, the network
     * communication ports are being registered and stub-objects are created and
     * bound for RMI network communication.
     */
    public MainServerController() {
        DatabaseUtilities.init();

        timer = new Timer();
        gameServers = new ArrayList<>();
        sessions = new HashMap<>();
        userRepository = new UserRepository(new UserMySqlContext());
        statisticsRepository = new StatisticsRepository(new StatisticsMySqlContext());

        try {
            mainServerForClient = new MainServerForClient(this, userRepository, statisticsRepository);
            registry = LocateRegistry.createRegistry(RmiConstants.PORT_NUMBER_CLIENT);
            registry.rebind(RmiConstants.BINDING_NAME_MAIN_SERVER_FOR_CLIENT, mainServerForClient);
            LOGGER.info("Registered MainServerForClient binding.");

            mainServerForGameServer = new MainServerForGameServer(this, userRepository, statisticsRepository);
            registry = LocateRegistry.createRegistry(RmiConstants.PORT_NUMBER_GAME_SERVER);
            registry.rebind(RmiConstants.BINDING_NAME_MAIN_SERVER_FOR_GAME_SERVER, mainServerForGameServer);
            LOGGER.info("Registered MainServerForGameServer binding.");

        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while locating and/or binding the registry.", e);
        }

        continuouslyPingGameServers();
    }

    /**
     * Continously pings all game servers to check if the RMI connection is
     * active. A random value is sent to the game servers for them to process
     * with a pre-defined formula. If the result of the formula is not correct,
     * the connected game server is possibly rogue. If the result of the formula
     * is not correct or there was no response at all, the game server is
     * deregistered.
     */
    public void continuouslyPingGameServers() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (gameServers) {
                    List<IGameServerForMainServer> serversToRemove = new ArrayList<>();

                    gameServers.forEach(server -> {
                        try {
                            int pingCalculationValue = RANDOM.nextInt(100);
                            if (pingCalculationValue * RmiConstants.PING_CALCULATION_FACTOR != server.ping(pingCalculationValue)) {
                                throw new RogueGameServerException("Error", "The connected game server did not return the correct result of the predefined ping formula.");
                            }
                        } catch (RemoteException e) {
                            serversToRemove.add(server);

                            LOGGER.log(Level.WARNING, "A game server is deregistered because it is no longer active.", e);
                        } catch (RogueGameServerException e) {
                            serversToRemove.add(server);

                            LOGGER.log(Level.WARNING, "A game server is deregisterd because it did not return the correct result of the predefined ping formula.", e);
                        }
                    });

                    serversToRemove.stream().forEach(s -> deregisterGameServer(s));
                }
            }
        }, 0, PING_INTERVAL);
    }

    /**
     * Stops the ping timer, unexports the RMI-stubs and closes the database
     * connection.
     */
    public void close() {
        timer.cancel();
        mainServerForClient.close();
        mainServerForGameServer.close();
        DatabaseUtilities.close();
    }

    public void registerGameServer(IGameServerForMainServer gameServer) {
        synchronized (gameServers) {
            if (gameServers.contains(gameServer)) {
                return;
            }

            gameServers.add(gameServer);
        }

        List<SessionData> list = new ArrayList<>();
        synchronized (sessions) {
            sessions.put(gameServer, list);
        }

        LOGGER.info("Game server registered.");
    }

    public void deregisterGameServer(IGameServerForMainServer gameServer) {
        synchronized (gameServers) {
            gameServers.remove(gameServer);
        }

        synchronized (sessions) {
            sessions.remove(gameServer);
        }

        LOGGER.info("Game server deregistered.");
    }

    public void sessionCreated(IGameServerForMainServer gameServer, SessionData sessionData) {
        synchronized (sessions) {
            sessions.get(gameServer).add(sessionData);
        }

        LOGGER.log(Level.INFO, "Session {0} added.", sessionData.getRoomName());
    }

    public void sessionDestroyed(IGameServerForMainServer gameServer, String roomName) {
        synchronized (sessions) {
            List<SessionData> data = sessions.get(gameServer);

            Optional<SessionData> sessionData = data.stream().filter(s -> s.getRoomName().equals(roomName)).findFirst();
            if (!sessionData.isPresent()) {
                return;
            }

            data.remove(sessionData.get());
        }

        LOGGER.log(Level.INFO, "Session {0} destroyed.", roomName);
    }

    public void hostChanged(IGameServerForMainServer gameServer, String roomName, String newHostName) {
        synchronized (sessions) {
            List<SessionData> data = sessions.get(gameServer);

            Optional<SessionData> sessionData = data.stream().filter(s -> s.getRoomName().equals(roomName)).findFirst();
            if (!sessionData.isPresent()) {
                return;
            }

            SessionData s = sessionData.get();
            s.setHostName(newHostName);
        }

        LOGGER.log(Level.INFO, "Host of room {0} changed to {1}.", new Object[]{roomName, newHostName});
    }

    public void increaseSessionOccupancy(IGameServerForMainServer gameServer, String roomName) {
        synchronized (sessions) {
            List<SessionData> data = sessions.get(gameServer);

            Optional<SessionData> sessionData = data.stream().filter(s -> s.getRoomName().equals(roomName)).findFirst();
            if (!sessionData.isPresent()) {
                return;
            }

            SessionData s = sessionData.get();
            int occupation = s.getOccupation();
            if (s.getCapacity() > occupation) {
                s.setOccupation(occupation + 1);
            }
        }

        LOGGER.log(Level.INFO, "Occupancy for room {0} increased.", roomName);
    }

    public void decreaseSessionOccupancy(IGameServerForMainServer gameServer, String roomName) {
        synchronized (sessions) {
            List<SessionData> data = sessions.get(gameServer);

            Optional<SessionData> sessionData = data.stream().filter(s -> s.getRoomName().equals(roomName)).findFirst();
            if (!sessionData.isPresent()) {
                return;
            }

            SessionData s = sessionData.get();
            int occupation = s.getOccupation();
            if (occupation > 0) {
                s.setOccupation(occupation - 1);
            }
        }

        LOGGER.log(Level.INFO, "Occupancy for room {0} decreased.", roomName);
    }

    public boolean createSession(String name, String password, String hostName, int capacity, Duration duration, MapType mapType, BallType ballType) throws RemoteException {
        synchronized (sessions) {
            for (List<SessionData> sessions : this.sessions.values()) {
                for (SessionData session : sessions) {
                    if (session.getRoomName().equals(name)) {
                        LOGGER.log(Level.INFO, "Session ({0}) is not created because the room name already exists.", name);

                        return false;
                    }
                }
            }
        }

        long maxAvailableMemory = Integer.MIN_VALUE;
        IGameServerForMainServer server = null;

        for (IGameServerForMainServer gameServer : gameServers) {
            long availableMemory = gameServer.getAvailableMemory();
            if (availableMemory > maxAvailableMemory) {
                maxAvailableMemory = availableMemory;
                server = gameServer;
            }
        }

        LOGGER.log(Level.INFO, "Session ({0}) created.", name);

        return server == null ? false : server.createSession(name, password, hostName, capacity, duration, mapType, ballType);
    }

    /**
     * Gets the collection of all sessions (synchronized).
     *
     * @return A collection of all sessions (synchronized).
     */
    public List<SessionData> getSessions() {
        List<SessionData> list = new ArrayList<>();

        synchronized (gameServers) {
            synchronized (sessions) {
                gameServers.stream().map(sessions::get).forEach(list::addAll);
            }
        }

        return list;
    }

}
