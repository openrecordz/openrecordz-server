package openrecordz.web.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

public class LocaleChangeInterceptorWithoutException extends
		LocaleChangeInterceptor {

	private Log logger = LogFactory.getLog(this.getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {

		try {
            super.preHandle(request, response, handler);
        } catch (ServletException e) {
        	logger.error("ServletException", e);
        } catch (IllegalArgumentException e) {
        	logger.warn("IllegalArgumentException", e);
        }
        return true;
	}
}
