package nl.soccar.mainserver.rmi.server;

import java.rmi.RemoteException;
import nl.soccar.rmi.interfaces.IGameServer;

/**
 * A MainServerForGameServer is an RMI-stub object used by game servers that
 * realizes the RMI-methods provided by the IGameServer interface.
 *
 * @author PTS34A
 */
public class MainServerForGameServer extends MainServer implements IGameServer {

    /**
     * Constructor used for instantiation of a MainServerForGameServer object.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    public MainServerForGameServer() throws RemoteException {
        // Empty because the Sessions ArrayList and data repositories are instantiated in the MainServer superclass.
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
