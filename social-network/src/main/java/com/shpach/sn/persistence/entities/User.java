package com.shpach.sn.persistence.entities;


import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="user_id")
	private int userId;

	@Lob
	@Column(name="avatar_url")
	private String avatarUrl;

	@Column(name="user_active")
	private byte userActive;

	@Column(name="user_comment_permition")
	private byte userCommentPermition;

	@Column(name="user_create_community_permition")
	private byte userCreateCommunityPermition;

	@Lob
	@Column(name="user_email")
	private String userEmail;

	@Column(name="user_invite_permition")
	private byte userInvitePermition;

	@Lob
	@Column(name="user_login")
	private String userLogin;

	@Lob
	@Column(name="user_name")
	private String userName;

	@Lob
	@Column(name="user_password")
	private String userPassword;

	@Column(name="user_post_permition")
	private byte userPostPermition;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="user_create_datetime")
	private Date userCreateDatetime;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="user")
	private List<Comment> comments;

	//bi-directional many-to-one association to CommunityMember
	@OneToMany(mappedBy="user")
	private List<CommunityMember> communityMembers;

	//bi-directional many-to-one association to Friend
	@OneToMany(mappedBy="user1")
	private List<Friend> friends1;

	//bi-directional many-to-one association to Friend
	@OneToMany(mappedBy="user2")
	private List<Friend> friends2;

	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="user")
	private List<Post> posts;

	public User() {
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAvatarUrl() {
		return this.avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public byte getUserActive() {
		return this.userActive;
	}

	public void setUserActive(byte userActive) {
		this.userActive = userActive;
	}

	public byte getUserCommentPermition() {
		return this.userCommentPermition;
	}

	public void setUserCommentPermition(byte userCommentPermition) {
		this.userCommentPermition = userCommentPermition;
	}

	public byte getUserCreateCommunityPermition() {
		return this.userCreateCommunityPermition;
	}

	public void setUserCreateCommunityPermition(byte userCreateCommunityPermition) {
		this.userCreateCommunityPermition = userCreateCommunityPermition;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public byte getUserInvitePermition() {
		return this.userInvitePermition;
	}

	public void setUserInvitePermition(byte userInvitePermition) {
		this.userInvitePermition = userInvitePermition;
	}

	public String getUserLogin() {
		return this.userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public byte getUserPostPermition() {
		return this.userPostPermition;
	}

	public void setUserPostPermition(byte userPostPermition) {
		this.userPostPermition = userPostPermition;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setUser(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setUser(null);

		return comment;
	}

	public List<CommunityMember> getCommunityMembers() {
		return this.communityMembers;
	}

	public void setCommunityMembers(List<CommunityMember> communityMembers) {
		this.communityMembers = communityMembers;
	}

	public CommunityMember addCommunityMember(CommunityMember communityMember) {
		getCommunityMembers().add(communityMember);
		communityMember.setUser(this);

		return communityMember;
	}

	public CommunityMember removeCommunityMember(CommunityMember communityMember) {
		getCommunityMembers().remove(communityMember);
		communityMember.setUser(null);

		return communityMember;
	}

	public List<Friend> getFriends1() {
		return this.friends1;
	}

	public void setFriends1(List<Friend> friends1) {
		this.friends1 = friends1;
	}

	public Friend addFriends1(Friend friends1) {
		getFriends1().add(friends1);
		friends1.setHostUser(this);

		return friends1;
	}

	public Friend removeFriends1(Friend friends1) {
		getFriends1().remove(friends1);
		friends1.setHostUser(null);

		return friends1;
	}

	public List<Friend> getFriends2() {
		return this.friends2;
	}

	public void setFriends2(List<Friend> friends2) {
		this.friends2 = friends2;
	}

	public Friend addFriends2(Friend friends2) {
		getFriends2().add(friends2);
		friends2.setSlaveUser(this);

		return friends2;
	}

	public Friend removeFriends2(Friend friends2) {
		getFriends2().remove(friends2);
		friends2.setSlaveUser(null);

		return friends2;
	}

	public List<Post> getPosts() {
		return this.posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Post addPost(Post post) {
		getPosts().add(post);
		post.setUser(this);

		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setUser(null);

		return post;
	}

	public Date getUserCreateDatetime() {
		return userCreateDatetime;
	}

	public void setUserCreateDatetime(Date userCreateDatetime) {
		this.userCreateDatetime = userCreateDatetime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avatarUrl == null) ? 0 : avatarUrl.hashCode());
		result = prime * result + userActive;
		result = prime * result + userCommentPermition;
		result = prime * result + userCreateCommunityPermition;
		result = prime * result + ((userCreateDatetime == null) ? 0 : userCreateDatetime.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result + userId;
		result = prime * result + userInvitePermition;
		result = prime * result + ((userLogin == null) ? 0 : userLogin.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userPassword == null) ? 0 : userPassword.hashCode());
		result = prime * result + userPostPermition;
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
		User other = (User) obj;
		if (avatarUrl == null) {
			if (other.avatarUrl != null)
				return false;
		} else if (!avatarUrl.equals(other.avatarUrl))
			return false;
		if (userActive != other.userActive)
			return false;
		if (userCommentPermition != other.userCommentPermition)
			return false;
		if (userCreateCommunityPermition != other.userCreateCommunityPermition)
			return false;
		if (userCreateDatetime == null) {
			if (other.userCreateDatetime != null)
				return false;
		} else if (!userCreateDatetime.equals(other.userCreateDatetime))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		if (userId != other.userId)
			return false;
		if (userInvitePermition != other.userInvitePermition)
			return false;
		if (userLogin == null) {
			if (other.userLogin != null)
				return false;
		} else if (!userLogin.equals(other.userLogin))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userPassword == null) {
			if (other.userPassword != null)
				return false;
		} else if (!userPassword.equals(other.userPassword))
			return false;
		if (userPostPermition != other.userPostPermition)
			return false;
		return true;
	}

}