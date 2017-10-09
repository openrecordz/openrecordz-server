<%@ include file="/jsp/include.jsp" %>
<%
response.setContentType("application/json");
%>
{
  "channel":"notifications",
  "generated":"${model.now}",
  "newNotifications" :"${model.newNotification}"
}