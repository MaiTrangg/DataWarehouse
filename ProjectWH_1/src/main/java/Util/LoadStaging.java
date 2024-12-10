package Util;

import Connection.DBConnection;
import DAO.Product_dao;
import Entity.Config;
import Entity.Log;

import java.sql.Connection;
import java.sql.SQLException;

public class LoadStaging {
    private static final String STATUS ="Extract_Ready";
    private static  int config_id ;
    private static  String date;
    private static String error;
    public static void main(String[] args) throws SQLException {
        //1. lay thong tin trong file config.properties
         date = args[0];
         config_id = Integer.parseInt(args[1]);
        System.out.println(config_id);

        System.out.println(date);
        ConfigLoader c = new ConfigLoader();
        c.loadProperties();

        //2. Ket noi DB control
        Connection con = DBConnection.getConnection(c);

        //3. Kiểm tra kết nối
        if (con == null) {
            //3.1 Gửi mail thông báo: Can not connect DB Control
            error = "Error: Can not connect DB Control";
            Email.sendEmail(c.getEmail(),error);
            return;
        }



        //4. Lấy ra thông tin log có config_id=x và status ="Extract_Ready" trong table log Load_R
//        config_id =c.getConfigId();
//         status = c.getStatus();
        Log log = Product_dao.getFirstLogByConfigAndStatus(con,date,config_id,STATUS);

        //5. Kiểm tra log
        if (log == null) {
            //5.1 hiển thị thông báo: Khong tim thay config = x ma co status = Extract_Ready
            error = "Error: No log found for config ID = " +config_id+ " with status = "+STATUS+"date = "+date;
            System.out.println(error);
            //5.2 gửi mail thong bao:
            Email.sendEmail(c.getEmail(),error);
            return;
        }

        //lay ra duong dan luu file csv
        String filePath = log.getIdConfig().getPathSaveCsv() + "\\" + log.getFileName();
        int rowCount;
        try {
            //6. load data từ file csv vào DB Staging
            rowCount = Product_dao.loadDataToStagingDirect(con, filePath, log.getIdConfig().getDesTableStaging());

            // 7. Kiểm tra load
            int firstRow = log.getCount();  // Expected row count for successful load
            if (rowCount < firstRow) {
               //data load khong thanh cong vao bang staging_daily: 7.1 cập nhật log: status= "Load_Fail" dt_update=now()
                Product_dao.updateLogStatus(con, config_id, "Load_Fail");
                //7.2 gui mail thong bao
                error= "Error: Load data to staging is fail! Number rows loaded is wrong!";
                Email.sendEmail(c.getEmail(),error);

            } else {

                //8. tiến hành transformed dữ liệu về đúng kiểu mong muốn
                boolean isSuccess = Product_dao.transformTypedata(con);
                //9. kiểm tra transform
                if(isSuccess) {
                    //10. data load thanh cong vao bang staging_daily_transformed: cập nhật log: status= "Load_Ready" dt_update=now()
                    Product_dao.updateLogStatus(con, config_id, "Load_Ready");
                    //11. gui mail thong bao
                    error= "Load data to staging is success!";
                    Email.sendEmail(c.getEmail(),error);
                }else{
                    //9.1 data load khong thanh cong vao bang staging_daily_transformed: cập nhật log: status= "Load_Ready" dt_update=now()
                    Product_dao.updateLogStatus(con, config_id, "Load_Fail");
                    //9.2 gui mail thong bao khi transform không thành công
                    error= "transform data is not success!";
                    Email.sendEmail(c.getEmail(),error);
                }


            }

            System.out.println("Data load process completed with row count: " + rowCount);

        } catch (Exception e) {
            e.printStackTrace();
            Product_dao.updateLogStatus(con,config_id,"Load_Fail");
            error= "Database Operation Error\n"+e.getMessage();
            Email.sendEmail(c.getEmail(),error);
        } finally {
            // Close the database connection after use
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    }

