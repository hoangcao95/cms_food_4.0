package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;
import java.util.Date;

@AntTable(name = "topup_first_reg", key = "")
public class TopupFirstReg implements Serializable {
    private String msisdn;
    private String total;
    private String createdDate;
    private String sumTime;

    @AntColumn(name = "msisdn", index = 0)
    public String getMsisdn() {
        return msisdn;
    }

    @AntColumn(name = "msisdn", index = 0)
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @AntColumn(name = "total", index = 1)
    public String getTotal() {
        return total;
    }

    @AntColumn(name = "total", index = 1)
    public void setTotal(String total) {
        this.total = total;
    }

    @AntColumn(name = "created_date", index = 2)
    public String getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date", index = 2)
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @AntColumn(name = "sum_time", index = 3)
    public String getSumTime() {
        return sumTime;
    }

    @AntColumn(name = "sum_time", index = 3)
    public void setSumTime(String sumTime) {
        this.sumTime = sumTime;
    }
}
