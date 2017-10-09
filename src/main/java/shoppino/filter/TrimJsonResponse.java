package shoppino.filter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class TrimJsonResponse implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		PrintWriter out = response.getWriter();
		CharResponseWrapper wrapper = new CharResponseWrapper(
		    (HttpServletResponse)response);
		chain.doFilter(request, wrapper);
		
		CharArrayWriter caw = new CharArrayWriter();
		caw.write(wrapper.toString().replaceAll("\\n", "").trim());
		
		response.setContentLength(caw.toString().getBytes().length);
		out.write(caw.toString());
		out.close();

		
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	public class CharResponseWrapper extends
    HttpServletResponseWrapper {
    private CharArrayWriter output;
    public String toString() {
        return output.toString();
    }
    public CharResponseWrapper(HttpServletResponse response){
        super(response);
        output = new CharArrayWriter();
    }
    public PrintWriter getWriter(){
        return new PrintWriter(output);
    }
}

}
