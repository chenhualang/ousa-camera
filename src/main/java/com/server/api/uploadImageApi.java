package com.server.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.server.api.dao.PhotoDao;
import com.server.api.model.PhotoInfo;

@Controller
@RequestMapping("/uploadimage")
public class uploadImageApi {
	
	private Logger         logger         = LoggerFactory.getLogger(uploadImageApi.class);
	
    @Autowired
    private PhotoDao            photoDao;

    @SuppressWarnings("restriction")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public void upload(HttpServletRequest request,HttpServletResponse response,PrintWriter out){
		logger.debug("���յ��ͻ�������"+request);
        //��ô����ļ���Ŀ����  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //��ȡ�ļ���Ҫ�ϴ�����·��  
        String path = request.getRealPath("/upload");  
//        String path = request.getSession().getServletContext().getRealPath("/");
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        factory.setRepository(new File(path));  
        //���� ����Ĵ�С�����ϴ��ļ������������û���ʱ��ֱ�ӷŵ� ��ʱ�洢��  
        factory.setSizeThreshold(1024*1024) ;  
        //��ˮƽ��API�ļ��ϴ�����  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        try {  
            //�����ϴ�����ļ�  
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request); 
            String deviceid = list.get(0).getString();
            String threshold = list.get(1).getString();
            for(FileItem item : list){  
                //��ȡ������������  
                String name = item.getFieldName();  
                //�����ȡ�� ����Ϣ����ͨ�� �ı� ��Ϣ  
                if(item.isFormField()){                     
                    //��ȡ�û�����������ַ��� ���������ͦ�ã���Ϊ���ύ�������� �ַ������͵�  
//                	deviceid = item.getString() ;  
                    request.setAttribute(name, deviceid);  
                }else{  
                    //��ȡ·����  
                    String value = item.getName() ;  
                    //���������һ����б��  
                    int start = value.lastIndexOf("\\");  
                    //��ȡ �ϴ��ļ��� �ַ������֣���1�� ȥ����б�ܣ�  
                    String filename = value.substring(start+1);  
                    String fileAbsolutPath = path+"/"+filename;
                    String fileRelativePath = "upload/"+filename;
                    logger.debug("�ļ���Ϊ"+filename);
                    request.setAttribute(name, filename);  
                    
                    
                    OutputStream fos = new FileOutputStream(new File(path,filename));
                    InputStream in = item.getInputStream() ;
                    int length = 0 ;
                    byte [] buf = new byte[1024] ;
                    System.out.println("��ȡ�ϴ��ļ����ܹ���������"+item.getSize());
                    // in.read(buf) ÿ�ζ��������ݴ����   buf ������
                    while( (length = in.read(buf) ) != -1){
                        //��   buf ������ ȡ������ д�� ���������������
                    	fos.write(buf, 0, length);
                    }
                    in.close();
                    fos.close();
                    
    	            PhotoInfo photo = new PhotoInfo();
    	            photo.setFileName(filename);
    	            photo.setFileAbsolutPath(fileRelativePath);
    	            photo.setDeviceid(deviceid);
    	            photo.setThreshold(threshold);
    	            photoDao.insertPhotoInfo(photo);
                    System.out.println("�ϴ��ɹ���"+filename);
                    logger.debug("ͼƬ�ɹ��ϴ������ݿ���");
                    
                 
                    //��·�����ظ��ͻ���
                    Response res = new Response();
                    res.setCode("0");
                    res.setMessage("�ϴ��ɹ�");
                    Map<String, String> params = new HashMap<String,String>();
                    params.put("filename", filename);
                    res.setParams(params);
                    out.print(JSON.toJSONString(res));
                    return;
                }  
            }  
              
        } catch (Exception e) {  
        	e.printStackTrace();
        	System.out.println("�ϴ�ʧ��");
            Response res = new Response();
            res.setCode("-1");
            res.setMessage("�ϴ�ʧ�ܣ�"+e.getMessage());
            out.print(JSON.toJSONString(res));
            return;
        }
        
        Response res = new Response();
        res.setCode("-2");
        res.setMessage("δ�����ļ�");
        out.print(JSON.toJSONString(res));
        return ;
	}
    
