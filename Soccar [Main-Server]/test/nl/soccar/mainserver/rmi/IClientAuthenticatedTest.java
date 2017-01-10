package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import nl.soccar.rmi.interfaces.IClientAuthenticated;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author PTS34A
 */
public class IClientAuthenticatedTest {

    private IClientAuthenticated clientAuthenticated;

    @Before
    public void setUp() {
        clientAuthenticated = new MockMainServerForClient();
    }

    @Test
    public void getStatisticsTest() throws RemoteException {
        Assert.assertNotNull(clientAuthenticated.getStatistics("username"));
    }

}
