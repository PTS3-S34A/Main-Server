package nl.soccar.rmi;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.soccar.library.SessionData;
import nl.soccar.mainserver.data.context.StatisticsMySqlContext;
import nl.soccar.mainserver.data.context.UserMySqlContext;
import nl.soccar.mainserver.data.repository.StatisticsRepository;
import nl.soccar.mainserver.data.repository.UserRepository;
import nl.soccar.mainserver.util.DatabaseUtilities;
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
    private IGameServerForMainServer gameServerForMainServer;

    private final List<SessionData> sessions;
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

        sessions = new ArrayList<>();
        userRepository = new UserRepository(new UserMySqlContext());
        statisticsRepository = new StatisticsRepository(new StatisticsMySqlContext());

        try {
            mainServerForClient = new MainServerForClient(this, userRepository, statisticsRepository, gameServerForMainServer);
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
     * Connects the main server to the game server(s) IP-address(es) that is/are
     * listed in the gameserver.properties file.
     *
     * @return True when the main server connected succesfully to the
     * games(servers) listed in the gameserver.properties file.
     */
    public boolean connectGameServers() {
        Properties props = new Properties();

        try (FileInputStream input = new FileInputStream("gameserver.prop")) {
            props.load(input);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while loading the gameerver properties file.", e);
            return false;
        }

        try {
            registry = LocateRegistry.getRegistry(props.getProperty("gameserver1"), RmiConstants.PORT_NUMBER_GAME_SERVER);
            gameServerForMainServer = (IGameServerForMainServer) registry.lookup(RmiConstants.BINDING_NAME_GAME_SERVER_FOR_MAIN_SERVER);
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while connecting to the Game server through RMI.", e);
            return false;
        }

        return true;
    }

    /**
     * Unexports the RMI-stubs and closes the database connection.
     */
    public void close() {
        mainServerForClient.close();
        mainServerForGameServer.close();
        DatabaseUtilities.close();
    }
    
    /**
     * Gets the collection of all sessions (synchronized).
     * 
     * @return A collection of all sessions (synchronized).
     */
    public List<SessionData> getSessions() {
        synchronized(sessions) {
            return sessions;
        }
    }

}
