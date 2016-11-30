package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.soccar.library.SessionData;
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
public class MainServerController {

    private static final Logger LOGGER = Logger.getLogger(MainServerController.class.getSimpleName());

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
    }

    /**
     * Unexports the RMI-stubs and closes the database connection.
     */
    public void close() {
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
