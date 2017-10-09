<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.shoppino.it/webtools" prefix="shp" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%-- <%@ taglib prefix="mtsec" uri="http://www.dressique.com/security/tags" %> --%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="shoppino.domain.properties.PropertyKeyConstants" %> 
<fmt:setBundle basename="application-${env}" var="application" scope="application"/>
<fmt:setBundle basename="application-social" var="applicationSocial" scope="application"/>
<fmt:setBundle basename="application-commons" var="applicationCommons" scope="application"/>