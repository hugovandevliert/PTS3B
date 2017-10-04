package utilities.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@SuppressWarnings("Duplicates")
public class Database {

    private static String server, username, password;
    private static SimpleDateFormat  dateFormatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

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
        try {
            Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

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
                    }
                    else if (isDate(values[i])){
                        preparedStatement.setDate(index, (Date)dateFormatter.parse(values[i]));
                    }else{
                        preparedStatement.setString(index, values[i]);
                    }
                }
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param query Usage for query --> {call increase_salaries_for_department(?, ?)}
     */
    public static int executeStoredProcedure(String query, String[] values) {
        int updateCount = -1;

        try {
            CallableStatement callableStatement = Database.getConnection().prepareCall(query);

            if (values != null && values.length > 0){
                for (int i = 0; i < values.length; i++){
                    final int index = i + 1;

                    if (isDouble(values[i])){
                        callableStatement.setDouble(index, Double.parseDouble(values[i]));
                    }
                    else if (isInteger(values[i])){
                        callableStatement.setInt(index, Integer.parseInt(values[i]));
                    }
                    else if (isBoolean(values[i])){
                        callableStatement.setBoolean(index, Boolean.parseBoolean(values[i]));
                    }
                    else if (isDate(values[i])){
                        callableStatement.setDate(index, (Date)dateFormatter.parse(values[i]));
                    }else{
                        callableStatement.setString(index, values[i]);
                    }
                }
            }

            callableStatement.execute();
            updateCount = callableStatement.getUpdateCount();
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return updateCount;
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
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isDate(String value) {
        try {
            dateFormatter.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
