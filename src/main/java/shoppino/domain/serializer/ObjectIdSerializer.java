package shoppino.domain.serializer;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;



public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

    @Override
    public void serialize(ObjectId value, JsonGenerator jgen,
        SerializerProvider provider) throws IOException,JsonProcessingException {
//        jgen.writeStartObject();
//        jgen.writeObject(value);
////        jgen.writeNumberField("y", value.getValue());
//        jgen.writeEndObject();
    	 jgen.writeString(value.toString());
    }

    @Override
    public Class<ObjectId> handledType() {
        return ObjectId.class;
    }
    
}