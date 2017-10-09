package openrecordz.domain.customdata;

import com.mongodb.BasicDBObject;

public class Record extends CustomDataImpl implements CustomData, Versionable{

	public static final String DATASET_REF_ID_PROP_KEY = "_dataset_ref_id";
	public static final String VERSIONED_FROM_ID = "_versioned_from_id";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Record() {
		super(null,null);
	}
	
	public Record(BasicDBObject basicDBObject) {
		super(basicDBObject);	
	}
	
	public Record(String type, String json) {
		super(type,json);
		
	}

	
	
	public String getDatasetRefId() {
		return this.get(DATASET_REF_ID_PROP_KEY).toString();
	}

	public void setDatasetRefId(String datasetRefId) {
		this.put(DATASET_REF_ID_PROP_KEY, datasetRefId);
	}

	@Override
	public String versionedFromId() {
		if (this.containsKey(VERSIONED_FROM_ID)){
			return this.get(VERSIONED_FROM_ID).toString();
		}else 
			return null;
	}

	
	
}
