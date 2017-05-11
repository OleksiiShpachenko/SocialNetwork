package com.shpach.sn.persistence.entities;

import java.io.Serializable;
import java.util.Date;

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
    private int postId;
	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	private int userId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="comment_create_datetime")
	private Date commentCreateDatetime;

	public Comment() {
		commentCreateDatetime=new Date();
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
		setPostId(post.getPostId());
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
		setUserId(user.getUserId());
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCommentCreateDatetime() {
		return commentCreateDatetime;
	}

	public void setCommentCreateDatetime(Date commentCreateDatetime) {
		this.commentCreateDatetime = commentCreateDatetime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentCreateDatetime == null) ? 0 : commentCreateDatetime.hashCode());
		result = prime * result + commentId;
		result = prime * result + ((commentText == null) ? 0 : commentText.hashCode());
		result = prime * result + postId;
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (commentCreateDatetime == null) {
			if (other.commentCreateDatetime != null)
				return false;
		} else if (!commentCreateDatetime.equals(other.commentCreateDatetime))
			return false;
		if (commentId != other.commentId)
			return false;
		if (commentText == null) {
			if (other.commentText != null)
				return false;
		} else if (!commentText.equals(other.commentText))
			return false;
		if (postId != other.postId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

}