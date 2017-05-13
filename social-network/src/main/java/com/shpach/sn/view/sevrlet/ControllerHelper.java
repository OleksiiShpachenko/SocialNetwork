package com.shpach.sn.view.sevrlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.shpach.sn.command.CommandAddComment;
import com.shpach.sn.command.CommandAddFriend;
import com.shpach.sn.command.CommandAddPost;
import com.shpach.sn.command.CommandAdminPermitions;
import com.shpach.sn.command.CommandApproveFriend;
import com.shpach.sn.command.CommandAvatarUpload;
import com.shpach.sn.command.CommandChangeLocale;
import com.shpach.sn.command.CommandDeleteFriend;
import com.shpach.sn.command.CommandDeletePost;
import com.shpach.sn.command.CommandFindFriends;
import com.shpach.sn.command.CommandFriends;
import com.shpach.sn.command.CommandHome;
import com.shpach.sn.command.CommandLogOut;
import com.shpach.sn.command.CommandLogin;
import com.shpach.sn.command.CommandMissing;
import com.shpach.sn.command.CommandNewsFeed;
import com.shpach.sn.command.CommandPagination;
import com.shpach.sn.command.CommandRegistration;
import com.shpach.sn.command.CommandTimeLine;
import com.shpach.sn.command.CommandUpdateUser;
import com.shpach.sn.command.CommandUserSettings;
import com.shpach.sn.command.ICommand;
import com.shpach.sn.persistence.entities.User;

/**
 * @author Shpachenko_A_K
 *
 */
public class ControllerHelper {

	private static ControllerHelper instance = null;
	HashMap<String, ICommand> commands = new HashMap<String, ICommand>();

	private ControllerHelper() {
		commands.put("updateUser", new CommandUpdateUser());
		commands.put("adminPermitions", new CommandAdminPermitions());
		commands.put("pagination", new CommandPagination());
		commands.put("newsFeed", new CommandNewsFeed());
		commands.put("deletePost", new CommandDeletePost());
		commands.put("addComment", new CommandAddComment());
		commands.put("addPost", new CommandAddPost());
		commands.put("timeLine", new CommandTimeLine());
		commands.put("friends", new CommandFriends());
		commands.put("approveFriend", new CommandApproveFriend());
		commands.put("deleteFriend", new CommandDeleteFriend());
		commands.put("addFriend", new CommandAddFriend());
		commands.put("findFriends", new CommandFindFriends());
		commands.put("login", new CommandLogin());
		commands.put("home", new CommandHome());
		commands.put("avatarUpload", new CommandAvatarUpload());
		commands.put("usersettings", new CommandUserSettings());
		commands.put("locale", new CommandChangeLocale());
		commands.put("logOut", new CommandLogOut());
		commands.put("registration", new CommandRegistration());
	}

	/**
	 * Return {@link ICommand} implementation according to "command" request
	 * parameter
	 * 
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return {@link ICommand} implementation
	 */
	public ICommand getCommand(HttpServletRequest request) {
		ICommand command = commands.get(request.getParameter("command"));
		if (command == null) {
			command = new CommandMissing();
		}
		return command;
	}

	public static ControllerHelper getInstance() {
		if (instance == null) {
			instance = new ControllerHelper();
		}
		return instance;
	}

	/**
	 * Wrap request according to "command" request parameter
	 * 
	 * @param request
	 *            - request {@link HttpServletRequest}
	 * @return wrapped {@link HttpServletRequest}
	 */
	@SuppressWarnings("unchecked")
	public HttpServletRequest wrapRequest(HttpServletRequest request) {
		HttpServletRequest res = request;

		String commandText = request.getParameter("command");
		boolean setLastRequest = commandText.equals("locale") || commandText.equals("avatarUpload")
				|| commandText.equals("addFriend") || commandText.equals("deleteFriend")
				|| commandText.equals("approveFriend") || commandText.equals("deletePost")
				|| commandText.equals("addPost") || commandText.equals("addComment") || commandText.equals("pagination")
				|| commandText.equals("updateUser");

		if (setLastRequest) {
			Map<String, String[]> lastRequest = (HashMap<String, String[]>) request.getSession()
					.getAttribute("lastRequest");
			if (lastRequest != null)
				res = new RequestWrapper(request, lastRequest);

		} else if (commandText.equals("login")) {
			Map<String, String[]> lastRequest = new HashMap<>();
			User user = (User) request.getSession().getAttribute("userEntity");
			String startCommand = "login";
			if (user != null) {
				startCommand = "timeLine";
			}
			lastRequest.put("command", new String[] { startCommand });
			if (lastRequest != null)
				res = new RequestWrapper(request, lastRequest);
		}

		return res;
	}
}
