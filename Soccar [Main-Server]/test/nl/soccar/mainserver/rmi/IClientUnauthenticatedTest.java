package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import nl.soccar.library.enumeration.BallType;
import nl.soccar.library.enumeration.Duration;
import nl.soccar.library.enumeration.MapType;
import nl.soccar.rmi.interfaces.IClientUnauthenticated;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test that tests the nl.soccar.rmi.IClientUnauthenticated interface.
 *
 * @author PTS34A
 */
public class IClientUnauthenticatedTest {

    // Declaration of test object.
    private IClientUnauthenticated clientUnauthenticated;

    /**
     * Instantiation of test objects.
     */
    @Before
    public void setUp() {
        clientUnauthenticated = new MockMainServerForClient();
    }

    /**
     * Tests the add Method.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    @Test
    public void addTest() throws RemoteException {
        assertTrue(clientUnauthenticated.add("username", new String("password").getBytes()));
    }

    /**
     * Tests the checkIfExists Method.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    @Test
    public void checkIfExistsTest() throws RemoteException {
        assertTrue(clientUnauthenticated.checkIfExists("username"));
    }

    /**
     * Tests the checkPassword method.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    @Test
    public void checkPasswordTest() throws RemoteException {
        assertNotNull(clientUnauthenticated.checkPassword("username", new String("password").getBytes()));
    }

    /**
     * Tests the getAllStatistics Method.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    @Test
    public void getAllStatisticsTest() throws RemoteException {
        assertNotNull(clientUnauthenticated.getAllStatistics());
    }

    /**
     * Tests the getAllSessions Method.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    @Test
    public void getAllSessionsTest() throws RemoteException {
        assertNotNull(clientUnauthenticated.getAllSessions());
    }

    /**
     * Tests the createSession Method.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    @Test
    public void createSessionTest() throws RemoteException {
        assertTrue(clientUnauthenticated.createSession("name", "password", "hostname", 0, Duration.MINUTES_3, MapType.ICE, BallType.FOOTBALL));
    }

}
