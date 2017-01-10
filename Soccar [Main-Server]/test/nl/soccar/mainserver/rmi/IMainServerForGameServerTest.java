package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import nl.soccar.library.SessionData;
import nl.soccar.library.enumeration.Privilege;
import nl.soccar.rmi.interfaces.IMainServerForGameServer;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test that tests the nl.soccar.rmi.IMainServerForGameServer interface.
 * 
 * @author PTS34A
 */
public class IMainServerForGameServerTest {

    // Declaration of test object.
    private IMainServerForGameServer mainServerForGameServer;

    /**
     * Instantiation of test objects.
     */
    @Before
    public void setUp() {
        mainServerForGameServer = new MockMainServerForGameServer();
    }

    /**
     * Tests the register Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void registerTest() throws RemoteException {
        mainServerForGameServer.register(new MockGameServerForMainServer());
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getGameServers());
    }

    /**
     * Tests the deregister Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void deregisterTest() throws RemoteException {
        mainServerForGameServer.deregister(new MockGameServerForMainServer());
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(-1, mockMainServerForGameServer.getGameServers());
    }

    /**
     * Tests the sessionCreated Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void sessionCreatedTest() throws RemoteException {
        mainServerForGameServer.sessionCreated(new MockGameServerForMainServer(), new SessionData("127.0.0.1", "roomName", "hostName", true));
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getSessions());
    }

    /**
     * Tests the sessionDestroyed Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void sessionDestroyedTest() throws RemoteException {
        mainServerForGameServer.sessionDestroyed(new MockGameServerForMainServer(), "roomName");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(-1, mockMainServerForGameServer.getSessions());
    }

    /**
     * Tests the hostChanged Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void hostChangedTest() throws RemoteException {
        mainServerForGameServer.hostChanged(new MockGameServerForMainServer(), "roomName", "newHostName");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals("roomName", mockMainServerForGameServer.getHost());
    }

    /**
     * Tests the increaseSessionOccupancy Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void increaseSessionOccupancyTest() throws RemoteException {
        mainServerForGameServer.increaseSessionOccupancy(new MockGameServerForMainServer(), "roomName");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getSessionOccupancy());
    }

    /**
     * Tests the decreaseSessionOccupancy Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void decreaseSessionOccupancyTest() throws RemoteException {
        mainServerForGameServer.decreaseSessionOccupancy(new MockGameServerForMainServer(), "roomName");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(-1, mockMainServerForGameServer.getSessionOccupancy());
    }

    /**
     * Tests the addGoals Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void addGoalsTest() throws RemoteException {
        mainServerForGameServer.addGoals("username", 5);
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(6, mockMainServerForGameServer.getGoals());
    }

    /**
     * Tests the addAssists Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void addAssistsTest() throws RemoteException {
        mainServerForGameServer.addAssists("username", 5);
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(5, mockMainServerForGameServer.getAssists());
    }

    /**
     * Tests the incrementGamesWon Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void incrementGamesWonTest() throws RemoteException {
        mainServerForGameServer.incrementGamesWon("username");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getGamesWon());
    }

    /**
     * Tests the incrementGamesLost Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void incrementGamesLostTest() throws RemoteException {
        mainServerForGameServer.incrementGamesLost("username");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getGamesLost());
    }

    /**
     * Tests the incrementGamesPlayed Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void incrementGamesPlayedTest() throws RemoteException {
        mainServerForGameServer.incrementGamesPlayed("username");
        MockMainServerForGameServer mockMainServerForGameServer = (MockMainServerForGameServer) mainServerForGameServer;
        assertEquals(1, mockMainServerForGameServer.getGamesPlayed());
    }

    /**
     * Tests the getPrivilege Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void getPrivilegeTest() throws RemoteException {
        assertEquals(Privilege.NORMAL, mainServerForGameServer.getPrivilege("username"));
    }

}
