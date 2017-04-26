package com.shpach.sn.persistence.entities;


import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the community_member database table.
 * 
 */
@Entity
@Table(name="community_member")
@NamedQuery(name="CommunityMember.findAll", query="SELECT c FROM CommunityMember c")
public class CommunityMember implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="community_member_id")
	private int communityMemberId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="community_member_datetime")
	private Date communityMemberDatetime;

	@Column(name="community_member_status")
	private int communityMemberStatus;

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

	public CommunityMember() {
	}

	public int getCommunityMemberId() {
		return this.communityMemberId;
	}

	public void setCommunityMemberId(int communityMemberId) {
		this.communityMemberId = communityMemberId;
	}

	public Date getCommunityMemberDatetime() {
		return this.communityMemberDatetime;
	}

	public void setCommunityMemberDatetime(Date communityMemberDatetime) {
		this.communityMemberDatetime = communityMemberDatetime;
	}

	public int getCommunityMemberStatus() {
		return this.communityMemberStatus;
	}

	public void setCommunityMemberStatus(int communityMemberStatus) {
		this.communityMemberStatus = communityMemberStatus;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + communityId;
		result = prime * result + ((communityMemberDatetime == null) ? 0 : communityMemberDatetime.hashCode());
		result = prime * result + communityMemberId;
		result = prime * result + communityMemberStatus;
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
		CommunityMember other = (CommunityMember) obj;
		if (communityId != other.communityId)
			return false;
		if (communityMemberDatetime == null) {
			if (other.communityMemberDatetime != null)
				return false;
		} else if (!communityMemberDatetime.equals(other.communityMemberDatetime))
			return false;
		if (communityMemberId != other.communityMemberId)
			return false;
		if (communityMemberStatus != other.communityMemberStatus)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

}