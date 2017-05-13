package com.shpach.sn.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Command which register new user in the system
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandFriends implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandFriends.class);

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = null;
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

		if (UserRoleService.getInstance().isUserAdmin(user)){
			request.setAttribute("userAdmin", true);
		}
		
		request.getSession().setAttribute("userEntity", user);
		User userInfo;
		try {
			int userInfoId = Integer.parseInt(request.getParameter("userInfoId"));
			userInfo = UserService.getInstance().getUserById(userInfoId);
			if (userInfo == null)
				userInfo = user;

		} catch (NumberFormatException e) {
			userInfo = user;
		}
		UserService.getInstance().injectFriendToUser(userInfo);
		UserService.getInstance().injectSecondUserToFriend(userInfo);

		request.getSession().setAttribute("user", user.getUserEmail());
		Map<String, String[]> lastRequest = new HashMap<String, String[]>();
		lastRequest.putAll(request.getParameterMap());
		request.getSession().setAttribute("lastRequest", lastRequest);

		List<User> userFriends = UserService.getInstance().getUserFriendsCollection(userInfo);
		List<User> userNeedToApprove = UserService.getInstance().getUserNeedApproveFriendsCollection(userInfo);
		List<User> userWaitForAccept = UserService.getInstance().getUseWaitForAcceptFriendsCollection(userInfo);
 
		if (user.getUserId()!=userInfo.getUserId())
			request.setAttribute("notMe", true);
		request.setAttribute("userInfo", userInfo);
		request.setAttribute("userFriends", userFriends);
		request.setAttribute("userNeedToApprove", userNeedToApprove);
		request.setAttribute("userWaitForAccept", userWaitForAccept);

		page = Config.getInstance().getProperty(Config.FRIENDS);
		return page;
	}

}
