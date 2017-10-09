/*
 * Created on 5-lug-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package microdev.db;

import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.linksmt.util.SearchForm;

/**
 * @author andrea
 */
public class QueryTips {
	
	private Log log = LogFactory.getLog(QueryTips.class);
	// direction constants
	public static final int ASC = 0;
	public static final int DESC = 1;
	public static final int NO_DIRECTION = 2;
	// properties
	//private String orderField;
	//private int orderDirection;
	private Vector orderFields = new Vector();
	private int start;
	private int rowsNumber;
	// if bound is true you must consider valid 
	// either "start" and "rowsNumber"
	// otherwise if bound = false ignor them
	private boolean paging;
	
	public QueryTips() {
		//orderDirection = NO_DIRECTION;
		paging = false;
		start = 0;
		rowsNumber = 0;
	}
	
//	public QueryTips(SearchForm form) {
//		int rowsPerPage = 10;
//		if ( form.getRowsParam().equals("") ) {
//			// recupera dal bundle
//			try {
//				ResourceBundle settings = ResourceBundle.getBundle("application");
//				rowsPerPage = Integer.parseInt(settings.getString("list.rowsPerPage"));
//				if (rowsPerPage <= 0 ) log.error("rowsPerPage NON pu� essere <= 0!");
//			} catch (Exception e) {
//				log.error("manca il bundle application.properties!");
//				//log.error("", e);
//				//throw e;
//			}
//		} else {
//			// recupera da query string
//			rowsPerPage = Integer.parseInt( form.getRowsParam() );
//		}
//		
//		// ordering
//		String orderField = form.getOrderFieldParam();
//		String orderDirection = form.getOrderDirectionParam();
//		if (!orderField.equals("")) {
//			orderFields = new Vector();
//			Field field = new Field();
//			orderFields.add(field);
//			field.name = orderField;
//			// imposta la direzione
//			if (orderDirection.equals("desc")) {
//				field.direction = QueryTips.DESC;
//			}
//			else {
//				field.direction = QueryTips.ASC;
//			}
//		}
//		
//		// limiting
//		String _start = form.getStartParam();
//		int start = 0;
//		if (!_start.equals("")) {
//			start = Integer.parseInt(_start);
//		}
//		
//		paging = form.isPaging();
//		if ( paging ) setLimit(start,rowsPerPage);
//		//try {
//		//if ( bound = true ) setLimit(start,rowsPerPage);
//		//} catch (Exception e) {
//		//	throw e;
//		//}
//	}
	
	/**La direzione del primo campo per l'ordinamento
	 * @return la direzione
	 */
	public int getOrderDirection() {
		if ( orderFields.size() > 0 )
			return ( (Field) orderFields.get(0) ).direction;
		else return NO_DIRECTION;
	}
	
	public void setOrderDirection(int orderDirection) {
		if ( orderFields.size() > 0 ) {
			( (Field) orderFields.get(0) ).direction = orderDirection;
		} else {
			Field field = new Field();
			field.direction = orderDirection;
			orderFields.add(field);
		}
		//this.orderDirection = orderDirection;
	}
	
	/**Il nome del primo campo per l'ordinamento
	 * @return il nome
	 */
	public String getOrderField() {
		if ( orderFields != null)
			return ( (Field) orderFields.get(0) ).name;
		else return null;
	}
	
	public void setOrderField(String orderField) {
		if ( orderFields.size() > 0 ) {
			( (Field) orderFields.get(0) ).name = orderField;
			log.debug(" aggiorno vettore esistente con: " + orderField);
			log.debug(" orderFields[0]: " + orderFields.get(0));
		} else {
			Field field = new Field();
			field.name = orderField;
			orderFields.add(field);
			log.debug(" creo nuovo Field con: " + orderField);
			log.debug(" orderFields[0]: " + orderFields.get(0));
		}
		//this.orderField = orderField;
	}
	
	public void addOrderField(String name, int order) {
		orderFields.add(new Field(name, order));
	}
	
	public void resetOrderFields() {
		orderFields = new Vector();
	}
	
	/**
	 * @param paging The paging to set.
	 */
	public void setPaging(boolean paging) {
		this.paging = paging;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getRowsNumber() {
		return rowsNumber;
	}
	
	public void setLimit(int start, int rowsNumber) { //throws Exception {
		this.start = start;
		this.rowsNumber = rowsNumber;
	}
	
	public String getQueryString() {
		return getOrderQuery() + getLimitQuery();
	}
	
	public String getLimitQuery() {
		String query = "";
		if ( paging ) {
			// query += " limit "+start+","+rowsNumber; // mysql
			query += " LIMIT "+rowsNumber+" OFFSET "+start; // postgres
		}
		return query;
	}
	
	public String getOrderQuery() {
		String query = "";
		Iterator iter = orderFields.iterator();
		if ( iter.hasNext() ) query += " order by ";
		while ( iter.hasNext() ) {
			Field field = (Field) iter.next();
			query += field.name + getDirAsString(field.direction);
			if ( iter.hasNext() ) query += ",";
		}
		return query;
	}
	
	private String getDirAsString(int dir) {
		if ( dir == ASC ) return " asc";
		else if ( dir == DESC ) return " desc";
		else return "";
	}
	
	class Field {
		public String name = null;
		public int direction;
		
		public Field() {
		}
		
		public Field(String name, int direction) {
			this.name = name;
			this.direction = direction;
		}
		
		public String toString() {
			return "name: " + name + ", direction = " + getDirAsString(direction);
		}
	}
	
}
