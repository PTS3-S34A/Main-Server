package nl.soccar.mainserver.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class that database access provides methods for initializing, closing
 * and querying the database.
 *
 * @author PTS34A
 */
public class DatabaseUtilities {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtilities.class);

    private static Connection connection;

    /**
     * Constructor that is intentionally marked private so a DatabaseUtilities
     * object can never be initiated outside this class.
     */
    private DatabaseUtilities() {
    }

    /**
     * Initializes the database connection based on the connection settings in
     * the database.properties file.
     */
    public static void init() {
        Properties props = new Properties();

        try (FileInputStream input = new FileInputStream("database.prop")) {
            props.load(input);
        } catch (IOException e) {
            LOGGER.error("An error occurred while loading the database properties file.", e);
        }

        try {
            connection = DriverManager.getConnection(
                    String.format("%s:%s://%s/%s?useSSL=false&connectTimeout=0&socketTimeout=0&autoReconnect=true",
                            props.getProperty("protocol"),
                            props.getProperty("system"),
                            props.getProperty("server"),
                            props.getProperty("database")),
                    props.getProperty("user"),
                    props.getProperty("password"));
            LOGGER.info("Database connection established.");
        } catch (SQLException e) {
            LOGGER.error("An error occurred while establishing the database connection.", e);
        }
    }

    /**
     * Closes the database connection if it's initialized.
     */
    public static void close() {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Database connection closed.");
            } catch (SQLException e) {
                LOGGER.error("An error occurred while closing the database connection.", e);
            }
        }
    }

    /**
     * Method that prepares an SQL query call to the database using the active
     * database connection.
     *
     * @param query The SQL query call that needs to be prepared.
     * @return The prepared CallableStatement that can be set and executed.
     * @throws SQLException The exception that can be thrown when the SQL query
     * fails.
     */
    public static CallableStatement prepareCall(String query) throws SQLException {
        return connection.prepareCall(query);
    }

    /**
     * Method that prepares an SQL query statement to the database using the
     * active database connection.
     *
     * @param query The SQL query statement that needs to be prepared.
     * @return The prepared PreparedStatement that can be set and executed.
     * @throws SQLException The exception that can be thrown when the SQL query
     * fails.
     */
    public static PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

}
