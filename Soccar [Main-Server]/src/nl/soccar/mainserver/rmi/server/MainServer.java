package nl.soccar.mainserver.rmi.server;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import nl.soccar.mainserver.data.repository.StatisticsRepository;
import nl.soccar.mainserver.data.repository.UserRepository;
import nl.soccar.rmi.SessionData;
import org.slf4j.LoggerFactory;

/**
 *
 * @author PTS34A
 */
public abstract class MainServer extends UnicastRemoteObject {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MainServer.class);

    private final List<SessionData> sessions;
    private final UserRepository userRepository;
    private final StatisticsRepository statisticsRepository;

    /**
     * Constructor that serves as base for all MainServer implementations.
     *
     * @param sessions The collection of all SessionData objects.
     * @param userRepository the UserRepository that is used for manipulation of
     * user data in the persistency service.
     * @param statisticsRepository the StatisticsRepository that is used for
     * manipulation of statistics data in the persistency service.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    public MainServer(List<SessionData> sessions, UserRepository userRepository, StatisticsRepository statisticsRepository) throws RemoteException {
        this.sessions = sessions;
        this.userRepository = userRepository;
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * Unexports this RMI-stub object.
     */
    public void close() {
        try {
            UnicastRemoteObject.unexportObject(this, true);
            LOGGER.info("Unregistered " + this.getClass().getSimpleName() + " binding.");
        } catch (NoSuchObjectException e) {
            LOGGER.error("Server could not be unexported.", e);
        }
    }

    /**
     * Gets the collection of all SessionData objects.
     *
     * @return A collection of all SessionData objects.
     */
    public List<SessionData> getSessions() {
        return sessions;
    }

    /**
     * Gets the UserRepository that is used for manipulation of user data in the
     * persistency service.
     *
     * @return The user data repository.
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Gets the StatisticsRepository that is used for manipulation of statistics
     * data in the persistency service.
     *
     * @return The statistics data repository..
     */
    public StatisticsRepository getStatisticsRepository() {
        return statisticsRepository;
    }

}
