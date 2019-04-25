package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@AntTable(name = "image_import", key = "id")
public class ImageImport implements Serializable {
    private Integer id;
    private String imageName;
    private String labelId;
    private String path;
    private Timestamp createdDate;
    private String createdByUser;
    private int status;

    public ImageImport() {

    }

    public ImageImport(Integer id, String imageName, String labelId, String path, Timestamp createdDate, String createdByUser, int status) {
        this.id = id;
        this.imageName = imageName;
        this.labelId = labelId;
        this.path = path;
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

    @AntColumn(name = "image_name", index = 1)
    public String getImageName() {
        return imageName;
    }

    @AntColumn(name = "image_name", index = 1)
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @AntColumn(name = "label_id", index = 2)
    public String getLabelId() {
        return labelId;
    }

    @AntColumn(name = "label_id", index = 2)
    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    @AntColumn(name = "path", index = 3)
    public String getPath() {
        return path;
    }

    @AntColumn(name = "path", index = 3)
    public void setPath(String path) {
        this.path = path;
    }

    @AntColumn(name = "created_date", index = 4)
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date", index = 4)
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @AntColumn(name = "created_by_user", index = 5)
    public String getCreatedByUser() {
        return createdByUser;
    }

    @AntColumn(name = "created_by_user", index = 5)
    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    @AntColumn(name = "status", index = 6)
    public int getStatus() {
        return status;
    }

    @AntColumn(name = "status", index = 6)
    public void setStatus(int status) {
        this.status = status;
    }
}
