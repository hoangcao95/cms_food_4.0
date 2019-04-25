package vn.vano.cms.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import vn.vano.cms.bean.CommandBean;
import vn.vano.cms.common.Common;
import vn.vano.cms.common.Constants;
import vn.vano.cms.service.CommonService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value = "/home")
public class HomeController {
    private final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/index.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Locale locale, Model model) {
        String transId = Common.randomString(Constants.TRANS_ID_LENGTH);
        LOG.debug(transId + "::BEGIN::index.html");
        try {

        } catch (Exception ex) {

        }
        LOG.debug(transId + "::END::index.html");
        return "/home/index";
    }
}
