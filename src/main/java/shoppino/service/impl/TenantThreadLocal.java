package shoppino.service.impl;

import openrecordz.domain.Tenant;
import shoppino.service.TenantService;

/**
 * this class acts as a container to our thread local variables.
 * @author andrea leo
 *
 */

public class TenantThreadLocal {

		public static ThreadLocal<Tenant> tenantThreadLocal = new ThreadLocal<Tenant>();
		protected synchronized Object initialValue() {
            return TenantService.DEFAULT_TENANT;
        }

		public static void set(Tenant tenant) {
			tenantThreadLocal.set(tenant);
		}

		public static void unset() {
			tenantThreadLocal.remove();
		}

		public static Tenant get() {
			return tenantThreadLocal.get();
		}
}

