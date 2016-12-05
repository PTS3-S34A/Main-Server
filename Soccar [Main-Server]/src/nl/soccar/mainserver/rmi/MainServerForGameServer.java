package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import nl.soccar.library.SessionData;
import nl.soccar.mainserver.data.repository.StatisticsRepository;
import nl.soccar.mainserver.data.repository.UserRepository;
import nl.soccar.rmi.interfaces.IGameServerForMainServer;
import nl.soccar.rmi.interfaces.IMainServerForGameServer;

/**
 * A MainServerForGameServer is an RMI-stub object used by game servers that
 * realizes the RMI-methods provided by the IMainServerForGameServer interface.
 *
 * @author PTS34A
 */
public class MainServerForGameServer extends GeneralMainServer implements IMainServerForGameServer {

    /**
     * Constructor used for instantiation of a MainServerForGameServer object.
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
    public MainServerForGameServer(MainServerController controller, UserRepository userRepository, StatisticsRepository statisticsRepository) throws RemoteException {
        super(controller, userRepository, statisticsRepository);
    }

    @Override
    public void register(IGameServerForMainServer gameServer) throws RemoteException {
        super.getController().registerGameServer(gameServer);
    }

    @Override
    public void deregister(IGameServerForMainServer gameServer) throws RemoteException {
        super.getController().deregisterGameServer(gameServer);
    }

    @Override
    public void sessionCreated(IGameServerForMainServer gamsServer, SessionData sessionData) throws RemoteException {
        super.getController().sessionCreated(gamsServer, sessionData);
    }

    @Override
    public void sessionDestroyed(IGameServerForMainServer gameServer, SessionData sessionData) throws RemoteException {
        super.getController().sessionDestroyed(gameServer, sessionData);
    }

    @Override
    public void increaseSessionOccupancy(IGameServerForMainServer gameServer, SessionData sessionData) throws RemoteException {
        super.getController().increaseSessionOccupancy(gameServer, sessionData);
    }

    @Override
    public void decreaseSessionOccupancy(IGameServerForMainServer gameServer, SessionData sessionData) throws RemoteException {
        super.getController().decreaseSessionOccupancy(gameServer, sessionData);
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
