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
		logger.debug("接收到客户端请求"+request);
        //获得磁盘文件条目工厂  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //获取文件需要上传到的路径  
        String path = request.getRealPath("/upload");  
//        String path = request.getSession().getServletContext().getRealPath("/");
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        factory.setRepository(new File(path));  
        //设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室  
        factory.setSizeThreshold(1024*1024) ;  
        //高水平的API文件上传处理  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        try {  
            //可以上传多个文件  
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request); 
            String deviceid = list.get(0).getString();
            String threshold = list.get(1).getString();
            for(FileItem item : list){  
                //获取表单的属性名字  
                String name = item.getFieldName();  
                //如果获取的 表单信息是普通的 文本 信息  
                if(item.isFormField()){                     
                    //获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的  
//                	deviceid = item.getString() ;  
                    request.setAttribute(name, deviceid);  
                }else{  
                    //获取路径名  
                    String value = item.getName() ;  
                    //索引到最后一个反斜杠  
                    int start = value.lastIndexOf("\\");  
                    //截取 上传文件的 字符串名字，加1是 去掉反斜杠，  
                    String filename = value.substring(start+1);  
                    String fileAbsolutPath = path+"/"+filename;
                    String fileRelativePath = "upload/"+filename;
                    logger.debug("文件名为"+filename);
                    request.setAttribute(name, filename);  
                    
                    
                    OutputStream fos = new FileOutputStream(new File(path,filename));
                    InputStream in = item.getInputStream() ;
                    int length = 0 ;
                    byte [] buf = new byte[1024] ;
                    System.out.println("获取上传文件的总共的容量："+item.getSize());
                    // in.read(buf) 每次读到的数据存放在   buf 数组中
                    while( (length = in.read(buf) ) != -1){
                        //在   buf 数组中 取出数据 写到 （输出流）磁盘上
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
                    System.out.println("上传成功："+filename);
                    logger.debug("图片成功上传到数据库中");
                    
                 
                    //将路径返回给客户端
                    Response res = new Response();
                    res.setCode("0");
                    res.setMessage("上传成功");
                    Map<String, String> params = new HashMap<String,String>();
                    params.put("filename", filename);
                    res.setParams(params);
                    out.print(JSON.toJSONString(res));
                    return;
                }  
            }  
              
        } catch (Exception e) {  
        	e.printStackTrace();
        	System.out.println("上传失败");
            Response res = new Response();
            res.setCode("-1");
            res.setMessage("上传失败："+e.getMessage());
            out.print(JSON.toJSONString(res));
            return;
        }
        
        Response res = new Response();
        res.setCode("-2");
        res.setMessage("未发现文件");
        out.print(JSON.toJSONString(res));
        return ;
	}
    
//    @RequestMapping(value = "/download", method = RequestMethod.POST) 
//    @ResponseBody
//    public void download(HttpServletRequest request,HttpServletResponse response) {
//	        // 以流的形式下载文件。
//	        try { 
//	        	
//	        	List<PhotoInfo> photoInfos = photoDao.getPhotoList();
//	        	
//	        	if(photoInfos == null && photoInfos.size() == 0){
//	        		System.out.println("未找到图片");
//	        		return;
//	        	}
//	        	
//	        	// 暂时取第一个元素
//	        	PhotoInfo photoInfo = photoInfos.get(0);
//	        	
//	        	@SuppressWarnings("restriction")
//				byte[] photoBytes = (new sun.misc.BASE64Decoder()).decodeBuffer(photoInfo.getPhotoContent());
//	        	
//	            //3.通过response获取ServletOutputStream对象(out)  
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
             int i = is.available(); // 得到文件大小
             byte data[] = new byte[i];
             is.read(data); // 读数据
//             toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
//             toClient.write(data); // 输出数据
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
