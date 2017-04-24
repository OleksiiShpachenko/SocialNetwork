package com.shpach.sn.persistence.entities;


import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the friend database table.
 * 
 */
@Entity
@NamedQuery(name="Friend.findAll", query="SELECT f FROM Friend f")
public class Friend implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FriendPK id;

	@Column(name="friend_status")
	private int friendStatus;

	@Column(name="friend_status_datetime")
	private Timestamp friendStatusDatetime;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="host_user_id")
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="slave_user_id")
	private User user2;

	public Friend() {
	}

	public FriendPK getId() {
		return this.id;
	}

	public void setId(FriendPK id) {
		this.id = id;
	}

	public int getFriendStatus() {
		return this.friendStatus;
	}

	public void setFriendStatus(int friendStatus) {
		this.friendStatus = friendStatus;
	}

	public Timestamp getFriendStatusDatetime() {
		return this.friendStatusDatetime;
	}

	public void setFriendStatusDatetime(Timestamp friendStatusDatetime) {
		this.friendStatusDatetime = friendStatusDatetime;
	}

	public User getUser1() {
		return this.user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return this.user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

}