<%@ include file="/jsp/include.jsp" %>

<div id="content">
<ul class="notify-list">
 <c:choose><c:when test="${ model.notifications!=null && model.notifications.size()>0 }">
	<c:forEach items="${model.notifications}" var="n">
			 <li class="notification-item">                
                    <a href="${pageContext.request.contextPath}/users/${n.from}"><img class="avartar" src="${pageContext.request.contextPath}/users/${n.from}/photo?h=60&w=60"></a>
                    <p class="right"><span class="title"><a class="user" href="${pageContext.request.contextPath}/users/${n.from}">${n.from}</a> <s:message code="notifications.type.like"/>
                        
                        <a href="${pageContext.request.contextPath}/products/${products.get(n.refId).id}/${shp:generateURL(products.get(n.refId))}"><c:out value="${products.get(n.refId).description}"/></a>
                        <%-- <a href="${pageContext.request.contextPath}/products/${products.get(n.refId).id}/${shp:safelyEncodeURL(products.get(n.refId).description)}">${products.get(n.refId).description}</a>. --%>
                        

                        </span>                           		
										   
                        <span class="activity-reply">
                        <time class="commentCreatedOn" datetime="${shp:date2string(n.createdOn,"YYYY-MM-dd'T'HH:mm:ss")}" pubdate>
							${shp:date2stringbystyle(n.createdOn, "0", "3", pageContext.request.locale)}	
						</time>
                        <!-- <a otype="thing" oid="190403263978277559" uname="chrissiburrell" class="btn-reply" href="#" style="display: none;"><i class="ic-reply"></i>Rispondi</a> -->
                        </span>
                        </p>
                </li>
    </c:forEach>     
              
    </c:when><c:otherwise><center><div style="padding-top:60px"><s:message code="nonotifications" /></div></center></c:otherwise></c:choose>
				          
              
               <%--  <li class="notification-item">                
                    <a href="/chrissiburrell"><img class="avartar" src="http://thefancy-media-ec2.thefancy.com/DefaultImages/avatars/pink_60.png"></a>
                    <p class="right"><span class="title"><a class="user" href="/chrissiburrell">chrissiburrell</a> ha commentato
                        
                        <a href="/things/190403263978277559/Fancy-Box-Subscription">Fancy Box Subscription</a>.

                        </span>                      
                        <span class="activity-reply">1 giorno, 15 ore fa
                        <a otype="thing" oid="190403263978277559" uname="chrissiburrell" class="btn-reply" href="#" style="display: none;"><i class="ic-reply"></i>Rispondi</a></span></p>
                </li>
				
                <li class="notification-item">
                    <a href="/things/190403263978277559/Fancy-Box-Subscription"><img class="u" style="background-image:url('http://thefancy-media-ec2.thefancy.com/90/20120919/190403263978277559_05b917480b72.jpg')" src="//static-ec3.thefancy.com/_static_gen/_ui/images/common/blank.325472601571.gif"></a>
                    <a href="/MissWhy"><img class="avartar" src="http://thefancy-media-ec6.thefancy.com/UserImages/MissWhy_6daa8e9ba8a0.jpg"></a>
                    <p class="right"><span class="title"><a class="user" href="/MissWhy">MissWhy</a> ha commentato
                        
                        <a href="/things/190403263978277559/Fancy-Box-Subscription">Fancy Box Subscription</a>.
                        
                        
                        </span>
                        <span class="activity-reply">3 giorni, 13 ore fa
                        <a otype="thing" oid="190403263978277559" uname="MissWhy" class="btn-reply" href="#" style="display: none;"><i class="ic-reply"></i>Rispondi</a></span></p>
                </li> --%>
				
				
			
</ul>

</div>