package nl.soccar.mainserver.data.contract;

import java.util.List;
import nl.soccar.library.Statistics;

/**
 * Interface that describes the required methods for manipulation of statistics
 * in a persistency service.
 *
 * @author PTS34A
 */
public interface IStatisticsDataContract {

    /**
     * Adds a given amount of goals to the given player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of goals needs to
     * be increased.
     * @param goals The amount of goals that need to be added to the player.
     */
    void addGoals(String username, int goals);

    /**
     * Adds a given amount of assists to the given player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of assists needs
     * to be increased.
     * @param assists The amount of assists that need to be added to the player.
     */
    void addAssists(String username, int assists);

    /**
     * Increments the amount of games won by a player stored in the persistency
     * service.
     *
     * @param username The username of the player whose amount of won games
     * needs to be incremented.
     */
    void incrementGamesWon(String username);

    /**
     * Increments the amount of games lost by a player stored in the persistency
     * service.
     *
     * @param username The username of the player whose amount of lost games
     * needs to be incremented.
     */
    void incrementGamesLost(String username);

    /**
     * Increments the amount of games played by a player stored in the
     * persistency service.
     *
     * @param username The username of the player whose amount of played games
     * needs to be incremented.
     */
    void incrementGamesPlayed(String username);

    /**
     * Gets the game statistics of a player stored in the persistency service.
     *
     * @param username The username of the player whose statistics needs to be
     * retrieved from the persistency service.
     * @return The statistics of the player.
     */
    Statistics getStatistics(String username);

    /**
     * Gets the game statistics of all user from the persistency service.
     *
     * @return A collection of all game statistics.
     */
    List<Statistics> getAllStatistics();

}
