package shoppino.web.exception.resolver;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import openrecordz.exception.ResourceNotFoundException;

public class LoggingExceptionResolver extends SimpleMappingExceptionResolver {

	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	protected void logException(Exception ex, HttpServletRequest request) {
		if (ex instanceof ResourceNotFoundException) {
			this.logger.info(buildLogMessage(ex, request), ex);
		}else {
			this.logger.warn(buildLogMessage(ex, request), ex);
		}
	}

}
