package openrecordz.web.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component("loggerInterceptor")
public class LoggerInterceptor extends HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(this.getClass());
	private Date timeStart;
	private Date timeEnd;
	
	static{
		BasicConfigurator.configure();
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.debug("Before handling the request for URI " + request.getRequestURI());
		timeStart = new Date();
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		timeEnd = new Date();
		long timemillis = timeEnd.getTime() - timeStart.getTime();
		logger.info("After handling the request for URI " + request.getRequestURI() + " in " + timemillis + " ms");
		
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.debug("After rendering the view");
		super.afterCompletion(request, response, handler, ex);
	}
}
