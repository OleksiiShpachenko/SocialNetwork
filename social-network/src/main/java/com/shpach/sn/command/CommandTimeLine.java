package com.shpach.sn.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.shpach.sn.manager.Config;
import com.shpach.sn.persistence.entities.Post;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.service.FriendService;
import com.shpach.sn.service.PostService;
import com.shpach.sn.service.SessionServise;
import com.shpach.sn.service.UserService;
import com.shpak.sn.pagination.IPaginationService;
import com.shpak.sn.pagination.Pagination;
import com.shpak.sn.pagination.PaginationServiceImpl;

/**
 * Command which register new user in the system
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandTimeLine implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandTimeLine.class);
	// private static final int NO_LIMIT = 0;
	private static final int ITEMS_ON_PAGE = 2;

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

		int currentPage = 1;
		if (request.getAttribute("currentPage") == null) {
			request.setAttribute("currentPage", currentPage);
		} else {
			currentPage = (int) request.getAttribute("currentPage");
		}
		int startFrom = (currentPage - 1) * ITEMS_ON_PAGE;

		PostService.getInstance().injectPostToUser(userInfo, startFrom, ITEMS_ON_PAGE);

		request.getSession().setAttribute("user", user.getUserEmail());
		Map<String, String[]> lastRequest = new HashMap<String, String[]>();
		lastRequest.putAll(request.getParameterMap());
		request.getSession().setAttribute("lastRequest", lastRequest);

		int postsCount=PostService.getInstance().countUserPosts(Arrays.asList(userInfo));
		
		Pagination pagination = new Pagination(currentPage, postsCount, ITEMS_ON_PAGE);
		IPaginationService paginationService = new PaginationServiceImpl(pagination);

		int startpage, maxPage, stopPage;
		if (paginationService.validatePaginationData()) {
			startpage = paginationService.calcStartPage();
			maxPage = paginationService.calcMaxPage();
			stopPage = paginationService.calcStopPage();
		} else {
			startpage = 1;
			maxPage = 1;
			stopPage = 1;
		}
		
		
		List<Post> timeLinePosts;

		// List<User> userFriends =
		// UserService.getInstance().getUserFriendsCollection(userInfo);
		// List<User> userNeedToApprove =
		// UserService.getInstance().getUserNeedApproveFriendsCollection(userInfo);
		// List<User> userWaitForAccept =
		// UserService.getInstance().getUseWaitForAcceptFriendsCollection(userInfo);
		// int startFrom = 0;
		// int limit = NO_LIMIT;
		// List<User> users = UserService.getInstance().findAll(startFrom,
		// limit);
		// users.removeAll(new ArrayList<User>(Arrays.asList(user)));
		// UserService.getInstance().injectToUserFriendRelationshipIfExist(user.getFriends(),users);
		if (user.getUserId() != userInfo.getUserId()) {
			request.setAttribute("notMe", true);
			timeLinePosts = PostService.getInstance().getPostsWithZeroCommunity(userInfo.getPosts());
		} else {
			timeLinePosts = userInfo.getPosts();
		}
		request.setAttribute("userInfo", userInfo);
		request.setAttribute("timeLinePosts", timeLinePosts);
		
		request.setAttribute("startPage", startpage);
		request.setAttribute("stopPage", stopPage);
		request.setAttribute("paginationCount", pagination.getPaginationCount());
		request.setAttribute("maxPage", maxPage);
		// request.setAttribute("userFriends", userFriends);
		// request.setAttribute("userNeedToApprove", userNeedToApprove);
		// request.setAttribute("userWaitForAccept", userWaitForAccept);

		page = Config.getInstance().getProperty(Config.TIMELINE);
		return page;
	}

}
