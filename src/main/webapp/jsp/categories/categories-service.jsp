<%@ include file="/jsp/include.jsp" %>
<%

response.setContentType("application/json");
//try {Thread.sleep(2000);} catch(Exception e) {	}
%>{
  "channel":"categories",
  "generated":"${model.now}",
  "items": [ <c:forEach items="${model.categories}" var="c"  varStatus="status">
    {
      <%-- "type":"<c:out value="${c.type}"/>", --%>
      "id":"<c:out value="${c.id}"/>",
      "name":"${shp:escapeJSON(c.name)}",
      "label":"${shp:escapeJSON(c.label)}",
      <%-- "description":"${shp:escapeJSON(c.description)}", --%>
      "parent":"${shp:escapeJSON(c.parent)}",
      "path":"${shp:escapeJSON(c.path)}",
      "otype":"${shp:escapeJSON(c.otype)}",
      "order":${c.order}, 
      "allowUserContentCreation":"${c.allowUserContentCreation}",    
      "visibility":"${c.visibility}"
    }${not status.last ? ',' : ''}</c:forEach>
  ]
}