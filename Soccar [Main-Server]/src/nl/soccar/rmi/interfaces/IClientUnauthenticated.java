package nl.soccar.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that specifies the methods that make it possible for a
 * unauthenticated remote client to communicate with the Main server through
 * RMI-communication.
 *
 * @author PTS34A
 */
public interface IClientUnauthenticated extends Remote {

    /**
     * Adds a new user to the persistency service.
     *
     * @param username The username of the new player.
     * @param password The password of the new player.
     * @return True when the username does not exist already and when player is
     * added succesfully to the persistency service.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    boolean add(String username, byte[] password) throws RemoteException;

    /**
     * Checks if a player exists in the persistency service based on the stored
     * username.
     *
     * @param username The username of the player that must be checked if it
     * exists in the persistency service.
     * @return True if the player exists.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    boolean checkIfExists(String username) throws RemoteException;

    /**
     * Checks if the given hashed password belongs to the given username stored
     * in the persistency service.
     *
     * @param username The username of the player that must be checked if it
     * mathces with the password.
     * @param hashedPassword The password that must be checked it is matches
     * with the username.
     * @return True if the password matches the username.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    IClientAuthenticated checkPassword(String username, byte[] hashedPassword) throws RemoteException;

}
