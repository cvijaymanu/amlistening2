/**
 * 
 */
package com.amlistening2.dao;

import com.amlistening2.commons.TrackInfoTO;

/**
 * @author vijju
 * 
 */
public interface IMusicSocialDAO {

	public void insertTrackInfo(TrackInfoTO trackInfoTO);

	public void trackLookup(TrackInfoTO trackInfoTO);

	public boolean updateTrackRecord(TrackInfoTO trackInfoTO);

	public boolean updateSocialPostStatus(long trackId, boolean status);

	public TrackInfoTO getTopSongFor(String timePeriod);

}
