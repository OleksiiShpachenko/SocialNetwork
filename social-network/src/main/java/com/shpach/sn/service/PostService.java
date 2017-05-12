package com.shpach.sn.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.sn.persistence.entities.Friend;
import com.shpach.sn.persistence.entities.Post;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.persistence.jdbc.connection.ConnectionPoolTomCatFactory;
import com.shpach.sn.persistence.jdbc.connection.IConnectionPoolFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.friend.IFriendDao;
import com.shpach.sn.persistence.jdbc.dao.post.IPostDao;
import com.shpach.sn.persistence.jdbc.dao.userrole.MySqlUserRoleDao;

//import com.shpach.tutor.persistance.entities.Community;
//import com.shpach.tutor.persistance.entities.Task;
//import com.shpach.tutor.persistance.entities.User;
//import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
//import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
//import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;

/**
 * Service layer for {@link Friend} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class PostService {
	private static final Logger logger = Logger.getLogger(PostService.class);
	private static PostService instance = null;

	public static enum friendStatus {
		WAIT, FRIEND
	}

	private IPostDao postDao;
	private CommentService commentService;
	private IConnectionPoolFactory connectionFactory;
	// private TaskService taskService;

	private PostService() {
		// IDaoFactory daoFactory = new MySqlDaoFactory();
		// friendDao = daoFactory.getFriendDao();
		// taskService = TaskService.getInstance();
	}

	public static synchronized PostService getInstance() {
		if (instance == null) {
			instance = new PostService();
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {
		if (connectionFactory == null)
			connectionFactory = (IConnectionPoolFactory) new ConnectionPoolTomCatFactory();
		return connectionFactory.getConnection();
	}

	public IPostDao getPostDao() {
		if (postDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			postDao = daoFactory.getPostDao();
		}
		return postDao;
	}

	// /**
	// * Get {@link User} by login (email)
	// *
	// * @param login
	// * - users login (email)
	// * @return {@link User}
	// */
	// public User getUserByLogin(String login) {
	// if (login==null)
	// return null;
	// User userEntitie = friendDao.findUserByEmail(login);
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

	private CommentService getCommentService() {
		if (commentService == null) {
			commentService = CommentService.getInstance();
		}
		return commentService;
	}

	/**
	 * add {@link Friend} to database
	 * 
	 * @param friend
	 * @return boolean
	 */
	public boolean addNewPost(Post post) {
		if (post == null)
			return false;
		Post postAppdated = getPostDao().addOrUpdate(post);
		if (postAppdated == null)
			return false;
		return true;
	}

	public void injectPostToUser(User user, int startFrom, int itemsOnPage) {
		if (user == null)
			return;
		List<Post> posts = getPostDao().findPostByUserId(user.getUserId(), startFrom, itemsOnPage);
		if (posts == null)
			posts = new ArrayList<>();
		getCommentService().injectCommentsToPost(posts);

		user.setPosts(posts);
	}

	public List<Post> getPostsWithZeroCommunity(List<Post> posts) {
		List<Post> res = new ArrayList<>();
		for (Post post : posts) {
			if (post.getCommunityId() == 0)
				res.add(post);
		}
		return res;
	}

	public boolean deletePost(int postId) {

		boolean res = false;

		Connection cn = null;
		try {
			cn = getConnection();
			cn.setAutoCommit(false);
			res = getCommentService().deleteCommentsByPostId(postId, cn);
			if (res == true)
				res = getPostDao().deletePostById(postId, cn);
			if (res == false)
				cn.rollback();
			cn.setAutoCommit(true);
			cn.close();
		} catch (SQLException e) {
			logger.error(e, e);
			res = false;
		}
		return res;
	}

	public List<Post> getFriendsPostsWhithZeroCommunityByUser(User user, int startFrom, int itemsOnPage) {
		List<User> friends=getUsersCollectionFromFriendsByUser(user);
		List<Integer> friendsId=getUsersId(friends);
		List<Post> posts=getPostDao().findPostByUserId(friendsId,startFrom, itemsOnPage);
		getCommentService().injectCommentsToPost(posts);
		return posts;
	}

	private List<Integer> getUsersId(List<User> friends) {
		List<Integer> friendsId=new ArrayList<>();
		for (User userFriend : friends) {
			friendsId.add(userFriend.getUserId());
		}
		return friendsId;
	}

	public List<User> getUsersCollectionFromFriendsByUser(User user) {
		List<User> friends=new ArrayList<>();
		for (Friend friend : user.getFriends()) {
			if (friend.getHostUserId()==user.getUserId()){
				friends.add(friend.getSlaveUser());
			}else{
				friends.add(friend.getHostUser());
			}
		}
		return friends;
	}

	public int countUserPosts(List<User> users) {
		Integer[] usersId= new Integer[users.size()];
		int i=0;
		for (User user : users) {
			usersId[i++]=user.getUserId();
		}
		return getPostDao().countUsersPostsById(usersId);
	}

	

	// public List<Friend> getFriendByUserId(int userId) {
	// List<Friend> friends = getFriendDao().findFriendByUserId(userId);
	// if (friends != null)
	// return friends;
	// return new ArrayList<Friend>();
	// }

	/**
	 * Delete {@link Friend} from database
	 * 
	 * @param friend
	 * @return
	 */
	// public boolean deleteFriend(Friend friend) {
	// if (friend == null)
	// return false;
	// return getFriendDao().deleteFriendByUsersId(friend.getHostUserId(),
	// friend.getSlaveUserId());
	// }
	//
	// public boolean update(Friend friend) {
	// if (friend == null)
	// return false;
	// return getFriendDao().update(friend) != null ? true : false;
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
	// public List<User> findAll(int startFrom, int limit) {
	// return friendDao.findAll();
	// }

}
