package vn.vano.cms.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import vn.vano.cms.common.Common;
import vn.vano.cms.common.Constants;
import vn.vano.cms.common.IGsonBase;
import vn.vano.cms.common.ResponseData;
import vn.vano.cms.jpa.TopupFirstReg;
import vn.vano.cms.service.CommonService;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
@Path(value = "/yomi")
@Produces(value = { MediaType.APPLICATION_JSON })
@Consumes(value = { MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED })
public class YomiResource implements IGsonBase {
    private final Logger LOG = LoggerFactory.getLogger(MBFResource.class);

    @Resource
    private CommonService commonService;

    @GET
    @Path(value = "/topup/list")
    public ResponseData listTopup() {
        String transId = Common.randomString(Constants.TRANS_ID_LENGTH);
        List<TopupFirstReg> lstTopup = new ArrayList<>();
        try {
            List<Object[]> lstObj = commonService.findBySQL(transId, "SELECT * FROM topup_first_reg WHERE date(sum_time) = CURRENT_DATE ", null);
            if (lstObj != null && !lstObj.isEmpty()) {
                lstObj.stream().forEach(item -> {
                    TopupFirstReg bean = new TopupFirstReg();
                    bean = Common.convertToBean(item, bean);
                    lstTopup.add(bean);
                });
            }
            return ResponseData.buildResponse(ResponseData.SUCCESS, "OK", lstTopup, TopupFirstReg.class);
        } catch (Exception ex) {
            LOG.error("", ex);
            return ResponseData.buildResponse(ResponseData.ERROR, "ERROR::" + ex.getMessage());
        }
    }
}
