<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>灰机</title>
<script type="text/javascript" src="js/jquery-3.3.1.js"></script>
<script type="text/javascript">
	

</script>
</head>
<body>
<form action="showTakeServlet" method="post">
	起飞机场：
	<select name="takeid">
		<option value="0">请选择</option>
		<c:forEach items="${takeport }" var="tp">
			<option value="${tp.id }">${tp.portName }</option>
		</c:forEach>
	</select>
	降落机场：
	<select name="landid">
		<option value="0">请选择</option>
		<c:forEach items="${landport }" var="lp">
			<option value="${lp.id }">${lp.portName }</option>
		</c:forEach>
	</select>
	<input type="submit" value="查询" />
</form>
	<table border="1">
		<tr>
			<th>飞机编号</th>
			<th>起飞机场</th>
			<th>起飞城市</th>
			<th>降落机场</th>
			<th>降落城市</th>
			<th>航行时间</th>
			<th>机票价格</th>
		</tr>
		<c:forEach items="${list }" var="plane">
			<tr>
				<td>${plane.airNo }</td>
				<td>${plane.takePort.portName }</td>
				<td>${plane.takePort.cityName }</td>
				<td>${plane.landPort.portName }</td>
				<td>${plane.landPort.cityName }</td>
				<td>
					<c:if test="${plane.time>=60 }">
						<fmt:formatNumber type="number" value="${plane.time/60 }" maxFractionDigits="0"/>小时${plane.time%60 }分钟
					</c:if>
					<c:if test="${plane.time<60 }">
						${plane.time }分钟
					</c:if>
				</td>
				<td>${plane.price }元</td>
			</tr>
		
		</c:forEach>
	</table>
</body>
</html>