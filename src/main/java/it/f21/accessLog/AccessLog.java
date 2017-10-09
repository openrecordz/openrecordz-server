/*
 * Created on 7-apr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.f21.accessLog;

import java.util.Date;

/**
 * @author andrea
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccessLog {
	/** oid */
	private Long id = null;
	
	/** ex. druido.unile.it */
	private String remote_host = "";
	/** ex. 87.7.33.250 */
	private String remote_addr = "";
	
	/** /erp-dev/fleets/track_veicoli/startTrack.do */
	private String request_uri = "";
	
	/** ex. id=37&start=false&... */
	private String query_string = "";
	
	/** ex. http://www.realtrack.it:8080/erp-dev/fleets/track_veicoli/startTrack.do?id=37 */
	private String request_url = "";
	
	/** ex. antonio@tundo */
	private String username = "";
	
//	private long user_id = -1;
//	private long organization_id = -1;
	
	/** la data */
	private Date date = null;
	
	private String user_agent = "";
	
	private String user_lang = "";
	
	private String tenant;
	
	
	public AccessLog() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQuery_string() {
		return query_string;
	}
	public void setQuery_string(String query_string) {
		this.query_string = query_string;
	}
	public String getRemote_addr() {
		return remote_addr;
	}
	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}
	public String getRemote_host() {
		return remote_host;
	}
	public void setRemote_host(String remote_host) {
		this.remote_host = remote_host;
	}
	public String getRequest_uri() {
		return request_uri;
	}
	public void setRequest_uri(String request_uri) {
		this.request_uri = request_uri;
	}
	public String getRequest_url() {
		return request_url;
	}
	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}
//	public long getOrganization_id() {
//		return organization_id;
//	}
//
//	public void setOrganization_id(long organization_id) {
//		this.organization_id = organization_id;
//	}
//
//	public long getUser_id() {
//		return user_id;
//	}
//	public void setUser_id(long user_id) {
//		this.user_id = user_id;
//	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	public String getUser_lang() {
		return user_lang;
	}

	public void setUser_lang(String user_lang) {
		this.user_lang = user_lang;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	
}
