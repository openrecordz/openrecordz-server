package microdev.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GlobbiUtil {
	static final Log log = LogFactory.getLog(GlobbiUtil.class);
	
	/**
	 * da qui: http://www.javapractices.com/Topic42.cjp
	 */
	static public String loadTextFile(File aFile) {
	    StringBuffer contents = null;

	    //declared here only to make visible to finally clause
	    BufferedReader input = null;
	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      input = new BufferedReader( new FileReader(aFile) );
	      String line = null; //not declared within while loop
	      contents = new StringBuffer();
	      /*
	      * readLine is a bit quirky :
	      * it returns the content of a line MINUS the newline.
	      * it returns null only for the END of the stream.
	      * it returns an empty String if two newlines appear in a row.
	      */
	      while (( line = input.readLine()) != null){
	        contents.append(line);
	        contents.append(System.getProperty("line.separator"));
	      }
	    }
	    catch (FileNotFoundException ex) {
	      ex.printStackTrace();
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
	    finally {
	      try {
	        if (input!= null) {
	          //flush and close both "input" and its underlying FileReader
	          input.close();
	        }
	      }
	      catch (IOException ex) {
	        ex.printStackTrace();
	      }
	    }
	    return contents.toString();
	  }
	
	/**
	 * da qui: http://www.javapractices.com/Topic42.cjp
	 */
	static public void saveTextFile(File aFile, String aContents) throws IOException {
		if (aFile == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
//		if (!aFile.exists()) {
//			throw new FileNotFoundException ("File does not exist: " + aFile);
//		}
//		if (!aFile.isFile()) {
//			throw new IllegalArgumentException("Should not be a directory: " + aFile);
//		}
//		if (!aFile.canWrite()) {
//			throw new IllegalArgumentException("File cannot be written: " + aFile);
//		}

		// declared here only to make visible to finally clause; generic reference
		Writer output = null;
		try {
			// use buffering
			// FileWriter always assumes default encoding is OK!
			output = new BufferedWriter( new FileWriter(aFile) );
			output.write( aContents );
		}
		finally {
			// flush and close both "output" and its underlying FileWriter
			if (output != null) output.close();
		}
	}
	
	/**
	 * da qui: http://www.javapractices.com/Topic42.cjp
	 */
	static public void saveTextStream(File aFile, InputStream stream) throws IOException {
		if (aFile == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		// declared here only to make visible to finally clause; generic reference
		Writer output = null;
		BufferedReader input = null;
	    try {
	    	//use buffering, reading one line at a time
	    	input = new BufferedReader( new InputStreamReader(stream) );
			output = new BufferedWriter( new FileWriter(aFile) );
			int i = 0;
			char[] buf = new char[8192];
			while (( i = input.read(buf, 0, 8000)) != -1 ) {
				output.write(buf, 0, i);
		      }
		}
		finally {
			// flush and close both "output" and its underlying FileWriter
			if (output != null) output.close();
		}
	}
	
	static public void printStream(InputStream in) {

		//declared here only to make visible to finally clause
		BufferedReader input = null;
		try {
			//use buffering, reading one line at a time
			//FileReader always assumes default encoding is OK!
			input = new BufferedReader( new InputStreamReader(in) );
			String line = null; //not declared within while loop
			/*
			 * readLine is a bit quirky :
			 * it returns the content of a line MINUS the newline.
			 * it returns null only for the END of the stream.
			 * it returns an empty String if two newlines appear in a row.
			 */
			while (( line = input.readLine()) != null){
				System.out.println(line);
			}
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		finally {
			try {
				if (input!= null) {
					//flush and close both "input" and its underlying FileReader
					input.close();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static String getUrlAsString(String url) {
		StringBuffer response = new StringBuffer();
		
		try {
	        URLConnection conn = new URL(url).openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                conn.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
	}
	
	public static String getUrlAsString(String url, String charset) {
		StringBuffer response = new StringBuffer();
		
		try {
	        URLConnection conn = new URL(url).openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                		conn.getInputStream(), 
	                                		charset)
	                                );
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
	}
	
//	public static String getUrlAuth(String url, String username, String password) {
//		StringBuffer response = new StringBuffer();
//		String userPassword = username + ":" + password;
//		String encoding = new sun.misc.BASE64Encoder().encode (userPassword.getBytes());
//		
//		try {
//	        URLConnection conn = new URL(url).openConnection();
//	        conn.setRequestProperty ("Authorization", "Basic " + encoding);
//	        BufferedReader in = new BufferedReader(
//	                                new InputStreamReader(
//	                                conn.getInputStream()));
//	        String inputLine;
//	        while ((inputLine = in.readLine()) != null) { 
//	            response.append(inputLine);
//	        }
//	        in.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        
//        return response.toString();
//	}
	
	
	
	public static byte[] getUrlAsBytes(String url) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		URLConnection conn = null;
		BufferedInputStream in = null;
		try {
	        conn = new URL(url).openConnection();
	        in = new BufferedInputStream(conn.getInputStream());
	        byte[] buf = new byte[2048];
	        int len = 0;
	        while ((len = in.read(buf)) != -1) { 
	            bytes.write(buf, 0, len);
	        }
	        in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {in.close();} catch (Exception ignore) {}
		}
        
		return bytes.toByteArray();
	}
	
	/** Recupera una risorsa stringa presente nel classpath */
	public static String getResourceAsString(String name) {
		// il nome va inserito come path assoluto, esempio: "/it/qbr/parser/test.txt"
		// da qui: http://www.javaworld.com/javaworld/javaqa/2003-08/01-qa-0808-property.html
		StringBuffer file = new StringBuffer("");
		InputStream in = null;
		try {
			in = System.class.getResourceAsStream(name);
			if ( in != null) {
				int c = -1;
				while ( (c = in.read()) != -1 ) {
					file.append((char) c);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {in.close();} catch(Exception ignore) {}
		}
		return file.toString();
	}
	
	
	
	
	
	
//	public static void postAuth(String _url,String username,String password,InputStream stream,String mime, String FileName,String FieldName,Map parameter) throws Exception {
//		URL url = new URL(_url);
//		// create a boundary string
//		String boundary = MultiPartFormOutputStream.createBoundary();
//		URLConnection urlConn = MultiPartFormOutputStream.createConnection(url, username, password);
//		urlConn.setRequestProperty("Accept", "*/*");
//		urlConn.setRequestProperty("Content-Type", 
//			MultiPartFormOutputStream.getContentType(boundary));
//		// set some other request headers...
//		urlConn.setRequestProperty("Connection", "Keep-Alive");
//		urlConn.setRequestProperty("Cache-Control", "no-cache");
//		// no need to connect cuz getOutputStream() does it
//		MultiPartFormOutputStream out = 
//			new MultiPartFormOutputStream(urlConn.getOutputStream(), boundary);
//		// write a text field element
//		 Iterator it = parameter.entrySet().iterator();
//		    while (it.hasNext()) {
//		        Map.Entry pairs = (Map.Entry)it.next();
//		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
//		       out.writeField(pairs.getKey().toString(), pairs.getValue().toString());
//		    }
//
////		out.writeField("title", "text field text");
////		out.writeField("entita", "Clienti");
////		out.writeField("fornitore", "Verdi");
//		
//		
//		// upload a file
//		out.writeFile(FieldName, mime,FileName, stream);
//		// can also write bytes directly
//		//out.writeFile("myFile", "text/plain", "C:\\test.txt", 
//	//		"This is some file text.".getBytes("ASCII"));
//		out.close();
//		// read response from server
//		BufferedReader in = new BufferedReader(
//			new InputStreamReader(urlConn.getInputStream()));
//		String line = "";
//		while((line = in.readLine()) != null) {
//			 System.out.println(line);
//		}
//		in.close();
//	}
	
	/**
	 * gets an absolute url from a source url and a relative one
	 * example:
	 * source: http://globbi.it/
	 * sub_link: /index.php (absolute link)
	 * result: http://globbi.it/index.php
	 * 
	 * source: http://globbi.it/subf/index.html
	 * sub_link: /list.php (absolute link)
	 * result: http://globbi.it/subf/list.php
	 * 
	 * source: http://globbi.it/subf/newsub/cities/
	 * sub_link: list.html (relative link)
	 * result: http://globbi.it/subf/newsub/cities/list.html
	 * @param page_url
	 * @param sub_link
	 * @return
	 */
	public static String getAbsoluteURL(String page_url, String sub_link) {
		String final_url = null;
		// if sub_link starts with / (absolute link) then concat root && sub_link
		if(sub_link.startsWith("/")) {
			String root = getURLRoot(page_url);
			final_url = root + sub_link;
		}
		// else (relative link) concat source with trailing / and sub_link
		else {
			String folder = getURLFolder(page_url);
			folder = (folder.endsWith("/") ? folder.substring(0, folder.lastIndexOf("/")): page_url);
			final_url = folder + "/" + sub_link;
		}
		return final_url;
	}
	
	public static void sleep(long millis) {
		log.debug("GlobbiUtil.sleep " + millis + " millis...");
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main() throws Exception {
		
		String s = GlobbiUtil.loadTextFile(new File("test/BEDttANDttBREAKFAST/bologna.html"));
		InputStream in = new ByteArrayInputStream(s.getBytes());
		GlobbiUtil.saveTextStream(new File("prova.txt"), in);
	}
//	public static void main(String args[]) {
//		try {			
//			// test getURLFolder(test1)
//			String test1 = "http://pippo.it/pluto/pippo.html?nino=bravo";
//			String test2 = "http://pippo.it/pluto.html/test";
//			String test3 = "http://pippo.it/pluto.html/";
//			String test4 = "http://pippo.it/pluto.html";
//			String test5 = "http://pippo.it/";
//			String test6 = "http://pippo.it";
//			String test7 = "http://pippo.it/pluto.html/test//";
//			System.out.println("Rel: " + getURLFolder(test1));
//			System.out.println("Rel: " + getURLFolder(test2));
//			System.out.println("Rel: " + getURLFolder(test3));
//			System.out.println("Rel: " + getURLFolder(test4));
//			System.out.println("Rel: " + getURLFolder(test5));
//			System.out.println("Rel: " + getURLFolder(test6));
//			System.out.println("Rel: " + getURLFolder(test7));
//			
//			// test getAbsoluteURL()
////			String page_url = "http://pippo.it/nino/pluto.htm";
//////			String sub_link = "/pantano";
////			String sub_link = "pantano/ii.xml";
////			String abso = getAbsoluteURL(page_url, sub_link);
////			System.out.println("absolute: " + abso);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
	
	/**
	 * restituisce la root di un url senza lo / finale
	 * http://pippo.it/pluto.html -> http://pippo.it
	 * http://pippo.it/ -> http://pippo.it
	 * http://pippo.it:8080/ -> http://pippo.it:8080
	 * http://pippo.it -> http://pippo.it
	 * @param uri
	 * @return
	 */
	public static String getURLRoot(String uri) {
		String root = null;
		try {
			URL url = new URL(uri);
			String file = url.getFile();
			int pos = uri.lastIndexOf(file);
			root = uri.substring(0,pos);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return root;
	}
	
	/**
	 * removes last part as in
	 * /pippo/pluto.htm -> /pippo/
	 * /path1/path2/path3 -> /path1/path2/
	 * @param uri
	 * @return
	 */
	public static String getURLFolder(String uri) {
//		System.out.println("uri: " + uri);
		if (uri.endsWith("/")) {
			return uri;
		}
		String rel_url = null;
		try {
			String root = getURLRoot(uri);
//			System.out.println("root:" + root);
			URL url = new URL(uri);
			String path = url.getPath();
//			System.out.println("path:" + path);
			int pos = path.lastIndexOf("/");
//			System.out.println("pos:" + pos);
			rel_url = root + path.substring(0, pos + 1);
//			System.out.println("rel_url:" + rel_url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return rel_url;
	}
	
	public static void printMap(Map m) {
		Log log = LogFactory.getLog(GlobbiUtil.class);
		for(Object k : m.keySet()) {
			Object _v = m.get(k);
			Object value = (_v == null) ? null : "\"" + _v + "\"";
			log.debug(k + ": " + value);
		}
	}
	
	public static String[] loadConfLines(String file_path) {
		// una alternativa potrebbe essere questa:
		// http://commons.apache.org/configuration/howto_properties.html
		File conf = new File(file_path);
		List good_lines = new ArrayList();
		
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(conf);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				if (!strLine.trim().isEmpty() && !strLine.trim().startsWith("#")) {
					good_lines.add(strLine);
				}
			}
			fstream.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return (String[]) good_lines.toArray(new String[good_lines.size()]);
	}
}
