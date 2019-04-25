package vn.vano.cms.web.controller;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.vano.cms.bean.AccountBean;
import vn.vano.cms.common.Common;
import vn.vano.cms.common.Constants;
import vn.vano.cms.common.IGsonBase;
import vn.vano.cms.common.MessageContants;
import vn.vano.cms.service.UserService;
import vn.yotel.admin.jpa.AuthUser;
import vn.yotel.admin.jpa.Role;
import vn.yotel.admin.service.AuthRoleService;
import vn.yotel.admin.service.AuthUserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "/account")
public class AccountController implements IGsonBase{
    private final Logger LOG = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    AuthUserService authUserService;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthRoleService authRoleService;

    @RequestMapping(value = "/index.html", method = {RequestMethod.GET})
    public String indexMain(HttpServletRequest request,
                            Locale locale, Model model, Pageable pageable) {
        LOG.debug("BEGIN::index.html");
        try {
            model.addAttribute("account", new AccountBean());
            model.addAttribute("sessionUser", (String) request.getSession().getAttribute(Constants.Session.USER_LOGIN));

            //Get all User
            getListUser(model, pageable,"", "");
        } catch (Exception ex) {
            LOG.debug("", ex);
        } finally {
            LOG.debug("END::index.html");
        }
        return "/account/index";
    }

    @RequestMapping(value = "/create.html", method = {RequestMethod.GET})
    public String indexAddNew(Locale locale, Model model) {
        LOG.debug("BEGIN::create_index.html");
        try {
            List<Role> lstAllRole = authRoleService.findAll();
            model.addAttribute("lstAllRole", lstAllRole);
            model.addAttribute("account", new AccountBean());
        } catch (Exception ex) {
            LOG.debug("", ex);
        }
        LOG.debug("END::create_index.html");
        return "/account/create";
    }

    @RequestMapping(value = "/create.html", method = {RequestMethod.POST})
    @PreAuthorize("hasAnyAuthority('SADMIN')") //Chi chap nhan cac user co quyen SADMIN moi thuc hien dc chuc nang nay
    public String create(Locale locale, Model model,
                         @ModelAttribute("account") @Valid AccountBean account,
                         RedirectAttributes redirectAttributes) {
        LOG.debug("BEGIN::create.html");
        LOG.debug("CREATE::PARAM[" + GSON.toJson(account) + "]");
        try {
            AuthUser user = new AuthUser();
            user.setUserName(account.getUserName());
            user.setFullName(account.getFullName());
            user.setEmail(account.getEmail());

            List<Integer> lstRoleId = account.getRoles();
            List<Role> lstRole = new ArrayList<>();
            if(lstRole != null && !lstRole.isEmpty()) {
                for (Integer roleId : lstRoleId) {
                    Role item = authRoleService.findOne(roleId);
                    lstRole.add(item);
                }
            }
            user.setPassword(passwordEncoder.encode("123456"));
            user.setSalt("5876695f8e4e1811");
            user.setAuthRoles(lstRole);
            user.setStatus(new Byte("1").byteValue());
            user.setCreatedDate(Common.truncDate(new DateTime()));
            authUserService.create(user);
            model.addAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.Account.ADDNEW_SUCCESS);
            model.addAttribute("account", new AccountBean());

            List<Role> lstAllRole = authRoleService.findAll();
            model.addAttribute("lstAllRole", lstAllRole);
            //Xu ly gui email co Mat khau den tai khoan
        } catch (Exception ex) {
            LOG.error("", ex);
            model.addAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.Account.ADDNEW_ERROR);
            model.addAttribute("account", account);
        } finally {
            LOG.debug("END::create.html");
        }
        return "/account/create";
    }

    @RequestMapping(value = "/search.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String search(HttpServletRequest request, Locale locale, Model model,
                         @ModelAttribute("account") AccountBean account,
                         Pageable pageable) {
        LOG.debug("BEGIN::search.html");
        try {
            //Get list User
            getListUser(model, pageable,account.getUserName(), account.getEmail());
            model.addAttribute("account", account);
            model.addAttribute("sessionUser", (String) request.getSession().getAttribute(Constants.Session.USER_LOGIN));
        } catch (Exception ex) {
            LOG.debug("", ex);
        }
        LOG.debug("END::search.html");
        return "/account/index";
    }

    @RequestMapping(value = "/change_password.html", method = {RequestMethod.GET})
    public String indexChangePass(Locale locale, Model model) {
        LOG.debug("BEGIN::change_password_index.html");

        LOG.debug("END::change_password_index.html");
        return "/change_password";
    }

    @RequestMapping(value = "/update.html/{id}", method = {RequestMethod.GET})
    @PreAuthorize("hasAnyAuthority('SADMIN')") //Chi chap nhan cac user co quyen SADMIN moi thuc hien dc chuc nang nay
    public String indexUpdate(Locale locale, Model model,
                              @PathVariable("id") Integer id) {
        LOG.debug("BEGIN::update_index.html");
        LOG.debug("UPDATE::PARAM[id="+ id +"]");
        try {
            AuthUser user = authUserService.findOne(id);
            AccountBean bean = new AccountBean();
            if (user != null) {
                bean.setUserName(user.getUserName());
                bean.setFullName(user.getFullName());
                bean.setEmail(user.getEmail());
                bean.setStatus(String.valueOf(user.getStatus()));
                List<Integer> lstRoldId = userService.findRoleByUser(id);
                bean.setRoles(lstRoldId);
            }

            List<Role> lstAllRole = authRoleService.findAll();
            model.addAttribute("lstAllRole", lstAllRole);
            model.addAttribute("account", bean);
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            LOG.debug("END::update_index.html");
        }
        return "/account/update";
    }

    @RequestMapping(value = "/update.html", method = {RequestMethod.POST})
    @PreAuthorize("hasAnyAuthority('SADMIN')") //Chi chap nhan cac user co quyen SADMIN moi thuc hien dc chuc nang nay
    public String update(Locale locale, Model model,
                         @ModelAttribute("account") @Valid AccountBean account,
                         RedirectAttributes redirectAttributes) {
        LOG.debug("BEGIN::update.html");
        try {
            AuthUser user = authUserService.findByUsername(account.getUserName());
            if(user != null) {
                user.setStatus(new Byte(account.getStatus()).byteValue());
                user.setFullName(account.getFullName());
                user.setEmail(account.getEmail());

                List<Integer> lstRoleId = account.getRoles();
                List<Role> lstRole = new ArrayList<>();
                for (Integer roleId : lstRoleId) {
                    Role item = authRoleService.findOne(roleId);
                    lstRole.add(item);
                }
                user.setAuthRoles(lstRole);

                authUserService.update(user);
                redirectAttributes.addFlashAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.Account.UPDATE_SUCCESS);
                return "redirect:/account/index.html";
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            LOG.debug("END::update.html");
        }
        return "/account/update";
    }

    @RequestMapping(value = "/delete.html/{id}", method = {RequestMethod.GET})
    @PreAuthorize("hasAnyAuthority('SADMIN')") //Chi chap nhan cac user co quyen SADMIN moi thuc hien dc chuc nang nay
    public String deleteAccount(Locale locale, Model model,
                                @PathVariable("id") Integer id,
                                Pageable pageable,
                                RedirectAttributes redirectAttributes) {
        LOG.debug("BEGIN::delete.html");
        LOG.debug("PARAM [id=" + id + "]");
        try {
            AuthUser user = authUserService.findOne(id);
            if (user != null) {
                user.setStatus(new Byte("0").byteValue());
                authUserService.update(user);
//                model.addAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.Account.DELETE_SUCCESS);
                redirectAttributes.addFlashAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.Account.DELETE_SUCCESS);
            } else {
//                model.addAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.Account.DELETE_ERROR);
                redirectAttributes.addFlashAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.Account.DELETE_ERROR);
            }
        } catch (Exception ex) {
            LOG.error("", ex);
//            model.addAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.Account.DELETE_ERROR);
            redirectAttributes.addFlashAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.Account.DELETE_ERROR);
        }
