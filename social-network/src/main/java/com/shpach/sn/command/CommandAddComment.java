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
import com.shpach.sn.persistence.entities.Comment;
import com.shpach.sn.persistence.entities.Friend;
import com.shpach.sn.persistence.entities.Post;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.service.CommentService;
import com.shpach.sn.service.FriendService;
import com.shpach.sn.service.PostService;
import com.shpach.sn.service.SessionServise;
import com.shpach.sn.service.UserService;

/**
 * Command which show users home page
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandAddComment implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandAddComment.class);

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

		String commentText = request.getParameter("commentText");
		String postIdStr = request.getParameter("postId");
		int postId = 0;
		try {
			postId = Integer.parseInt(postIdStr);
		} catch (NumberFormatException ex) {
			logger.warn("invalid postId parsing");
		}

		if (postId > 0 && commentText != null) {
			Comment comment = new Comment();
			comment.setCommentText(commentText);
			comment.setPostId(postId);
			comment.setUserId(user.getUserId());
			if (CommentService.getInstance().addNewComment(comment)) {
				request.setAttribute("addCommentSuccessAlert", true);
				request.setAttribute("createdCommentId",comment.getCommentId());

			} else {
				request.setAttribute("addCommentFailAlert", true);
			}
		} else {
			request.setAttribute("addCommentFailAlert", true);
		}

		// User userFriend = UserService.getInstance().getUserByLogin((String)
		// request.getParameter("friendEmail"));

		// if (user!=null && userFriend!=null){
		// Friend friend=new Friend();
		// friend.setHostUser(user);
		// friend.setSlaveUser(userFriend);
		// if(FriendService.getInstance().addNewFriend(friend)){
		// request.setAttribute("addFriendSuccessAlert", true);
		// }else{
		// request.setAttribute("addFriendFailAlert", true);
		// }
		// }

		// Map<String, String[]> lastRequest = new HashMap<String, String[]>();
		// lastRequest.putAll(request.getParameterMap());

		request.getSession().setAttribute("userEntity", user);
		// request.getSession().setAttribute("lastRequest", lastRequest);

		/*
		 * List<UserMenuItem> tutorMenu = new MenuStrategy(user).getMenu();
		 * UserMenuService.getInstance().setActiveMenuByCommand(tutorMenu,
		 * "tutorTests"); request.setAttribute("menu", tutorMenu);
		 * 
		 * List<Test> tests = TestService.getInstance().getTestsByUsers(user);
		 * //TestService.getInstance().insertCommunitiesToTests(tests);
		 * TestService.getInstance().insertCategoriesToTests(tests);
		 * request.setAttribute("tests", tests);
		 */
		page = "/pages";
		return page;

	}

}
