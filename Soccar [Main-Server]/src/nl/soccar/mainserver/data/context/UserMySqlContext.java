package nl.soccar.mainserver.data.context;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.soccar.library.enumeration.Privilege;
import nl.soccar.mainserver.util.DatabaseUtilities;
import nl.soccar.mainserver.util.PasswordUtilities;
import nl.soccar.mainserver.data.contract.IUserDataContract;

/**
 * Class that implements all methods prescribed in the IUserDataContract
 * interface for manupulation of users in a MySQL database context.
 *
 * @author PTS34A
 */
public class UserMySqlContext implements IUserDataContract {

    private static final Logger LOGGER = Logger.getLogger(UserMySqlContext.class.getSimpleName());

    @Override
    public boolean add(String username, byte[] password) {
        byte[] salt = PasswordUtilities.generateSalt();
        byte[] hash = PasswordUtilities.addSalt(password, salt);

        try (CallableStatement cs = DatabaseUtilities.prepareCall("{call add_user(?, ?, ?)}")) {
            cs.setString(1, username);
            cs.setBytes(2, hash);
            cs.setBytes(3, salt);
            cs.execute();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while adding a new user to the database. It is possible that this happened because of a database constaint that prevents duplicate usernames.", e);
        }

        return false;
    }

    @Override
    public void changePrivilege(String username, Privilege privilege) {
        try (CallableStatement cs = DatabaseUtilities.prepareCall("{call change_privilege(?, ?)}")) {
            cs.setString(1, username);
            cs.setString(2, privilege.name());
            cs.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while changing the privilege of a user in the database.", e);
        }
    }

    @Override
    public boolean checkIfExists(String username) {
        try (CallableStatement cs = DatabaseUtilities.prepareCall("{? = call check_user_exists(?)}")) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setString(2, username);
            cs.execute();
            return cs.getBoolean(1);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while checking if a uses exists in the database.", e);
        }

        return false;
    }

    @Override
    public boolean checkPassword(String username, byte[] password) {
        byte[] storedHash = new byte[64];
        byte[] salt = new byte[16];

        try (PreparedStatement ps = DatabaseUtilities.prepareStatement("SELECT password, password_salt FROM User WHERE username = ?")) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    storedHash = rs.getBytes("password");
                    salt = rs.getBytes("password_salt");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while authenticating a user.", e);
        }

        byte[] hash = PasswordUtilities.addSalt(password, salt);
        return Arrays.equals(hash, storedHash);
    }

    @Override
    public Privilege getPrivilege(String username) {
        Privilege privilege = Privilege.NORMAL;

        try (CallableStatement cs = DatabaseUtilities.prepareCall("{? = call get_privilege(?)}")) {
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setString(2, username);
            cs.execute();
            privilege = Privilege.valueOf(cs.getString(1));
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while retrieving the privilege of a user from the database.", e);
        }

        return privilege;
    }

}
