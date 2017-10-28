package openrecordz.csv.mapping;

import java.util.Map;

public class LocationFieldMapper implements AutoFieldMapper {

	@Override
	public void transform(Map<String, Integer> headersMap) {
		
	}

	@Override
	public boolean canMap(Map<String, Integer> headersMap) {
		if (headersMap!=null
				&& ( 
						headersMap.containsKey("lat")
						|| headersMap.containsKey("latitude")
						|| headersMap.containsKey("_lat")
					)
				&& (
						headersMap.containsKey("lon")
						|| headersMap.containsKey("longitude")
						|| headersMap.containsKey("_lon")
					)
			) {
			return true;
		} else return false;
	}
	

}
