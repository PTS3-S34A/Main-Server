package nl.soccar.mainserver.data.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.soccar.library.enumeration.Privilege;
import nl.soccar.mainserver.data.contract.IUserDataContract;

/**
 * A UserRepository object is used for manipulation of user data in a
 * persistence service.
 *
 * @author PTS34A
 */
public class UserRepository extends Repository {

    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getSimpleName());

    private final IUserDataContract context;

    private final Map<String, Privilege> privileges;

    /**
     * Constructor used for initiation of a UserClientDataRepository object that
     * can be used for manipulation of user data in a persistence service that
     * is passed in as argument by a client.
     *
     * @param context The persistance service (context) that this repository
     * communicates with.
     */
    public UserRepository(IUserDataContract context) {
        this.context = context;

        privileges = new HashMap<>();
    }

    /**
     * Adds a new user to the persistency service.
     *
     * @param username The username of the new player.
     * @param password The password of the new player.
     * @return True when the username does not exist already and when player is
     * added succesfully to the persistency service.
     */
    public boolean add(String username, byte[] password) {
        try {
            Future<Boolean> f = super.getPool().submit(() -> context.add(username, password));
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.WARNING, "An error occurred while submitting a callable in the add method.", e);
        }
        return false;
    }

    /**
     * Changes the privilege of the given player stored in the persistency
     * service.
     *
     * @param username The username of the player whose privilege needs to be
     * changed.
     * @param privilege The privilege that needs to be set for the player with
     * the given username.
     */
    public void changePrivilege(String username, Privilege privilege) {
        super.getPool().execute(() -> context.changePrivilege(username, privilege));
    }

    /**
     * Checks if a player exists in the persistency service based on the stored
     * username.
     *
     * @param username The username of the player that must be checked if it
     * exists in the persistency service.
     * @return True if the player exists.
     */
    public boolean checkIfExists(String username) {
        try {
            Future<Boolean> f = super.getPool().submit(() -> context.checkIfExists(username));
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.WARNING, "An error occurred while submitting a callable in the checkIfExists method.", e);
        }
        return false;
    }

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
    public boolean checkPassword(String username, byte[] hashedPassword) {
        try {
            Future<Boolean> f = super.getPool().submit(() -> context.checkPassword(username, hashedPassword));
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.WARNING, "An error occurred while submitting a callable in the checkPassword method.", e);
        }
        return false;
    }

    /**
     * Retrieves the privilege of the user with the given username from the
     * persistency service.
     *
     * @param username The username of the user whose privilege is being
     * retrieved.
     * @return The privilege of the given user.
     */
    public Privilege getPrivilege(String username) {
        Privilege privilege = privileges.get(username);

        if (privilege == null) {
            try {
                Future<Privilege> f = super.getPool().submit(() -> context.getPrivilege(username));
                privilege = f.get();
                
                privileges.put(username, privilege);
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.log(Level.WARNING, "An error occurred while submitting a callable in the getPrivilege method.", e);
            }
        }

        return privilege;
    }

}
