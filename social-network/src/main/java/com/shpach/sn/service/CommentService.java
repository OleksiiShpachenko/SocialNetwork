package com.shpach.sn.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.shpach.sn.persistence.entities.Comment;
import com.shpach.sn.persistence.entities.Friend;
import com.shpach.sn.persistence.entities.Post;
import com.shpach.sn.persistence.entities.User;
import com.shpach.sn.persistence.jdbc.dao.comment.ICommentDao;
import com.shpach.sn.persistence.jdbc.dao.factory.IDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.sn.persistence.jdbc.dao.friend.IFriendDao;
import com.shpach.sn.persistence.jdbc.dao.post.IPostDao;

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
public class CommentService {
	private static CommentService instance = null;

	public static enum friendStatus {
		WAIT, FRIEND
	}

	private ICommentDao commentDao;
	private UserService userService;
	// private TaskService taskService;

	private CommentService() {
		// IDaoFactory daoFactory = new MySqlDaoFactory();
		// friendDao = daoFactory.getFriendDao();
		// taskService = TaskService.getInstance();
	}

	public static synchronized CommentService getInstance() {
		if (instance == null) {
			instance = new CommentService();
		}
		return instance;
	}

	public ICommentDao getCommentDao() {
		if (commentDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			commentDao = daoFactory.getCommentDao();
		}
		return commentDao;
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

	private UserService getUserService() {
		if (userService == null) {
			userService = UserService.getInstance();
		}
		return userService;
	}

	private void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * add {@link Comment} to database
	 * 
	 * @param comment
	 * @return boolean
	 */
	public boolean addNewComment(Comment comment) {
		if (comment == null)
			return false;
		Comment postAppdated = getCommentDao().addOrUpdate(comment);
		if (postAppdated == null)
			return false;
		return true;
	}

	public  void injectCommentsToPost(List<Post> posts) {
		for (Post post : posts) {
			List<Comment> comments=getCommentDao().findCommentByPostId(post.getPostId());
			getUserService().injectUserToComment(comments);
			post.setComments(comments);
		}
		
	}

	public boolean deleteCommentsByPostId(int postId, Connection cn) {
		
		return getCommentDao().deleteCommentsByPostId(postId,cn);
	}

//	public void injectPostToUser(User user) {
//		if (user==null)
//			return;
//		List<Post> posts=getPostDao().findPostByUserId(user.getUserId());
//		if (posts==null)
//			posts=new ArrayList<>();
//		user.setPosts(posts);
//	}
//
//	public List<Post> getPostsWithZeroCommunity(List<Post> posts) {
//		List<Post> res=new ArrayList<>();
//		for (Post post : posts) {
//			if (post.getCommunityId()==0)
//				res.add(post);
//		}
//		return res;
//	}

//	public List<Friend> getFriendByUserId(int userId) {
//		List<Friend> friends = getFriendDao().findFriendByUserId(userId);
//		if (friends != null)
//			return friends;
//		return new ArrayList<Friend>();
//	}

	/**
	 * Delete {@link Friend} from database
	 * 
	 * @param friend
	 * @return
	 */
//	public boolean deleteFriend(Friend friend) {
//		if (friend == null)
//			return false;
//		return getFriendDao().deleteFriendByUsersId(friend.getHostUserId(), friend.getSlaveUserId());
//	}
//
//	public boolean update(Friend friend) {
//		if (friend == null)
//			return false;
//		return getFriendDao().update(friend) != null ? true : false;
//	}

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
