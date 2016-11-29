package nl.soccar.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import nl.soccar.library.Statistics;

/**
 * Interface that specifies the methods that make it possible for a
 * authenticated remote client to communicate with the Main server through
 * RMI-communication.
 *
 * @author PTS34A
 */
public interface IClientAuthenticated extends Remote {

    /**
     * Gets the game statistics of a player stored in the persistency service.
     *
     * @param username The username of the player whose statistics needs to be
     * retrieved from the persistency service.
     * @return The statistics of the player.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    Statistics getStatistics(String username) throws RemoteException;

}
