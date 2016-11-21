package nl.soccar.mainserver.data.repository;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import nl.soccar.library.enumeration.Privilege;
import nl.soccar.mainserver.data.contract.IUserDataContract;
import org.slf4j.LoggerFactory;

/**
 * A UserRepository object is used for manipulation of user data in a
 * persistence service.
 *
 * @author PTS34A
 */
public class UserRepository extends Repository {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private final IUserDataContract context;

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
    }

    /**
     * Method that adds a new user to the persistency service.
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
            LOGGER.warn("An error occurred while submitting a callable in the add method.", e);
            return false;
        }
    }

    /**
     * Method that changes the privilege of the given player stored in the
     * persistency service.
     *
     * @param username The username of the player whose privilege needs to be
     * changed.
     * @param privilege The privilege that needs to be set for the player with
     * the given username.
     */
    public void changePrivilege(String username, Privilege privilege) {
        super.getPool().execute(() -> {
            context.changePrivilege(username, privilege);
        });
    }

    /**
     * Method that checks if a player exists in the persistency service based on
     * the stored username.
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
            LOGGER.warn("An error occurred while submitting a callable in the checkIfExists method.", e);
            return false;
        }
    }

    /**
     * Method that checks if the given hashed password belongs to the given
     * username stored in the persistency service.
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
            LOGGER.warn("An error occurred while submitting a callable in the checkPassword method.", e);
            return false;
        }
    }

}
