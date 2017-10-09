<%@ include file="/jsp/include.jsp" %>
<!-- <div id="content"> -->
<table>
	<tr>
		<td class="auditing">Remote Host</td>
		<td class="auditing">Remote Addr</td>
		<td class="auditing">Request Uri</td>
		<td class="auditing">Query String</td>
		<td class="auditing">Request url</td>
		<td class="auditing">Date</td>
		<td class="auditing">User Agent</td>
		<td class="auditing">User Lang</td>
	</tr>
<c:forEach items="${model.audits}" var="a">
	<tr>
		<td class="auditing"><span title="<c:out value="${a.remote_host}"/>">${shp:truncate(a.remote_host,100)}</span></td>
		<td class="auditing"><span title="<c:out value="${a.remote_addr}"/>">${shp:truncate(a.remote_addr,100)}</span></td>
		<td class="auditing"><span title="<c:out value="${a.request_uri}"/>">${shp:truncate(a.request_uri,100)}</span></td>
		<td class="auditing"><span title="<c:out value="${a.query_string}"/>">${shp:truncate(a.query_string,40)}</span></td>
		<td class="auditing"><span title="<c:out value="${a.request_url}"/>">${shp:truncate(a.request_url,100)}</span></td>
		<td class="auditing"><span title="<c:out value="${a.date}"/>"></span>${shp:truncate(a.date,100)}</td>
		<td class="auditing"><span title="<c:out value="${a.user_agent}"/>">${shp:truncate(a.user_agent,100)}</span></td>
		<td class="auditing"><span title="<c:out value="${a.user_lang}"/>">${shp:truncate(a.user_lang,100)}</span></td>	
	</tr>
</c:forEach>
</table>

<!-- </div> -->