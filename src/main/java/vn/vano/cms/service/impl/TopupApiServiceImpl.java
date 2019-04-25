//package vn.vano.cms.service.impl;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import vn.vano.cms.common.*;
//import vn.vano.cms.jpa.TopupSubs;
//import vn.vano.cms.service.TopupApiService;
//import vn.vano.cms.service.TopupSubsService;
//import vn.yotel.admin.jpa.AuthUser;
//import vn.yotel.admin.service.AuthUserService;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.text.MessageFormat;
//import java.util.Date;
//
//@Service(value = "topupApiService")
//public class TopupApiServiceImpl implements TopupApiService, IGsonBase {
//    private final Logger LOG = LoggerFactory.getLogger(TopupApiServiceImpl.class);
//
//    @Autowired
//    TopupSubsService topupSubsService;
//    @Autowired
//    AuthUserService authUserService;
//
//    @Override
//    public int topupAdmin(String userLogin, String authKey, String isdn, String amount, String cpId, String serviceCode, String note) throws Exception {
//        if (!validUser(userLogin, authKey)) {
//            LOG.info("ERROR: User " + userLogin + " not valid");
//            return -2;
//        }
//        TopupSubs topupSubs = new TopupSubs();
//        HttpURLConnection httpConnection = null;
//        InputStream is = null;
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//        try {
//            String msisdn = PhoneUtils.normalizeMsIsdn(isdn);
//
//            topupSubs.setMsisdn("0".concat(msisdn));
//            topupSubs.setCreatedDate(new Date());
//            String strServiceCode = "ILO".equals(serviceCode) || "LIV".equals(serviceCode) || "FR".equals(serviceCode) ? "VTV" : serviceCode;
//            topupSubs.setServiceCode(serviceCode);
//            topupSubs.setNote(strServiceCode.concat("|").concat(note));
//            topupSubs.setAmount(amount);
//            topupSubs.setUserName(userLogin);
//
//            String urlTopup = MessageFormat.format(Constants.TopupConstant.URL_TOPUP, msisdn, amount, cpId, strServiceCode,
//                    "TP" + strServiceCode.toUpperCase(),"UTF-8");
//            URL url = new URL(urlTopup);
//            httpConnection = (HttpURLConnection) url.openConnection();
//            httpConnection.setRequestMethod("GET");
//
//            is = httpConnection.getInputStream();
//            isr = new InputStreamReader(is);
//            br = new BufferedReader(isr);
//            String line = null;
//            StringBuilder sb = new StringBuilder();
//
//            while ((line = br.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            String result = sb.toString().trim();
//            if(result.startsWith("OK")) {
//                topupSubs.setTopupResult(MessageContants.Result.SUCCESS);
//                topupSubs.setErrorResult(result);
//                return 1;
//            } else {
//                topupSubs.setTopupResult(MessageContants.Result.ERROR);
//                topupSubs.setErrorResult(result);
//                return 0;
//            }
//        } catch (Exception ex) {
//            topupSubs.setTopupResult(MessageContants.Result.EXCEPTION);
//            LOG.error("", ex);
//            return -1;
//        } finally {
//            LOG.info("CREATE_TOPSUBS: " + GSON.toJson(topupSubs));
//            topupSubsService.create(topupSubs);
//            try {
//                if (br != null) {
//                    br.close();
//                }
//                if (isr != null) {
//                    isr.close();
//                }
//                if (is != null) {
//                    is.close();
//                }
//                if(httpConnection != null) {
//                    httpConnection.disconnect();
//                }
//            } catch (Exception ex) {
//                LOG.error("", ex);
//            }
//        }
//    }
//
//    @Override
//    public int topupAdmin(String userLogin, String authKey, String isdn, String amount, String cpId, String serviceCode, String note, String createdDate) {
//        if (!validUser(userLogin, authKey)) {
//            LOG.info("ERROR: User " + userLogin + " not valid");
//            return -2;
//        }
//        TopupSubs topupSubs = new TopupSubs();
//        HttpURLConnection httpConnection = null;
//        InputStream is = null;
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//        try {
//            String msisdn = PhoneUtils.normalizeMsIsdn(isdn);
//
//            topupSubs.setMsisdn("0".concat(msisdn));
//            try {
//                topupSubs.setCreatedDate(Common.strToDate(createdDate, Common.DATE_YYYYMMDDHHMMSS_WP));
//            } catch (Exception ex) {
//                topupSubs.setCreatedDate(new Date());
//            }
//            String strServiceCode = "ILO".equals(serviceCode) || "LIV".equals(serviceCode) || "FR".equals(serviceCode) ? "VTV" : serviceCode;
//            topupSubs.setServiceCode(serviceCode);
//            topupSubs.setNote(strServiceCode.concat("|").concat(note));
//            topupSubs.setAmount(amount);
//            topupSubs.setUserName(userLogin);
//
//            String urlTopup = MessageFormat.format(Constants.TopupConstant.URL_TOPUP, msisdn, amount, cpId, strServiceCode,
//                    "TP" + strServiceCode.toUpperCase(),"UTF-8");
//            URL url = new URL(urlTopup);
//            httpConnection = (HttpURLConnection) url.openConnection();
//            httpConnection.setRequestMethod("GET");
//
//            is = httpConnection.getInputStream();
//            isr = new InputStreamReader(is);
//            br = new BufferedReader(isr);
//            String line = null;
//            StringBuilder sb = new StringBuilder();
//
//            while ((line = br.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            String result = sb.toString().trim();
//            if(result.startsWith("OK")) {
//                topupSubs.setTopupResult(MessageContants.Result.SUCCESS);
//                topupSubs.setErrorResult(result);
//                return 1;
//            } else {
//                topupSubs.setTopupResult(MessageContants.Result.ERROR);
//                topupSubs.setErrorResult(result);
//                return 0;
//            }
//        } catch (Exception ex) {
//            topupSubs.setTopupResult(MessageContants.Result.EXCEPTION);
//            LOG.error("", ex);
//            return -1;
//        } finally {
//            LOG.info("CREATE_TOPSUBS: " + GSON.toJson(topupSubs));
//            topupSubsService.create(topupSubs);
//            try {
//                if (br != null) {
//                    br.close();
//                }
//                if (isr != null) {
//                    isr.close();
//                }
//                if (is != null) {
//                    is.close();
//                }
//                if(httpConnection != null) {
//                    httpConnection.disconnect();
//                }
//            } catch (Exception ex) {
//                LOG.error("", ex);
//            }
//        }
//    }
//
//    @Override
//    public int topupCSKH(String userLogin, String authKey, String isdn, String amount, String cpId, String serviceCode, String note) throws Exception {
//        if (!validUser(userLogin, authKey)) {
//            LOG.info("ERROR: User " + userLogin + " not valid");
//            return -2;
//        }
//        TopupSubs topupSubs = new TopupSubs();
//        HttpURLConnection httpConnection = null;
//        InputStream is = null;
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//        try {
//            String msisdn = PhoneUtils.normalizeMsIsdn(isdn);
//
//            topupSubs.setMsisdn("0".concat(msisdn));
//            topupSubs.setCreatedDate(new Date());
//            String strServiceCode = "ILO".equals(serviceCode) || "LIV".equals(serviceCode) || "FR".equals(serviceCode) ? "VTV" : serviceCode;
//            topupSubs.setServiceCode(serviceCode);
//            topupSubs.setNote(strServiceCode.concat("|").concat(note));
//            topupSubs.setAmount(amount);
//            topupSubs.setUserName(userLogin);
//
//            String urlTopup = MessageFormat.format(Constants.TopupConstant.URL_TOPUP_CSKH, msisdn, amount, cpId, strServiceCode,
//                    "TPS" + strServiceCode.toUpperCase(),"UTF-8");
//            URL url = new URL(urlTopup);
//            httpConnection = (HttpURLConnection) url.openConnection();
//            httpConnection.setRequestMethod("GET");
//
//            is = httpConnection.getInputStream();
//            isr = new InputStreamReader(is);
//            br = new BufferedReader(isr);
//            String line = null;
//            StringBuilder sb = new StringBuilder();
//
//            while ((line = br.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            String result = sb.toString().trim();
//            if(result.startsWith("OK")) {
//                topupSubs.setTopupResult(MessageContants.Result.SUCCESS);
//                topupSubs.setErrorResult(result);
//                return 1;
//            } else {
//                topupSubs.setTopupResult(MessageContants.Result.ERROR);
//                topupSubs.setErrorResult(result);
//                return 0;
//            }
//        } catch (Exception ex) {
//            topupSubs.setTopupResult(MessageContants.Result.EXCEPTION);
//            LOG.error("", ex);
//            return -1;
//        } finally {
//            topupSubsService.create(topupSubs);
//            try {
//                if (br != null) {
//                    br.close();
//                }
//                if (isr != null) {
//                    isr.close();
//                }
//                if (is != null) {
//                    is.close();
//                }
//                if(httpConnection != null) {
//                    httpConnection.disconnect();
//                }
//            } catch (Exception ex) {
//                LOG.error("", ex);
//            }
//        }
//    }
//
//    @Override
//    public int sendSms(String userLogin, String authKey, String isdn, String content) throws Exception {
//        if (!validUser(userLogin, authKey)) {
//            LOG.info("ERROR: User " + userLogin + " not valid");
//            return -2;
//        }
//        HttpURLConnection httpConnection = null;
//        InputStream is = null;
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//        try {
//            String msisdn = PhoneUtils.normalizeMsIsdn(isdn);
//            String urlSendSms = MessageFormat.format(Constants.TopupConstant.URL_SEND_SMS, "84".concat(msisdn), URLEncoder.encode(content,"UTF-8"));
//            URL url = new URL(urlSendSms);
//            httpConnection = (HttpURLConnection) url.openConnection();
//            httpConnection.setRequestMethod("GET");
//
//            is = httpConnection.getInputStream();
//            isr = new InputStreamReader(is);
//            br = new BufferedReader(isr);
//
//            String line = null;
//            StringBuilder sb = new StringBuilder();
//            while ((line = br.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            String result = sb.toString().trim();
//            if(result.startsWith("OK")) {
//                return 1;
//            } else {
//                return 0;
//            }
//        } catch (Exception ex) {
//            LOG.error("", ex);
//            return -1;
//        }
//    }
//
//    @Override
//    public boolean validUser(String userLogin, String authKey) {
//        if(Common.isBlank(userLogin)) {
//            return false;
//        }
//        AuthUser authUser = authUserService.findByUsername(userLogin);
//        if(authUser == null) {
//            return false;
//        }
//        if (authUser.getPassword().equals(authKey)) {
//            return true;
//        }
//        return false;
//    }
//}
