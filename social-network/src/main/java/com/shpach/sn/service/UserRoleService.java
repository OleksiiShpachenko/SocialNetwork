package com.shpach.sn.service;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.shpach.sn.persistence.jdbc.dao.userrole.IUserRoleDao;

//import com.shpach.tutor.persistance.entities.Community;
//import com.shpach.tutor.persistance.entities.Task;
//import com.shpach.tutor.persistance.entities.User;
//import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
//import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
//import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;

/**
 * Service layer for {@link UserRole} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class UserRoleService {
	private static final int ADMIN = 1;
	private static UserRoleService instance = null;
	private IUserRoleDao userRoleDao;
	// private IConnectionPoolFactory connectionFactory;
	// private FriendService friendService;
	// private UserRoleService userRoleService;

	private UserRoleService() {
		// IDaoFactory daoFactory = new MySqlDaoFactory();
		// userRoleDao = daoFactory.getUserRoleDao();
		// taskService = TaskService.getInstance();
	}

	public static synchronized UserRoleService getInstance() {
		if (instance == null) {
			instance = new UserRoleService();
		}
		return instance;
	}

	// private FriendService getFriendService() {
	// if (friendService == null)
	// friendService = FriendService.getInstance();
	// return friendService;
	// }
	//
	// public IConnectionPoolFactory getConnectionFactory() {
	// if (connectionFactory == null) {
	// connectionFactory = (IConnectionPoolFactory) new
	// ConnectionPoolTomCatFactory();
	// }
	// return connectionFactory;
	// }

	private IUserRoleDao getUserRoleDao() {
		if (userRoleDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			userRoleDao = daoFactory.getUserRoleDao();
		}
		return userRoleDao;
	}

	public List<UserRole> getUserRoleByUser(User user) {
		List<UserRole> userRoles = new ArrayList<>();
		if (user == null)
			return userRoles;
		userRoles = getUserRoleDao().findUserRoleByUserId(user.getUserId());
		if (userRoles != null) {
			return userRoles;
		}
		return new ArrayList<UserRole>();
	}

	public boolean isUserAdmin(User user) {
		for (UserRole userRole : user.getUserRoles()) {
			if (userRole.getUserRoleId() == ADMIN)
				return true;
		}
		return false;
	}

	public void AddOrRemoveAdminStatus(User user, boolean adminStatus) {
		if (adminStatus) {
			UserRole adminUserRole=getUserRoleDao().findUserRoleById(ADMIN);
			if (!user.getUserRoles().contains(adminUserRole))
				user.getUserRoles().add(adminUserRole);

		} else {
			for (Iterator<UserRole> iterator = user.getUserRoles().iterator(); iterator.hasNext();) {
				UserRole userRole = iterator.next();
				if (userRole.getUserRoleId() == ADMIN) {
					iterator.remove();
				}
			}
		}

	}

	/**
	 * Get {@link User} by login (email)
	 * 
	 * @param login
	 *            - users login (email)
	 * @return {@link User}
	 */
	// public User getUserByLogin(String login) {
	// if (login==null)
	// return null;
	// User userEntitie = getUserDao().findUserByEmail(login);
	// return userEntitie;
	// }

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
	// public boolean addNewUser(User user) {
	// if (user==null)
	// return false;
	// User userAppdated = getUserDao().addOrUpdate(user);
	// if (userAppdated == null)
	// return false;
	// return true;
	// }

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

	// public boolean validateUserName(String testString) {
	// Pattern p = Pattern.compile("^[à-ÿÀ-ß¸¨ÙÜüÞþßÿ¯¿²³ªº¥´'a-zA-Z0-9
	// ]{1,128}");
	// Matcher m = p.matcher(testString);
	// return m.matches();
	// }
	//
	// public boolean validatePassword(String userPassword) {
	// Pattern p = Pattern.compile("[A-z,0-9]{6,128}");
	// Matcher m = p.matcher(userPassword);
	// return m.matches();
	// }
	//
	// /**
	// * Return all {@link User}
	// * @param startFrom
	// * @param limit
	// * @return
	// */
	// public List<User> findAllExcludMe(int userId,int startFrom, int limit) {
	// return getUserDao().findAllExcludMe(userId,startFrom, limit);
	// }
	//
	// public void injectToUserFriendRelationshipIfExist(List<Friend> friends,
	// List<User> users) {
	// for (User user : users) {
	// for (Friend friend : friends) {
	// if (friend.getHostUserId()==user.getUserId() ||
	// friend.getSlaveUserId()==user.getUserId())
	// user.addFriends1(friend);
	// }
	// }
	//
	// }
	//
	// /**Inject to {@link user} collection of related {@link Friend}
	// * @param user
	// */
	// public void injectFriendToUser(User user) {
	// List<Friend>
	// friends=getFriendService().getFriendByUserId(user.getUserId());
	// if (friends!=null && friends.size()>0)
	// user.setFriends(friends);
	// }
	//
	// public List<User> getUserFriendsCollection(User user) {
	// List<User> res=new ArrayList<>();
	// for (Friend friend : user.getFriends()) {
	// if(friend.getFriendStatus()!=FriendService.friendStatus.FRIEND.ordinal())
	// continue;
	// if(friend.getHostUserId()==user.getUserId())
	// res.add(friend.getSlaveUser());
	// else
	// res.add(friend.getHostUser());
	// }
	// return res;
	// }
	//
	// public void injectSecondUserToFriend(User user) {
	// for (Friend friend : user.getFriends()) {
	// if(friend.getHostUserId()==user.getUserId()){
	// friend.setHostUser(user);
	// User slaveUser=getUserById(friend.getSlaveUserId());
	// friend.setSlaveUser(slaveUser);
	// }else{
	// friend.setSlaveUser(user);
	// User hostUser=getUserById(friend.getHostUserId());
	// friend.setHostUser(hostUser);
	// }
	// }
	//
	// }
	//
	// public User getUserById(int userId) {
	// return getUserDao().findUserById(userId);
	// }
	//
	// public List<User> getUserNeedApproveFriendsCollection(User user) {
	// List<User> res=new ArrayList<>();
	// for (Friend friend : user.getFriends()) {
	// if(friend.getFriendStatus()==FriendService.friendStatus.WAIT.ordinal() &&
	// friend.getSlaveUserId()==user.getUserId())
	// res.add(friend.getHostUser());
	// }
	// return res;
	// }
	//
	// public List<User> getUseWaitForAcceptFriendsCollection(User user) {
	// List<User> res=new ArrayList<>();
	// for (Friend friend : user.getFriends()) {
	// if(friend.getFriendStatus()==FriendService.friendStatus.WAIT.ordinal() &&
	// friend.getHostUserId()==user.getUserId())
	// res.add(friend.getSlaveUser());
	// }
	// return res;
	// }
	//
	// public void injectUserToComment(List<Comment> comments) {
	// for (Comment comment : comments) {
	// User user=getUserDao().findUserById(comment.getUserId());
	// comment.setUser(user);
	// }
	//
	// }
	//
	// public void injectUserToPost(List<Post> newsFeed) {
	// for (Post post : newsFeed) {
	// User user=getUserDao().findUserById(post.getUserId());
	// post.setUser(user);
	// }
	//
	// }
	//
	// public int coutUsers() {
	// return getUserDao().count();
	//
	// }
	//
	//

}
