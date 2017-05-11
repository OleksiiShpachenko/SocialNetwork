package com.shpach.sn.persistence.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the friend database table.
 * 
 */
@Entity
@NamedQuery(name = "Friend.findAll", query = "SELECT f FROM Friend f")
public class Friend implements Serializable {
	private static final long serialVersionUID = 1L;

	// @EmbeddedId
	// private FriendPK id;

	@Column(name = "friend_status")
	private int friendStatus;

	@Column(name = "friend_status_datetime")
	private Date friendStatusDatetime;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "host_user_id")
	private User HostUser;
	private int hostUserId;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "slave_user_id")
	private User SlaveUser;
	private int slaveUserId;

	public Friend() {
		friendStatusDatetime=new Date();
	}

	// public FriendPK getId() {
	// return this.id;
	// }
	//
	// public void setId(FriendPK id) {
	// this.id = id;
	// }

	public int getHostUserId() {
		return hostUserId;
	}

	public void setHostUserId(int hostUserId) {
		this.hostUserId = hostUserId;
	}

	public int getSlaveUserId() {
		return slaveUserId;
	}

	public void setSlaveUserId(int slaveUserId) {
		this.slaveUserId = slaveUserId;
	}

	public int getFriendStatus() {
		return this.friendStatus;
	}

	public void setFriendStatus(int friendStatus) {
		this.friendStatus = friendStatus;
	}

	public Date getFriendStatusDatetime() {
		return this.friendStatusDatetime;
	}

	public void setFriendStatusDatetime(Date friendStatusDatetime) {
		this.friendStatusDatetime = friendStatusDatetime;
	}

	public User getHostUser() {
		return this.HostUser;
	}

	public void setHostUser(User hostUser) {
		this.HostUser = hostUser;
		setHostUserId(hostUser.getUserId());
	}

	public User getSlaveUser() {
		return this.SlaveUser;
	}

	public void setSlaveUser(User slaveUser) {
		this.SlaveUser = slaveUser;
		setSlaveUserId(slaveUser.getUserId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + friendStatus;
		result = prime * result + ((friendStatusDatetime == null) ? 0 : friendStatusDatetime.hashCode());
		result = prime * result + hostUserId;
		result = prime * result + slaveUserId;
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
		Friend other = (Friend) obj;
		if (friendStatus != other.friendStatus)
			return false;
		if (friendStatusDatetime == null) {
			if (other.friendStatusDatetime != null)
				return false;
		} else if (!friendStatusDatetime.equals(other.friendStatusDatetime))
			return false;
		if (hostUserId != other.hostUserId)
			return false;
		if (slaveUserId != other.slaveUserId)
			return false;
		return true;
	}

}