package nl.soccar.mainserver.rmi.server;

import java.rmi.RemoteException;
import java.util.List;
import nl.soccar.library.SessionData;
import nl.soccar.mainserver.data.repository.StatisticsRepository;
import nl.soccar.mainserver.data.repository.UserRepository;
import nl.soccar.rmi.interfaces.IMainServerForGameServer;

/**
 * A MainServerForGameServer is an RMI-stub object used by game servers that
 realizes the RMI-methods provided by the IMainServerForGameServer interface.
 *
 * @author PTS34A
 */
public class MainServerForGameServer extends MainServer implements IMainServerForGameServer {

    /**
     * Constructor used for instantiation of a MainServerForGameServer object.
     *
     * @param sessions The collection of all SessionData objects.
     * @param userRepository the UserRepository that is used for manipulation of
     * user data in the persistency service.
     * @param statisticsRepository the StatisticsRepository that is used for
     * manipulation of statistics data in the persistency service.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    public MainServerForGameServer(List<SessionData> sessions, UserRepository userRepository, StatisticsRepository statisticsRepository) throws RemoteException {
        super(sessions, userRepository, statisticsRepository);
    }

    @Override
    public void addGoals(String username, int goals) throws RemoteException {
        super.getStatisticsRepository().addGoals(username, goals);
    }

    @Override
    public void addAssists(String username, int assists) throws RemoteException {
        super.getStatisticsRepository().addAssists(username, assists);
    }

    @Override
    public void incrementGamesWon(String username) throws RemoteException {
        super.getStatisticsRepository().incrementGamesWon(username);
    }

    @Override
    public void incrementGamesLost(String username) throws RemoteException {
        super.getStatisticsRepository().incrementGamesLost(username);
    }

    @Override
    public void incrementGamesPlayed(String username) throws RemoteException {
        super.getStatisticsRepository().incrementGamesPlayed(username);
    }

}
