package Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String FILE_CONFIG = "/config.properties";

    private String db;
    private String host;
    private String port;
    private String nameDB;
    private String username;
    private String password;
    private String email;

    public ConfigLoader() {
//        loadProperties();
    }

    public void loadProperties() {
        Properties properties = new Properties();
        String currentDir = System.getProperty("user.dir");
        try (InputStream inputStream = new FileInputStream(currentDir + FILE_CONFIG)) {
            // Load properties từ file
            properties.load(inputStream);

            // Gán các giá trị từ properties file vào các thuộc tính
            db = properties.getProperty("db");
            host = properties.getProperty("host");
            port = properties.getProperty("port");
            nameDB = properties.getProperty("name_database");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            email = properties.getProperty("email");
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Invalid format for config_id: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Các phương thức getter để truy cập thông tin cấu hình
    public String getDb() {
        return db;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getNameDB() {
        return nameDB;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }
}
