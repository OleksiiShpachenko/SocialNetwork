package com.shpach.sn.persistence.entities;


import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the post database table.
 * 
 */
@Entity
@NamedQuery(name="Post.findAll", query="SELECT p FROM Post p")
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="post_id")
	private int postId;

	@Lob
	@Column(name="post_text")
	private String postText;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="post")
	private List<Comment> comments;

	//bi-directional many-to-one association to Image
	@OneToMany(mappedBy="post")
	private List<Image> images;

	//bi-directional many-to-one association to Community
	@ManyToOne
	@JoinColumn(name="community_id")
	private Community community;
	private int communityId;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	private int userId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="post_create_datetime")
	private Date postCreateDatetime;

	public Post() {
		postCreateDatetime=new Date();
	}

	public int getPostId() {
		return this.postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getPostText() {
		return this.postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setPost(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setPost(null);

		return comment;
	}

	public List<Image> getImages() {
		return this.images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Image addImage(Image image) {
		getImages().add(image);
		image.setPost(this);

		return image;
	}

	public Image removeImage(Image image) {
		getImages().remove(image);
		image.setPost(null);

		return image;
	}

	public Community getCommunity() {
		return this.community;
	}

	public void setCommunity(Community community) {
		this.community = community;
		setCommunityId(community.getCommunityId());
	}

	public int getCommunityId() {
		return communityId;
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
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

	public Date getPostCreateDatetime() {
		return postCreateDatetime;
	}

	public void setPostCreateDatetime(Date postCreateDatetime) {
		this.postCreateDatetime = postCreateDatetime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + communityId;
		result = prime * result + ((postCreateDatetime == null) ? 0 : postCreateDatetime.hashCode());
		result = prime * result + postId;
		result = prime * result + ((postText == null) ? 0 : postText.hashCode());
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
		Post other = (Post) obj;
		if (communityId != other.communityId)
			return false;
		if (postCreateDatetime == null) {
			if (other.postCreateDatetime != null)
				return false;
		} else if (!postCreateDatetime.equals(other.postCreateDatetime))
			return false;
		if (postId != other.postId)
			return false;
		if (postText == null) {
			if (other.postText != null)
				return false;
		} else if (!postText.equals(other.postText))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	
	

}