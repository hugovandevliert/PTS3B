package utilities.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    public static Connection getConnection() {
        return null;
    }

    public static ResultSet getData(String query, String[] values) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            preparedStatement = connection.prepareStatement(query);

            for (int i = 0; i < values.length; i++){
                final int index = i + 1;

                if (isDouble(values[i])){
                    preparedStatement.setDouble(index, Double.parseDouble(values[i]));
                }
                else if (isInteger(values[i])){
                    preparedStatement.setInt(index, Integer.parseInt(values[i]));
                }
                else if (isBoolean(values[i])){
                    preparedStatement.setBoolean(index, Boolean.parseBoolean(values[i]));
                }else{
                    preparedStatement.setString(index, values[i]);
                }
            }

            resultSet = preparedStatement.executeQuery();
            return resultSet;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if (resultSet != null) {
                resultSet.close();
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
        return null;
    }

    public static int setData(PreparedStatement statement) {
        return -1;
    }

    private static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isBoolean(String value) {
        try {
            Boolean.parseBoolean(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
