package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import nl.soccar.rmi.interfaces.IClientAuthenticated;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test that tests the nl.soccar.rmi.IClientAuthenticated interface.
 *
 * @author PTS34A
 */
public class IClientAuthenticatedTest {

    // Declaration of test object.
    private IClientAuthenticated clientAuthenticated;

    /**
     * Instantiation of test object.
     */
    @Before
    public void setUp() {
        clientAuthenticated = new MockMainServerForClient();
    }

    /**
     * Tests the getStatistics Method.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    @Test
    public void getStatisticsTest() throws RemoteException {
        assertNotNull(clientAuthenticated.getStatistics("username"));
    }

}
