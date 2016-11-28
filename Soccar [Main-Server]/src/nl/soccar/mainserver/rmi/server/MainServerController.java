package nl.soccar.mainserver.rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import nl.soccar.mainserver.data.context.StatisticsMySqlContext;
import nl.soccar.mainserver.data.context.UserMySqlContext;
import nl.soccar.mainserver.data.repository.StatisticsRepository;
import nl.soccar.mainserver.data.repository.UserRepository;
import nl.soccar.mainserver.util.DatabaseUtilities;
import nl.soccar.rmi.RmiConstants;
import nl.soccar.rmi.SessionData;
import org.slf4j.LoggerFactory;

/**
 * Abstract class that serves as base for all MainServerController
 * implementations. It provides the data repositories that are used for
 * manipulation of data in the used persistency service.
 *
 * @author PTS34A
 */
public class MainServerController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MainServerController.class);

    private Registry r;
    private MainServerForClient mainServerForClient;
    private MainServerForGameServer mainServerForGameServer;

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
            mainServerForClient = new MainServerForClient(sessions, userRepository, statisticsRepository);
            r = LocateRegistry.createRegistry(RmiConstants.PORT_NUMBER_CLIENT);
            r.rebind(RmiConstants.BINDING_NAME_CLIENT, mainServerForClient);
            LOGGER.info("Registered MainServerForClient binding.");

            mainServerForGameServer = new MainServerForGameServer(sessions, userRepository, statisticsRepository);
            r = LocateRegistry.createRegistry(RmiConstants.PORT_NUMBER_GAME_SERVER);
            r.rebind(RmiConstants.BINDING_NAME_GAME_SERVER, mainServerForGameServer);
            LOGGER.info("Registered MainServerForGameServer binding.");

        } catch (RemoteException e) {
            LOGGER.error("An error occurred while locating and/or binding the registry.", e);
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

}
