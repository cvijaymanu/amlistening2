package com.amlistening2.commons;

import java.util.Date;

/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
public class TrackInfoTO {
	private long trackId;
	private String trackName;
	private String trackAlbum;
	private String trackArtist;
	private String playedDate;
	private long totalPlayedCount;
	private long todayPlayedCount;
	private long weekPlayedCount; // recent means for 7 days
	private boolean postToFacebook;
	private boolean postedToGPlus;
	private boolean postedToTwitter;
	private Date socialPostDate;

	/**
	 * @return the trackId
	 */
	public long getTrackId() {
		return trackId;
	}

	/**
	 * @param trackId
	 *            the trackId to set
	 */
	public void setTrackId(long trackId) {
		this.trackId = trackId;
	}

	/**
	 * @return the trackName
	 */
	public String getTrackName() {
		return trackName;
	}

	/**
	 * @param trackName
	 *            the trackName to set
	 */
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	/**
	 * @return the trackAlbum
	 */
	public String getTrackAlbum() {
		return trackAlbum;
	}

	/**
	 * @param trackAlbum
	 *            the trackAlbum to set
	 */
	public void setTrackAlbum(String trackAlbum) {
		this.trackAlbum = trackAlbum;
	}

	/**
	 * @return the trackArtist
	 */
	public String getTrackArtist() {
		return trackArtist;
	}

	/**
	 * @param trackArtist
	 *            the trackArtist to set
	 */
	public void setTrackArtist(String trackArtist) {
		this.trackArtist = trackArtist;
	}

	public void setTotalPlayedCount(long trackCount) {
		this.totalPlayedCount = trackCount;
	}

	public long getTotalPlayedCount() {
		return totalPlayedCount;
	}

	/**
	 * @param todayPlayedCount
	 *            the todayPlayedCount to set
	 */
	public void setTodayPlayedCount(long todayPlayedCount) {
		this.todayPlayedCount = todayPlayedCount;
	}

	/**
	 * @return the todayPlayedCount
	 */
	public long getTodayPlayedCount() {
		return todayPlayedCount;
	}

	/**
	 * @param weekPlayedCount
	 *            the weekPlayedCount to set
	 */
	public void setWeekPlayedCount(long recentPlayedCount) {
		this.weekPlayedCount = recentPlayedCount;
	}

	/**
	 * @return the weekPlayedCount
	 */
	public long getWeekPlayedCount() {
		return weekPlayedCount;
	}

	/**
	 * @return the playedDate
	 */
	public String getPlayedDate() {
		return playedDate;
	}

	/**
	 * @param playedDate
	 *            the playedDate to set
	 */
	public void setPlayedDate(String playedDate) {
		this.playedDate = playedDate;
	}

	/**
	 * @return the postToFacebook
	 */
	public boolean postToFacebook() {
		return postToFacebook;
	}

	/**
	 * @param postToFacebook
	 *            the postToFacebook to set
	 */
	public void setPostToFacebook(boolean postedToFacebook) {
		this.postToFacebook = postedToFacebook;
	}

	/**
	 * @return the postedToGPlus
	 */
	public boolean isPostedToGPlus() {
		return postedToGPlus;
	}

	/**
	 * @param postedToGPlus
	 *            the postedToGPlus to set
	 */
	public void setPostedToGPlus(boolean postedToGPlus) {
		this.postedToGPlus = postedToGPlus;
	}

	/**
	 * @return the postedToTwitter
	 */
	public boolean isPostedToTwitter() {
		return postedToTwitter;
	}

	/**
	 * @param postedToTwitter
	 *            the postedToTwitter to set
	 */
	public void setPostedToTwitter(boolean postedToTwitter) {
		this.postedToTwitter = postedToTwitter;
	}

	/**
	 * @param socialPostDate
	 *            the socialPostDate to set
	 */
	public void setSocialPostDate(Date socialPostDate) {
		this.socialPostDate = socialPostDate;
	}

	/**
	 * @return the socialPostDate
	 */
	public Date getSocialPostDate() {
		return socialPostDate;
	}

}
