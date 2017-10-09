<%@ include file="/jsp/include.jsp" %>

<script type="text/javascript">
$(document).ready(function() {
	$("#more-info-error-btn").live("click",function(event) {
		event.preventDefault();
		$("#more-info-error").show();
	});
		
});

</script>
<div class="error">
	<div class="error-notify">
	   <!--  <div class="img"><img alt="" src="http://cf2.fancyimgs.com/images/common/error-404.png"></div> -->
		<h2>Hoops!</h2>
		<p><s:message code="errorpage.404"/>
		<small><a href="javascript:history.go(-1)">Go back</a> or <a href="mailto:info@dressique.com">let us know</a> if this problem persists. </small></p>
		
		<%-- <a href="#" id="more-info-error-btn">More Info</a>
		
		<div id="more-info-error">	
			<h2>${exception}</h2>
			<pre name="code" class="core">
			<%@ page isErrorPage="true" import="java.io.*"%>
			
				<!-- <div id="stacktrace" style="display:none;"> -->
				<div id="stacktrace">
					<pre>
					<%
					// if there is an exception
					if (exception != null) {
					// print the stack trace hidden in the HTML source code for debug
						exception.printStackTrace(new PrintWriter(out));
					}
					%>
					</pre>
				</div>
			</pre>
		</div> --%>
	</div>
</div>