package openrecordz.domain.customdata.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import openrecordz.domain.customdata.CustomData;



public class CustomDataJsonSerializer extends JsonSerializer<CustomData> {

    @Override
    public void serialize(CustomData value, JsonGenerator jgen,
        SerializerProvider provider) throws IOException,JsonProcessingException {
    	
//    	value.removeField("_id");
//		value.put("id", value.getId());
////		jgen.writeStartObject();
//		jgen.writeObjectField("test", value);
//    	jgen.writeObject(value);
//    	jgen.writeEndObject();
//    	String id =  value.getId();
//    	value.setId("");
//    	value.removeField("_id");
//        jgen.writeStartObject();
////        jgen.writeObjectField("id", id);
//        jgen.write(value);
//////        jgen.writeString(value.toString());
//////        jgen.writeObjectField("id", value.getId());
////////        jgen.writeNumberField("y", value.getValue());
//////        jgen.writeRaw(value);
//        jgen.writeEndObject();
       
//        System.out.println("CustomDataJsonSerializer");
        
    }

    @Override
    public Class<CustomData> handledType() {
        return CustomData.class;
    }
}