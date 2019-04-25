package vn.vano.cms.jpa;


import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@AntTable(name = "image_label", key = "id")
public class Label implements Serializable {
    private Integer id;
    private String labelName;
    private String labelId;
    private Date createdDate;
    private String createdByUser;
    private Integer status;

    public Label() {

    }

    public Label(Integer id, String labelName, String labelId, Date createdDate, String createdByUser, Integer status) {
        this.id = id;
        this.labelName = labelName;
        this.labelId = labelId;
        this.createdDate = createdDate;
        this.createdByUser = createdByUser;
        this.status = status;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public Integer getId() {
        return id;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public void setId(Integer id) {
        this.id = id;
    }

    @AntColumn(name = "label_name", index = 1)
    public String getLabelName() {
        return labelName;
    }

    @AntColumn(name = "label_name", index = 1)
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    @AntColumn(name = "label_id", index = 2)
    public String getLabelId() {
        return labelId;
    }

    @AntColumn(name = "label_id", index = 2)
    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    @AntColumn(name = "created_date", index = 3)
    public Date getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date", index = 3)
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @AntColumn(name = "created_by_user", index = 4)
    public String getCreatedByUser() {
        return createdByUser;
    }

    @AntColumn(name = "created_by_user", index = 4)
    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    @AntColumn(name = "status", index = 5)
    public Integer getStatus() {
        return status;
    }

    @AntColumn(name = "status", index = 5)
    public void setStatus(Integer status) {
        this.status = status;
    }
}
