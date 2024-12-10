package Entity;

public class Config {
    private int id_config;
    private String nameSrc;
    private String pathToSrc;
    private String pathSaveCsv;
    private String desTableStaging;
    private String desTableStaging_transformed;
    private String desTableDW;

    public Config(int id_config, String nameSrc, String pathToSrc, String pathSaveCsv, String desTableStaging, String desTableStaging_transformed, String desTableDW) {
        this.id_config = id_config;
        this.nameSrc = nameSrc;
        this.pathToSrc = pathToSrc;
        this.pathSaveCsv = pathSaveCsv;
        this.desTableStaging = desTableStaging;
        this.desTableStaging_transformed = desTableStaging_transformed;
        this.desTableDW = desTableDW;
    }

    public int getId_config() {
        return id_config;
    }

    public void setId_config(int id_config) {
        this.id_config = id_config;
    }

    public String getNameSrc() {
        return nameSrc;
    }

    public void setNameSrc(String nameSrc) {
        this.nameSrc = nameSrc;
    }

    public String getPathToSrc() {
        return pathToSrc;
    }

    public void setPathToSrc(String pathToSrc) {
        this.pathToSrc = pathToSrc;
    }

    public String getPathSaveCsv() {
        return pathSaveCsv;
    }

    public void setPathSaveCsv(String pathSaveCsv) {
        this.pathSaveCsv = pathSaveCsv;
    }

    public String getDesTableStaging() {
        return desTableStaging;
    }

    public void setDesTableStaging(String desTableStaging) {
        this.desTableStaging = desTableStaging;
    }

    public String getDesTableStaging_transformed() {
        return desTableStaging_transformed;
    }

    public void setDesTableStaging_transformed(String desTableStaging_transformed) {
        this.desTableStaging_transformed = desTableStaging_transformed;
    }

    public String getDesTableDW() {
        return desTableDW;
    }

    public void setDesTableDW(String desTableDW) {
        this.desTableDW = desTableDW;
    }
}
