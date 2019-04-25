package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;
import java.util.Date;

@AntTable(name = "sys_param", key = "id")
public class SysParamAnt implements Serializable {
    private Long id;
    private String key;
    private String value;
    private String type;
    private Date createdTime;
    private Date modifiedTime;
    private Integer status;

    public SysParamAnt() {

    }

    public SysParamAnt(Long id, String key, String value, String type, Date createdTime, Date modifiedTime, Integer status) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.type = type;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.status = status;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public Long getId() {
        return id;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public void setId(Long id) {
        this.id = id;
    }

    @AntColumn(name = "_key", index = 1)
    public String getKey() {
        return key;
    }

    @AntColumn(name = "_key", index = 1)
    public void setKey(String key) {
        this.key = key;
    }

    @AntColumn(name = "_value", index = 2)
    public String getValue() {
        return value;
    }

    @AntColumn(name = "_value", index = 2)
    public void setValue(String value) {
        this.value = value;
    }

    @AntColumn(name = "_type", index = 3)
    public String getType() {
        return type;
    }

    @AntColumn(name = "_type", index = 3)
    public void setType(String type) {
        this.type = type;
    }

    @AntColumn(name = "created_time", index = 4)
    public Date getCreatedTime() {
        return createdTime;
    }

    @AntColumn(name = "created_time", index = 4)
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @AntColumn(name = "modified_time", index = 5)
    public Date getModifiedTime() {
        return modifiedTime;
    }

    @AntColumn(name = "modified_time", index = 5)
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @AntColumn(name = "status", index = 6)
    public Integer getStatus() {
        return status;
    }

    @AntColumn(name = "status", index = 6)
    public void setStatus(Integer status) {
        this.status = status;
    }
}
