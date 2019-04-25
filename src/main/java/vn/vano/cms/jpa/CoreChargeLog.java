package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@AntTable(name = "core_charge_log_" + AntTable.DATE_PATTERN, key = "id", time_pattern = "yyyyMM")
public class CoreChargeLog implements Serializable {
    private Long id;
    private String msisdn;
    private String serviceCode;
    private String packageCode;
    private String action;
    private Integer amount;
    private String channel;
    private Date createdDate;
    private Timestamp createdTime;
    private Integer callResult;
    private String resultMessage;
    private Integer status;
    private String cpId;
    private String note;
    private Long promoId;
    private String transId;

    public CoreChargeLog() {

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

    @AntColumn(name = "service_code", index = 2)
    public String getServiceCode() {
        return serviceCode;
    }

    @AntColumn(name = "service_code", index = 2)
    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @AntColumn(name = "package_code", index = 3)
    public String getPackageCode() {
        return packageCode;
    }

    @AntColumn(name = "package_code", index = 3)
    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    @AntColumn(name = "action", index = 4)
    public String getAction() {
        return action;
    }

    @AntColumn(name = "action", index = 4)
    public void setAction(String action) {
        this.action = action;
    }

    @AntColumn(name = "amount", index = 5)
    public Integer getAmount() {
        return amount;
    }

    @AntColumn(name = "amount", index = 5)
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @AntColumn(name = "channel", index = 6)
    public String getChannel() {
        return channel;
    }

    @AntColumn(name = "channel", index = 6)
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @AntColumn(name = "created_date", index = 7)
    public Date getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date", index = 7)
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @AntColumn(name = "created_time", index = 8)
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    @AntColumn(name = "created_time", index = 8)
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @AntColumn(name = "call_result", index = 9)
    public Integer getCallResult() {
        return callResult;
    }

    @AntColumn(name = "call_result", index = 9)
    public void setCallResult(Integer callResult) {
        this.callResult = callResult;
    }

    @AntColumn(name = "result_message", index = 10)
    public String getResultMessage() {
        return resultMessage;
    }

    @AntColumn(name = "result_message", index = 10)
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    @AntColumn(name = "status", index = 11)
    public Integer getStatus() {
        return status;
    }

    @AntColumn(name = "status", index = 11)
    public void setStatus(Integer status) {
        this.status = status;
    }

    @AntColumn(name = "cpid", index = 12)
    public String getCpId() {
        return cpId;
    }

    @AntColumn(name = "cpid", index = 12)
    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    @AntColumn(name = "note", index = 13)
    public String getNote() {
        return note;
    }

    @AntColumn(name = "note", index = 13)
    public void setNote(String note) {
        this.note = note;
    }

    @AntColumn(name = "promo_id", index = 14)
    public Long getPromoId() {
        return promoId;
    }

    @AntColumn(name = "promo_id", index = 14)
    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }

    @AntColumn(name = "trans_id", index = 15)
    public String getTransId() {
        return transId;
    }

    @AntColumn(name = "trans_id", index = 15)
    public void setTransId(String transId) {
        this.transId = transId;
    }
}
