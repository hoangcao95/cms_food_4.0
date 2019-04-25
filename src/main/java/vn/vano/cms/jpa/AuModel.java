package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.sql.Timestamp;
import java.util.Date;

@AntTable(name = "automl_model", key = "id")
public class AuModel {
    private Long id;
    private String modelName;
    private String modelId;
    private String datasetName;
    private Integer qttyImage;
    private Timestamp trainningDate;
    private Timestamp completedDate;
    private String createdByUser;
    private Integer status;

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public Long getId() {
        return id;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public void setId(Long id) {
        this.id = id;
    }

    @AntColumn(name = "model_name", index = 1)
    public String getModelName() {
        return modelName;
    }

    @AntColumn(name = "model_name", index = 1)
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @AntColumn(name = "model_id", index = 2)
    public String getModelId() {
        return modelId;
    }

    @AntColumn(name = "model_id", index = 2)
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    @AntColumn(name = "dataset_name", index = 3)
    public String getDatasetName() {
        return datasetName;
    }

    @AntColumn(name = "dataset_name", index = 3)
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    @AntColumn(name = "quantity_image", index = 4)
    public Integer getQttyImage() {
        return qttyImage;
    }

    @AntColumn(name = "quantity_image", index = 4)
    public void setQttyImage(Integer qttyImage) {
        this.qttyImage = qttyImage;
    }

    @AntColumn(name = "trainning_date", index = 5)
    public Timestamp getTrainningDate() {
        return trainningDate;
    }

    @AntColumn(name = "trainning_date", index = 5)
    public void setTrainningDate(Timestamp trainningDate) {
        this.trainningDate = trainningDate;
    }

    @AntColumn(name = "completed_date", index = 6)
    public Timestamp getCompletedDate() {
        return completedDate;
    }

    @AntColumn(name = "completed_date", index = 6)
    public void setCompletedDate(Timestamp completedDate) {
        this.completedDate = completedDate;
    }

    @AntColumn(name = "created_by_user", index = 7)
    public String getCreatedByUser() {
        return createdByUser;
    }

    @AntColumn(name = "created_by_user", index = 7)
    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    @AntColumn(name = "status", index = 8)
    public Integer getStatus() {
        return status;
    }

    @AntColumn(name = "status", index = 8)
    public void setStatus(Integer status) {
        this.status = status;
    }
}
