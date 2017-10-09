package shoppino.service.impl;

import java.util.HashMap;
import java.util.Map;

import openrecordz.mail.service.MailServiceImpl;


/**
 * this class acts as a container to our thread local variables.
 * @author andrea leo
 *
 */

public class ConfigThreadLocal {

		public static ThreadLocal<Map<String,Object>> indexerEnabledThreadLocal = new ThreadLocal<Map<String,Object>>() {
		protected synchronized Map<String,Object> initialValue() {
			Map<String,Object> configs = new HashMap<String,Object>();
//			configs.put(CustomDataSearchServiceImpl.CONFIG_THREADLOCAL_INDEXER_ENABLED_KEY, true);
			configs.put(ScriptServiceMultiEngineImpl.CONFIG_THREADLOCAL_SCRIPTING_ENABLED_KEY, true);
			configs.put(MailServiceImpl.CONFIG_THREADLOCAL_MAIL_ENABLED_KEY, true);
			
			
            return configs;
        }
		};
		
		public static void set(Map<String,Object> configs) {
			indexerEnabledThreadLocal.set(configs);
		}

		public static void unset() {
			indexerEnabledThreadLocal.remove();
		}

		public static Map<String,Object> get() {
			return indexerEnabledThreadLocal.get();
		}
		
		public static void init() {
//			get().put(CustomDataSearchServiceImpl.CONFIG_THREADLOCAL_INDEXER_ENABLED_KEY, true);
			get().put(ScriptServiceMultiEngineImpl.CONFIG_THREADLOCAL_SCRIPTING_ENABLED_KEY, true);
			get().put(MailServiceImpl.CONFIG_THREADLOCAL_MAIL_ENABLED_KEY, true);
		}

}


