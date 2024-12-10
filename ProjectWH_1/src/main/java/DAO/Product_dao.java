package DAO;

import Entity.Config;
import Entity.Log;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product_dao {

    public static void insertLog(Connection connection, int idConfig, String fileName, String status, int rowCount) throws SQLException {
        String sql = "{CALL InsertLog(?, ?, ?, ?)}";

        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, idConfig);
            cs.setString(2, fileName);
            cs.setString(3, status);
            cs.setInt(4, rowCount);
            cs.executeUpdate();
        }
    }

    public static void updateLogStatus(Connection connection, int idConfig, String status) throws SQLException {
        String sql = "{CALL UpdateLogStatus(?, ?)}";

        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, idConfig);
            cs.setString(2, status);
            cs.executeUpdate();
        }
    }

public static int loadDataToStagingDirect(Connection connection, String filePath, String desTableStaging) throws SQLException {
    String correctedFilePath = filePath.replace("\\", "\\\\");
    // Truncate table trước khi load dữ liệu
    System.out.println(filePath+" đây đây-------------");
    String truncateSql = "TRUNCATE TABLE db_staging.staging_daily";


            String loadSql = "LOAD DATA INFILE '" + correctedFilePath + "' " +
            "INTO TABLE " + desTableStaging + " " +
            "FIELDS TERMINATED BY ';' " +
            "LINES TERMINATED BY '\\n' " +
            "(id_product, product_name, price, image_url, capacity, freezer_capacity, cooling_capacity, dimensions, power_consumption, brand, origin);";

    try (Statement stmt = connection.createStatement()) {
        stmt.execute(truncateSql);  // Xóa dữ liệu cũ trong bảng staging
        int rowCount = stmt.executeUpdate(loadSql);  // Thực thi LOAD DATA INFILE
        System.out.println("haha"+rowCount);
        return rowCount;
    }
}

//    public static int loadDataToStagingDirect(Connection connection, String filePath) throws SQLException {
//        String correctedFilePath = filePath.replace("\\", "/"); // Thay thế dấu \ bằng / cho đường dẫn
//
//        String callProcedure = "{CALL db_staging.LoadDataToStagingDirect(?)}"; // Gọi procedure với 1 tham số IN
//
//        try (CallableStatement callableStatement = connection.prepareCall(callProcedure)) {
//            // Thiết lập tham số IN
//            callableStatement.setString(1, correctedFilePath);
//
//            // Thực thi procedure
//         int rowCount=  callableStatement.executeUpdate();
//
//            // Lấy số dòng đã được load
////            int rowCount = 0;
////            if (resultSet.next()) {
////                rowCount = resultSet.getInt("rowCount");
////            }
//
//            System.out.println("Dữ liệu đã được load thành công từ file: " + filePath);
////            System.out.println("Số dòng đã được load: " + rowCount);
//
//            return rowCount; // Trả về số dòng đã được load
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }


    public static Log getFirstLogByConfigAndStatus(Connection connection, String date, int idConfig, String status) {
    String sql = "{CALL GetFirstLogByConfigAndStatus(?, ?, ?)}";
    Log log = null;

    try (CallableStatement cs = connection.prepareCall(sql)) {
        // Định dạng chuỗi ngày
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy"); // Nếu chuỗi ngày đầu vào có định dạng dd/MM/yyyy
        Date parsedDate = inputFormat.parse(date);
        System.out.println(parsedDate);

        // Chuyển sang java.sql.Timestamp
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        System.out.println(timestamp);

        // Thiết lập tham số cho thủ tục
        cs.setTimestamp(1, timestamp); // Truyền tham số p_date dưới dạng Timestamp
        cs.setInt(2, idConfig);       // Truyền id_config
        cs.setString(3, status);      // Truyền status

        // Thực thi procedure
        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Config config = getConfigById(connection, idConfig);
                log = new Log(
                        rs.getInt("id_log"),
                        config,
                        rs.getString("fileName"),
                        rs.getDate("time"),  // Lấy kiểu DATETIME
                        rs.getString("status"),
                        rs.getInt("count"),
                        rs.getDate("dt_update")
                );
            } else {
                System.out.println("No logs found for the given config ID and status.");
            }
        }
    } catch (Exception e) {
        e.printStackTrace(); // Log lỗi
    }

    return log;
}

    public static Config getConfigById(Connection connection, int idConfig) throws SQLException {
        String sql = "SELECT * FROM config WHERE id_config = ?";
        Config config = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idConfig);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nameSrc = rs.getString("nameSrc");
                    String pathToSrc = rs.getString("pathToSrc");
                    String pathSaveCsv = rs.getString("pathSaveCsv");
                    String desTableStaging = rs.getString("desTableStaging");
                    String desTableStaging_transformed = rs.getString("desTableStaging_transformed");
                    String desTableDW = rs.getString("desTableDW");


                    // Tạo đối tượng Config từ kết quả truy vấn
                    config = new Config(idConfig, nameSrc, pathToSrc, pathSaveCsv, desTableStaging,desTableStaging_transformed, desTableDW);
                }
            }
        }
        return config;

    }

    public static boolean transformTypedata(Connection connection) {
        boolean check = false;
        try {

            if (connection == null) {
                System.out.println("Error: Cannot connect to the database!");
                check = false;

            }

            // Gọi stored procedure
            String procedureCall = "{CALL db_staging.transform_and_load_price()}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Thực thi stored procedure
                callableStatement.execute();
//                System.out.println("Procedure " + procedureName + " executed successfully!");
                check = true;
            }

        } catch (SQLException e) {
            // Xử lý lỗi khi gọi procedure
//            System.err.println("Error while executing procedure " + procedureName + ": " + e.getMessage());
            e.printStackTrace();
            check=false;
        }
        return check;
    }


}
