package com.server.api;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.server.api.dao.NfcDeviceDao;
import com.server.api.model.NfcDeviceInfo;


@Controller
@RequestMapping("/handlejson")
public class uploadJsonStringApi {
	private Logger         logger         = LoggerFactory.getLogger(uploadImageApi.class);
    @Autowired
    private NfcDeviceDao            nfcDeviceDao;
    @RequestMapping(value = "/read", method = RequestMethod.POST) 
    public void read(HttpServletRequest request,HttpServletResponse response,PrintWriter out) {
    	String deviceid = request.getParameter("deviceid");
    	String result = null;
    	NfcDeviceInfo nfcDeviceInfo = nfcDeviceDao.selectByPrimaryKey(deviceid);
    	if(nfcDeviceInfo==null){
    		logger.info("数据库中不存在此设备信息，请检查参数!");
    		result = "数据库中不存在此设备信息，请检查参数!";
    		out.print(result);
    		
    	}else{
    		result = JSON.toJSONString(nfcDeviceInfo); 
    		out.print(result);
    	}
    	return;
    }
    
    @RequestMapping(value = "/write", method = RequestMethod.POST) 
    public void write(HttpServletRequest request,HttpServletResponse response,PrintWriter out){
    	NfcDeviceInfo nfcDeviceInfo = new NfcDeviceInfo();
    	String deviceid = request.getParameter("deviceid");
    	String nfcid = request.getParameter("nfcid");
    	String longitude = request.getParameter("longitude");
    	String latitude = request.getParameter("latitude");
    	nfcDeviceInfo.setDeviceid(deviceid);
    	nfcDeviceInfo.setNfcid(nfcid);
    	nfcDeviceInfo.setLongitude(longitude);
    	nfcDeviceInfo.setLatitude(latitude);
    	//插入前先检查数据库中是否已经存在这条NFC设备信息
    	NfcDeviceInfo nfcResult = nfcDeviceDao.selectByPrimaryKey(deviceid);
    	if(nfcResult==null){
    		logger.info("数据库中不存在该NFC设备信息，插入此条NFC信息");
    		nfcDeviceDao.insert(nfcDeviceInfo);
    	}else{
    		logger.info("数据库中已经存在此NFC设备信息，请检查参数");
    	    out.print("数据库中已经存在此NFC设备信息，请检查参数");
    	    return;
    	}
    	
    	logger.info("NFC信息储存成功");
    	out.print("NFC信息储存成功");
    	return;
    }
	
}
