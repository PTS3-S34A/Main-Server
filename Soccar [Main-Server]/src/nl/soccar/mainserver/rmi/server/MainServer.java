package nl.soccar.mainserver.rmi.server;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.soccar.library.SessionData;
import nl.soccar.mainserver.data.repository.StatisticsRepository;
import nl.soccar.mainserver.data.repository.UserRepository;

/**
 *
 * @author PTS34A
 */
public abstract class MainServer extends UnicastRemoteObject {

    private static final Logger LOGGER = Logger.getLogger(MainServer.class.getSimpleName());

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
            LOGGER.log(Level.INFO, "Unregistered {0} binding.", this.getClass().getSimpleName());
        } catch (NoSuchObjectException e) {
            LOGGER.log(Level.SEVERE, "Server could not be unexported.", e);
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
