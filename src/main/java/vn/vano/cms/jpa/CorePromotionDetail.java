package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;

@AntTable(name = "core_promotion_detail", key = "id")
public class CorePromotionDetail implements Serializable {
    private Long id;
    private Long promoId;
    private Long smsSyntaxId;
    private String smsSyntax;
    private String sendMT1;
    private String sendMT2;
    private String sendMT3;
    private String createdDate;
    private Integer status;

    public CorePromotionDetail() {

    }

    public CorePromotionDetail(Long id, Long promoId, Long smsSyntaxId, String smsSyntax, String sendMT1, String sendMT2, String sendMT3, String createdDate, Integer status) {
        this.id = id;
        this.promoId = promoId;
        this.smsSyntaxId = smsSyntaxId;
        this.smsSyntax = smsSyntax;
        this.sendMT1 = sendMT1;
        this.sendMT2 = sendMT2;
        this.sendMT3 = sendMT3;
        this.createdDate = createdDate;
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

    @AntColumn(name = "promo_id", index = 1)
    public Long getPromoId() {
        return promoId;
    }

    @AntColumn(name = "promo_id", index = 1)
    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }

    @AntColumn(name = "sms_syntax_id", index = 2)
    public Long getSmsSyntaxId() {
        return smsSyntaxId;
    }

    @AntColumn(name = "sms_syntax_id", index = 2)
    public void setSmsSyntaxId(Long smsSyntaxId) {
        this.smsSyntaxId = smsSyntaxId;
    }

    @AntColumn(name = "sms_syntax", index = 3)
    public String getSmsSyntax() {
        return smsSyntax;
    }

    @AntColumn(name = "sms_syntax", index = 3)
    public void setSmsSyntax(String smsSyntax) {
        this.smsSyntax = smsSyntax;
    }

    @AntColumn(name = "send_mt1", index = 4)
    public String getSendMT1() {
        return sendMT1;
    }

    @AntColumn(name = "send_mt1", index = 4)
    public void setSendMT1(String sendMT1) {
        this.sendMT1 = sendMT1;
    }

    @AntColumn(name = "send_mt2", index = 5)
    public String getSendMT2() {
        return sendMT2;
    }

    @AntColumn(name = "send_mt2", index = 5)
    public void setSendMT2(String sendMT2) {
        this.sendMT2 = sendMT2;
    }

    @AntColumn(name = "send_mt3", index = 6)
    public String getSendMT3() {
        return sendMT3;
    }

    @AntColumn(name = "send_mt3", index = 6)
    public void setSendMT3(String sendMT3) {
        this.sendMT3 = sendMT3;
    }

    @AntColumn(name = "created_date", index = 7)
    public String getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date", index = 7)
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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
