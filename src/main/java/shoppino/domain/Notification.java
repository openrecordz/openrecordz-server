package shoppino.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.data.mongodb.core.index.Indexed;

public class Notification extends TenantableEntity implements Cloneable,Baseable, Typeable,Tenantable {

	
	
	private static final long serialVersionUID = 8910957993282731503L;
	
	@Deprecated
	public static String LIKE_MESSAGE = "like_msg";
	@Deprecated
	public static String COMMENT_MESSAGE = "comment_msg";
	
	
//	
	
	public static String JSON_TYPE = "json";
	
	public static String URI_TYPE = "uri";
	
	public static String OPERATION_TYPE = "operation";

	private String message;
	
	@Indexed
	private String from;
	
	@Indexed
	private String to;
	
	@Indexed
	private String refId;		//se GENERIC_WEB_URL_TYPE contiene url
	
	
	/**
	 * operation : operation performed by the user
	 * “like_msg”: Notifica di un Like su un determinato prodotto con apertura del dettaglio del prodotto.
	 * 	alert”: Notifica ALERT con apertura di una popup.
	 * “uri”: Notifica di tipo URI (apertura di una webview al link indicato oppure apertura del dettaglio del prodotto indicato
	 * "chat" : 
	 */
	
	private String type;
	
	private boolean read;
	
	private Date createdOn;
	
	private boolean historyVisible;

	
	public Notification() {
		this(null,null, null, null);
	}
	
	public Notification(String from, String to, String refId, String message) {
		this.message = message;
		this.from = from;
		this.to = to;
		this.refId = refId;
		this.read = false;
		this.createdOn = new Date();
		this.historyVisible = true;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public String getCreatedOnRFC822() {
		  SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
		  return sdf.format(this.createdOn);
	}
	
	
	
	

	public boolean isHistoryVisible() {
		return historyVisible;
	}

	public void setHistoryVisible(boolean historyVisible) {
		this.historyVisible = historyVisible;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
