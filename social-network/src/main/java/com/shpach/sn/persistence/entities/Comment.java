package com.shpach.sn.persistence.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the comment database table.
 * 
 */
@Entity
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="comment_id")
	private int commentId;

	@Lob
	@Column(name="comment_text")
	private String commentText;

	//bi-directional many-to-one association to Post
	@ManyToOne
	@JoinColumn(name="post_id")
	private Post post;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public Comment() {
	}

	public int getCommentId() {
		return this.commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getCommentText() {
		return this.commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public Post getPost() {
		return this.post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}