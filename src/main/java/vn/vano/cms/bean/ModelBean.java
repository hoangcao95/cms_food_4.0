package vn.vano.cms.bean;

public class ModelBean {
    private String modelName;
    private String modelId;
    private String datasetId;
    private String datasetName;
    private int qttyImage;

    public ModelBean() {
    }

    public ModelBean(String modelName, String modelId, String datasetId, String datasetName, int qttyImage) {
        this.modelName = modelName;
        this.modelId = modelId;
        this.datasetId = datasetId;
        this.datasetName = datasetName;
        this.qttyImage = qttyImage;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public int getQttyImage() {
        return qttyImage;
    }

    public void setQttyImage(int qttyImage) {
        this.qttyImage = qttyImage;
    }
}
