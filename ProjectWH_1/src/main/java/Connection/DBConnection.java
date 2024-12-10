package Connection;

import Util.ConfigLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

//    private static final ConfigLoader config = new ConfigLoader(); // Load config khi khởi tạo lớp DBConnection

    // Constructor
    private DBConnection() {

    }

    // Kết nối với database
    public static Connection getConnection(ConfigLoader config) {
        if (connection == null) {
            try {
                String urlDb = "jdbc:" + config.getDb() + "://" + config.getHost() + ":" + config.getPort() + "/" + config.getNameDB() + "?allowLoadLocalInfile=true";

                // Kết nối đến database
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(urlDb, config.getUsername(), config.getPassword());

                System.out.println("Kết nối thành công");
            } catch (SQLException | ClassNotFoundException e) {

                throw new RuntimeException("Không thể kết nối DB Control: " + e.getMessage(), e);
            }
        }
        return connection;
    }

    // Phương thức đóng kết nối
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Đã đóng kết nối");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

}
