package com.shpach.sn.persistence.entities;


import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


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

	@Column(name="community_member_datetime")
	private Timestamp communityMemberDatetime;

	@Column(name="community_member_status")
	private int communityMemberStatus;

	//bi-directional many-to-one association to Community
	@ManyToOne
	@JoinColumn(name="community_id")
	private Community community;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public CommunityMember() {
	}

	public int getCommunityMemberId() {
		return this.communityMemberId;
	}

	public void setCommunityMemberId(int communityMemberId) {
		this.communityMemberId = communityMemberId;
	}

	public Timestamp getCommunityMemberDatetime() {
		return this.communityMemberDatetime;
	}

	public void setCommunityMemberDatetime(Timestamp communityMemberDatetime) {
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
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}