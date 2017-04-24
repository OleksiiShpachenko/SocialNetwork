package com.shpach.sn.persistence.entities;


import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the friend database table.
 * 
 */
@Embeddable
public class FriendPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="host_user_id", insertable=false, updatable=false)
	private int hostUserId;

	@Column(name="slave_user_id", insertable=false, updatable=false)
	private int slaveUserId;

	public FriendPK() {
	}
	public int getHostUserId() {
		return this.hostUserId;
	}
	public void setHostUserId(int hostUserId) {
		this.hostUserId = hostUserId;
	}
	public int getSlaveUserId() {
		return this.slaveUserId;
	}
	public void setSlaveUserId(int slaveUserId) {
		this.slaveUserId = slaveUserId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FriendPK)) {
			return false;
		}
		FriendPK castOther = (FriendPK)other;
		return 
			(this.hostUserId == castOther.hostUserId)
			&& (this.slaveUserId == castOther.slaveUserId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.hostUserId;
		hash = hash * prime + this.slaveUserId;
		
		return hash;
	}
}