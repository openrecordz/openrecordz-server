package openrecordz.csv.mapping;

import java.util.List;
import java.util.Map;

public class AutoFieldsMappingService {

	List<AutoFieldMapper> autofieldsMapperFactory;
	
	public void autoMapping(Map<String, Integer> headersMap) {
		for (AutoFieldMapper autoFieldMapper : autofieldsMapperFactory) {
			if (autoFieldMapper.canMap(headersMap)) {
				autoFieldMapper.transform(headersMap);
			}
		}
	}
	
}
