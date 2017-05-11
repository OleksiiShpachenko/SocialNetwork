package com.shpach.sn.persistence.jdbc.dao.post;

import java.sql.Connection;
import java.util.List;


import com.shpach.sn.persistence.entities.Post;
import com.shpach.sn.persistence.entities.User;

public interface IPostDao {

	public List<Post> findAll();

	public Post addOrUpdate(Post post);

	public Post findPostById(int id);

	public List<Post> findPostByUserId(int userId, int startFrom, int itemsOnPage);

	public List<Post> findPostByUserId(int userId);
	
	public List<Post> findPostByCommunityId(int communityId);

	//public boolean deletePostById(int id);

	boolean deletePostById(int id, Connection con);

	public List<Post> findPostByUserId(List<Integer> usersId);

	public int countUsersPostsById(Object[] array);
}
