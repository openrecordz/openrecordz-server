<%@ include file="/jsp/include.jsp" %>
<%
response.setContentType("application/json");
%>
{
  "channel":"notifications",
  "generated":"${model.now}",
  "count":"${model.count}",
  "items": [
    <c:forEach items="${model.notifications}" var="n"  varStatus="status">
    {
      "id":"<c:out value="${n.id}"/>",     
      "message":"${shp:escapeJSON(n.message)}",
      "from":"${n.from}",
      "to":"${n.to}",
      "refId":"${n.refId}",
      "createdOn":"${n.createdOnRFC822}",
      "read":"${n.read}",
      "type":"${n.type}",
      "historyVisible":"${n.historyVisible}"              
    }${not status.last ? ',' : ''}
    </c:forEach>
  ]
}