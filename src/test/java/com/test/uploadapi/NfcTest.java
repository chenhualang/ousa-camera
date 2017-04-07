//package com.test.uploadapi;
//
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import com.server.api.dao.NfcDeviceDao;
//import com.server.api.dao.PhotoDao;
//import com.server.api.model.NfcDeviceInfo;
//import com.server.api.model.PhotoInfo;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath*:/applicationContextTest.xml")
//public class NfcTest {
//	 @Autowired
//    private NfcDeviceDao            nfcDeviceDao;
//	 
//	    @Autowired
//	    private PhotoDao            photoDao;
//	    
//	 @org.junit.Test
//	public void testNFC(){
//		NfcDeviceInfo nfcDeviceInfo = new NfcDeviceInfo();
//    	String deviceid = "1123eq";
//    	String nfcid = "fafagfag";
//    	String longitude = "fagfag";
//    	String latitude = "fasfaga";
//    	nfcDeviceInfo.setDeviceid(deviceid);
//    	nfcDeviceInfo.setNfcid(nfcid);
//    	nfcDeviceInfo.setLongitude(longitude);
//    	nfcDeviceInfo.setLatitude(latitude);
//    	nfcDeviceDao.insert(nfcDeviceInfo);
//    	System.out.println("插入NFC信息成功");
//	}
//	 
//	 @org.junit.Test
//		public void testUpdate(){
//	    	String deviceid = "3";
//	    	String threshold = "0.8";
//	    	int num = photoDao.updateThresholdValue(threshold, deviceid);
//	    	if(num==1)
//	    	System.out.println("更新图片阈值成功");
//		}
//	 
//	 @org.junit.Test
//		public void testPicture(){
//		 PhotoInfo photo = new PhotoInfo();
//		 String filename = "02.jpg";
//		 String fileAbsolutPath = "/Users/chenhualang/software/apache-tomcat-7.0.73/webapps/uploadapi/upload/02.jpg";
//         photo.setFileName(filename);
//         photo.setFileAbsolutPath(fileAbsolutPath);
//         photo.setDeviceid("75944524523679");
//         photoDao.insertPhotoInfo(photo);
//         System.out.println("插入图片信息成功");
//	 }
//	 
//	 
//	 
//	 @org.junit.Test
//		public void testGetAllPhotos(){
//		 Map<String,Object> reqMap = new HashMap<String,Object>();
//		   reqMap.put("startPage",0);
//		   reqMap.put("limit", 10);
//		   List<PhotoInfo> photoList = photoDao.getPhotoList(reqMap);
//		   int photoCount = photoDao.getPhotoCount();
//         System.out.println("插入图片信息成功");
//	 }
//}
