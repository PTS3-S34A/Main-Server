/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author PTS34A
 */
public class IClientUnauthenticatedTest {
    
    private IClientUnauthenticated clientUnauthenticated;
    
    @Before
    public void setUp() {
        clientUnauthenticated = new MockMainServerForClient();
    }
    
    @Test
    public void addTest() throws RemoteException {
        assertTrue(clientUnauthenticated.add("username", new String("password").getBytes()));
    }
    
    @Test
    public void checkIfExistsTest() throws RemoteException {
        assertTrue(clientUnauthenticated.checkIfExists("username"));
    }
    
    @Test
    public void checkPasswordTest() throws RemoteException {
        assertNotNull(clientUnauthenticated.checkPassword("username", new String("password").getBytes()));
    }
    
    @Test
    public void getAllStatisticsTest() throws RemoteException {
        assertNotNull(clientUnauthenticated.getAllStatistics());
    }
    
    @Test
    public void getAllSessionsTest() throws RemoteException {
        assertNotNull(clientUnauthenticated.getAllSessions());
    }
    
    @Test
    public void createSessionTest() throws RemoteException {
        assertTrue(clientUnauthenticated.createSession("name", "password", "hostname", 0, Duration.MINUTES_3, MapType.ICE, BallType.FOOTBALL));
    }
    
}
