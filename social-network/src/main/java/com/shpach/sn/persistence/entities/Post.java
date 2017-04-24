package com.shpach.sn.persistence.entities;


import java.io.Serializable;
import javax.persistence.*;
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

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public Post() {
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
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}