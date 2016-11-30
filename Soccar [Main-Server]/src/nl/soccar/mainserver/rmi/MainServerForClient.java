package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import java.util.List;
import nl.soccar.library.SessionData;
import nl.soccar.library.Statistics;
import nl.soccar.library.enumeration.BallType;
import nl.soccar.library.enumeration.Duration;
import nl.soccar.library.enumeration.MapType;
import nl.soccar.mainserver.data.repository.StatisticsRepository;
import nl.soccar.mainserver.data.repository.UserRepository;
import nl.soccar.rmi.interfaces.IClientAuthenticated;
import nl.soccar.rmi.interfaces.IClientUnauthenticated;

/**
 * A MainServerForClient is an RMI-stub object used by clients that realizes the
 * RMI-methods provided by the IClientUnauthenticated and IClientAuthenticated
 * interfaces.
 *
 * @author PTS34A
 */
public class MainServerForClient extends GeneralMainServer implements IClientUnauthenticated, IClientAuthenticated {

    /**
     * Constructor used for instantiation of a MainServerForClient object.
     *
     * @param controller The MainServerController that is used to retrieve the
     * list of sessions.
     * @param userRepository the UserRepository that is used for manipulation of
     * user data in the persistency service.
     * @param statisticsRepository the StatisticsRepository that is used for
     * manipulation of statistics data in the persistency service.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    public MainServerForClient(MainServerController controller, UserRepository userRepository, StatisticsRepository statisticsRepository) throws RemoteException {
        super(controller, userRepository, statisticsRepository);
    }

    // IClientUnauthenticated methods
    @Override
    public boolean add(String username, byte[] password) throws RemoteException {
        return super.getUserRepository().add(username, password);
    }

    @Override
    public boolean checkIfExists(String username) throws RemoteException {
        return super.getUserRepository().checkIfExists(username);
    }

    @Override
    public IClientAuthenticated checkPassword(String username, byte[] hashedPassword) throws RemoteException {
        if (super.getUserRepository().checkPassword(username, hashedPassword)) {
            return this;
        }
        return null;
    }

    @Override
    public List<Statistics> getAllStatistics() throws RemoteException {
        return super.getStatisticsRepository().getAllStatistics();
    }

    @Override
    public List<SessionData> getAllSessions() throws RemoteException {
        return super.getController().getSessions();
    }

    @Override
    public boolean createSession(String name, String password, int capacity, Duration duration, MapType mapType, BallType ballType) throws RemoteException {
        // TODO
        return false;
    }

    // IClientAuthenticated methods
    @Override
    public Statistics getStatistics(String username) throws RemoteException {
        return super.getStatisticsRepository().getStatistics(username);
    }

}
