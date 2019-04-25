package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.sql.Timestamp;
import java.util.Date;

@AntTable(name = "automl_dataset", key = "id")
public class Dataset {
    private Long id;
    private String datasetName;
    private String datasetId;
    private Integer qttyImage;
    private Timestamp createdDate;
    private Timestamp modifyDate;
    private String createdByUser;
    private String modifyByUser;
    private Integer status;

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public Long getId() {
        return id;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public void setId(Long id) {
        this.id = id;
    }

    @AntColumn(name = "dataset_name", index = 1)
    public String getDatasetName() {
        return datasetName;
    }

    @AntColumn(name = "dataset_name", index = 1)
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    @AntColumn(name = "dataset_id", index = 2)
    public String getDatasetId() {
        return datasetId;
    }

    @AntColumn(name = "dataset_id", index = 2)
    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    @AntColumn(name = "quantity_image", index = 3)
    public Integer getQttyImage() {
        return qttyImage;
    }

    @AntColumn(name = "quantity_image", index = 3)
    public void setQttyImage(Integer qttyImage) {
        this.qttyImage = qttyImage;
    }

    @AntColumn(name = "created_date", index = 4)
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date", index = 4)
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @AntColumn(name = "modify_date", index = 5)
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    @AntColumn(name = "modify_date", index = 5)
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    @AntColumn(name = "created_by_user", index = 6)
    public String getCreatedByUser() {
        return createdByUser;
    }

    @AntColumn(name = "created_by_user", index = 6)
    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    @AntColumn(name = "modify_by_user", index = 7)
    public String getModifyByUser() {
        return modifyByUser;
    }

    @AntColumn(name = "modify_by_user", index = 7)
    public void setModifyByUser(String modifyByUser) {
        this.modifyByUser = modifyByUser;
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
