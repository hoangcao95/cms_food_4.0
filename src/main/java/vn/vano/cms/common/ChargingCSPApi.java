package vn.vano.cms.common;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vano.cms.jpa.CoreMTSms;
import vn.vano.cms.jpa.CoreMtQueue;
import vn.vano.cms.service.CommonService;
import vn.yotel.commons.context.AppContext;
import vn.yotel.commons.model.LinkModel;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ChargingCSPApi implements IGsonBase{
    protected final Logger LOG = LoggerFactory.getLogger(ChargingCSPApi.class);
    protected static CloseableHttpClient httpclient;

    protected CommonService commonService = (CommonService) AppContext.getBean("commonService");

    private String serviceNumber;
    private String username;
    private String password;
    private String wsUrl;
    private String wsTargetNamespace;
    private String wsNamespacePrefix;
    private String headerKey = "";//x-ibm-client-id
    private String headerValue = ""; //412c4625-5aed-459b-bf58-a10494129fc2
    private String sendMessageUrl = "/mbfn/sb/SOAPRequestServicecps/sendMessage";
    private String sendMessageFunc = "sendMessage";
    private String actionServiceUrl = "/mbfn/sb/SOAPRequestServicecps/receiverServiceReq";
    private String actionServiceFunc = "receiverServiceReq";
    private String chargeContentUrl = "/mbfn/sb/SOAPRequestServicecps/receiverPackageReq";
    private String chargeContentFunc = "minusMoneyCheckMO";
    private String sendMTRemindUrl = "/mbfn/sb/SOAPRequestServicecps/exeReceivedCP_MT";
    private String sendMTRemindFunc = "exeReceivedCP_MT";

    public String sendMT(CoreMtQueue bean) {
        LOG.info("CALL SEND MT[{}]", GSON.toJson(bean));
        logCoreMtSms(bean.getTransId(), bean, new DateTime());
        return "OK";
    }

    public String sendMT(CoreMtQueue bean, boolean useBrandName) {
        String result = null;
        try {
            if (useBrandName) {
                result = sendMT2(bean.getTransId(), bean.getMsisdn(), bean.getMessageSend(), true);
            } else {
                result = sendMT1(bean.getTransId(), bean.getMsisdn(), bean.getMessageSend());
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            try {
                logCoreMtSms(bean.getTransId(), bean, new DateTime());
            } catch (Exception e) {
                LOG.error("", e);
            }
        }
        return result;
    }

    public String sendMTDelay(CoreMtQueue bean, boolean useBrandName, int numSeconds) {
        String result = null;
        try {
            if(useBrandName) {
                String xml =
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:obj=\"http://object.app.telsoft/\">\n" +
                                "   <soapenv:Header/>\n" +
                                "   <soapenv:Body>\n" +
                                "      <obj:sendMessage>\n" +
                                "         <ServiceCode>" + serviceNumber +"</ServiceCode>\n" +
                                "         <ISDN>" + bean.getMsisdn() + "</ISDN>\n" +
                                "         <Content>" + bean.getMessageSend() + "</Content>\n" +
                                "         <UseBrandname>" + (useBrandName ? "1" : "0") + "</UseBrandname>\n" +
                                "         <User>"+ username +"</User>\n" +
                                "         <Password>" + password +"</Password>\n" +
                                "      </obj:sendMessage>\n" +
                                "   </soapenv:Body>\n" +
                                "</soapenv:Envelope>";

                HttpPost post = new HttpPost(wsUrl + sendMessageUrl);
                CloseableHttpResponse resp = null;
                try {
                    ByteArrayEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
                    post.setEntity(entity);
                    post.addHeader("Content-Type","text/xml; charset=utf-8");
                    post.addHeader("Connection","close");
                    post.addHeader(headerKey, headerValue);
                    LOG.debug(bean.getTransId() + "::MT2_SEND::" + xml.replace("\n", " ")
                            .replace("\t", " ").replaceAll(" +", " "));
                    //Lay thoi gian bat dau goi API
                    long lstart = System.currentTimeMillis();

                    resp = httpclient.execute(post);
                    //Lay thoi gian ket thuc goi API
                    long lend = System.currentTimeMillis();
                    LOG.info(bean.getTransId() + "::MT2_EXEC::TIME=" + (lend - lstart));

                    String str = Common.readInputStream(resp.getEntity().getContent()) + "";
                    LOG.info(bean.getTransId() + "::MT2_RESP::" + str.replace("\n", " ")
                            .replace("\t", " ").replaceAll(" +", " "));
                    return str;

                } catch (Exception e) {
                    LOG.error(bean.getTransId() + "::MT2_ERROR::" + bean.getMsisdn() + "::" + Common.unicode2ASII(bean.getMessageSend()));
                }finally {
                    if(resp != null) {
                        try {
                            resp.close();
                        } catch (Exception e) {}
                    }
                    post.releaseConnection();

                    try {
                        logCoreMtSms(bean.getTransId(), bean, new DateTime());
                    } catch (Exception ex) {
                        LOG.error("", ex);
                    }
                }
            } else {
                new Thread() {
                    @Override
                    public void run(){
                        Common.sleep(numSeconds);
                        String prefixForChildElement = "";

                        List<LinkModel> childElements = new ArrayList<LinkModel>();
                        childElements.add(new LinkModel("ServiceCode", serviceNumber));
                        childElements.add(new LinkModel("ISDN", bean.getMsisdn()));
                        childElements.add(new LinkModel("Content", bean.getMessageSend()));
                        childElements.add(new LinkModel("User", username));
                        childElements.add(new LinkModel("Password", password));
                        //Lay thoi gian bat dau goi API
                        long lstart = System.currentTimeMillis();
                        String _funcWsUrl = wsUrl + sendMessageUrl;

                        SaaJSoapClient saaJSoapClient = new SaaJSoapClient(_funcWsUrl, wsNamespacePrefix, prefixForChildElement, wsTargetNamespace, sendMessageFunc, childElements);
                        List<LinkModel> headerElements = new ArrayList<LinkModel>();
                        headerElements.add(new LinkModel(headerKey, headerValue));
                        headerElements.add(new LinkModel("Content-Type", "application/xml"));
                        saaJSoapClient.setAddedHeaders(headerElements);
                        //Lay thoi gian ket thuc goi API
                        long lend = System.currentTimeMillis();
                        LOG.info(bean.getTransId() + "::MT1_EXEC::TIME=" + (lend - lstart));

                        String resultCode = null;
                        try {
                            resultCode = saaJSoapClient.processSoapCallAndResultByTagName("return");
                            LOG.info(bean.getTransId() + "::MT1_RESULT::MSISDN=" + bean.getMsisdn() + "::RESULT=" + resultCode);
                        } catch (Exception e) {
                            LOG.error("", e);
                        } finally {
                            try {
                                logCoreMtSms(bean.getTransId(), bean, new DateTime());
                            } catch (Exception ex) {
                                LOG.error("", ex);
                            }
                        }
                    }
                }.start();
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        }
        return result;
    }

    public String sendMTRemind(CoreMtQueue bean) {
        String result = null;
        try {
            bean.setKeyword(Constants.CoreMt.Keyword.SYNTAX_MTREMIND);
            result = sendMTRemind1(bean.getTransId(), bean.getPackageCode(), bean.getMessageSend());
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            try {
                logCoreMtSms(bean.getTransId(), bean, new DateTime());
            } catch (Exception ex) {
                LOG.error("", ex);
            }
        }

        return result;
    }

    /**
     * Ham thuc hien gui tin nhan den so thue bao qua CCSP tu dau so dich vu
     * @param transId
     * @param msisdn
     * @param content
     * @return
     * @throws Exception
     */
    private String sendMT1(String transId, String msisdn, String content) {
        String resultCode = null;
        try {
            String prefixForChildElement = "";

            List<LinkModel> childElements = new ArrayList<LinkModel>();
            childElements.add(new LinkModel("ServiceCode", serviceNumber));
            childElements.add(new LinkModel("ISDN", msisdn));
            childElements.add(new LinkModel("Content", content));
            childElements.add(new LinkModel("User", username));
            childElements.add(new LinkModel("Password", password));
            //Lay thoi gian bat dau goi API
            long lstart = System.currentTimeMillis();
            String _funcWsUrl = wsUrl + sendMessageUrl;

            SaaJSoapClient saaJSoapClient = new SaaJSoapClient(_funcWsUrl, wsNamespacePrefix, prefixForChildElement, wsTargetNamespace, sendMessageFunc, childElements);
            List<LinkModel> headerElements = new ArrayList<LinkModel>();
            headerElements.add(new LinkModel(headerKey, headerValue));
            headerElements.add(new LinkModel("Content-Type", "application/xml"));
            saaJSoapClient.setAddedHeaders(headerElements);
            //Lay thoi gian ket thuc goi API
            long lend = System.currentTimeMillis();
            LOG.info(transId + "::MT1_EXEC::TIME=" + (lend - lstart));

            resultCode = saaJSoapClient.processSoapCallAndResultByTagName("return");
            LOG.info(transId + "::MT1_RESULT::MSISDN=" + msisdn + "::RESULT=" + resultCode);
        } catch (Exception ex) {
            LOG.error("", ex);
        }
        return resultCode;
    }

    private void sendMT1Delay(String transId, String msisdn, String content, int numSeconds) {
        new Thread() {
            @Override
            public void run(){
                Common.sleep(numSeconds);
                String prefixForChildElement = "";

                List<LinkModel> childElements = new ArrayList<LinkModel>();
                childElements.add(new LinkModel("ServiceCode", serviceNumber));
                childElements.add(new LinkModel("ISDN", msisdn));
                childElements.add(new LinkModel("Content", content));
                childElements.add(new LinkModel("User", username));
                childElements.add(new LinkModel("Password", password));
                //Lay thoi gian bat dau goi API
                long lstart = System.currentTimeMillis();
                String _funcWsUrl = wsUrl + sendMessageUrl;

                SaaJSoapClient saaJSoapClient = new SaaJSoapClient(_funcWsUrl, wsNamespacePrefix, prefixForChildElement, wsTargetNamespace, sendMessageFunc, childElements);
                List<LinkModel> headerElements = new ArrayList<LinkModel>();
                headerElements.add(new LinkModel(headerKey, headerValue));
                headerElements.add(new LinkModel("Content-Type", "application/xml"));
                saaJSoapClient.setAddedHeaders(headerElements);
                //Lay thoi gian ket thuc goi API
                long lend = System.currentTimeMillis();
                LOG.info(transId + "::MT1_EXEC::TIME=" + (lend - lstart));

                String resultCode = null;
                try {
                    resultCode = saaJSoapClient.processSoapCallAndResultByTagName("return");
                    LOG.info(transId + "::MT1_RESULT::MSISDN=" + msisdn + "::RESULT=" + resultCode);
                } catch (Exception e) {
                    LOG.error("", e);
                }
            }
        }.start();
    }

    /**
     * Ham thuc hien gui tin nhan den so thue bao qua CCSP tu dau so dich vu
     * @param transId
     * @param msisdn
     * @param content
     * @param useBrandName
     * @return
     */
    private String sendMT2(String transId, String msisdn, String content, boolean useBrandName) {
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:obj=\"http://object.app.telsoft/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <obj:sendMessage>\n" +
                        "         <ServiceCode>" + serviceNumber +"</ServiceCode>\n" +
                        "         <ISDN>" + msisdn + "</ISDN>\n" +
                        "         <Content>" + content + "</Content>\n" +
                        "         <UseBrandname>" + (useBrandName ? "1" : "0") + "</UseBrandname>\n" +
                        "         <User>"+ username +"</User>\n" +
                        "         <Password>" + password +"</Password>\n" +
                        "      </obj:sendMessage>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        HttpPost post = new HttpPost(wsUrl + sendMessageUrl);
        CloseableHttpResponse resp = null;
        try {
            ByteArrayEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
            post.setEntity(entity);
            post.addHeader("Content-Type","text/xml; charset=utf-8");
            post.addHeader("Connection","close");
            post.addHeader(headerKey, headerValue);
            LOG.debug(transId + "::MT2_SEND::" + xml.replace("\n", " ")
                    .replace("\t", " ").replaceAll(" +", " "));
            //Lay thoi gian bat dau goi API
            long lstart = System.currentTimeMillis();

            resp = httpclient.execute(post);
            //Lay thoi gian ket thuc goi API
            long lend = System.currentTimeMillis();
            LOG.info(transId + "MT2_EXEC::TIME=" + (lend - lstart));

            String str = Common.readInputStream(resp.getEntity().getContent()) + "";
            LOG.info(transId + "::MT2_RESP::" + str.replace("\n", " ")
                    .replace("\t", " ").replaceAll(" +", " "));
            return str;

        } catch (Exception e) {
            LOG.error(transId + "::MT2_ERROR::" + msisdn + "::" + Common.unicode2ASII(content));
        }finally {
            if(resp != null) {
                try {
                    resp.close();
                } catch (Exception e) {}
            }
            post.releaseConnection();

            try {

            } catch (Exception ex) {
                LOG.error("", ex);
            }
        }
        return null;
    }

    private void sendMT2Delay(String transId, String msisdn, String content, boolean useBrandName, int numSeconds) {
        new Thread(){
            @Override
            public void run() {
                Common.sleep(numSeconds);

                String xml =
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:obj=\"http://object.app.telsoft/\">\n" +
                                "   <soapenv:Header/>\n" +
                                "   <soapenv:Body>\n" +
                                "      <obj:sendMessage>\n" +
                                "         <ServiceCode>" + serviceNumber +"</ServiceCode>\n" +
                                "         <ISDN>" + msisdn + "</ISDN>\n" +
                                "         <Content>" + content + "</Content>\n" +
                                "         <UseBrandname>" + (useBrandName ? "1" : "0") + "</UseBrandname>\n" +
                                "         <User>"+ username +"</User>\n" +
                                "         <Password>" + password +"</Password>\n" +
                                "      </obj:sendMessage>\n" +
                                "   </soapenv:Body>\n" +
                                "</soapenv:Envelope>";

                HttpPost post = new HttpPost(wsUrl + sendMessageUrl);
                CloseableHttpResponse resp = null;
                try {
                    ByteArrayEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
                    post.setEntity(entity);
                    post.addHeader("Content-Type","text/xml; charset=utf-8");
                    post.addHeader("Connection","close");
                    post.addHeader(headerKey, headerValue);
                    LOG.debug(transId + "::MT2_SEND::" + xml.replace("\n", " ")
                            .replace("\t", " ").replaceAll(" +", " "));
                    //Lay thoi gian bat dau goi API
                    long lstart = System.currentTimeMillis();

                    resp = httpclient.execute(post);
                    //Lay thoi gian ket thuc goi API
                    long lend = System.currentTimeMillis();
                    LOG.info(transId + "::MT2_EXEC::TIME=" + (lend - lstart));

                    String str = Common.readInputStream(resp.getEntity().getContent()) + "";
                    LOG.info(transId + "::MT2_RESP: " + str.replace("\n", " ")
                            .replace("\t", " ").replaceAll(" +", " "));

                } catch (Exception e) {
                    LOG.error(transId + "::MT2_ERROR::" + msisdn + "::" + Common.unicode2ASII(content));
                }finally {
                    if(resp != null) {
                        try {
                            resp.close();
                        } catch (Exception e) {}
                    }
                    post.releaseConnection();
                }
            }
        }.start();
    }

    /**
     * Ham thuc hien goi DK/Huy cho thue bao
     * @param transId
     * @param msisdn
     * @param actionCode    default = 'HUY'
     * @param packageCode
     * @param sourceCode    default = 'CP'
     * @return
     * @throws Exception
     */
    public String actionService1(String transId, String msisdn, String actionCode, String packageCode,
                                String sourceCode) throws Exception {
        String prefixForChildElement = "";

        List<LinkModel> childElements = new ArrayList<LinkModel>();
        childElements.add(new LinkModel("ServiceCode", serviceNumber));
        childElements.add(new LinkModel("ISDN", msisdn));
        childElements.add(new LinkModel("CommandCode", actionCode));
        childElements.add(new LinkModel("PackageCode", packageCode));
        childElements.add(new LinkModel("SourceCode", sourceCode));
        childElements.add(new LinkModel("User", username));
        childElements.add(new LinkModel("Password", password));
        childElements.add(new LinkModel("Description", "Yomi"));
        long lstart = System.currentTimeMillis();
        String _funcWsUrl = wsUrl + actionServiceUrl;
        SaaJSoapClient saaJSoapClient = new SaaJSoapClient(_funcWsUrl, wsNamespacePrefix, prefixForChildElement,
                wsTargetNamespace, actionServiceFunc, childElements);

        List<LinkModel> headerElements = new ArrayList<LinkModel>();
        headerElements.add(new LinkModel(headerKey, headerValue));
        saaJSoapClient.setAddedHeaders(headerElements);
        //
        long lend = System.currentTimeMillis();
        LOG.info(transId + "::ACTION_EXEC::TIME=" + (lend - lstart));

        String resultCode = saaJSoapClient.processSoapCallAndResultByTagName("return");

        LOG.info(transId + "::ACTION_RESULT::MSISDN=" + msisdn + "::RESULT=" + resultCode);
        return resultCode;
    }

    /**
     * Ham thuc hien tru tien khong can confirm
     * @param transId
     * @param msisdn
     * @param requestId
     * @param packageCode
     * @param packageName
     * @param spId
     * @param cpId
     * @param contentId
     * @param categoryId
     * @param amount
     * @return
     * @throws Exception
     */
    public String minusMoney1(String transId, String msisdn, String requestId, String packageCode, String packageName,
                             String spId, String cpId, String contentId, String categoryId, String amount) throws Exception {
        String resultCode = null;
        try {
            String prefixForChildElement = "";

            List<LinkModel> childElements = new ArrayList<LinkModel>();
            childElements.add(new LinkModel("ServiceCode", serviceNumber));
            childElements.add(new LinkModel("ISDN", msisdn));
            childElements.add(new LinkModel("RequestId", requestId));
            childElements.add(new LinkModel("PackageCode", packageCode));
            childElements.add(new LinkModel("PackageName", packageName));
            childElements.add(new LinkModel("SP_ID", spId));
            childElements.add(new LinkModel("CP_ID", cpId));
            childElements.add(new LinkModel("Content_ID", contentId));
            childElements.add(new LinkModel("Category_ID", categoryId));
            childElements.add(new LinkModel("Amount", amount));
            childElements.add(new LinkModel("UserName", username));
            childElements.add(new LinkModel("Password", password));
            long lstart = System.currentTimeMillis();
            String _funcWsUrl = wsUrl + chargeContentUrl;
            SaaJSoapClient saaJSoapClient = new SaaJSoapClient(_funcWsUrl, wsNamespacePrefix, prefixForChildElement,
                    wsTargetNamespace, chargeContentFunc, childElements);

            List<LinkModel> headerElements = new ArrayList<LinkModel>();
            headerElements.add(new LinkModel(headerKey, headerValue));
            saaJSoapClient.setAddedHeaders(headerElements);

            long lend = System.currentTimeMillis();
            LOG.info(transId + "::MONEY_EXEC::TIME=" + (lend - lstart));

            resultCode = saaJSoapClient.processSoapCallAndResultByTagName("return");

            LOG.info(transId + "::MONEY_RESULT::MSISDN=" + msisdn + "::RESULT=" + resultCode);
        } catch (Exception ex) {
            LOG.error("", ex);
        }
        return resultCode;
    }

    /**
     * Ham thuc hien gui MT Remind
     * @param transId
     * @param packageCode
     * @param contents
     * @return
     * @throws Exception
     */
    private String sendMTRemind1(String transId, String packageCode, String contents) throws Exception {
        String resultCode = null;
        try {
            String prefixForChildElement = "";

            List<LinkModel> childElements = new ArrayList<LinkModel>();
            childElements.add(new LinkModel("ServiceCode", serviceNumber));
            childElements.add(new LinkModel("PackageCode", packageCode));
            childElements.add(new LinkModel("Contents", contents));
            childElements.add(new LinkModel("User", username));
            childElements.add(new LinkModel("Password", password));
            long lstart = System.currentTimeMillis();
            String _funcWsUrl = wsUrl + sendMTRemindUrl;
            SaaJSoapClient saaJSoapClient = new SaaJSoapClient(_funcWsUrl, wsNamespacePrefix, prefixForChildElement, wsTargetNamespace, sendMTRemindFunc, childElements);

            List<LinkModel> headerElements = new ArrayList<LinkModel>();
            headerElements.add(new LinkModel(headerKey, headerValue));
            saaJSoapClient.setAddedHeaders(headerElements);

            long lend = System.currentTimeMillis();
            LOG.info(transId + "::MT_REMIND1_EXE::TIME=" + (lend - lstart));

            resultCode = saaJSoapClient.processSoapCallAndResultByTagName("return");
            LOG.info(transId + "::MT_REMIND1_RESULT::RESULT=" + resultCode);
        } catch (Exception ex) {
            LOG.error("", ex);
        }
        return resultCode;
    }

    private String sendMTRemind2(String transId, String packageCode,String content) {
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:obj=\"http://object.app.telsoft/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <obj:exeReceivedCP_MT>\n" +
                        "         <ServiceCode>" + serviceNumber + "</ServiceCode>\n" +
                        "         <PackageCode>" + packageCode + "</PackageCode>\n" +
                        "         <Contents>" + content + "</Contents>\n" +
                        "         <UserName>" + username + "</UserName>\n" +
                        "         <Password>" + password + "</Password>\n" +
                        "      </obj:exeReceivedCP_MT>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        HttpPost post = new HttpPost(wsUrl + sendMTRemindUrl);
        CloseableHttpResponse resp = null;
        try {
            ByteArrayEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
            post.setEntity(entity);
            post.addHeader("Content-Type","text/xml; charset=utf-8");
            post.addHeader("Connection","close");
            post.addHeader(headerKey, headerValue);
            LOG.info(transId + "::MT_REMIND2_SEND::" + xml.replace("\n", " ")
                    .replace("\t", " ").replaceAll(" +", " "));

            //Lay thoi gian bat dau goi API
            long lstart = System.currentTimeMillis();
            resp = httpclient.execute(post);
            String response = Common.readInputStream(resp.getEntity().getContent());
            //Lay thoi gian ket thuc goi API
            long lend = System.currentTimeMillis();
            LOG.info(transId + "::MT_REMIND2_EXE::TIME=" + (lend - lstart));
            LOG.info(transId + "::MT_REMIND2_RESP::" + String.valueOf(response).replace("\n", " ")
                    .replace("\t", " ").replaceAll(" +", " "));
            response = parseReturnCode(response);
            LOG.info(transId + "::MT_REMIND2_RESULT::RESULT=" + response);

            return response;
        } catch (Exception e) {
            LOG.error(transId + "::MT_REMIND2_ERROR::PACKAGE_CODE=" + packageCode + "::" + Common.unicode2ASII(content));
            LOG.error("", e);
        }finally {
            if(resp != null) {
                try {
                    resp.close();
                } catch (Exception e) {}
            }
            post.releaseConnection();
        }
        return null;
    }

    private static String parseReturnCode(String input) {
        if (input == null) {
            return null;
        }
        String startTag = "<return>";
        String endTag = "</return>";
        int start = input.indexOf(startTag);
        int end = input.indexOf(endTag);
        if (start < 0 || end < 0) {
            return null;
        }
        String result = input.substring(start + startTag.length(), end);
        return result;
    }

    private void logCoreMtSms(String transId, CoreMtQueue mtQueue, DateTime dtCurrent) {
        try {
            CoreMTSms bean = new CoreMTSms();
            bean.setMsisdn(mtQueue.getMsisdn());
            bean.setServiceCode(mtQueue.getServiceCode());
            bean.setMessage(Common.upper(mtQueue.getOrgRequest()));
            bean.setCommand(Common.upper(mtQueue.getCommandCode()));
            bean.setChannel(Common.upper(mtQueue.getChannel()));
            bean.setCreatedDate(Common.truncDate(dtCurrent));
            bean.setCreatedTime(new Timestamp(dtCurrent.getMillis()));
            if(!Common.isBlank(mtQueue.getPromoId())) {
                bean.setPromoId(Long.valueOf(mtQueue.getPromoId()));
            }
            bean.setStatus(Constants.CoreMo.Status.ACTIVE);
            bean.setTransId(transId);

            commonService.create(transId, bean);
        } catch (Exception ex) {
            LOG.error("", ex);
        }
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public String getWsTargetNamespace() {
        return wsTargetNamespace;
    }

    public void setWsTargetNamespace(String wsTargetNamespace) {
        this.wsTargetNamespace = wsTargetNamespace;
    }

    public String getWsNamespacePrefix() {
        return wsNamespacePrefix;
    }

    public void setWsNamespacePrefix(String wsNamespacePrefix) {
        this.wsNamespacePrefix = wsNamespacePrefix;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public String getSendMessageUrl() {
        return sendMessageUrl;
    }

    public void setSendMessageUrl(String sendMessageUrl) {
        this.sendMessageUrl = sendMessageUrl;
    }

    public String getSendMessageFunc() {
        return sendMessageFunc;
    }

    public void setSendMessageFunc(String sendMessageFunc) {
        this.sendMessageFunc = sendMessageFunc;
    }

    public String getActionServiceUrl() {
        return actionServiceUrl;
    }

    public void setActionServiceUrl(String actionServiceUrl) {
        this.actionServiceUrl = actionServiceUrl;
    }

    public String getActionServiceFunc() {
        return actionServiceFunc;
    }

    public void setActionServiceFunc(String actionServiceFunc) {
        this.actionServiceFunc = actionServiceFunc;
    }

    public String getChargeContentUrl() {
        return chargeContentUrl;
    }

    public void setChargeContentUrl(String chargeContentUrl) {
        this.chargeContentUrl = chargeContentUrl;
    }

    public String getChargeContentFunc() {
        return chargeContentFunc;
    }

    public void setChargeContentFunc(String chargeContentFunc) {
        this.chargeContentFunc = chargeContentFunc;
    }

    public String getSendMTRemindUrl() {
        return sendMTRemindUrl;
    }

    public void setSendMTRemindUrl(String sendMTRemindUrl) {
        this.sendMTRemindUrl = sendMTRemindUrl;
    }

    public String getSendMTRemindFunc() {
        return sendMTRemindFunc;
    }

    public void setSendMTRemindFunc(String sendMTRemindFunc) {
        this.sendMTRemindFunc = sendMTRemindFunc;
    }
}
