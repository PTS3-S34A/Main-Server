package nl.soccar.rmi.interfaces;

import java.rmi.RemoteException;
import nl.soccar.library.enumeration.BallType;
import nl.soccar.library.enumeration.Duration;
import nl.soccar.library.enumeration.MapType;

/**
 * Interface that specifies the methods that make it possible for a Main server
 * to communicate with the Game server through RMI-communication.
 *
 * @author PTS34A
 */
public interface IGameServerForMainServer {

    /**
     * Creates a new game session on the remote Game Server.
     *
     * @param name The roomname of the session that is being created.
     * @param password The password of the session that is being created.
     * @param capacity The player capacity of the session that is being created.
     * @param duration The game duration of the session that is being created.
     * @param mapType The maptype of the session that is being created.
     * @param ballType The balltype of the session that is being created.
     * @return True if the session is created successfully.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    boolean createSession(String name, String password, int capacity, Duration duration, MapType mapType, BallType ballType) throws RemoteException;

}
