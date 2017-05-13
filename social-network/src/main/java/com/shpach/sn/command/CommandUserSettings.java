package com.shpach.sn.command;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

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
public class CommandUserSettings implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandUserSettings.class);

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
		
		if (request.getParameter("savesettings") != null) {

			boolean validation = true;
			String userEmail = request.getParameter("userEmailNew");
			String userName = request.getParameter("userNameNew");
			if (userName != null && !user.getUserName().equals(userName)) {
				if (!UserService.getInstance().validateUserName(userName)) {
					request.setAttribute("fillUserNameMessage", true);
					validation = false;
				}
			}
			if (userEmail != null && !user.getUserEmail().equals(userEmail)) {
				if (!userEmail.contains("@")) {
					request.setAttribute("fillCorrectEmailMessage", true);
					validation = false;
				}
			}

			String applicationPath = request.getServletContext().getRealPath("");
			// constructs path of the directory to save uploaded file
			String uploadFilePath = applicationPath + File.separator
					+ Config.getInstance().getProperty(Config.AVATAR_UPLOAD_DIR);
			// creates the save directory if it does not exists
			File fileSaveDir = new File(uploadFilePath);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdirs();
			}
			logger.info("Upload File Directory=" + fileSaveDir.getAbsolutePath());

			String fileName = null;
			// Get all the parts from request and write it to the file on server
			for (Part part : request.getParts()) {
				fileName = getFileName(part);
				if (!fileName.equals("")) {
					part.write(uploadFilePath + File.separator + fileName);
					request.setAttribute("message", fileName + " File uploaded successfully!");
					user.setAvatarUrl(Config.getInstance().getProperty(Config.AVATAR_UPLOAD_DIR)+ File.separator +fileName);
				}
			}

			if (validation) {
				user.setUserName(userName);
				user.setUserEmail(userEmail);
				user.setUserLogin(userEmail);
				if (UserService.getInstance().addNewUser(user)) {
					logger.info("user succefull updated. User email:" + userEmail);
					request.setAttribute("registrationSuccess", true);
					request.getSession().setAttribute("userEntity", user);
					request.getSession().setAttribute("user", user.getUserEmail());
				} else {
					request.setAttribute("emailAllReadyExistMessage", true);
				}
			}

		} else {
			Map<String, String[]> lastRequest = new HashMap<String, String[]>();
			lastRequest.putAll(request.getParameterMap());
			request.getSession().setAttribute("lastRequest", lastRequest);
		}

		request.getSession().setAttribute("userEntity", user);

		page = Config.getInstance().getProperty(Config.USER_SETTINGS);
		return page;
	}

	/**
	 * Utility method to get file name from HTTP header content-disposition
	 */
	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		if (contentDisp.contains("avatar-2")) {
			logger.info("content-disposition header= " + contentDisp);
			String[] tokens = contentDisp.split(";");
			for (String token : tokens) {
				if (token.trim().startsWith("filename")) {
					return token.substring(token.indexOf("=") + 2, token.length() - 1);
				}
			}
		}
		return "";
	}

}
