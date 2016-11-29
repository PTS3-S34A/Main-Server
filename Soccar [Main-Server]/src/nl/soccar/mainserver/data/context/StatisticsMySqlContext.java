package nl.soccar.mainserver.data.context;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.soccar.mainserver.util.DatabaseUtilities;
import nl.soccar.library.Statistics;
import nl.soccar.mainserver.data.contract.IStatisticsDataContract;

/**
 * Class that implements all methods prescribed in the IStatisticsDataContract
 * interface for manupulation of statistics in a MySQL database context.
 *
 * @author PTS34A
 */
public class StatisticsMySqlContext implements IStatisticsDataContract {

    private static final Logger LOGGER = Logger.getLogger(StatisticsMySqlContext.class.getSimpleName());

    @Override
    public void addGoals(String username, int goals) {
        try {
            CallableStatement cs = DatabaseUtilities.prepareCall("{call add_goals(?, ?)}");
            cs.setString(1, username);
            cs.setInt(2, goals);
            cs.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while adding goals to a statistics record in the database.", e);
        }
    }

    @Override
    public void addAssists(String username, int assists) {
        try {
            CallableStatement cs = DatabaseUtilities.prepareCall("{call add_assists(?, ?)}");
            cs.setString(1, username);
            cs.setInt(2, assists);
            cs.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while adding assists to a statistics record in the database.", e);
        }
    }

    @Override
    public void incrementGamesWon(String username) {
        try {
            CallableStatement cs = DatabaseUtilities.prepareCall("{call increment_games_won(?)}");
            cs.setString(1, username);
            cs.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while incrementing the amount of games won in a statistics record in the database.", e);
        }
    }

    @Override
    public void incrementGamesLost(String username) {
        try {
            CallableStatement cs = DatabaseUtilities.prepareCall("{call increment_games_lost(?)}");
            cs.setString(1, username);
            cs.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while incrementing the amount of games lost in a statistics record in the database.", e);
        }
    }

    @Override
    public void incrementGamesPlayed(String username) {
        try {
            CallableStatement cs = DatabaseUtilities.prepareCall("{call increment_games_played(?)}");
            cs.setString(1, username);
            cs.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while incrementing the amount of games played in a statistics record in the database.", e);
        }
    }

    @Override
    public Statistics getStatistics(String username) {
        Statistics statistics = null;

        try {
            CallableStatement cs = DatabaseUtilities.prepareCall("{? = call get_user_id(?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, username);
            cs.execute();
            int userId = cs.getInt(1);

            PreparedStatement ps = DatabaseUtilities.prepareStatement("SELECT * FROM Statistics WHERE user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                statistics = new Statistics(username, rs.getInt("goals"), rs.getInt("assists"), rs.getInt("games_won"), rs.getInt("games_lost"), rs.getInt("games_played"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while retrieving the statistics of a player from the database.", e);
        }

        return statistics;
    }

    @Override
    public List<Statistics> getAllStatistics() {
        List<Statistics> statistics = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseUtilities.prepareStatement("SELECT u.username, s.goals, s.assists, s.games_won, s.games_lost, s.games_played FROM User u, Statistics s WHERE u.id = s.user_id;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                statistics.add(new Statistics(rs.getString("username"), rs.getInt("goals"), rs.getInt("assists"), rs.getInt("games_won"), rs.getInt("games_lost"), rs.getInt("games_played")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while retrieving all statistics from the database.", e);
        }

        return statistics;
    }

}
