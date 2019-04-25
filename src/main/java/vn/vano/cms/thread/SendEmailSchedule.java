package vn.vano.cms.thread;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import vn.vano.cms.bean.EmailPrivateBean;
import vn.vano.cms.common.IGsonBase;
import vn.vano.cms.common.RestCallApp;
import vn.yotel.commons.context.AppContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class SendEmailSchedule implements IGsonBase {
    private final Logger LOG = LoggerFactory.getLogger(SendEmailSchedule.class);
    private final SimpleDateFormat sdf_DDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");

    EmailPrivateBean emailPrivateBean = (EmailPrivateBean) AppContext.getBean("EmailPrivateBean");
    RestCallApp restCallApp = (RestCallApp) AppContext.getBean("restCallApp");
    //second, minute, hour, day of month, month, day(s) of week
    //Send email at 8h every day
    @Scheduled(cron = "0 0 8,12,17 * * ?")
    public void sendEmail(){
        LOG.debug("BEGIN::sendEmail");
        try {
            Document doc = Jsoup.parse(new File( restCallApp.getPathHtmlData() + "htop.html"), "UTF-8");
            Document doc2 = Jsoup.parse(new File(restCallApp.getPathHtmlData() + "df.html"), "UTF-8");
            String htop = doc2.body().append(doc.body().html()).html();

            File htmlTemplateFile = new File(restCallApp.getPathTemplate() + "htop.html");
            String htmlContent = FileUtils.readFileToString(htmlTemplateFile);
            htmlContent = htmlContent.replaceFirst("HTOP_DATA", htop);
            File newHtmlFile = new File(restCallApp.getPathHtmlResult() + "sv.html");
            FileUtils.writeStringToFile(newHtmlFile, htmlContent);

            String host = restCallApp.getHost();
            String emailFrom = restCallApp.getFrom();
            List<String> lstEmailCC = new ArrayList<>();
            Enumeration e = emailPrivateBean.getWarnings().propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = emailPrivateBean.getWarnings().getProperty(key);
                lstEmailCC.add(value);
            }
            restCallApp.sendEmail(host, "BÁO CÁO PHẦN CỨNG VTVSHOWBIZ " + sdf_DDMMYYYY.format(new Date()), htmlContent, emailFrom, lstEmailCC);

        } catch (Exception ex) {
            LOG.debug("", ex);
        }
        LOG.debug("END::sendEmail");
    }
}
