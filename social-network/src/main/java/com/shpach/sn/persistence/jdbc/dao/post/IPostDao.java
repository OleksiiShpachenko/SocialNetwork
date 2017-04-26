package com.shpach.sn.persistence.jdbc.dao.post;

import java.util.List;

import com.shpach.sn.persistence.entities.Post;

public interface IPostDao {

	public List<Post> findAll();

	public Post addOrUpdate(Post post);

	public Post findPostById(int id);

	public List<Post> findPostByUserId(int userId);

	public List<Post> findPostByCommunityId(int communityId);

	public boolean deletePostById(int id);
}
