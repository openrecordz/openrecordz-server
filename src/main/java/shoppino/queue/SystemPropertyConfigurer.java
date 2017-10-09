package shoppino.queue;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;

public class SystemPropertyConfigurer {

 Properties systemProperties;

 @PostConstruct
 public void init() throws NamingException {

  if (systemProperties == null || systemProperties.isEmpty()) {
   return;
  }

  Iterator<Map.Entry<Object, Object>> it = systemProperties.entrySet().iterator();

  while (it.hasNext()) {

   Map.Entry<Object, Object> systemProperty = it.next();
   String key = (String) systemProperty.getKey();
   String value = (String) systemProperty.getValue();

   System.setProperty(key, value);
  }

 }

 public void setSystemProperties(Properties systemProperties) {
  this.systemProperties = systemProperties;
 }

}