package nl.soccar.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that specifies the methods that make it possible for a Game server
 * to communicate with the Main server through RMI-communication.
 *
 * @author PTS34A
 */
public interface IGameServer extends Remote {

    /**
     * Adds a given amount of goals to the given player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of goals needs to
     * be increased.
     * @param goals The amount of goals that need to be added to the player.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    void addGoals(String username, int goals) throws RemoteException;

    /**
     * Adds a given amount of assists to the given player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of assists needs
     * to be increased.
     * @param assists The amount of assists that need to be added to the player.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    void addAssists(String username, int assists) throws RemoteException;

    /**
     * Increments the amount of games won by a player stored in the persistency
     * service.
     *
     * @param username The username of the player whose amount of won games
     * needs to be incremented.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    void incrementGamesWon(String username) throws RemoteException;

    /**
     * Increments the amount of games lost by a player stored in the persistency
     * service.
     *
     * @param username The username of the player whose amount of lost games
     * needs to be incremented.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    void incrementGamesLost(String username) throws RemoteException;

    /**
     * Increments the amount of games played by a player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of played games
     * needs to be incremented.
     * @throws RemoteException Thrown when a communication error occurs during
     * the remote call of this method.
     */
    void incrementGamesPlayed(String username) throws RemoteException;

}