//    @RequestMapping(value = "/download", method = RequestMethod.POST) 
//    @ResponseBody
//    public void download(HttpServletRequest request,HttpServletResponse response) {
//	        // ��������ʽ�����ļ���
//	        try { 
//	        	
//	        	List<PhotoInfo> photoInfos = photoDao.getPhotoList();
//	        	
//	        	if(photoInfos == null && photoInfos.size() == 0){
//	        		System.out.println("δ�ҵ�ͼƬ");
//	        		return;
//	        	}
//	        	
//	        	// ��ʱȡ��һ��Ԫ��
//	        	PhotoInfo photoInfo = photoInfos.get(0);
//	        	
//	        	@SuppressWarnings("restriction")
//				byte[] photoBytes = (new sun.misc.BASE64Decoder()).decodeBuffer(photoInfo.getPhotoContent());
//	        	
//	            //3.ͨ��response��ȡServletOutputStream����(out)  
//	            OutputStream out = response.getOutputStream();  
//	            out.write(photoBytes);
//	            
//	            out.close();  
//	            out.flush();  
//	  
//	        } catch (IOException e) {  
//	            e.printStackTrace();  
//	        }
//   }
    
    
    
  @RequestMapping(value = "/getAllPhotos", method = RequestMethod.POST) 
  public void getAllPhotos(@RequestParam(value="rows", required = false)Integer pageSize, 
			@RequestParam(value="page", required = false)Integer pageNo,
			HttpServletRequest request,HttpServletResponse response,PrintWriter out){
	   Map<String,Object> reqMap = new HashMap<String,Object>();
	   reqMap.put("startPage", (pageNo - 1)*pageSize);
	   reqMap.put("limit", pageSize);
	   List<PhotoInfo> photoList = photoDao.getPhotoList(reqMap);
	   int photoCount = photoDao.getPhotoCount();
	   
		JSONObject resultJson =  new JSONObject();
		resultJson.put("rows", photoList);	
		resultJson.put("total",photoCount);
		String resultInfo = resultJson.toJSONString();
		out.write(resultInfo);
  }
  
  
  @RequestMapping(value = "/getPhotoByDeviceId", method = RequestMethod.GET) 
  @ResponseBody
  public void getPhotoByDeviceId(HttpServletRequest request,HttpServletResponse response,PrintWriter out){
         String deviceid = request.getParameter("deviceid");
         PhotoInfo photoInfo = photoDao.getPhotoByDeviceId(deviceid);
         String fileRelativePath = null;
         Map<String,String> resultMap = new HashMap<String,String>();
         String photo = "";
         if(photoInfo!=null){
        	  fileRelativePath = photoInfo.getFileAbsolutPath();
        	  resultMap.put("flag","1");
    		 
         }else{
        	  resultMap.put("flag","0");
    		  resultMap.put("photoContent",photo);
         }
        
         String contextPath = request.getRealPath("/");
         String filePath = contextPath+"/"+fileRelativePath;
         
         
         FileInputStream is = null;
//         OutputStream toClient = null;
         try{
        	 is = new FileInputStream(filePath);
             int i = is.available(); // �õ��ļ���С
             byte data[] = new byte[i];
             is.read(data); // ������
//             toClient = response.getOutputStream(); // �õ���ͻ���������������ݵĶ���
//             toClient.write(data); // �������
             resultMap.put("photoContent",(new sun.misc.BASE64Encoder()).encode(data));
         }catch(Exception e){
        	 e.printStackTrace();
         }finally{  
			   try{
				   if(is!=null){  
					    is.close();  
					   }  
//					   if(toClient!=null){  
//						toClient.close();  
//						toClient.flush();
//					   }  
			   }catch(Exception e){
				   e.printStackTrace();
			   }
		     
		  }
         String json = JSON.toJSONString(resultMap);
  	   out.print(json);
  }
  
  
  @RequestMapping(value = "/getThreshOldByDeviceId", method = RequestMethod.GET) 
  @ResponseBody
  public void getThreshOldByDeviceId(HttpServletRequest request,HttpServletResponse response,PrintWriter out){
	  String deviceid = request.getParameter("deviceid");
      PhotoInfo photoInfo = photoDao.getPhotoByDeviceId(deviceid);
      String threshold = "";
      Map<String,String> resultMap = new HashMap<String,String>();
      if(photoInfo!=null){
    	  threshold = photoInfo.getThreshold(); 
    	  resultMap.put("flag","1");
		  resultMap.put("thresholdvalue",threshold);
      }else{
    	  resultMap.put("flag","0");
		  resultMap.put("thresholdvalue",threshold);
      }
      String json = JSON.toJSONString(resultMap);
	  out.print(json);
  }
  
  
  @RequestMapping(value = "/updateThreshOldValue", method = RequestMethod.POST) 
  @ResponseBody
  public void updateThreshOldValue(HttpServletRequest request,HttpServletResponse response,PrintWriter out){
	  String deviceid = request.getParameter("deviceid");
	  String threshold = request.getParameter("threshold");
	  PhotoInfo p = new PhotoInfo();
	  p.setDeviceid(deviceid);
	  p.setThreshold(threshold);
	  int num = photoDao.updateThresholdValue(p);
	  if(num==1)
      out.print("update threshold value success");
  }
  
