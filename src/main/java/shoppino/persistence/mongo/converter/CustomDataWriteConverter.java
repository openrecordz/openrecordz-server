package shoppino.persistence.mongo.converter;

import org.springframework.core.convert.converter.Converter;

import shoppino.domain.customdata.CustomDataImpl;

import com.mongodb.DBObject;

public class CustomDataWriteConverter implements Converter<CustomDataImpl, DBObject> {

//	@Autowired
//	MappingMongoConverter mappingMongoConverter;
	
  public DBObject convert(CustomDataImpl source) {
//	  GenericConverter g = new GenericCo
//    DBObject dbo = new BasicDBObject();
//    dbo.put("_id", source.getId());
//    dbo.put("name", source.getFirstName());
//    dbo.put("age", source.getAge());
//    return dbo;
	  source.removeField("id");
//	  mappingMongoConverter.get
	  source.put("_class","shoppino.domain.customdata.CustomDataImpl");
	  return  source;
  }
}