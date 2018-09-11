<%@ page language="java" isErrorPage="true" pageEncoding="UTF-8" import="java.util.Map"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>thread running</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  
  <body>
  <%
  Map<Thread, StackTraceElement[]>  map  =Thread.getAllStackTraces();
	for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
		if(entry.getKey() == Thread.currentThread()){
			continue;
		}
		out.println("<br/>---"+entry.getKey().getName()+"---");
		for (StackTraceElement element : entry.getValue()) {
			out.println("<br/>"+element);
		}
	}
  %>
  </body>
</html>
