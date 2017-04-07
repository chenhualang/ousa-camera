<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icon.css" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src='<%=request.getContextPath()%>/js/easyui-lang-zh_CN.js'></script>
</head>
<body>
<!-- 	<form action="uploadimage/setIdentifyThreshold.htm" method="post" name="setThreshold">
	        <div>
	        设置图片识别阈值(0-1之间):
	        <input type="text" name="threshold"/>
	        </div>  
	        <div><input type="submit" value="提交"/></div>  
	</form> -->
	
	<div>
	        更新图片识别阈值(0-1之间):
	        <input type="text" name="threshold" id="threshold"/>
   </div>  
	        
	<br>
   <table id="dg" title="camera management" class="easyui-datagrid" style="width:1300px;height:600px"
            url="<%=request.getContextPath()%>/uploadimage/getAllPhotos.htm"
            toolbar="#toolbar" pagination="true" align="center"
            rownumbers="true" fitColumns="true" onClickRow="clickRow" singleSelect="true">
        <thead>
            <tr>
                <th field="fileName" width="50">图片文件名</th>
                <th field="fileAbsolutPath" data-options="formatter:formatImg">图片</th>
                <th field="deviceid" width="50">NFC设备图片编号</th>
                <th field="threshold" width="50">NFC设备图片对应的识别阈值</th>
            </tr>
        </thead>
    </table>
    
        <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyUser()">删除图片</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-upadte" plain="true" onclick="updateThreshOld()">更新阈值</a>
        </div>
        
            <script type="text/javascript">
        var url;
        function destroyUser(){
            var row = $('#dg').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','Are you sure you want to delete this picture?',function(r){
                    if (r){
                        $.post('<%=request.getContextPath()%>/uploadimage/destroy_picture.htm',{id:row.id,deviceid:row.deviceid},function(result){
                        	if (result){
                                $('#dg').datagrid('reload');    // reload the user data
                            } else {
                                $.messager.show({    // show error message
                                    title: 'Error',
                                    msg: result.errorMsg
                                });
                            }
                        },'text');
                    }
                });
            }
        }
        
        
        function updateThreshOld(){
        	  var row = $('#dg').datagrid('getSelected');
        	  var deviceid = row.deviceid;    
        	  var threshold = $("#threshold").val();
        	  if(threshold==""||threshold.length==0){
        		  alert("请输入识别图片所需的阈值");
        		  return;
        	  }
        	 
        	  if(isNaN(threshold)){
        		  alert("输入的阈值不是数字，请输入符合要求的数字");
        		  return;
        	  }
        	  var n = parseFloat(threshold);
        	  if(n>=1||n<=0){
        		  alert("请输入图片阈值所需的合适的值(0-1之间)");
        		  return;
        	  }
        	  var url = "<%=request.getContextPath()%>/uploadimage/updateThreshOldValue.htm";
        	  $.post(url,{deviceid:deviceid,threshold:threshold},function(result){
        		    alert(result);
        		    $('#dg').datagrid('reload');
        		  });
        }
        
       function formatImg(value,row){
            var str = "";
            if(value!="" || value!=null){
            str = "<img style=\"height: 150px;width: 150px;\" src=\""+value+"\"/>";
                                               return str;
            }
       }
        
        function refreshDatagrid(){
            $('#dg').datagrid('load', {
            });
        }
        
        $(function() {
			$('#dg').datagrid({
				onClickCell : function(rowIndex, field, value) {
					if (field != 'id') {
						$(this).datagrid('unselectAll');
					}

				},
			});

		});
        
    </script>
    

</body>
</html>