//        getListUser(model, pageable, "", "");
//        model.addAttribute("account", new AccountBean());
        LOG.debug("END::delete.html");
        return "redirect:/account/index.html";
    }

    @RequestMapping(value = "/onchange_pass.html", method = {RequestMethod.POST})
    public String changePass(Locale locale, Model model,
                             @RequestParam String username,
                             @RequestParam String old_password,
                             @RequestParam String new_password,
                             @RequestParam String verify_password) {
        LOG.debug("BEGIN::onchange_pass.html");
        LOG.debug("CHANGE_PASS::PARAM[username=" + username + "]");
        try {
            AuthUser _user = authUserService.findByUsername(username);
            if (!passwordEncoder.matches(old_password, _user.getPassword()) || !new_password.equals(verify_password)) {
                model.addAttribute("username", username);
                model.addAttribute("old_password", old_password);
                model.addAttribute("new_password", new_password);
                model.addAttribute("verify_password", verify_password);
                return "/change_password";
            } else {
                String newEncryptedPassword = passwordEncoder.encode(new_password);
                _user.setPassword(newEncryptedPassword);
                authUserService.update(_user);
                return "redirect:/logout.html";
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            LOG.debug("END::onchange_pass.html");
        }
        return "redirect:/logout.html";
    }

    /**
     * Lay danh sach tat ca User
     * @param model
     * @param pageable
     */
    private void getListUser(Model model, Pageable pageable, String userName, String email) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "userName"));
        Pageable _pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
//        Pageable _pageable = new PageRequest(pageable.getPageNumber(), 1000, sort);
        Page<AuthUser> lstAllUser = userService.findByParam(userName,email, _pageable);

        model.addAttribute("page", lstAllUser);
        LOG.debug("SEARCH::PARAM[userName=" + userName + ",email=" + email + "]::TOTAL=" + lstAllUser.getSize());
    }
}
