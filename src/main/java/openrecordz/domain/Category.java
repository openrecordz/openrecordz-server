package openrecordz.domain;


import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;


public class Category extends TenantableEntity implements  Baseable, Tenantable, Viewable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6074506757478596159L;
	
	public static String PHOTO_OTYPE = "photo";
	public static String DEAL_OTYPE = "deal";
	public static String EVENT_OTYPE = "event";
	public static String MENU_RESTAURANT_OTYPE = "menu_restaurant";
	public static String COVER_OTYPE = "cover";
	
	private String name;
	@Transient
	private String label;
	@Indexed
	private String parent;
	@Indexed
	private String path;
	
	@Indexed
	private Integer order;
	
	private String otype;
	
	private boolean allowUserContentCreation;
	
	private Map<String, String> labels;
	
	 
	private Integer visibility;
	
//	@Indexed 
//	private List<String> tenants;
	
	public Category(String path) {
		super();
		
//		Slugify slg = new Slugify();
//		slg.
		this.setType(this.getClass().getSimpleName());
		this.setParent(path.substring(0, path.lastIndexOf("/")));
		this.setPath(path);
		this.setId(path);
		this.setName(path.replaceAll("/", "-").substring(1));
		this.setOrder(0);
		this.otype = PHOTO_OTYPE;
		this.allowUserContentCreation = true;
		this.labels = new HashMap<String, String>();
		this.visibility = VISIBILITY_ALL;
	}
	
	@PersistenceConstructor
	public Category(String name, String parent, String path) {
		super();
		this.setType(this.getClass().getSimpleName());
		this.setParent(parent);
		this.setPath(path);
		this.setId(path);
		this.setName(name);
		this.setOrder(0);
		this.otype = PHOTO_OTYPE;
		this.allowUserContentCreation = true;
		this.labels = new HashMap<String, String>();
		this.visibility = VISIBILITY_ALL;
//		this.tenants = new ArrayList<String>();
//		String l = CategoryLabelUtil.getProperty(name);
//		this.setLabel(l);
//		this.setLabel(label);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
//		String l = CategoryLabelUtil.getProperty(name);
//		this.setLabel(l);
	}
	
	
	

	public String getLabel() {
//		return label;
		if (this.labels.get("en_US")!=null)
			return this.labels.get("en_US");
		else
			return this.name;
	}

//	public void setLabel(String label) {
//		this.label = label;
//	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	
//	public List<String> getTenants() {
//		return tenants;
//	}
//
//	public void setTenants(List<String> tenants) {
//		this.tenants = tenants;
//	}
//	
//	public void addTenant(String name) {
//		if (name!=null && !tenants.contains(name)) {			
//			this.tenants.add(name);
//		}
//	}
	
	
	
	public Integer getOrder() {
		return order;
	}

	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
	
	public boolean isAllowUserContentCreation() {
		return allowUserContentCreation;
	}

	public void setAllowUserContentCreation(boolean allowUserContentCreation) {
		this.allowUserContentCreation = allowUserContentCreation;
	}
	
	

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	@Override
	public String toString() {
	    return "Category [id=" + getId() 
	    		+ ", name=" + name 
	    		+ ", label=" + label
	    		+ ", order=" + order
	    		+ ", parent= " + parent 
	    		+ ", path= " +path+"]";
	}

	@Override
	public Integer getVisibility() {
		return visibility;
	}
	
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	
}
