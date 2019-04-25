package vn.vano.cms.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import vn.yotel.admin.jpa.AuthUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(AppAuthenticationSuccessHandler.class);

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
		String targetLink = "/";
		request.getSession().setAttribute("fullName", authUser.getFullName());
		request.getSession().setAttribute("userId", authUser.getId());
		request.getSession().setAttribute("userName", authUser.getUserName());
		List<GrantedAuthority> lstAuthorities = new ArrayList<GrantedAuthority>(authentication.getAuthorities());
//		for (GrantedAuthority grantedAuthority : lstAuthorities) {
//		    if (grantedAuthority.getAuthority().equals("Administrators") ||
//					grantedAuthority.getAuthority().equals("CEO")){
//                targetLink ="/account/switch-account.html";
//            }else if (grantedAuthority.getAuthority().equals("HrOfficers")) {
		targetLink = "/home/index.html";
//			}else {
//				targetLink = "/employee/employee-profile.html/" + authUser.getId() ;
//			}
//		}

//		targetLink = "/index.html";

        if (response.isCommitted()) {
        	logger.warn("Response has already been committed. Unable to redirect to " + targetLink);
        } else {
			//request.getSession().setAttribute("dashboardLink", targetLink);
        	redirectStrategy.sendRedirect(request, response, targetLink);        	
        }
        // lấy ra địa chỉ ip đăng nhập vào hệ thống
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
//				.getRequest();
		//String ipAddress = ((WebAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getRemoteAddress();
//		String ipAddress = request.getHeader("X-FORWARDED-FOR");
//		UserLog userLog = new UserLog();
//		userLog.setUserId(authUser.getId());
//		userLog.setUserName(authUser.getUserName());
//		userLog.setFullName(authUser.getFullName());
//		userLog.setAction(Constants.Action.LOGIN);
//		userLog.setIpAddress(ipAddress);
//		userLog.setDateInsertLog(new Date());
//		userLog.setStatus(Constants.STATUS_ACTIVITY);
//		userLogService.addUserLog(userLog);
	}

	//grantedAuthority.getAuthority().equals("Administrators")||

}
