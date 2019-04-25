package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@AntTable(name = "core_subscriber", key = "id")
public class CoreSubscriber implements Serializable {
    private Long id;
    private String msisdn;
    private String mpin;
    private String packageCode;
    private Date createdDate;
    private Timestamp createdTime;
    private Timestamp expireTime;
    private Timestamp activeTime;
    private Timestamp deactiveTime;
    private String activeChannel;
    private String deactiveChannel;
    private Integer status;
    private String note;
    private String cpId;
    private Long promoId;
    private String ip;
    private String message;

    public CoreSubscriber() {

    }

    public CoreSubscriber(Long id, String msisdn, String mpin, String packageCode, Date createdDate,
                          Timestamp createdTime, Timestamp expireTime, Timestamp activeTime, Timestamp deactiveTime,
                          String activeChannel, String deactiveChannel, Integer status, String note, String cpId,
                          Long promoId, String ip, String message) {
        this.id = id;
        this.msisdn = msisdn;
        this.mpin = mpin;
        this.packageCode = packageCode;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
        this.expireTime = expireTime;
        this.activeTime = activeTime;
        this.deactiveTime = deactiveTime;
        this.activeChannel = activeChannel;
        this.deactiveChannel = deactiveChannel;
        this.status = status;
        this.note = note;
        this.cpId = cpId;
        this.promoId = promoId;
        this.ip = ip;
        this.message = message;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public Long getId() {
        return id;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public void setId(Long id) {
        this.id = id;
    }

    @AntColumn(name = "msisdn", index = 1)
    public String getMsisdn() {
        return msisdn;
    }

    @AntColumn(name = "msisdn", index = 1)
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @AntColumn(name = "mpin", index = 2)
    public String getMpin() {
        return mpin;
    }

    @AntColumn(name = "mpin", index = 2)
    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    @AntColumn(name = "package_code", index = 3)
    public String getPackageCode() {
        return packageCode;
    }

    @AntColumn(name = "package_code", index = 3)
    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    @AntColumn(name = "created_date", index = 4)
    public Date getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date", index = 4)
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @AntColumn(name = "created_time", index = 5)
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    @AntColumn(name = "created_time", index = 5)
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @AntColumn(name = "expire_time", index = 6)
    public Timestamp getExpireTime() {
        return expireTime;
    }

    @AntColumn(name = "expire_time", index = 6)
    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    @AntColumn(name = "active_time", index = 7)
    public Timestamp getActiveTime() {
        return activeTime;
    }

    @AntColumn(name = "active_time", index = 7)
    public void setActiveTime(Timestamp activeTime) {
        this.activeTime = activeTime;
    }

    @AntColumn(name = "deactive_time", index = 8)
    public Timestamp getDeactiveTime() {
        return deactiveTime;
    }

    @AntColumn(name = "deactive_time", index = 8)
    public void setDeactiveTime(Timestamp deactiveTime) {
        this.deactiveTime = deactiveTime;
    }

    @AntColumn(name = "active_channel", index = 9)
    public String getActiveChannel() {
        return activeChannel;
    }

    @AntColumn(name = "active_channel", index = 9)
    public void setActiveChannel(String activeChannel) {
        this.activeChannel = activeChannel;
    }

    @AntColumn(name = "deactive_channel", index = 10)
    public String getDeactiveChannel() {
        return deactiveChannel;
    }

    @AntColumn(name = "deactive_channel", index = 10)
    public void setDeactiveChannel(String deactiveChannel) {
        this.deactiveChannel = deactiveChannel;
    }

    @AntColumn(name = "status", index = 11)
    public Integer getStatus() {
        return status;
    }

    @AntColumn(name = "status", index = 11)
    public void setStatus(Integer status) {
        this.status = status;
    }

    @AntColumn(name = "note", index = 12)
    public String getNote() {
        return note;
    }

    @AntColumn(name = "note", index = 12)
    public void setNote(String note) {
        this.note = note;
    }

    @AntColumn(name = "cpid", index = 13)
    public String getCpId() {
        return cpId;
    }

    @AntColumn(name = "cpid", index = 13)
    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    @AntColumn(name = "promo_id", index = 14)
    public Long getPromoId() {
        return promoId;
    }

    @AntColumn(name = "promo_id", index = 14)
    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }

    @AntColumn(name = "ip", index = 15)
    public String getIp() {
        return ip;
    }

    @AntColumn(name = "ip", index = 15)
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
