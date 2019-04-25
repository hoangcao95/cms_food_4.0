package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;

@AntTable(name = "core_mo_queue", key = "id")
public class CoreMtQueue implements Serializable {
    private Long id;
    private String msisdn;
    private String serviceCode;
    private String commandCode;
    private String orgRequest;
    private String channel;
    private String status;
    private String groupCode;
    private String packageCode;
    private String chargePrice;
    private String regDatetime;
    private String staDatetime;
    private String endDatetime;
    private String expireDatetime;
    private String messageSend;
    private String note;
    private Integer processStatus = 0;
    private String transId;
    private String keyword;
    private String promoId;
    private Integer delay;

    public CoreMtQueue() {
    }

    public CoreMtQueue(Long id, String msisdn, String serviceCode, String commandCode, String orgRequest, String channel, String status, String groupCode, String packageCode, String chargePrice, String regDatetime, String staDatetime, String endDatetime, String expireDatetime, String messageSend, String note, Integer processStatus, String transId, String keyword, String promoId, Integer delay) {
        this.id = id;
        this.msisdn = msisdn;
        this.serviceCode = serviceCode;
        this.commandCode = commandCode;
        this.orgRequest = orgRequest;
        this.channel = channel;
        this.status = status;
        this.groupCode = groupCode;
        this.packageCode = packageCode;
        this.chargePrice = chargePrice;
        this.regDatetime = regDatetime;
        this.staDatetime = staDatetime;
        this.endDatetime = endDatetime;
        this.expireDatetime = expireDatetime;
        this.messageSend = messageSend;
        this.note = note;
        this.processStatus = processStatus;
        this.transId = transId;
        this.keyword = keyword;
        this.promoId = promoId;
        this.delay = delay;
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

    @AntColumn(name = "command_code", index = 3)
    public String getCommandCode() {
        return commandCode;
    }

    @AntColumn(name = "command_code", index = 3)
    public void setCommandCode(String commandCode) {
        this.commandCode = commandCode;
    }

    @AntColumn(name = "org_request", index = 4)
    public String getOrgRequest() {
        return orgRequest;
    }

    @AntColumn(name = "org_request", index = 4)
    public void setOrgRequest(String orgRequest) {
        this.orgRequest = orgRequest;
    }

    @AntColumn(name = "channel", index = 5)
    public String getChannel() {
        return channel;
    }

    @AntColumn(name = "channel", index = 5)
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @AntColumn(name = "status", index = 6)
    public String getStatus() {
        return status;
    }

    @AntColumn(name = "status", index = 6)
    public void setStatus(String status) {
        this.status = status;
    }

    @AntColumn(name = "group_code", index = 7)
    public String getGroupCode() {
        return groupCode;
    }

    @AntColumn(name = "group_code", index = 7)
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @AntColumn(name = "package_code", index = 8)
    public String getPackageCode() {
        return packageCode;
    }

    @AntColumn(name = "package_code", index = 8)
    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    @AntColumn(name = "charge_price", index = 9)
    public String getChargePrice() {
        return chargePrice;
    }

    @AntColumn(name = "charge_price", index = 9)
    public void setChargePrice(String chargePrice) {
        this.chargePrice = chargePrice;
    }

    @AntColumn(name = "reg_datetime", index = 10)
    public String getRegDatetime() {
        return regDatetime;
    }

    @AntColumn(name = "reg_datetime", index = 10)
    public void setRegDatetime(String regDatetime) {
        this.regDatetime = regDatetime;
    }

    @AntColumn(name = "sta_datetime", index = 11)
    public String getStaDatetime() {
        return staDatetime;
    }

    @AntColumn(name = "sta_datetime", index = 11)
    public void setStaDatetime(String staDatetime) {
        this.staDatetime = staDatetime;
    }

    @AntColumn(name = "end_datetime", index = 12)
    public String getEndDatetime() {
        return endDatetime;
    }

    @AntColumn(name = "end_datetime", index = 12)
    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    @AntColumn(name = "expire_datetime", index = 13)
    public String getExpireDatetime() {
        return expireDatetime;
    }

    @AntColumn(name = "expire_datetime", index = 13)
    public void setExpireDatetime(String expireDatetime) {
        this.expireDatetime = expireDatetime;
    }

    @AntColumn(name = "message_send", index = 14)
    public String getMessageSend() {
        return messageSend;
    }

    @AntColumn(name = "message_send", index = 14)
    public void setMessageSend(String messageSend) {
        this.messageSend = messageSend;
    }

    @AntColumn(name = "note", index = 16)
    public String getNote() {
        return note;
    }

    @AntColumn(name = "note", index = 16)
    public void setNote(String note) {
        this.note = note;
    }

    @AntColumn(name = "process_status", index = 15)
    public Integer getProcessStatus() {
        return processStatus;
    }

    @AntColumn(name = "process_status", index = 15)
    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    @AntColumn(name = "trans_id", index = 18)
    public String getTransId() {
        return transId;
    }

    @AntColumn(name = "trans_id", index = 18)
    public void setTransId(String transId) {
        this.transId = transId;
    }

    @AntColumn(name = "keyword", index = 19)
    public String getKeyword() {
        return keyword;
    }

    @AntColumn(name = "keyword", index = 19)
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @AntColumn(name = "promo_id", index = 20)
    public String getPromoId() {
        return promoId;
    }

    @AntColumn(name = "promo_id", index = 20)
    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }
}
