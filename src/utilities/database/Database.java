package utilities.database;

import javafx.scene.image.Image;

import java.io.*;
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

    public static ResultSet getData(final String query, final String[] values) {
        try {
            final Connection connection = Database.getConnection();
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

            final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int setData(final String query, final String[] values, final boolean isUpdateQuery) {
        int updateCount = -1;

        try {
            final Connection connection = Database.getConnection();
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

            if (isUpdateQuery) preparedStatement.executeUpdate();
            else preparedStatement.executeQuery();

            updateCount = preparedStatement.getUpdateCount();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return updateCount;
    }

    /**
     * @param query     This is the query to execute. Make sure to write it in such a way that all the normal values can be parsed first, then afterwards the
     *                  images will be parsed, all happening in the order that the arrays are in.
     * @param values    This is the array of normal values, like the the getData() method
     * @param images    This is an array of images
     */
    public static int setDataWithImages(final String query, final String[] values, final Image[] images, final boolean isUpdateQuery) {
        int updateCount = -1;

        try {
            final Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int index = 0;

            if (values != null && values.length > 0){
                for (int i = 0; i < values.length; i++){
                    index += 1;

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

            if (images != null && images.length > 0){
                for (int i = 0; i < values.length; i++){
                    index += 1;

                    final byte[] serializedImage = getSerializedObject(images[i]);
                    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedImage);

                    preparedStatement.setBinaryStream(index, byteArrayInputStream, serializedImage.length);
                }
            }

            if (isUpdateQuery) preparedStatement.executeUpdate();
            else preparedStatement.executeQuery();

            updateCount = preparedStatement.getUpdateCount();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return updateCount;
    }

    /**
     * @param query Usage for query --> {call increase_salaries_for_department(?, ?)}
     *              increase_salaries_for_department being the name of the stored procedure
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

    private static byte[] getSerializedObject(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        return byteArrayOutputStream.toByteArray();
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
