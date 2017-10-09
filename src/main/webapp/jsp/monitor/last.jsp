<%@ include file="/jsp/include.jsp" %>
<h1>Ultimi prodotti inseriti</h1>
<table>
	<tr>
		<td class="monitor">Image</td>
		<td class="monitor">Description</td>
		<td class="monitor">User</td>
	</tr>
	<c:forEach items="${model.lastProducts}" var="product">
			<tr>
				<td class="monitor">
					<img width="100px" height="100px" src="<fmt:message key="imagerepo.url" bundle="${application}"/><fmt:message key="imagerepo.search.url" bundle="${application}"/>${product.image}&w=640&h=640"/>
				</td>
				<td class="monitor"> 
					<a href="${pageContext.request.contextPath}/products/${product.id}"><c:out value="${product.description}"/></a>
				</td>				
				<td class="monitor">
					<c:out value="${product.createdBy}"/>
				</td>
			</tr>
	</c:forEach>
</table>

<h1>Ultimi utenti</h1>
<table>
	<tr>
		<td class="monitor">Photo</td>
		<td class="monitor">Username</td>
	</tr>
	<c:forEach items="${model.lastPeople}" var="person">
			<tr>
				<td class="monitor">
					<img style="background-image: url('<fmt:message key="imagerepo.url" bundle="${application}"/><fmt:message key="imagerepo.search.url" bundle="${application}"/>${person.photo}&w=200&h=200'); width: 100px; margin-top: 0px;background-position: 50% 50%;background-size: cover;')"	
										src="<c:url value="/resources/images/common/blank.gif"/>"/>										
				</td>
				<td class="monitor"> 
					<a href="${pageContext.request.contextPath}/users/${person.username}"><c:out value="${person.username}"/></a>
				</td>						
			</tr>
	</c:forEach>
</table>

<h1>Ultimi commenti</h1>
<table>
	<tr>
		<td class="monitor">id</td>
		<td class="monitor">discussionId</td>
		<td class="monitor">discussionClass</td>
		<td class="monitor">createdBy</td>
		<td class="monitor">createdOn</td>		
	</tr>
	<c:forEach items="${model.lastComments}" var="comment">
			<tr>				
				<td class="monitor"> 
					<c:out value="${comment.id}"/>					
				</td>		
				<td class="monitor"> 
					<a href="${pageContext.request.contextPath}/products/${comment.discussionId}"><c:out value="${comment.discussionId}"/></a>
				</td>		
				<td class="monitor"> 
					<c:out value="${comment.discussionClass}"/>
				</td>		
				<td class="monitor"> 
					<c:out value="${comment.createdBy}"/>
				</td>
				<td class="monitor"> 
					<c:out value="${comment.createdOn}"/>
				</td>
			</tr>
	</c:forEach>
</table>

<h1>Ultimi abusi</h1>
<table>
	<tr>
		<td class="monitor">objectId</td>
		<td class="monitor">objectType</td>
		<td class="monitor">reporterUsername</td>
		<td class="monitor">text</td>
		<td class="monitor">abuseType</td>
		<td class="monitor">createdOn</td>
	</tr>
	<c:forEach items="${model.lastAbuses}" var="abuse">
			<tr>				
				<td class="monitor"> 
					<a href="${pageContext.request.contextPath}/products/${abuse.objectId}"><c:out value="${abuse.objectId}"/></a>
				</td>		
				<td class="monitor"> 
					<c:out value="${abuse.objectType}"/>
				</td>		
				<td class="monitor"> 
					<c:out value="${abuse.reporterUsername}"/>
				</td>		
				<td class="monitor"> 
					<c:out value="${abuse.text}"/>
				</td>
				<td class="monitor"> 
					<c:out value="${abuse.abuseType}"/>
				</td>
				<td class="monitor"> 
					<c:out value="${abuse.createdOn}"/>
				</td>
			</tr>
	</c:forEach>
</table>
