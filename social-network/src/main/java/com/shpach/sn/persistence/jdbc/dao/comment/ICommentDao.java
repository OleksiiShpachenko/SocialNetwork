package com.shpach.sn.persistence.jdbc.dao.comment;

import java.util.List;

import com.shpach.sn.persistence.entities.Comment;

public interface ICommentDao {
	public List<Comment> findAll();

	public Comment addOrUpdate(Comment comment);

	public Comment findCommentById(int id);

	public List<Comment> findCommentByUserId(int userId);

	public List<Comment> findCommentByPostId(int postId);

	public boolean deleteCommentById(int id);
}
