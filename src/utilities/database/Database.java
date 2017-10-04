package utilities.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Database {

    private static String server, username, password;

    public static Connection getConnection() {
        try {
            if (server == null || username == null || password == null){
                FileInputStream fileInput = new FileInputStream("src/utilities/database/DatabaseCredentials.properties");

                Properties properties = new Properties();
                properties.load(fileInput);
                fileInput.close();

                server = properties.getProperty("server");
                username = properties.getProperty("username");
                password = properties.getProperty("password");
            }

            return DriverManager.getConnection("jdbc:mysql://" + server + ":3306/MyAuctions", username, password);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static ResultSet getData(String query, String[] values) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            preparedStatement = connection.prepareStatement(query);

            if (values != null && values.length > 0){
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
            }

            resultSet = preparedStatement.executeQuery();
            return resultSet;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                if (resultSet != null) {
                    //resultSet.close();
                }

                if (preparedStatement != null) {
                    //preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex){
                ex.printStackTrace();
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