@RequestMapping(value = "/destroy_picture", method = RequestMethod.POST) 
public void destroy_picture(HttpServletRequest request,HttpServletResponse response,PrintWriter out){
       String deviceid = request.getParameter("deviceid");
       photoDao.deletePhotoById(deviceid);
	   out.print("delete picture success");
}


@RequestMapping(value = "/setIdentifyThreshold", method = RequestMethod.POST) 
public void setIdentifyThreshold(HttpServletRequest request,HttpServletResponse response,PrintWriter out){
       String threshold = request.getParameter("threshold");
       String path = request.getRealPath("/");
       String filePath = path+"threshold.properties";
       PrintWriter fo = null;
	       try {
	    	   File f = new File(filePath);
	           if(!f.exists()){
	        	   f.createNewFile();
	           }
			fo = new PrintWriter(filePath);
			fo.write(threshold.toCharArray());
			fo.flush();
			out.print("success update threshold value");
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			out.print("update threshold value exception");
			return;
		} catch(IOException e){
			e.printStackTrace();
			out.print("update threshold value exception");
			return;
		}finally{
			if(fo!=null){
			  fo.close();
			}
			
		}
   }



@RequestMapping(value = "/getThresholdValue", method = RequestMethod.POST) 
public void getThresholdValue(HttpServletRequest request,HttpServletResponse response,PrintWriter out){
	 FileReader fileReader=null;  
	 BufferedReader bufferedReader=null; 
	 String path = request.getRealPath("/");
     String filePath = path+"threshold.properties";
     String result=""; 
     Map<String,String> resultMap = new HashMap<String,String>();
	 try{
		 fileReader=new FileReader(filePath);  
		 bufferedReader=new BufferedReader(fileReader);  
		 String read="";  
		    while((read=bufferedReader.readLine())!=null){  
		     result=result+read;  
		    }  
		    resultMap.put("flag","1");
			resultMap.put("thresholdvalue",result.trim());
	 }catch(Exception e){
		 e.printStackTrace();
		
		 resultMap.put("flag","0");
		 resultMap.put("thresholdvalue", result);
	 }finally{  
				   try{
					   if(bufferedReader!=null){  
						    bufferedReader.close();  
						   }  
						   if(fileReader!=null){  
						    fileReader.close();  
						   }  
				   }catch(Exception e){
					   e.printStackTrace();
				   }
			     
			  }  
	   String json = JSON.toJSONString(resultMap);
	   out.print(json);
}


}
