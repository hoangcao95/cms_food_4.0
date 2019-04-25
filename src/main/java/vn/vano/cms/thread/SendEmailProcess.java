package vn.vano.cms.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vano.cms.common.IGsonBase;
import vn.yotel.commons.exception.AppException;
import vn.yotel.thread.ManageableThread;

public class SendEmailProcess extends ManageableThread implements IGsonBase {
    private final Logger LOG = LoggerFactory.getLogger(SendEmailProcess.class);

    @Override
    protected boolean processSession() throws AppException {
        LOG.info("BEGIN::------------------------------------------------------------");
        try {

        } catch (Exception ex) {
            LOG.error("", ex);
        }
        LOG.info("END::------------------------------------------------------------");
        return false;
    }
}
