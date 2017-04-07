# ousa-camera
1.首先从git上clone项目下来到本地，eclipse和intellij idea导入项目，或者直接checkout from git.
2.根据需要更改applicationContext-mybatis.xml配置文件中的数据库相关配置，数据库服务器机器IP地址，端口一般默认，用户名，密码。
3.右键点击pom.xml，运行maven install,在console信息里会显示生成的war包路径，进入该路径找到war包，复制war包到tomcat的webapp目录下。
4.进入tomcat的bin目录下，执行startup.bat启动tomcat.如果提示进程已存在，可以执行命令：ps -ef|grep java，找到Java进程号然后kill -9 processid杀死进程再重启tomcat;
5.Tomcat成功启动以后,访问http://106.14.60.205:8080/uploadapi/test.jsp后台管理页面，成功显示即可。http://106.14.60.205:8080/uploadapi/testUploadImage.html为后台上传图片页面。  
  根据需要修改IP地址为部署tomcat的服务器的IP地址,URL其他部分不需要修改。
  
  
  
  注意事项：
  1.到时候安卓部分应该需要对应修改相应地址，根据你们那边服务端地址对应修改即可，具体到时候可以咨询我们；
  2.使用的数据库为mysql,版本5.6 5.7应该都行；
  3.建表语句如下：
  DROP TABLE IF EXISTS `t_photo_info`;
CREATE TABLE `t_photo_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) DEFAULT NULL,
  `threshold` varchar(10) DEFAULT NULL,
  `deviceid` varchar(100) DEFAULT NULL,
  `file_Absolut_Path` varchar(100) DEFAULT NULL,
  `gmt_create` timestamp(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8;
4.如果需要自己运行查看代码，IDE需要配置maven，下载maven，相关配置，具体可以百度或者咨询我。
