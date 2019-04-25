package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@AntTable(name = "core_mo_sms_" + AntTable.DATE_PATTERN, key = "id", time_pattern = "yyyyMM")
public class CoreMOSms implements Serializable {
    private Long id;
    private String msisdn;
    private String serviceCode;
    private String message;
    private String messageOriginal;
    private String command;
    private String channel;
    private Date createdDate;
    private Timestamp createdTime;
    private String note;
    private Long promoId;
    private Long status;
    private String transId;

    @AntColumn(name = "id", auto_increment = true)
    public Long getId() {
        return id;
    }

    @AntColumn(name = "id", auto_increment = true)
    public void setId(Long id) {
        this.id = id;
    }

    @AntColumn(name = "msisdn")
    public String getMsisdn() {
        return msisdn;
    }

    @AntColumn(name = "msisdn")
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @AntColumn(name = "service_code")
    public String getServiceCode() {
        return serviceCode;
    }

    @AntColumn(name = "service_code")
    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @AntColumn(name = "message")
    public String getMessage() {
        return message;
    }

    @AntColumn(name = "message")
    public void setMessage(String message) {
        this.message = message;
    }

    @AntColumn(name = "message_original")
    public String getMessageOriginal() {
        return messageOriginal;
    }

    @AntColumn(name = "message_original")
    public void setMessageOriginal(String messageOriginal) {
        this.messageOriginal = messageOriginal;
    }

    @AntColumn(name = "command")
    public String getCommand() {
        return command;
    }

    @AntColumn(name = "command")
    public void setCommand(String command) {
        this.command = command;
    }

    @AntColumn(name = "channel")
    public String getChannel() {
        return channel;
    }

    @AntColumn(name = "channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @AntColumn(name = "created_date")
    public Date getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date")
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @AntColumn(name = "created_time")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    @AntColumn(name = "created_time")
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @AntColumn(name = "note")
    public String getNote() {
        return note;
    }

    @AntColumn(name = "note")
    public void setNote(String note) {
        this.note = note;
    }

    @AntColumn(name = "promo_id")
    public Long getPromoId() {
        return promoId;
    }

    @AntColumn(name = "promo_id")
    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }

    @AntColumn(name = "status")
    public Long getStatus() {
        return status;
    }

    @AntColumn(name = "status")
    public void setStatus(Long status) {
        this.status = status;
    }

    @AntColumn(name = "trans_id")
    public String getTransId() {
        return transId;
    }

    @AntColumn(name = "trans_id")
    public void setTransId(String transId) {
        this.transId = transId;
    }
}
