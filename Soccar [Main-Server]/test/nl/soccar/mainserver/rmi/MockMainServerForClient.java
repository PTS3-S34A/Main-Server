package nl.soccar.mainserver.rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import nl.soccar.library.SessionData;
import nl.soccar.library.Statistics;
import nl.soccar.library.enumeration.BallType;
import nl.soccar.library.enumeration.Duration;
import nl.soccar.library.enumeration.MapType;
import nl.soccar.rmi.interfaces.IClientAuthenticated;
import nl.soccar.rmi.interfaces.IClientUnauthenticated;

/**
 *
 * @author PTS34A
 */
public class MockMainServerForClient implements IClientAuthenticated, IClientUnauthenticated {

    @Override
    public Statistics getStatistics(String username) throws RemoteException {
        return new Statistics("username", 0, 0, 0, 0, 0);
    }

    @Override
    public boolean add(String username, byte[] password) throws RemoteException {
        return true;
    }

    @Override
    public boolean checkIfExists(String username) throws RemoteException {
        return true;
    }

    @Override
    public IClientAuthenticated checkPassword(String username, byte[] hashedPassword) throws RemoteException {
        return this;
    }

    @Override
    public List<Statistics> getAllStatistics() throws RemoteException {
        return new ArrayList<Statistics>();
    }

    @Override
    public List<SessionData> getAllSessions() throws RemoteException {
        return new ArrayList<SessionData>();
    }

    @Override
    public boolean createSession(String name, String password, String hostName, int capacity, Duration duration, MapType mapType, BallType ballType) throws RemoteException {
        return true;
    }
    
}
