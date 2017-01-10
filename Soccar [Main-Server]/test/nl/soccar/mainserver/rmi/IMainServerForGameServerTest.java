package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import nl.soccar.library.SessionData;
import nl.soccar.library.enumeration.Privilege;
import nl.soccar.rmi.interfaces.IMainServerForGameServer;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author PTS34A
 */
public class IMainServerForGameServerTest {

    private IMainServerForGameServer mainServerForGameServer;

    @Before
    public void setUp() {
        mainServerForGameServer = new MockMainServerForGameServer();
    }

    @Test
    public void registerTest() throws RemoteException {
        mainServerForGameServer.register(new MockGameServerForMainServer());
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getGameServers());
    }

    @Test
    public void deregisterTest() throws RemoteException {
        mainServerForGameServer.deregister(new MockGameServerForMainServer());
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(-1, mockMainServerForGameServer.getGameServers());
    }

    @Test
    public void sessionCreatedTest() throws RemoteException {
        mainServerForGameServer.sessionCreated(new MockGameServerForMainServer(), new SessionData("127.0.0.1", "roomName", "hostName", true));
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getSessions());
    }

    @Test
    public void sessionDestroyedTest() throws RemoteException {
        mainServerForGameServer.sessionDestroyed(new MockGameServerForMainServer(), "roomName");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(-1, mockMainServerForGameServer.getSessions());
    }

    @Test
    public void hostChangedTest() throws RemoteException {
        mainServerForGameServer.hostChanged(new MockGameServerForMainServer(), "roomName", "newHostName");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals("roomName", mockMainServerForGameServer.getHost());
    }

    @Test
    public void increaseSessionOccupancyTest() throws RemoteException {
        mainServerForGameServer.increaseSessionOccupancy(new MockGameServerForMainServer(), "roomName");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getSessionOccupancy());
    }

    @Test
    public void decreaseSessionOccupancyTest() throws RemoteException {
        mainServerForGameServer.decreaseSessionOccupancy(new MockGameServerForMainServer(), "roomName");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(-1, mockMainServerForGameServer.getSessionOccupancy());
    }

    @Test
    public void addGoalsTest() throws RemoteException {
        mainServerForGameServer.addGoals("username", 5);
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(6, mockMainServerForGameServer.getGoals());
    }

    @Test
    public void addAssistsTest() throws RemoteException {
        mainServerForGameServer.addAssists("username", 5);
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(5, mockMainServerForGameServer.getAssists());
    }

    @Test
    public void incrementGamesWonTest() throws RemoteException {
        mainServerForGameServer.incrementGamesWon("username");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getGamesWon());
    }

    @Test
    public void incrementGamesLostTest() throws RemoteException {
        mainServerForGameServer.incrementGamesLost("username");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getGamesLost());
    }

    @Test
    public void incrementGamesPlayedTest() throws RemoteException {
        mainServerForGameServer.incrementGamesPlayed("username");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getGamesPlayed());
    }

    @Test
    public void getPrivilegeTest() throws RemoteException {
        assertEquals(Privilege.NORMAL, mainServerForGameServer.getPrivilege("username"));
    }

}
