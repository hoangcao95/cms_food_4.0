package vn.vano.cms.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import vn.vano.cms.common.*;
import vn.vano.cms.jpa.CoreMoQueue;
import vn.vano.cms.service.CommonService;
import vn.yotel.commons.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Path(value = "/")
@Produces(value = { MediaType.APPLICATION_JSON })
@Consumes(value = { MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED })
public class MBFResource implements IGsonBase {
    private final Logger LOG = LoggerFactory.getLogger(MBFResource.class);

    @Resource
    private CommonService commonService;

    @Resource
    ConcurrentLinkedQueue<CoreMoQueue> moProcessQueue;
    @Resource
    Object moProcessQueueNotifier;

    @POST
    @Path(value = "/updatePackage")
    public ResponseData updatePackage(@Context HttpServletRequest req,
                                      @FormParam("isdn") String isdn,
                                      @FormParam("serviceCode") String serviceCode,
                                      @FormParam("commandCode") String commandCode,
                                      @FormParam("org_request") String orgRequest,
                                      @FormParam("channel") String channel,
                                      @FormParam("status") String status,
                                      @FormParam("groupCode") String groupCode,
                                      @FormParam("packageCode") String packageCode,
                                      @FormParam("charge_price") String chargePrice,
                                      @FormParam("regDatetime") String regDatetime,
                                      @FormParam("staDatetime") String staDatetime,
                                      @FormParam("endDatetime") String endDatetime,
                                      @FormParam("expireDatetime") String expireDatetime,
                                      @FormParam("message_send") String messageSend) {
        String transId = Common.randomString(Constants.TRANS_ID_LENGTH);
        LOG.debug(transId + "::BEGIN::updatePackage");
        CoreMoQueue moQueue = new CoreMoQueue();
        // status 0: Gia hạn, 1: Đăng kí, 3: Hủy, 2: Chờ confirm
        try {
            moQueue.setMsisdn(PhoneUtils.normalizeMsIsdn(isdn));
            moQueue.setServiceCode(StringUtils.nvl(serviceCode, ""));
            moQueue.setCommandCode(StringUtils.nvl(commandCode, ""));
            moQueue.setOrgRequest(StringUtils.nvl(orgRequest, ""));
            moQueue.setChannel(StringUtils.nvl(channel, ""));
            moQueue.setStatus(StringUtils.nvl(status, ""));
            moQueue.setGroupCode(StringUtils.nvl(groupCode, ""));
            moQueue.setPackageCode(StringUtils.nvl(packageCode, ""));
            moQueue.setChargePrice(StringUtils.nvl(chargePrice, ""));
            moQueue.setRegDatetime(StringUtils.nvl(regDatetime, null));
            moQueue.setStaDatetime(StringUtils.nvl(staDatetime, null));
            moQueue.setEndDatetime(StringUtils.nvl(endDatetime, null));
            moQueue.setExpireDatetime(StringUtils.nvl(expireDatetime, null));
            moQueue.setMessageSend(StringUtils.nvl(messageSend, ""));
            moQueue.setProcessStatus(Constants.CoreMo.ProcessStatus.DEFAULT);
            String note = "";
            if (!Common.isBlank(moQueue.getEndDatetime()) || Constants.CoreMo.Status.CANCEL.equals(status)) {
                //Neu CCSP truyen sang endDatetime thi trang thai la HUY
                note = Constants.CoreMo.Status.CANCEL_NAME;
            } else if (Constants.CoreMo.Status.EXTEND.equals(status)) {
                //Neu trang thai la GIA HAN
                note = Constants.CoreMo.Status.EXTEND_NAME;
            } else if (Constants.CoreMo.Status.REG.equals(status)) {
                //Neu trang thai la DANG KY
                note = Constants.CoreMo.Status.REG_NAME;
            } else {
                //Neu trang thai la CHO XAC NHAN
                note = Constants.CoreMo.Status.CONFIRM_NAME;
            }
            moQueue.setNote(note);
            moQueue.setTransId(transId);
        } catch (Exception ex) {
            LOG.error(transId, ex);
            return ResponseData.buildResponse(ResponseData.ERROR, "Khong dang ky duoc goi cuoc");
        } finally {
            try {
                LOG.debug(transId + "::" + GSON.toJson(moQueue));
                moProcessQueue.offer(moQueue);
                synchronized (moProcessQueueNotifier) {
                    moProcessQueueNotifier.notifyAll();
                }
            } catch (Exception e) {
                LOG.error(transId, e);
            }
            LOG.debug(transId + "::END::updatePackage");
        }
        return ResponseData.buildResponse(ResponseData.SUCCESS, "OK");
    }

    @POST
    @Path(value = "/forwardMessage")
    public ResponseData forwardMessage(
            @Context HttpServletRequest req,
            @FormParam("isdn") String isdn,
            @FormParam("content") String content,
            @DefaultValue("") @FormParam("request_id") String requestId) {
        String transId = Common.randomString(Constants.TRANS_ID_LENGTH);
        LOG.debug(transId + "::BEGIN::forwardMessage");
        CoreMoQueue moQueue = null;
        try {
            moQueue = new CoreMoQueue();
            moQueue.setMsisdn(PhoneUtils.normalizeMsIsdn(isdn));
            moQueue.setOrgRequest(StringUtils.nvl(content, ""));
            moQueue.setProcessStatus(Constants.CoreMo.ProcessStatus.DEFAULT);
            moQueue.setNote(Constants.CoreMo.Status.NORMAL_NAME);
            moQueue.setChannel(Constants.CoreMo.Channel.CHANNEL_SMS);
            moQueue.setTransId(transId);
        } catch (Exception ex) {
            LOG.error(transId, ex);
        } finally {
            if (moQueue != null) {
                commonService.create(transId, moQueue);
            }
            LOG.debug(transId + "::END::forwardMessage");
        }
        return ResponseData.buildResponse(ResponseData.SUCCESS, "OK");
    }

    @GET
    @Path(value = "/sendMessage")
    public ResponseData sendMessage(@Context HttpServletRequest req,
                                    @QueryParam("isdn") String isdn,
                                    @QueryParam("content") String content,
                                    @DefaultValue("") @QueryParam("request_id") String requestId,
                                    @DefaultValue(Constants.CoreMo.Channel.CHANNEL_API) @QueryParam("channel") String channel) {
        String transId = Common.randomString(Constants.TRANS_ID_LENGTH);
        LOG.debug(transId + "::END::sendMessage");
        CoreMoQueue moQueue = null;
        try {
            moQueue = new CoreMoQueue();
            moQueue.setMsisdn(PhoneUtils.normalizeMsIsdn(isdn));
            moQueue.setOrgRequest(StringUtils.nvl(content, ""));
            moQueue.setProcessStatus(Constants.CoreMo.ProcessStatus.DEFAULT);
            moQueue.setNote(Constants.CoreMo.Status.CONTENT_NAME);
            moQueue.setChannel(channel);
            moQueue.setTransId(transId);
            moQueue.setProcessStatus(Constants.CoreMo.ProcessStatus.DEFAULT);
        } catch (Exception ex) {
            LOG.error(transId, ex);
            return ResponseData.buildResponse(ResponseData.ERROR, ex.getMessage());
        } finally {
            if (moQueue != null) {
                commonService.create(transId, moQueue);
            }
            LOG.debug(transId + "::END::sendMessage");
        }
        return ResponseData.buildResponse(ResponseData.SUCCESS, "OK");
    }
}
