package openrecordz.persistence.mongo.converter;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.BasicDBObject;

import openrecordz.domain.customdata.CustomDataImpl;

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