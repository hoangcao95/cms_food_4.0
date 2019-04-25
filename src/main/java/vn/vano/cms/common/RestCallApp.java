package vn.vano.cms.common;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RestCallApp {
    private final Logger LOG = LoggerFactory.getLogger(RestCallApp.class);

    public RestMessage sendEmail(String host, String subject, String message, String emailForm, List<String> emailTo){
        RestMessage restMessage = new RestMessage();
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : emailTo) {
            stringBuilder.append(item);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        String toStr = stringBuilder.toString();
        HttpResponse<JsonNode> resp = null;
        try {
            resp = Unirest.post(host + sendEmailAPI).header("Content-Type", "application/x-www-form-urlencoded")
                    .field("message", message).field("subject", subject).field("from", emailForm).field("to", toStr)
                    .asJson();
            if (resp == null) {
                return RestMessage.RestMessageBuilder.FAIL("0", "FAIL");
            }
            String respStatus = resp.getBody().getObject().get("status").toString();
            String respMessage = resp.getBody().getObject().get("message").toString();
            restMessage.setStatus(respStatus);
            restMessage.setMessage(respMessage);
        } catch (Exception ex){
            LOG.error("", ex);
            return RestMessage.RestMessageBuilder.FAIL("9999", "ERROR");
        }

        return restMessage;
    }

    public RestMessage getListTopupIbid(String fromDate, String toDate) {
        RestMessage restMessage = new RestMessage();
        HttpResponse<String> resp = null;
        try {
            resp = Unirest.get(ibidTopupList).header("accept", "application/json")
                    .queryString("from", fromDate).queryString("to", toDate).asString();
            if (resp == null) {
                return RestMessage.RestMessageBuilder.FAIL("0", "FAIL");
            }
            String dataResp = resp.getBody();
            if(dataResp != null && !dataResp.equals("NULL")) {
                restMessage.setStatus("1");
                restMessage.setMessage("SUCCESS");
            } else {
                restMessage.setStatus("0");
                restMessage.setMessage("NO_DATA");
            }
            restMessage.setData(dataResp);
        } catch (Exception ex) {
            LOG.error("", ex);
            restMessage.setStatus("0");
            restMessage.setMessage("ERROR");
        }

        return restMessage;
    }

    public RestMessage updateStatusTopupIbid(Integer id) {
        RestMessage restMessage = new RestMessage();
        HttpResponse<String> resp = null;
        try {
            resp = Unirest.get(ibidTopupList).header("accept", "application/json")
                    .queryString("a", "1").queryString("p", id).asString();
            if (resp == null) {
                return RestMessage.RestMessageBuilder.FAIL("0", "FAIL");
            }
            String dataResp = resp.getBody();
            if(dataResp != null && dataResp.equals("SUCCESS")) {
                restMessage.setStatus("1");
                restMessage.setMessage("SUCCESS");
            } else {
                restMessage.setStatus("0");
                restMessage.setMessage("FAIL");
            }
            restMessage.setData(dataResp);
        } catch (Exception ex) {
            LOG.error("", ex);
            restMessage.setStatus("0");
            restMessage.setMessage("ERROR");
        }

        return restMessage;
    }

    public RestMessage getPromotionIbid() {
        RestMessage restMessage = new RestMessage();
        HttpResponse<String> resp = null;
        try {
            resp = Unirest.get(ibidTopupList).header("accept", "application/json")
                    .queryString("a", "2").asString();
            if (resp == null) {
                return RestMessage.RestMessageBuilder.FAIL("0", "FAIL");
            }
            String dataResp = resp.getBody();
            if(dataResp != null && !dataResp.equals("ERROR")) {
                restMessage.setStatus("1");
                restMessage.setMessage("SUCCESS");
            } else {
                restMessage.setStatus("0");
                restMessage.setMessage("FAIL");
            }
            restMessage.setData(dataResp);
        } catch (Exception ex) {
            LOG.error("", ex);
            restMessage.setStatus("0");
            restMessage.setMessage("ERROR");
        }

        return restMessage;
    }

    private String host;
    private String from;
    private String sendEmailAPI = "/custcare/vtvsb/send_email.html";
    private String ibidTopupList = "http://daugiaibid.vn/ccsp/topup/promo.jsp";
    private String pathTemplate;
    private String pathHtmlData;
    private String pathHtmlResult;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSendEmailAPI() {
        return sendEmailAPI;
    }

    public void setSendEmailAPI(String sendEmailAPI) {
        this.sendEmailAPI = sendEmailAPI;
    }

    public String getPathTemplate() {
        return pathTemplate;
    }

    public void setPathTemplate(String pathTemplate) {
        this.pathTemplate = pathTemplate;
    }

    public String getPathHtmlData() {
        return pathHtmlData;
    }

    public void setPathHtmlData(String pathHtmlData) {
        this.pathHtmlData = pathHtmlData;
    }

    public String getPathHtmlResult() {
        return pathHtmlResult;
    }

    public void setPathHtmlResult(String pathHtmlResult) {
        this.pathHtmlResult = pathHtmlResult;
    }
}
