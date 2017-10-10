package openrecordz.service.impl;

import java.util.HashMap;
import java.util.Map;


/**
 * this class acts as a container to our thread local variables.
 * @author andrea leo
 *
 */

public class RequestThreadLocal {

		public static String REQUEST_INFO_TREADEDLOCAL_USER_AGENT = "user-agent";
		
		public static ThreadLocal<Map<String,Object>> requestInfoThreadLocal = new ThreadLocal<Map<String,Object>>() {
		protected synchronized Map<String,Object> initialValue() {
			Map<String,Object> requestInfo = new HashMap<String,Object>();
			requestInfo.put(REQUEST_INFO_TREADEDLOCAL_USER_AGENT, null);			
			
            return requestInfo;
        }
		};
		
		public static void set(Map<String,Object> requestInfo) {
			requestInfoThreadLocal.set(requestInfo);
		}

		public static void unset() {
			requestInfoThreadLocal.remove();
		}

		public static Map<String,Object> get() {
			return requestInfoThreadLocal.get();
		}
		
		public static void init() {
			get().put(REQUEST_INFO_TREADEDLOCAL_USER_AGENT, null);		
			
		}

}


