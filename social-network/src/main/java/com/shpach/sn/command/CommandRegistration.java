package com.shpach.sn.command;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.shpach.sn.manager.Config;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.service.UserService;



/**
 * Command which register new user in the system
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandRegistration implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandRegistration.class);

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = Config.getInstance().getProperty(Config.LOGIN);
	
		String userName = request.getParameter("userName");
		if (userName != null) {
			String userEmail = request.getParameter("userEmail");
			String userPassword = request.getParameter("password");
			//String userPasswordConfirm = request.getParameter("confirm_password");
			//String userRole = request.getParameter("userRole");
			request.setAttribute("userName", userName);
			request.setAttribute("userEmail", userEmail);
			int userRoleInt = 2;
			boolean validation = true;
//			try {
//				userRoleInt = Integer.parseInt(userRole);
//			} catch (NumberFormatException ex) {
//				validation = false;
//			}
			if (!UserService.getInstance().validateUserName(userName) ) {
				request.setAttribute("fillUserNameMessage", true);
				validation = false;
			}
			if (!userEmail.contains("@")) {
				request.setAttribute("fillCorrectEmailMessage", true);
				validation = false;
			}
			if (!UserService.getInstance().validatePassword(userPassword) ) {
				request.setAttribute("fillCorrectPasswordMessage", true);
				validation = false;
			}
			if (validation) {
				User user = new User();
				user.setUserLogin(userEmail);
				user.setUserEmail(userEmail);
				user.setUserName(userName);
				user.setUserPassword(userPassword);
				user.setAvatarUrl(Config.getInstance().getProperty(Config.DEFAULT_AVATAR));
				//user.setRoleId(userRoleInt);
				if (UserService.getInstance().addNewUser(user)) {
					logger.info("new user succefull added. User email:" + userEmail);
					request.setAttribute("registrationSuccess", true);
					request.removeAttribute("userName");
					request.removeAttribute("userEmail");
					request.setAttribute("userEmailLogin", userEmail);
			
					page = Config.getInstance().getProperty(Config.LOGIN);
				} else {
					request.setAttribute("emailAllReadyExistMessage", true);
				}
			}
		}
		return page;
	}
	
}
