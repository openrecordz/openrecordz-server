package shoppino.persistence.mongo.converter;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.DBObject;

import openrecordz.domain.customdata.CustomDataImpl;

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
	  source.put("_class","openrecordz.domain.customdata.CustomDataImpl");
	  return  source;
  }
}