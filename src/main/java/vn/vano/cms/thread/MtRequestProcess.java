package vn.vano.cms.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vano.cms.common.ChargingCSPApi;
import vn.vano.cms.common.Common;
import vn.vano.cms.common.Constants;
import vn.vano.cms.common.IGsonBase;
import vn.vano.cms.jpa.*;
import vn.vano.cms.service.CommonService;
import vn.yotel.commons.context.AppContext;
import vn.yotel.commons.exception.AppException;
import vn.yotel.thread.ManageableThread;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MtRequestProcess extends ManageableThread implements IGsonBase {
    protected final Logger LOG = LoggerFactory.getLogger(MtRequestProcess.class);

    protected CommonService commonService;
    protected ConcurrentLinkedQueue<CoreMtQueue> mtProcessCSPQueue;
    protected Object mtProcessCSPQueueNotifier;
    protected ChargingCSPApi cspApi;
    protected String SERVER_MODE = "PROD";

    @Override
    protected void initializeSession() throws AppException {
        super.initializeSession();
        mtProcessCSPQueue = (ConcurrentLinkedQueue<CoreMtQueue>) AppContext.getBean("mtProcessCSPQueue");
        mtProcessCSPQueueNotifier = AppContext.getBean("mtProcessCSPQueueNotifier");
        commonService = (CommonService) AppContext.getBean("commonService");
        cspApi = (ChargingCSPApi) AppContext.getBean("chargingCSPApi");
    }

    @Override
    protected void loadParameters() throws AppException {
        if(this.params != null) {
            this.SERVER_MODE = this.getParamAsString("profile");
        }
    }

    @Override
    protected boolean processSession() throws AppException {
        try {
            while (!requireStop) {
                String transId = Common.randomString(Constants.TRANS_ID_LENGTH);
                CoreMtQueue bean = mtProcessCSPQueue.poll();

                if(bean != null) {
                    if(!Common.isBlank(bean.getTransId())) {
                        transId = bean.getTransId();
                    }
                    LOG.info(transId + "::BEGIN::mtProcessCSPQueue");

                    if (SERVER_MODE.equals("DEV")) {
                        cspApi.sendMT(bean);
                    } else {
                        if (bean.getDelay() > 0) {
                            cspApi.sendMTDelay(bean, false, bean.getDelay());
                        } else {
                            cspApi.sendMT(bean, false);
                        }
                    }

                    LOG.info(transId + "::END::mtProcessCSPQueue");
                }
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        }
        return true;
    }
}
