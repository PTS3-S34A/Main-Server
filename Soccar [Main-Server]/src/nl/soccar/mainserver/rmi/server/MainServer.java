package nl.soccar.mainserver.rmi.server;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.soccar.library.SessionData;
import nl.soccar.mainserver.data.context.StatisticsMySqlContext;
import nl.soccar.mainserver.data.context.UserMySqlContext;
import nl.soccar.mainserver.data.repository.StatisticsRepository;
import nl.soccar.mainserver.data.repository.UserRepository;
import org.slf4j.LoggerFactory;

/**
 * Abstract class that serves as base for all MainServer implementations. It
 * provides the data repositories that are used for manipulation of data in the
 * used persistency service.
 *
 * @author PTS34A
 */
public abstract class MainServer extends UnicastRemoteObject {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MainServer.class);

    private final List<SessionData> sessions;
    private final UserRepository userRepository;
    private final StatisticsRepository statisticsRepository;

    /**
     * Constructor that serves as base for all MainServer implementations. It
     * instantiates the data repositories with the MySQL data context.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    public MainServer() throws RemoteException {
        sessions = new ArrayList<>();
        userRepository = new UserRepository(new UserMySqlContext());
        statisticsRepository = new StatisticsRepository(new StatisticsMySqlContext());
    }

    /**
     * Unexports this RMI-sub and closes the data repositories.
     */
    public void close() {
        try {
            UnicastRemoteObject.unexportObject(this, true);
            LOGGER.info("Unregistered " + this.getClass().getSimpleName() + " binding.");

            userRepository.close();
            statisticsRepository.close();
        } catch (NoSuchObjectException e) {
            LOGGER.error("Server could not be unexported.", e);
        }
    }

    public List<SessionData> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    /**
     * Gets the user data repository.
     *
     * @return The user data repository.
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Gets the statistics data repository.
     *
     * @return The statistics data repository.
     */
    public StatisticsRepository getStatisticsRepository() {
        return statisticsRepository;
    }

}
