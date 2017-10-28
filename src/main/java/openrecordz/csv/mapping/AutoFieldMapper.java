package openrecordz.csv.mapping;

import java.util.Map;

public interface AutoFieldMapper {

	public void transform(Map<String, Integer> headersMao);
	
	public boolean canMap(Map<String, Integer> headersMao);
}
