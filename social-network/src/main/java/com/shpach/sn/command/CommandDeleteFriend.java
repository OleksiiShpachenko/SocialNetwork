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
import com.shpach.sn.persistence.entities.Friend;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.service.FriendService;
import com.shpach.sn.service.SessionServise;
import com.shpach.sn.service.UserService;




/**
 * Command which show users home page
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandDeleteFriend implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandDeleteFriend.class);

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = null;
		boolean checkSession = false;

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.warn("try to access without session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		checkSession = SessionServise.getInstance().checkSession(session.getId(), (String) session.getAttribute("user"));
		if (!checkSession) {
			session.invalidate();
			logger.warn("invalid session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		User user = UserService.getInstance().getUserByLogin((String) session.getAttribute("user"));
		User userFriend = UserService.getInstance().getUserByLogin((String) request.getParameter("friendEmail"));

		if (user!=null && userFriend!=null){
			Friend friend=new Friend();
			friend.setHostUser(user);
			friend.setSlaveUser(userFriend);
			if(FriendService.getInstance().deleteFriend(friend)){
				request.setAttribute("deleteFriendSuccessAlert", true);
			}else{
				request.setAttribute("deleteFriendFailAlert", true);
			}
		}
		
		
		
//		Map<String, String[]> lastRequest = new HashMap<String, String[]>();
//		lastRequest.putAll(request.getParameterMap());
		
		request.getSession().setAttribute("userEntity", user);
		//request.getSession().setAttribute("lastRequest", lastRequest);
		
		

		/*List<UserMenuItem> tutorMenu = new MenuStrategy(user).getMenu();
		UserMenuService.getInstance().setActiveMenuByCommand(tutorMenu, "tutorTests");
		request.setAttribute("menu", tutorMenu);

		List<Test> tests = TestService.getInstance().getTestsByUsers(user);
		//TestService.getInstance().insertCommunitiesToTests(tests);
		TestService.getInstance().insertCategoriesToTests(tests);
		request.setAttribute("tests", tests);*/
		page = "/pages";
		return page;

	}

}
