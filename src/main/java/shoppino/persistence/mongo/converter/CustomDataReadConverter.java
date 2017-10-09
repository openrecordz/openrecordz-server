package shoppino.persistence.mongo.converter;

import org.springframework.core.convert.converter.Converter;

import shoppino.domain.customdata.CustomDataImpl;

import com.mongodb.BasicDBObject;

public class CustomDataReadConverter implements Converter<BasicDBObject, CustomDataImpl> {

  public CustomDataImpl convert(BasicDBObject source) {
//	  if (source instanceof BasicDBObject)
		  CustomDataImpl cdi =  new CustomDataImpl((BasicDBObject)source);
//		  cdi.put("id", cdi.getObjectId("_id").toString());
//		  cdi.setId(cdi.getObjectId("_id").toString());
		  return cdi;
			
//	  else
//		  return null;
	  
  }
}