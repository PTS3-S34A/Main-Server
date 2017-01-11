package nl.soccar.mainserver.rmi;

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

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final List<IGameServerForMainServer> gameServers;
    private final Map<IGameServerForMainServer, List<SessionData>> sessions;
    private final UserRepository userRepository;
    private final StatisticsRepository statisticsRepository;
    private Timer timer;
    private Registry registry;
    private MainServerForClient mainServerForClient;
    private MainServerForGameServer mainServerForGameServer;

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
     * active.
     */
    public void continuouslyPingGameServers() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pingGameServers();
            }
        }, 0, PING_INTERVAL);
    }

    /**
     * Pings all game servers to check if the RMI connection is active. A random
     * value is sent to the game servers for them to process with a pre-defined
     * formula. If the result of the formula is not correct, the connected game
     * server is possibly rogue. If the result of the formula is not correct or
     * there was no response at all, the game server is deregistered.
     */
    private void pingGameServers() {
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

    /**
     * Stops the ping timer, unexports the RMI-stubs and closes the database
     * connection.
     */
    public void close() {
        timer.cancel();
        timer.purge();
        timer = null;

        mainServerForClient.close();
        mainServerForGameServer.close();
        DatabaseUtilities.close();
    }

    /**
     * Notifies the Main server when a new game server is registered.
     *
     * @param gameServer The game server that is registered at the Main server.
     */
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

    /**
     * Notifies the Main server when a game server is deregistered.
     *
     * @param gameServer The game server that is deregistered at the Main
     * server.
     */
    public void deregisterGameServer(IGameServerForMainServer gameServer) {
        synchronized (gameServers) {
            gameServers.remove(gameServer);
        }

        synchronized (sessions) {
            sessions.remove(gameServer);
        }

        LOGGER.info("Game server deregistered.");
    }

    /**
     * Notifies the Main server when new session is created on a Game server.
     *
     * @param gameServer The game server on which the session is created.
     * @param sessionData The data of the session that is created.
     */
    public void sessionCreated(IGameServerForMainServer gameServer, SessionData sessionData) {
        synchronized (sessions) {
            sessions.get(gameServer).add(sessionData);
        }

        LOGGER.log(Level.INFO, "Session {0} added.", sessionData.getRoomName());
    }

    /**
     * Notifies the Main server when a session is terminated on a Game server.
     *
     * @param gameServer The game server on which the session is hosted.
     * @param roomName The name of the room that is terminated.
     */
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

    /**
     * Notifies the Main server when the host plauer leaves the session.
     *
     * @param gameServer The game server on which the session is hosted.
     * @param roomName The name of the room whose host player leaves.
     * @param newHostName The name of the new host player.
     */
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

    /**
     * Increases the occupancy of the given session on the Main server.
     *
     * @param gameServer The game server on which the session occupancy needs to
     * be increased.
     * @param roomName The name of the room whose occupancy is being increased.
     */
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

    /**
     * Decreases the occupancy of the given session on the Main server.
     *
     * @param gameServer The game server on which the session occupancy needs to
     * be decreased.
     * @param roomName The name of the room whose occupancy is being decreased.
     */
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

    /**
     * Creates a new game session on a remote Game server. The Game Server on
     * which the session will be created is chosen based on the amount of free
     * memory for the Java Virtual Machine.
     *
     * @param name The roomname of the session that is being created.
     * @param password The password of the session that is being created.
     * @param hostName The username of the player that created the session.
     * @param capacity The player capacity of the session that is being created.
     * @param duration The game duration of the session that is being created.
     * @param mapType The maptype of the session that is being created.
     * @param ballType The balltype of the session that is being created.
     * @return True if the session is created successfully.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    public boolean createSession(String name, String password, String hostName, int capacity, Duration duration, MapType mapType, BallType ballType) throws RemoteException {
        synchronized (sessions) {
            for (List<SessionData> allSessions : this.sessions.values()) {
                for (SessionData session : allSessions) {
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

        return server != null && server.createSession(name, password, hostName, capacity, duration, mapType, ballType);
    }

    /**
     * Gets all running sessions.
     *
     * @return a collection of all running sessions.
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
