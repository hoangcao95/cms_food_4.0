package vn.vano.cms.thread;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import vn.vano.cms.common.Common;
import vn.vano.cms.common.Constants;
import vn.vano.cms.common.IGsonBase;
import vn.vano.cms.jpa.*;
import vn.vano.cms.service.CommonService;
import vn.yotel.commons.context.AppContext;
import vn.yotel.commons.exception.AppException;
import vn.yotel.thread.ManageableThread;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class MoRequestProcess extends ManageableThread implements IGsonBase {
    protected final Logger LOG = LoggerFactory.getLogger(MoRequestProcess.class);

    protected CommonService commonService;
    protected ConcurrentLinkedQueue<CoreMoQueue> moProcessQueue;
    protected Object moProcessQueueNotifier;
    protected ConcurrentLinkedQueue<CoreMtQueue> mtProcessCSPQueue;
    protected Object mtProcessCSPQueueNotifier;
    protected List<CorePromotion> lstPromo;
    protected List<CorePromotionDetail> lstPromoDetail;
    protected List<CoreSmsSyntax> lstSmsSyntax;
    protected DateTime dtCurrent;

    @Override
    protected void initializeSession() throws AppException {
        try {
            String transId = Common.randomString(Constants.TRANS_ID_LENGTH);
            LOG.debug(transId + "::BEGIN::initializeSession");
            super.initializeSession();
            moProcessQueue = (ConcurrentLinkedQueue<CoreMoQueue>) AppContext.getBean("moProcessQueue");
            moProcessQueueNotifier = AppContext.getBean("moProcessQueueNotifier");
            mtProcessCSPQueue = (ConcurrentLinkedQueue<CoreMtQueue>) AppContext.getBean("mtProcessCSPQueue");
            mtProcessCSPQueueNotifier = AppContext.getBean("mtProcessCSPQueueNotifier");
            commonService = (CommonService) AppContext.getBean("commonService");

            lstPromo = new ArrayList<>();
            lstPromoDetail = new ArrayList<>();

            List<Object[]> lstPromoObj = commonService.findBySQL(transId,
                    "SELECT * FROM core_promotion WHERE (end_time IS NULL OR end_time >= CURRENT_DATE()) AND is_main = 0", null);
            if (lstPromoObj != null && !lstPromoObj.isEmpty()) {
                lstPromoObj.stream().forEach(item -> {
                    CorePromotion promoBean = new CorePromotion();
                    promoBean = Common.convertToBean(item, promoBean);
                    lstPromo.add(promoBean);
                });
            } else {
                //Lay ra CTKM chinh cua he thong
                lstPromoObj = commonService.findBySQL(transId,
                        "SELECT * FROM core_promotion WHERE is_main = 1", null);
                lstPromo.clear();
                if (lstPromoObj != null && !lstPromoObj.isEmpty()) {
                    lstPromoObj.stream().forEach(item -> {
                        CorePromotion promoBean = new CorePromotion();
                        promoBean = Common.convertToBean(item, promoBean);
                        lstPromo.add(promoBean);
                    });
                }
            }
            LOG.debug(transId + "::INIT::PROMOTION::" + GSON.toJson(lstPromo));

            if (lstPromo != null && !lstPromo.isEmpty()) {
                CorePromotion corePromotion = lstPromo.get(0);
                //Neu ton tai CTKM thi kiem tra xem cu phap DK nhan len co yc gui MT them ko
                List<Object[]> lstPromoDetailObj = commonService.findBySQL(transId,
                        "SELECT * FROM core_promotion_detail WHERE promo_id = ? ",
                        new String[]{String.valueOf(corePromotion.getId())});
                if (lstPromoDetailObj != null && !lstPromoDetailObj.isEmpty()) {
                    lstPromoDetailObj.stream().forEach(item -> {
                        CorePromotionDetail promoDetailBean = new CorePromotionDetail();
                        promoDetailBean = Common.convertToBean(item, promoDetailBean);
                        lstPromoDetail.add(promoDetailBean);
                    });
                }
            }
            LOG.debug(transId + "::INIT::PROMOTION_DETAIL::" + GSON.toJson(lstPromoDetail));

            //Lay danh sach cac cu phap tin nhan duoc khai bao
            List<Object[]> lstSmsSyntaxObj = commonService.findBySQL(transId, "SELECT * FROM core_sms_syntax WHERE status = 1", null);
            lstSmsSyntax = new ArrayList<>();
            if (lstSmsSyntaxObj != null && !lstSmsSyntaxObj.isEmpty()) {
                lstSmsSyntaxObj.stream().forEach(item -> {
                    CoreSmsSyntax bean = new CoreSmsSyntax();
                    bean = Common.convertToBean(item, bean);
                    lstSmsSyntax.add(bean);
                });
            }
            LOG.debug(transId + "::INIT::SMS_SYNTAX::" + GSON.toJson(lstSmsSyntax));
            LOG.debug(transId + "::END::initializeSession");
        } catch (Exception ex) {
            LOG.error("", ex);
        }
    }

    @Override
    protected boolean processSession() throws AppException {
        try {
            while (!requireStop) {
                String transId = Common.randomString(Constants.TRANS_ID_LENGTH);
                CoreMoQueue bean = moProcessQueue.poll();

                if(bean != null) {
                    if(!Common.isBlank(bean.getTransId())) {
                        transId = bean.getTransId();
                    }
                    LOG.info(transId + "::BEGIN::moProcessQueue");

                    if (bean.getProcessStatus().intValue() == Constants.CoreMo.ProcessStatus.EXECUTED) {
                        //Luu log MO vao bang core_mo_sms
                        logCoreMoSms(transId, bean);
                        LOG.debug(transId + "::MO da duoc xu ly xong");
                        continue;
                    }

                    //Chi check syntax MO cac truong hop MO khong phai gia han va MO noi dung
                    if (!bean.getNote().equals(Constants.CoreMo.Status.EXTEND_NAME) && !bean.getNote().equals(Constants.CoreMo.Status.CONTENT_NAME)) {
                        //Neu syntax MO khong chinh xac thi log MO va gui MT thong bao sai
                        if (!Common.validateSmsSyntax(lstSmsSyntax, bean.getOrgRequest())) {
                            //Luu log MO vao bang core_mo_sms
                            logCoreMoSms(transId, bean);

                            //Gui MT thong bao cu phap khai bao khong hop le
                            CoreMtQueue mtQueue = new CoreMtQueue();
                            BeanUtils.copyProperties(bean, mtQueue);
                            mtQueue.setCommandCode(Constants.CoreMo.Command.CMD_ERROR);
                            mtQueue.setProcessStatus(Constants.CoreMo.ProcessStatus.EXECUTED);
                            mtQueue.setKeyword(Constants.CoreMt.Keyword.SYNTAX_INVALID);
                            mtQueue.setMessageSend("");
                            //Day tin MT di de xu ly
                            mtProcessCSPQueue.offer(mtQueue);
                            synchronized (mtProcessCSPQueueNotifier) {
                                mtProcessCSPQueueNotifier.notifyAll();
                            }

                            LOG.debug(transId + "::MO dang ky khong hop le");
                            continue;
                        }
                    }

                    switch (bean.getNote()) {
                        case Constants.CoreMo.Status.REG_NAME:
                            processMORegister(transId, bean);
                            break;
                        case Constants.CoreMo.Status.EXTEND_NAME:
                            processMOExtend(transId, bean);
                            break;
                        case Constants.CoreMo.Status.CANCEL_NAME:
                            processMOCancel(transId, bean);
                            break;
                        case Constants.CoreMo.Status.CONFIRM_NAME:
                            processMOConfirm(transId, bean);
                            break;
                        case Constants.CoreMo.Status.CONTENT_NAME:
                            processMOContent(transId, bean);
                            break;
                        default:
                            processMODefault(transId, bean);
                            break;
                    }

                    LOG.info(transId + "::END::moProcessQueue");
                }
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        }
        return true;
    }

    /**
     * Ham thuc hien dang ky cho thue bao
     * @param transId
     * @param bean
     */
    private void processMORegister(String transId, CoreMoQueue bean) {
        LOG.debug(transId + "::BEGIN::processMORegister");
        CoreSubscriber subscriber = null;
        try {
            //Kiem tra xem thue bao DK len da dang ky len he thong chua
            List<Object[]> lstSubObj = commonService.findBySQL(transId,
                    "SELECT * FROM core_subscriber WHERE msisdn = ? AND package_code = ? ", new String[]{bean.getMsisdn(), Common.upper(bean.getPackageCode())});

            if (lstSubObj != null && !lstSubObj.isEmpty()) {
                subscriber = new CoreSubscriber();
                subscriber = Common.convertToBean(lstSubObj.get(0), subscriber);
            }

            //Neu khong ton tai thong tin TB thi tao moi Subscriber
            dtCurrent = new DateTime();
            //Lay gia tri regDatetime de dong bo thoi gian DK voi CCSP
            String regDatetime = bean.getRegDatetime();
            DateTime dtReg = new DateTime();
            if(!Common.isBlank(regDatetime)) {
                Date dt = Common.strToDate(regDatetime, Common.CSP_DATE_DDMMYYYYHHmmss);
                dtReg = dtReg.withMillis(dt.getTime());
            } else {
                dtReg = dtCurrent;
            }

            //Lay gia tri expireDatetime de dong bo thoi gian het han goi voi CCSP
            String expireDatetime = bean.getExpireDatetime();
            DateTime dtExpire = new DateTime();
            if(!Common.isBlank(expireDatetime)) {
                Date dt = Common.strToDate(expireDatetime, Common.CSP_DATE_DDMMYYYYHHmmss);
                dtExpire = dtExpire.withMillis(dt.getTime());
            }

            if (subscriber == null) {
                subscriber = new CoreSubscriber();
                subscriber.setMsisdn(bean.getMsisdn());
                subscriber.setMpin(Common.randomDigit(Constants.MPIN_LENGTH));
                subscriber.setPackageCode(Common.upper(bean.getPackageCode()));
                subscriber.setActiveChannel(Common.upper(bean.getChannel()));
                subscriber.setStatus(Constants.Subscriber.ACTIVE);
                subscriber.setCreatedDate(Common.truncDate(dtReg));
                subscriber.setCreatedTime(new Timestamp(dtReg.getMillis()));
                subscriber.setActiveTime(new Timestamp(dtReg.getMillis()));
                subscriber.setExpireTime(new Timestamp(dtExpire.getMillis()));
                subscriber.setPromoId(Common.isEmpty(lstPromo) ? 0L : lstPromo.get(0).getId());
                subscriber.setMessage(bean.getOrgRequest());
                subscriber.setNote(bean.getTransId());
                commonService.create(transId, subscriber);

                //Log thong tin tao Sub vao bang
                logSubcriber(transId, subscriber, Constants.SubscriberLog.ACTION_REGNEW);

                //LOG charge cuoc
                logChargeSubsriber(Constants.ChargeLog.ACTION_REG, bean.getChargePrice(), 1,
                        Constants.ChargeLog.DEFAULT_RESULT,  null, bean, subscriber);
            } else {
                //Neu trang thai khac ACTIVE thi cap nhat lai trang thai thanh ACTIVE
                if (subscriber.getStatus().intValue() != Constants.Subscriber.ACTIVE.intValue()) {
                    subscriber.setMpin(Common.randomDigit(Constants.MPIN_LENGTH));
                    subscriber.setActiveChannel(Common.upper(bean.getChannel()));
                    subscriber.setStatus(Constants.Subscriber.ACTIVE);
                    subscriber.setActiveTime(new Timestamp(dtReg.getMillis()));
                    subscriber.setExpireTime(new Timestamp(dtExpire.getMillis()));
                    subscriber.setPromoId(Common.isEmpty(lstPromoDetail) ? 0L : lstPromoDetail.get(0).getPromoId());
                    subscriber.setMessage(bean.getOrgRequest());
                    subscriber.setNote(bean.getTransId());
                    subscriber.setDeactiveChannel(null);
                    subscriber.setDeactiveTime(null);

                    commonService.update(transId, subscriber, null, null, false, true);

                    //Log thong tin thay doi Sub vao bang
                    logSubcriber(transId, subscriber, Constants.SubscriberLog.ACTION_RENEW);

                    //LOG charge cuoc
                    logChargeSubsriber(Constants.ChargeLog.ACTION_REG, bean.getChargePrice(), 1,
                            Constants.ChargeLog.DEFAULT_RESULT,  null, bean, subscriber);
                }
            }

            //Kiem tra xem MO nhan len co yc gui MT2,MT3 khong
            String smsContent = bean.getOrgRequest().toUpperCase();
            if(lstPromoDetail!= null && !lstPromoDetail.isEmpty()) {
                //Lay ra thong tin CTKM cua cu phap MO
                List<CorePromotionDetail> lst = lstPromoDetail.stream().filter(item -> item.getSmsSyntax().equals(smsContent)).collect(Collectors.toList());
                if(lst != null && !lst.isEmpty()) {
                    CorePromotionDetail promoDetail = lst.get(0);
                    if(promoDetail != null) {
                        //Neu tin nhan DK nay co YC gui MT them thi quet URL gui MT va goi
                        if(!Common.isBlank(promoDetail.getSendMT1())) {
                            LOG.info(transId + "::CALL::" + promoDetail.getSendMT1());
                        }
                        if(!Common.isBlank(promoDetail.getSendMT2())) {
                            safeSleep(100);
                            LOG.info(transId + "::CALL::" + promoDetail.getSendMT2());
                        }
                        if(!Common.isBlank(promoDetail.getSendMT3())) {
                            safeSleep(200);
                            LOG.info(transId + "::CALL::" + promoDetail.getSendMT3());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            //Luu log MO vao bang core_mo_sms
            logCoreMoSms(transId, bean);
            LOG.debug(transId + "::END::processMORegister");
        }
    }

    /**
     * Ham thuc hien gia han cho thue bao
     * @param transId
     * @param bean
     */
    private void processMOExtend(String transId, CoreMoQueue bean) {
        LOG.debug(transId + "::BEGIN::processMOExtend");
        CoreSubscriber subscriber = null;
        try {
            //Kiem tra xem thue bao DK len da dang ky len he thong chua
            List<Object[]> lstSubObj = commonService.findBySQL(transId,
                    "SELECT * FROM core_subscriber WHERE msisdn = ? AND package_code = ? ", new String[]{bean.getMsisdn(), Common.upper(bean.getPackageCode())});

            if (lstSubObj != null && !lstSubObj.isEmpty()) {
                subscriber = new CoreSubscriber();
                subscriber = Common.convertToBean(lstSubObj.get(0), subscriber);
            }

            dtCurrent = new DateTime();
            //Lay gia tri regDatetime de dong bo thoi gian DK voi CCSP
            String regDatetime = bean.getRegDatetime();
            DateTime dtReg = new DateTime();
            if(!Common.isBlank(regDatetime)) {
                Date dt = Common.strToDate(regDatetime, Common.CSP_DATE_DDMMYYYYHHmmss);
                dtReg = dtReg.withMillis(dt.getTime());
            } else {
                dtReg = dtCurrent;
            }

            //Lay gia tri expireDatetime de dong bo thoi gian het han goi voi CCSP
            String expireDatetime = bean.getExpireDatetime();
            DateTime dtExpire = new DateTime();
            if(!Common.isBlank(expireDatetime)) {
                Date dt = Common.strToDate(expireDatetime, Common.CSP_DATE_DDMMYYYYHHmmss);
                dtExpire = dtExpire.withMillis(dt.getTime());
            }

            //Neu ton tai thong tin thue bao va dang o trang thai ACTIVE thi tuc la CCSP dang goi Gia han
            if(subscriber != null && subscriber.getStatus().intValue() == Constants.Subscriber.ACTIVE) {
                //Update cac thong tin nay khi gia han
                CoreSubscriber subUpdate = new CoreSubscriber();
                subUpdate.setId(subscriber.getId());
                subUpdate.setActiveTime(new Timestamp(dtReg.getMillis()));
                subUpdate.setActiveChannel(Common.upper(bean.getChannel()));
                subUpdate.setExpireTime(new Timestamp(dtExpire.getMillis()));

                commonService.update(transId, subUpdate, null, null, false);

                logSubcriber(transId, subscriber, Constants.SubscriberLog.ACTION_EXTEND);

                //LOG charge cuoc
                logChargeSubsriber(Constants.ChargeLog.ACTION_REG, bean.getChargePrice(), 1,
                        Constants.ChargeLog.DEFAULT_RESULT,  null, bean, subscriber);
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            logCoreMoSms(transId, bean);
            LOG.debug(transId + "::END::processMOExtend");
        }
    }

    /**
     * Ham thuc hien huy cho thue bao
     * @param transId
     * @param bean
     */
    private void processMOCancel(String transId, CoreMoQueue bean) {
        LOG.debug(transId + "::BEGIN::processMOCancel");
        CoreSubscriber subscriber = null;
        try {
            //Kiem tra xem thue bao DK len da dang ky len he thong chua
            List<Object[]> lstSubObj = commonService.findBySQL(transId,
                    "SELECT * FROM core_subscriber WHERE msisdn = ? AND package_code = ? ", new String[]{bean.getMsisdn(), Common.upper(bean.getPackageCode())});

            if (lstSubObj != null && !lstSubObj.isEmpty()) {
                subscriber = new CoreSubscriber();
                subscriber = Common.convertToBean(lstSubObj.get(0), subscriber);
            }

            dtCurrent = new DateTime();

            String endDatetime = bean.getEndDatetime();
            DateTime dtEnd = new DateTime();
            if(!Common.isBlank(endDatetime)) {
                Date dt = Common.strToDate(endDatetime, Common.CSP_DATE_DDMMYYYYHHmmss);
                dtEnd = dtEnd.withMillis(dt.getTime());
            } else {
                dtEnd = dtCurrent;
            }

            //Neu ton tai thong tin thue bao va dang o trang thai ACTIVE thi tuc la CCSP dang goi Gia han
            if(subscriber != null && subscriber.getStatus().intValue() == Constants.Subscriber.ACTIVE) {
                CoreSubscriber subUpdate = new CoreSubscriber();
                subUpdate.setId(subscriber.getId());
                subUpdate.setStatus(Constants.Subscriber.DEACTIVE);
                subUpdate.setDeactiveTime(new Timestamp(dtEnd.getMillis()));
                subUpdate.setDeactiveChannel(Common.upper(bean.getChannel()));

                commonService.update(transId, subUpdate, null, null, false);

                subscriber.setMessage(bean.getOrgRequest());
                subscriber.setStatus(Constants.Subscriber.DEACTIVE);
                subscriber.setDeactiveTime(new Timestamp(dtEnd.getMillis()));
                subscriber.setDeactiveChannel(Common.upper(bean.getChannel()));
                subscriber.setNote(bean.getTransId());

                logSubcriber(transId, subscriber, Constants.SubscriberLog.ACTION_CANCEL);
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            logCoreMoSms(transId, bean);
            LOG.debug(transId + "::END::processMOCancel");
        }
    }

    private void processMOConfirm(String transId, CoreMoQueue bean) {
        LOG.debug(transId + "::BEGIN::processMOConfirm");
        try {
            LOG.info(GSON.toJson(bean));
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            LOG.debug(transId + "::END::processMOConfirm");
        }
    }

    private void processMOContent(String transId, CoreMoQueue bean) {
        LOG.debug(transId + "::BEGIN::processMOContent");
        try {
            CoreMtQueue mtQueue = new CoreMtQueue();
            BeanUtils.copyProperties(bean, mtQueue);
            mtQueue.setCommandCode(Constants.CoreMo.Command.CMD_CONTENT);
            mtQueue.setKeyword(Constants.CoreMt.Keyword.SYNTAX_CONTENT);
            mtQueue.setProcessStatus(Constants.CoreMo.ProcessStatus.EXECUTED);
            //Day tin MT di de xu ly
            mtProcessCSPQueue.offer(mtQueue);
            synchronized (mtProcessCSPQueueNotifier) {
                mtProcessCSPQueueNotifier.notifyAll();
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            LOG.debug(transId + "::END::processMOContent");
        }
    }

    /**
     * Ham mac thuc hien xu ly khi MO khong thuoc cac loai tren
     * @param transId
     * @param bean
     */
    private void processMODefault(String transId, CoreMoQueue bean) {
        LOG.debug(transId + "::BEGIN::processMODefault");
        try {
            CoreMtQueue mtQueue = new CoreMtQueue();
            BeanUtils.copyProperties(bean, mtQueue);
            //Neu khong phai tin noi dung thi xu ly MO lay ra command theo khai bao
            if(!bean.getNote().equals(Constants.CoreMo.Status.CONTENT_NAME)) {
                mtQueue.setCommandCode(Common.getSmsCommand(lstSmsSyntax, Common.upper(bean.getOrgRequest())));
                mtQueue.setKeyword(Common.getSmsCommand(lstSmsSyntax, Common.upper(bean.getOrgRequest())));

                //Kiem tra xem MO nhan len co yc gui MT2,MT3 khong
                String smsContent = bean.getOrgRequest().toUpperCase();
                if(lstPromoDetail!= null && !lstPromoDetail.isEmpty()) {
                    //Lay ra thong tin CTKM cua cu phap MO
                    List<CorePromotionDetail> lst = lstPromoDetail.stream().filter(item -> item.getSmsSyntax().equals(smsContent)).collect(Collectors.toList());
                    if(lst != null && !lst.isEmpty()) {
                        CorePromotionDetail promoDetail = lst.get(0);
                        if(promoDetail != null) {
                            //Neu tin nhan DK nay co YC gui MT them thi quet URL gui MT va goi
                            if(!Common.isBlank(promoDetail.getSendMT1())) {
                                LOG.info(transId + "::CALL::" + promoDetail.getSendMT1());
                            }
                            if(!Common.isBlank(promoDetail.getSendMT2())) {
                                safeSleep(100);
                                LOG.info(transId + "::CALL::" + promoDetail.getSendMT2());
                            }
                            if(!Common.isBlank(promoDetail.getSendMT3())) {
                                safeSleep(200);
                                LOG.info(transId + "::CALL::" + promoDetail.getSendMT3());
                            }
                        }
                    }
                }
            }
            mtQueue.setProcessStatus(Constants.CoreMo.ProcessStatus.EXECUTED);
            //Day tin MT di de xu ly
            mtProcessCSPQueue.offer(mtQueue);
            synchronized (mtProcessCSPQueueNotifier) {
                mtProcessCSPQueueNotifier.notifyAll();
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            LOG.debug(transId + "::END::processMODefault");
        }
    }

    /**
     * Ham ghi log MO vao bang core_mo_sms
     * @param transId
     * @param moQueue
     */
    private void logCoreMoSms(String transId, CoreMoQueue moQueue) {
        LOG.debug(transId + "::BEGIN::logCoreMoSms");
        try {
            CoreMOSms bean = new CoreMOSms();
            bean.setMsisdn(moQueue.getMsisdn());
            bean.setServiceCode(moQueue.getServiceCode());
            bean.setMessage(Common.upper(moQueue.getOrgRequest()));
            bean.setMessageOriginal(moQueue.getOrgRequest());
            bean.setCommand(Common.getSmsCommand(lstSmsSyntax, moQueue.getOrgRequest()));
            bean.setChannel(Common.upper(moQueue.getChannel()));
            bean.setCreatedDate(Common.truncDate(dtCurrent));
            bean.setCreatedTime(new Timestamp(dtCurrent.getMillis()));
            bean.setPromoId(Common.isEmpty(lstPromoDetail) ? 0L : lstPromoDetail.get(0).getPromoId());
            bean.setStatus(Constants.CoreMo.Status.ACTIVE);
            bean.setTransId(transId);

            commonService.create(transId, bean);
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            LOG.debug(transId + "::END::logCoreMoSms");
        }
    }

    /**
     * Ham thuc hien log thong tin thay doi cua SUBSCRIBER
     * @param transId
     * @param sub
     * @param action
     */
    private void logSubcriber(String transId, CoreSubscriber sub, String action) {
        LOG.debug(transId + "::BEGIN::logSubcriber");
        try {
            CoreSubscriberLog subLog = new CoreSubscriberLog();
            subLog.setMsisdn(sub.getMsisdn());
            subLog.setMpin(sub.getMpin());
            subLog.setPackageCode(sub.getPackageCode());
            subLog.setCreatedDate(sub.getCreatedDate());
            subLog.setCreatedTime(sub.getCreatedTime());
            subLog.setExpireTime(sub.getExpireTime());
            subLog.setActiveTime(sub.getActiveTime());
            subLog.setDeactiveTime(sub.getDeactiveTime());
            subLog.setActiveChannel(sub.getActiveChannel());
            subLog.setDeactiveChannel(sub.getDeactiveChannel());
            subLog.setStatus(sub.getStatus());
            subLog.setNote(sub.getNote());
            subLog.setCpId(sub.getCpId());
            subLog.setPromoId(sub.getPromoId());
            subLog.setIp(sub.getIp());
            subLog.setMessage(Common.upper(sub.getMessage()));
            subLog.setLogTime(new Timestamp(dtCurrent.getMillis()));
            subLog.setUserName(Constants.SubscriberLog.USER_SYS);
            subLog.setAction(action);

            commonService.create(transId, subLog);
            LOG.debug(transId + "::PARAM::" + GSON.toJson(subLog));
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            LOG.debug(transId + "::END::logSubcriber");
        }
    }

    private void logChargeSubsriber(String action, String amount, Integer callResult, String resultMessage,
                                    String note, CoreMoQueue moQueue, CoreSubscriber subs) {
        Integer chargePrice = Common.isBlank(amount) ? 0 : Integer.parseInt(amount);
        logChargeSubsriber(moQueue.getTransId(), subs.getMsisdn(), subs.getPackageCode(), action, moQueue.getChannel(), chargePrice,
                callResult, resultMessage, subs.getCpId(), note, subs.getPromoId());
    }

    private void logChargeSubsriber(String transId, String msisdn, String packageCode, String action, String channel,
                                    Integer amount, Integer callResult, String resultMessage, String cpId, String note,
                                    Long promoId){
        LOG.debug(transId + "::BEGIN::logChargeSubsriber");
        try {
            CoreChargeLog chargeLog = new CoreChargeLog();
            chargeLog.setMsisdn(msisdn);
            chargeLog.setPackageCode(packageCode);
            chargeLog.setAction(action);
            chargeLog.setAmount(amount);
            chargeLog.setChannel(channel);
            chargeLog.setCreatedDate(Common.truncDate(dtCurrent));
            chargeLog.setCreatedTime(new Timestamp(dtCurrent.getMillis()));
            chargeLog.setCallResult(callResult);
            chargeLog.setResultMessage(resultMessage);
            chargeLog.setStatus(Constants.ChargeLog.ACTIVE);
            chargeLog.setCpId(cpId);
            chargeLog.setNote(note);
            chargeLog.setPromoId(promoId);
            chargeLog.setTransId(transId);
            commonService.create(transId, chargeLog);
            LOG.debug(transId + "::PARAM::" + GSON.toJson(chargeLog));
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            LOG.debug(transId + "::END::logChargeSubsriber");
        }
    }
}
