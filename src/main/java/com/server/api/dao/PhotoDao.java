package com.server.api.dao;

import java.util.List;
import java.util.Map;

import com.server.api.model.PhotoInfo;

public interface PhotoDao {
	
	public int insertPhotoInfo(PhotoInfo photo);
	
	public List<PhotoInfo> getPhotoList(Map<String, Object> reqMap);
	
	public int deletePhotoById(String deviceid);

	public int getPhotoCount();

	public PhotoInfo getPhotoByDeviceId(String deviceid);

	public int updateThresholdValue(PhotoInfo p);

}
