package com.shpach.sn.persistence.entities;


import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the community database table.
 * 
 */
@Entity
@NamedQuery(name="Community.findAll", query="SELECT c FROM Community c")
public class Community implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="community_id")
	private int communityId;

	@Column(name="community_name")
	private int communityName;

	//bi-directional many-to-one association to CommunityMember
	@OneToMany(mappedBy="community")
	private List<CommunityMember> communityMembers;

	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="community")
	private List<Post> posts;

	public Community() {
	}

	public int getCommunityId() {
		return this.communityId;
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	public int getCommunityName() {
		return this.communityName;
	}

	public void setCommunityName(int communityName) {
		this.communityName = communityName;
	}

	public List<CommunityMember> getCommunityMembers() {
		return this.communityMembers;
	}

	public void setCommunityMembers(List<CommunityMember> communityMembers) {
		this.communityMembers = communityMembers;
	}

	public CommunityMember addCommunityMember(CommunityMember communityMember) {
		getCommunityMembers().add(communityMember);
		communityMember.setCommunity(this);

		return communityMember;
	}

	public CommunityMember removeCommunityMember(CommunityMember communityMember) {
		getCommunityMembers().remove(communityMember);
		communityMember.setCommunity(null);

		return communityMember;
	}

	public List<Post> getPosts() {
		return this.posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Post addPost(Post post) {
		getPosts().add(post);
		post.setCommunity(this);

		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setCommunity(null);

		return post;
	}

}