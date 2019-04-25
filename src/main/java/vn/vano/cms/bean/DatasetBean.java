package vn.vano.cms.bean;

import java.sql.Timestamp;

public class DatasetBean {
    private String datasetName;
    private String datasetId;
    private int qttyImage;

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public int getQttyImage() {
        return qttyImage;
    }

    public void setQttyImage(int qttyImage) {
        this.qttyImage = qttyImage;
    }
}
