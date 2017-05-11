package com.shpach.sn.command;

import java.io.File;
import java.io.IOException;
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
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.service.SessionServise;
import com.shpach.sn.service.UserService;

/**
 * Command which show users home page
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandAvatarUpload implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandAvatarUpload.class);
	/**
	 * Directory where uploaded files will be saved, its relative to the web
	 * application directory.
	 */
	private static final String UPLOAD_DIR = "user_avatars";

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

		
		
		
		
		
		
		/*
		 * Map<String, String[]> lastRequest = new HashMap<String, String[]>();
		 * lastRequest.putAll(request.getParameterMap());
		 * 
		 * request.getSession().setAttribute("userEntity", user);
		 * request.getSession().setAttribute("lastRequest", lastRequest);
		 */
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

		// gets absolute path of the web application
		String applicationPath = request.getServletContext().getRealPath("");
		// constructs path of the directory to save uploaded file
		String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

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
				//TODO: save to user avatar_URl
			}
		}

		/*
		 * request.setAttribute("message", fileName +
		 * " File uploaded successfully!");
		 * getServletContext().getRequestDispatcher("/response.jsp").forward(
		 * request, response);
		 */

		page = "/pages";
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
