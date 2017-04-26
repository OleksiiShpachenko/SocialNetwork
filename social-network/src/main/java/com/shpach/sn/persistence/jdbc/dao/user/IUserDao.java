package com.shpach.sn.persistence.jdbc.dao.user;

import java.util.List;

import com.shpach.sn.persistence.entities.User;

public interface IUserDao {
	 List<User> findAll();

	 User addOrUpdate(User user);

	 User findUserById(int id);

	 User findUserByEmail(String login);
	
	 User findUserByName(String name);
	
	List<User> findUsersWhichLikesPostByPostId(int postId);

	//public List<User> findUsersByCommunityId(int id);
}
