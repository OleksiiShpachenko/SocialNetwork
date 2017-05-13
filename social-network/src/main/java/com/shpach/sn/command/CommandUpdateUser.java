package com.shpach.sn.command;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import com.shpach.sn.manager.Config;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.service.SessionServise;
import com.shpach.sn.service.UserRoleService;
import com.shpach.sn.service.UserService;

/**
 * Command which update {@link User} in the system
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandUpdateUser implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandUpdateUser.class);

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = "/pages";
		boolean checkSession = false;

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.warn("try to access without session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		checkSession = SessionServise.getInstance().checkSession(session.getId(),
				(String) session.getAttribute("user"));
		if (!checkSession) {
			session.invalidate();
			logger.warn("invalid session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		User user = UserService.getInstance().getUserByLogin((String) session.getAttribute("user"));
		request.getSession().setAttribute("userEntity", user);
		
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(request.getParameter("pageNo"));
		} catch (NumberFormatException ex) {
			logger.warn("invalid currentPage parsing");
			
		}
		
		request.setAttribute("currentPage", currentPage);

		String userIdToUpdateStr = request.getParameter("userId");
		int userIdToUpdate = 0;
		try {
			userIdToUpdate = Integer.parseInt(userIdToUpdateStr);
		} catch (NumberFormatException ex) {
			logger.warn("invalid userId parsing");
			return page;
		}

		if (userIdToUpdate == 0) {
			logger.warn("invalid userId parsing");
			return page;
		}

		User userToUpdate = UserService.getInstance().getUserById(userIdToUpdate);
		if (userToUpdate == null) {
			logger.warn("invalid userId");
			return page;
		}

		String[] userPostPermitionValues = request.getParameterValues("postPermition");
		String[] userCommentPermitionValues = request.getParameterValues("commentPermition");
		String[] userAdminStatusValues = request.getParameterValues("adminStatus");

		byte postPerm = (userPostPermitionValues != null && userPostPermitionValues[0].equals("true")) ? (byte) 1 : 0;
		userToUpdate.setUserPostPermition(postPerm);
		byte commPerm = (userCommentPermitionValues != null && userCommentPermitionValues[0].equals("true")) ? (byte) 1
				: 0;
		userToUpdate.setUserCommentPermition(commPerm);
		boolean adminStatus = (userAdminStatusValues != null && userAdminStatusValues[0].equals("true")) ? true
				: false;
		UserRoleService.getInstance().AddOrRemoveAdminStatus(userToUpdate, adminStatus);
		if (UserService.getInstance().addNewUser(userToUpdate)) {
			logger.info("user succefull updated. User email:" + userToUpdate.getUserEmail());
			request.setAttribute("userUpdateSuccess", true);
		} else {
			request.setAttribute("userUpdateFail", true);
		}

		return page;
	}

}
