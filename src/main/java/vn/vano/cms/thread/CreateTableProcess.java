package vn.vano.cms.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vano.cms.common.Common;
import vn.vano.cms.service.CommonService;
import vn.yotel.commons.context.AppContext;
import vn.yotel.commons.exception.AppException;
import vn.yotel.thread.ManageableThread;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateTableProcess extends ManageableThread {
    private final Logger LOG = LoggerFactory.getLogger(CreateTableProcess.class);
    private final String[] ARR_TABLE_MONTHLY = {"core_mo_sms_{0}","core_mt_sms_{0}","core_charge_log_{0}","core_subscriber_log_{0}"};

    private CommonService commonService;
    private boolean isExistTable;
    private boolean isExistTable2;

    @Override
    protected void initializeSession() throws AppException {
        super.initializeSession();
        commonService = (CommonService) AppContext.getBean("commonService");
    }

    @Override
    protected boolean processSession() throws AppException {
        try {
            for (int i = 0; i < ARR_TABLE_MONTHLY.length; i++) {
                createTable(ARR_TABLE_MONTHLY[i]);
            }

            if(isExistTable && isExistTable2) {
                safeSleep(60 * 1000);
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return false;
    }

    private void createTable(String tableTemplate) {
        try {
            String strMonth = Common.dateToString(new Date(), Common.DATE_YYYYMM);
            String strNextMonth = Common.dateToString(Common.addTime(new Date(), Calendar.MONTH, 1), Common.DATE_YYYYMM);
            String tableCore = tableTemplate.replace("_{0}", "");

            String tableName = MessageFormat.format(tableTemplate, strMonth);
            String tableNameNextMonth = MessageFormat.format(tableTemplate, strNextMonth);

            isExistTable = commonService.isExistTable(tableName);
            if(!isExistTable) {
                try {
                    commonService.createTable(tableName, tableCore);
                    LOG.info("CREATE TABLE " + tableName + " DONE");
                } catch (Exception ex) {
                    LOG.error("ERR_CREATE_TALE " + tableName);
                    LOG.error("", ex);
                }
            }

            isExistTable2 = commonService.isExistTable(tableNameNextMonth);
            if(!isExistTable2) {
                try {
                    commonService.createTable(tableNameNextMonth, tableCore);
                    LOG.info("CREATE TABLE " + tableNameNextMonth + " DONE");
                } catch (Exception ex) {
                    LOG.error("ERR_CREATE_TALE " + tableNameNextMonth);
                    LOG.error("", ex);
                }
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        }
    }
}
