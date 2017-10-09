package microdev.util;

public class SystemInfo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    String locale = System.getProperty("user.language");
	    System.out.println("user.language: " + locale);
	    System.out.println("file.encoding: " + System.getProperty("file.encoding"));
	    System.out.println("file.encoding: " + System.getProperty("charset"));
//	    // jdk1.4
//	    System.out.println(
//	    		new java.io.OutputStreamWriter(
//	    				new java.io.ByteArrayOutputStream()).getEncoding()
//	    );
	    // jdk1.5
	    System.out.println("java.nio.charset.Charset.defaultCharset().name(): " + java.nio.charset.Charset.defaultCharset().name());
	}
}
