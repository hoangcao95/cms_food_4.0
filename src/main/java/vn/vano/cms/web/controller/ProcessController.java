package vn.vano.cms.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.vano.cms.common.AppParams;
import vn.vano.cms.common.Constants;
import vn.yotel.thread.ManageableThread;
import vn.yotel.thread.ThreadManager;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = { "/process" })
public class ProcessController {
    private final Logger LOG = LoggerFactory.getLogger(ProcessController.class);

    @Resource
    ThreadManager threadManager;

    @RequestMapping(value = {"/list.html"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String listThreads(Model model, HttpServletRequest request,
                              @RequestParam(value = "displayName",defaultValue = "") String displayName,
                              Pageable pageable) {
        LOG.info("BEGIN::list.html");
        Collection<ManageableThread> collect = threadManager.getThreadList().values();
        List<ManageableThread> list = new ArrayList<ManageableThread>(collect);
        Collections.sort(list, new Comparator<ManageableThread>() {
            @Override
            public int compare(ManageableThread thread1, ManageableThread thread2) {
                return (thread1.getOrder() - thread2.getOrder());
            }
        });
        model.addAttribute("threads", list);
        model.addAttribute("haMode", Constants.App.SERVER_MODE);

        Page<ManageableThread> page = new PageImpl(list);
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        Pageable _pageable = new PageRequest(pageable.getPageNumber(), 10, sort);
        model.addAttribute("page", page);
        LOG.info("END::list.html");
        return "thread/list";
    }

    @PreAuthorize("hasAnyAuthority('Administrators', 'ThreadManagers', 'SADMIN')")
    @RequestMapping({"/{id}/start.html"})
    public String startThread(Model model, @PathVariable("id") String threadId) {
        threadManager.startThread(threadId);
        LOG.info("END::start.html");
        return "redirect:/thread/list.html";
    }

    @PreAuthorize("hasAnyAuthority('Administrators', 'ThreadManagers', 'SADMIN')")
    @RequestMapping({"/{id}/stop.html"})
    public String stopThread(Model model, @PathVariable("id") String threadId) {
        LOG.info("BEGIN::stop.html| id=" + threadId);
        this.threadManager.stopThread(threadId);
        LOG.info("END::stop.html");
        return "redirect:/thread/list.html";
    }
}
