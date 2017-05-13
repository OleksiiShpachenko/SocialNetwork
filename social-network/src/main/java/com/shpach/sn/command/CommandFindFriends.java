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
import com.shpach.sn.pagination.IPaginationService;
import com.shpach.sn.pagination.Pagination;
import com.shpach.sn.pagination.PaginationServiceImpl;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.service.SessionServise;
import com.shpach.sn.service.UserRoleService;
import com.shpach.sn.service.UserService;

/**
 * Command for Find User page
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandFindFriends implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandFindFriends.class);
	private static final int ITEMS_ON_PAGE = 8;

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
		UserService.getInstance().injectFriendToUser(user);
		request.getSession().setAttribute("user", user.getUserEmail());
		Map<String, String[]> lastRequest = new HashMap<String, String[]>();
		lastRequest.putAll(request.getParameterMap());
		request.getSession().setAttribute("lastRequest", lastRequest);
		
		int currentPage = 1;
		if (request.getAttribute("currentPage") == null) {
			request.setAttribute("currentPage", currentPage);
		} else {
			currentPage = (int) request.getAttribute("currentPage");
		}
		
		int startFrom = (currentPage - 1) * ITEMS_ON_PAGE;
		List<User> users = UserService.getInstance().findAllExcludMe(user.getUserId(), startFrom, ITEMS_ON_PAGE);
		UserService.getInstance().injectToUserFriendRelationshipIfExist(user.getFriends(), users);

		int usersCount = UserService.getInstance().coutUsers();

		Pagination pagination = new Pagination(currentPage, usersCount - 1, ITEMS_ON_PAGE);
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

		request.setAttribute("users", users);
		request.setAttribute("startPage", startpage);
		request.setAttribute("stopPage", stopPage);
		request.setAttribute("paginationCount", pagination.getPaginationCount());
		request.setAttribute("maxPage", maxPage);

		page = Config.getInstance().getProperty(Config.FIND_FRIENDS);
		return page;
	}

}
