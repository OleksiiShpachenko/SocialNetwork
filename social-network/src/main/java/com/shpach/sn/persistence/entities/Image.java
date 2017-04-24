package com.shpach.sn.persistence.entities;


import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the image database table.
 * 
 */
@Entity
@NamedQuery(name="Image.findAll", query="SELECT i FROM Image i")
public class Image implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="image_id")
	private int imageId;

	@Lob
	@Column(name="image_url")
	private String imageUrl;

	//bi-directional many-to-one association to Post
	@ManyToOne
	@JoinColumn(name="post_id")
	private Post post;

	public Image() {
	}

	public int getImageId() {
		return this.imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Post getPost() {
		return this.post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

}