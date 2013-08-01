package com.neopixl.restpixlsample.domain;

import java.util.ArrayList;

/**
 * Mooder = people
 * @author odemolliens
 *
 */
public class Mooder {

	private String mId;
	private String mLastName;
	private String mFirstName;
	private String mAvatarUrl;
	private ArrayList<String> mMessageList;
	private ArrayList<String> mFriendList;

	public Mooder(String id, String lastName, String firstName,
			String avatarUrl, ArrayList<String> messageList,
			ArrayList<String> friendList) {
		super();
		this.mId = id;
		this.mLastName = lastName;
		this.mFirstName = firstName;
		this.mAvatarUrl = avatarUrl;
		this.mMessageList = messageList;
		this.mFriendList = friendList;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String lastName) {
		this.mLastName = lastName;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String firstName) {
		this.mFirstName = firstName;
	}

	public String getAvatarUrl() {
		return mAvatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.mAvatarUrl = avatarUrl;
	}

	public ArrayList<String> getMessageList() {
		return mMessageList;
	}

	public void setMessageList(ArrayList<String> messageList) {
		this.mMessageList = messageList;
	}

	@Override
	public String toString() {
		return "Mooder [id=" + mId + ", lastName=" + mLastName + ", firstName="
				+ mFirstName + ", avatarUrl=" + mAvatarUrl + ", messageList="
				+ mMessageList + "]";
	}

	public ArrayList<String> getFriendList() {
		return mFriendList;
	}

	public void setFriendList(ArrayList<String> friendList) {
		this.mFriendList = friendList;
	}


}
