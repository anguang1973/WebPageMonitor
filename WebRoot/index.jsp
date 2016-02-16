<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.sict.service.MonitorTask" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">

<title>WebPageMonitor</title>
<meta charset="UTF-8">
<meta name="viewport"
	content="height = device-height,
	width = device-width,
	initial-scale = 1,
	minimum-scale = 0.1,
	maximum-scale = 3,
	user-scalable = yes,
	target-densitydpi = device-dpi
	" />

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>

<body>
	<h2>Task time is <%=MonitorTask.getTickCount() %>.</h2>
</body>
</html>
