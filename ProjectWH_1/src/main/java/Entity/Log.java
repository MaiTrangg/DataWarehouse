package Entity;

import java.sql.Date;

public class Log {
    private int idLog;
    private Config idConfig;
    private String fileName;
    private Date time;
    private String status;
    private int count;
    private Date dt_update;

    public Log(int idLog, Config idConfig, String fileName, Date time, String status, int count, Date dt_update) {
        this.idLog = idLog;
        this.idConfig = idConfig;
        this.fileName = fileName;
        this.time = time;
        this.status = status;
        this.count = count;
        this.dt_update = dt_update;
    }

    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public Config getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(Config idConfig) {
        this.idConfig = idConfig;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDt_update() {
        return dt_update;
    }

    public void setDt_update(Date dt_update) {
        this.dt_update = dt_update;
    }
}

