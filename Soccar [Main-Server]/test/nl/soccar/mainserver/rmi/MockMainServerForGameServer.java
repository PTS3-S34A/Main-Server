package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import nl.soccar.library.SessionData;
import nl.soccar.library.enumeration.Privilege;
import nl.soccar.rmi.interfaces.IGameServerForMainServer;
import nl.soccar.rmi.interfaces.IMainServerForGameServer;

/**
 *
 * @author PTS34A
 */
public class MockMainServerForGameServer implements IMainServerForGameServer {

    private int gameServers;
    private int sessions;

    private int goals;
    private int assists;
    private int gamesWon;
    private int gamesLost;
    private int gamesPlayed;

    private String host = "defaulthost";

    private int sessionOccupancy;

    @Override
    public void register(IGameServerForMainServer gameServer) throws RemoteException {
        gameServers++;
    }

    @Override
    public void deregister(IGameServerForMainServer gameServer) throws RemoteException {
        gameServers--;
    }

    @Override
    public void sessionCreated(IGameServerForMainServer gameServer, SessionData sessionData) throws RemoteException {
        sessions++;
    }

    @Override
    public void sessionDestroyed(IGameServerForMainServer gameServer, String roomName) throws RemoteException {
        sessions--;
    }

    @Override
    public void hostChanged(IGameServerForMainServer gameServer, String roomName, String newHostName) throws RemoteException {
        host = newHostName;
    }

    @Override
    public void increaseSessionOccupancy(IGameServerForMainServer gameServer, String roomName) throws RemoteException {
        sessionOccupancy++;
    }

    @Override
    public void decreaseSessionOccupancy(IGameServerForMainServer gameServer, String roomName) throws RemoteException {
        sessionOccupancy--;
    }

    @Override
    public void addGoals(String username, int goals) throws RemoteException {
        this.goals += goals;
    }

    @Override
    public void addAssists(String username, int assists) throws RemoteException {
        this.assists += assists;
    }

    @Override
    public void incrementGamesWon(String username) throws RemoteException {
        gamesWon++;
    }

    @Override
    public void incrementGamesLost(String username) throws RemoteException {
        gamesLost++;
    }

    @Override
    public void incrementGamesPlayed(String username) throws RemoteException {
        gamesPlayed++;
    }

    @Override
    public Privilege getPrivilege(String username) throws RemoteException {
        return Privilege.NORMAL;
    }

    public int getGameServers() {
        return gameServers;
    }

    public int getSessions() {
        return sessions;
    }

    public int getGoals() {
        return goals;
    }

    public int getAssists() {
        return assists;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public String getHost() {
        return host;
    }

    public int getSessionOccupancy() {
        return sessionOccupancy;
    }

}
