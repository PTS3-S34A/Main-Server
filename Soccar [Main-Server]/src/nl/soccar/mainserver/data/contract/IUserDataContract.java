package nl.soccar.mainserver.data.contract;

import nl.soccar.library.enumeration.Privilege;

/**
 * Interface that describes the required methods for manipulation of users in a
 * persistency service.
 *
 * @author PTS34A
 */
public interface IUserDataContract {

    /**
     * Adds a new user to the persistency service.
     *
     * @param username The username of the new player.
     * @param password The password of the new player.
     * @return True when the username does not exist already and when player is
     * added succesfully to the persistency service.
     */
    boolean add(String username, byte[] password);

    /**
     * Changes the privilege of the given player stored in the persistency
     * service.
     *
     * @param username The username of the player whose privilege needs to be
     * changed.
     * @param privilege The privilege that needs to be set for the player with
     * the given username.
     */
    void changePrivilege(String username, Privilege privilege);

    /**
     * Checks if a player exists in the persistency service based on the stored
     * username.
     *
     * @param username The username of the player that must be checked if it
     * exists in the persistency service.
     * @return True if the player exists.
     */
    boolean checkIfExists(String username);

    /**
     * Checks if the given hashed password belongs to the given username stored
     * in the persistency service.
     *
     * @param username The username of the player that must be checked if it
     * mathces with the password.
     * @param hashedPassword The password that must be checked it is matches
     * with the username.
     * @return True if the password matches the username.
     */
    boolean checkPassword(String username, byte[] hashedPassword);

}
