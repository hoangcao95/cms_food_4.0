package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;

@AntTable(name = "core_promotion", key = "id")
public class CorePromotion implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String startTime;
    private String endTime;
    private String createdDate;
    private Integer isMain;
    private String note;
    private Integer status;

    public CorePromotion() {

    }

    public CorePromotion(Long id, String code, String name, String startTime, String endTime, String createdDate, Integer isMain, String note, Integer status) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdDate = createdDate;
        this.isMain = isMain;
        this.note = note;
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

    @AntColumn(name = "code", index = 1)
    public String getCode() {
        return code;
    }

    @AntColumn(name = "code", index = 1)
    public void setCode(String code) {
        this.code = code;
    }

    @AntColumn(name = "name", index = 2)
    public String getName() {
        return name;
    }

    @AntColumn(name = "name", index = 2)
    public void setName(String name) {
        this.name = name;
    }

    @AntColumn(name = "start_time", index = 3)
    public String getStartTime() {
        return startTime;
    }

    @AntColumn(name = "start_time", index = 3)
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @AntColumn(name = "end_time", index = 4)
    public String getEndTime() {
        return endTime;
    }

    @AntColumn(name = "end_time", index = 4)
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @AntColumn(name = "created_date", index = 5)
    public String getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date", index = 5)
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @AntColumn(name = "is_main", index = 6)
    public Integer getIsMain() {
        return isMain;
    }

    @AntColumn(name = "is_main", index = 6)
    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
    }

    @AntColumn(name = "note", index = 7)
    public String getNote() {
        return note;
    }

    @AntColumn(name = "note", index = 7)
    public void setNote(String note) {
        this.note = note;
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
