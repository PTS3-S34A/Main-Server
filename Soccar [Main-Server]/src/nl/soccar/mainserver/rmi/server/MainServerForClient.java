package nl.soccar.mainserver.rmi.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import nl.soccar.library.Statistics;
import nl.soccar.rmi.interfaces.IClientAuthenticated;
import nl.soccar.rmi.interfaces.IClientUnauthenticated;

/**
 * A MainServerForClient is an RMI-stub object used by clients that realizes the
 * RMI-methods provided by the IClientUnauthenticated and IClientAuthenticated
 * interfaces.
 *
 * @author PTS34A
 */
public class MainServerForClient extends MainServer implements IClientUnauthenticated, IClientAuthenticated {

    /**
     * Constructor used for instantiation of a MainServerForClient object.
     *
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    public MainServerForClient() throws RemoteException {
    }

    @Override
    public boolean add(String username, byte[] password) throws RemoteException {
        return super.getUserRepository().add(username, password);
    }

    @Override
    public boolean checkIfExists(String username) throws RemoteException {
        return super.getUserRepository().checkIfExists(username);
    }

    @Override
    public IClientAuthenticated checkPassword(String username, byte[] hashedPassword) throws RemoteException {
        if (super.getUserRepository().checkPassword(username, hashedPassword)) {
            return this;
        }
        return null;
    }

    @Override
    public Statistics getStatistics(String username) throws RemoteException {
        return super.getStatisticsRepository().getStatistics(username);
    }

    @Override
    public ArrayList<Statistics> getAllStatistics() throws RemoteException {
        return super.getStatisticsRepository().getAllStatistics();
    }

}
