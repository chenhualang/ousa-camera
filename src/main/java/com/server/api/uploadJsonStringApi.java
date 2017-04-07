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
    		logger.info("���ݿ��в����ڴ��豸��Ϣ���������!");
    		result = "���ݿ��в����ڴ��豸��Ϣ���������!";
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
    	//����ǰ�ȼ�����ݿ����Ƿ��Ѿ���������NFC�豸��Ϣ
    	NfcDeviceInfo nfcResult = nfcDeviceDao.selectByPrimaryKey(deviceid);
    	if(nfcResult==null){
    		logger.info("���ݿ��в����ڸ�NFC�豸��Ϣ���������NFC��Ϣ");
    		nfcDeviceDao.insert(nfcDeviceInfo);
    	}else{
    		logger.info("���ݿ����Ѿ����ڴ�NFC�豸��Ϣ���������");
    	    out.print("���ݿ����Ѿ����ڴ�NFC�豸��Ϣ���������");
    	    return;
    	}
    	
    	logger.info("NFC��Ϣ����ɹ�");
    	out.print("NFC��Ϣ����ɹ�");
    	return;
    }
	
}
