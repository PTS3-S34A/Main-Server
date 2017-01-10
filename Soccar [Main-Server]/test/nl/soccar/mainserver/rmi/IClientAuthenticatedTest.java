package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import nl.soccar.rmi.interfaces.IClientAuthenticated;
import org.junit.Assert;
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
     * Instantiation of test objects.
     */
    @Before
    public void setUp() {
        clientAuthenticated = new MockMainServerForClient();
    }

    /**
     * Tests the getStatistics Method.
     * 
     * @throws RemoteException 
     */
    @Test
    public void getStatisticsTest() throws RemoteException {
        Assert.assertNotNull(clientAuthenticated.getStatistics("username"));
    }

}
