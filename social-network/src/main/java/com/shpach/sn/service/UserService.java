package com.shpach.sn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shpach.sn.persistence.entities.Comment;
import com.shpach.sn.persistence.entities.Friend;
import com.shpach.sn.persistence.entities.Post;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.persistence.entities.UserRole;
import com.shpach.sn.persistence.jdbc.connection.ConnectionPoolTomCatFactory;
import com.shpach.sn.persistence.jdbc.connection.IConnectionPoolFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.user.IUserDao;

/**
 * Service layer for {@link User} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class UserService {
	private static UserService instance = null;
	private IUserDao userDao;
	private IConnectionPoolFactory connectionFactory;
	private FriendService friendService;
	private UserRoleService userRoleService;

	private UserService() {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		userDao = daoFactory.getUserDao();
		// taskService = TaskService.getInstance();
	}

	public static synchronized UserService getInstance() {
		if (instance == null) {
			instance = new UserService();
		}
		return instance;
	}

	private FriendService getFriendService() {
		if (friendService == null)
			friendService = FriendService.getInstance();
		return friendService;
	}

	public IConnectionPoolFactory getConnectionFactory() {
		if (connectionFactory == null) {
			connectionFactory = (IConnectionPoolFactory) new ConnectionPoolTomCatFactory();
		}
		return connectionFactory;
	}

	private IUserDao getUserDao() {
		if (userDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			userDao = daoFactory.getUserDao();
		}
		return userDao;
	}

	private UserRoleService getUserRoleService() {
		if (userRoleService == null) {
			userRoleService = UserRoleService.getInstance();
		}
		return userRoleService;
	}

	/**
	 * Get {@link User} by login (email)
	 * 
	 * @param login
	 *            - users login (email)
	 * @return {@link User}
	 */
	public User getUserByLogin(String login) {
		if (login == null)
			return null;
		User userEntitie = getUserDao().findUserByEmail(login);
		List<UserRole> userRoles = getUserRoleService().getUserRoleByUser(userEntitie);
		userEntitie.setUserRoles(userRoles);
		return userEntitie;
	}

	/**
	 * Gets collections of {@link User} by {@link Community}
	 * 
	 * @param community
	 *            - {@link Community}
	 * @return collections of {@link User}
	 */
	// public List<User> getUsersByCommunity(Community community) {
	// if (community == null)
	// return null;
	// return userDao.findUsersByCommunityId(community.getCommunityId());
	// }

	/**
	 * add {@link User} to database
	 * 
	 * @param user
	 * @return
	 */
	public boolean addNewUser(User user) {
		if (user == null)
			return false;
		User userAppdated = getUserDao().addOrUpdate(user);
		if (userAppdated == null)
			return false;
		return true;
	}

	// public User findUserWithGreatWorstStatistic() {
	// List<User> users = userDao.findAll();
	// if (users==null)
	// return null;
	// User res = null;
	// int max = -1;
	// for (User user : users) {
	// List<Task> tasks =taskService.getTasksByUser(user);
	// int min = taskService.getMinScore(tasks);
	// if (max < min && min!=Integer.MAX_VALUE) {
	// res = user;
	// max = min;
	// }
	// }
	// return res;
	// }

	public boolean validateUserName(String testString) {
		Pattern p = Pattern.compile("^[à-ÿÀ-ß¸¨ÙÜüÞþßÿ¯¿²³ªº¥´'a-zA-Z0-9 ]{1,128}");
		Matcher m = p.matcher(testString);
		return m.matches();
	}

	public boolean validatePassword(String userPassword) {
		Pattern p = Pattern.compile("[A-z,0-9]{6,128}");
		Matcher m = p.matcher(userPassword);
		return m.matches();
	}

	/**
	 * Return all {@link User}
	 * 
	 * @param startFrom
	 * @param limit
	 * @return
	 */
	public List<User> findAllExcludMe(int userId, int startFrom, int limit) {
		List<User> users = getUserDao().findAllExcludMe(userId, startFrom, limit);
		if (users != null) {
			for (User user : users) {
				List<UserRole> userRoles = getUserRoleService().getUserRoleByUser(user);
				user.setUserRoles(userRoles);
			}
		}
		return users;

	}

	public void injectToUserFriendRelationshipIfExist(List<Friend> friends, List<User> users) {
		for (User user : users) {
			for (Friend friend : friends) {
				if (friend.getHostUserId() == user.getUserId() || friend.getSlaveUserId() == user.getUserId())
					user.addFriends1(friend);
			}
		}

	}

	/**
	 * Inject to {@link user} collection of related {@link Friend}
	 * 
	 * @param user
	 */
	public void injectFriendToUser(User user) {
		List<Friend> friends = getFriendService().getFriendByUserId(user.getUserId());
		if (friends != null && friends.size() > 0)
			user.setFriends(friends);
	}

	public List<User> getUserFriendsCollection(User user) {
		List<User> res = new ArrayList<>();
		for (Friend friend : user.getFriends()) {
			if (friend.getFriendStatus() != FriendService.friendStatus.FRIEND.ordinal())
				continue;
			if (friend.getHostUserId() == user.getUserId())
				res.add(friend.getSlaveUser());
			else
				res.add(friend.getHostUser());
		}
		return res;
	}

	public void injectSecondUserToFriend(User user) {
		for (Friend friend : user.getFriends()) {
			if (friend.getHostUserId() == user.getUserId()) {
				friend.setHostUser(user);
				User slaveUser = getUserById(friend.getSlaveUserId());
				friend.setSlaveUser(slaveUser);
			} else {
				friend.setSlaveUser(user);
				User hostUser = getUserById(friend.getHostUserId());
				friend.setHostUser(hostUser);
			}
		}

	}

	public User getUserById(int userId) {
		User user = getUserDao().findUserById(userId);
		if (user != null) {
			List<UserRole> userRoles = getUserRoleService().getUserRoleByUser(user);
			user.setUserRoles(userRoles);
		}
		return user;
	}

	public List<User> getUserNeedApproveFriendsCollection(User user) {
		List<User> res = new ArrayList<>();
		for (Friend friend : user.getFriends()) {
			if (friend.getFriendStatus() == FriendService.friendStatus.WAIT.ordinal()
					&& friend.getSlaveUserId() == user.getUserId())
				res.add(friend.getHostUser());
		}
		return res;
	}

	public List<User> getUseWaitForAcceptFriendsCollection(User user) {
		List<User> res = new ArrayList<>();
		for (Friend friend : user.getFriends()) {
			if (friend.getFriendStatus() == FriendService.friendStatus.WAIT.ordinal()
					&& friend.getHostUserId() == user.getUserId())
				res.add(friend.getSlaveUser());
		}
		return res;
	}

	public void injectUserToComment(List<Comment> comments) {
		for (Comment comment : comments) {
			User user = getUserDao().findUserById(comment.getUserId());
			comment.setUser(user);
		}

	}

	public void injectUserToPost(List<Post> newsFeed) {
		for (Post post : newsFeed) {
			User user = getUserDao().findUserById(post.getUserId());
			post.setUser(user);
		}

	}

	public int coutUsers() {
		return getUserDao().count();

	}

}
