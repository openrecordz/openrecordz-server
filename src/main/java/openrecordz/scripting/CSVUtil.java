package openrecordz.scripting;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CSVUtil{
	
	
	protected final Log logger = LogFactory.getLog(getClass());


	public List<Map<String, String>> parse(String filePath) throws IOException {
		return this.parse(filePath, true, null);
	}
	
	public List<Map<String, String>> parse(String filePath, boolean withHeader, String[] headers) throws IOException {
		return this.parse(filePath, withHeader, headers,null);
	}
	public List<Map<String, String>> parse(String filePath, boolean withHeader, String[] headers, Character delimiter) throws IOException {

		FileReader fileReader = null;
		CSVParser csvFileParser = null;

		//Create the CSVFormat object with the header mapping
		CSVFormat csvFileFormat = CSVFormat.DEFAULT;
		
			if (delimiter!=null) 
				csvFileFormat=csvFileFormat.withDelimiter(delimiter);
			
			if (withHeader)
				csvFileFormat = csvFileFormat.withHeader();

			//initialize FileReader object
			fileReader = new FileReader(filePath); 

			try {
				//initialize CSVParser object
				csvFileParser = new CSVParser(fileReader, csvFileFormat);
			} catch (IllegalArgumentException iae) {
				CSVDuplicateHeaderRuntimeException csvDuplicateHeaderRuntimeException = new CSVDuplicateHeaderRuntimeException(iae);
				logger.warn("Duplicate CSV Header ", csvDuplicateHeaderRuntimeException);
				throw csvDuplicateHeaderRuntimeException;
			}
			
			List<Map<String, String>> lm1 = new ArrayList<Map<String, String>>();
			
			//Get a list of CSV file records
			List<CSVRecord> csvRecords = csvFileParser.getRecords();
			for (int i=0; i<csvRecords.size(); i++) {
				CSVRecord record = csvRecords.get(i);
				
				if (headers!=null){
					Map<String,String> map = new HashMap<String, String>();
					for (int j=0;j<headers.length;j++) {
						if (headers[j].equals("_skipcolumn")){
							//skip column
						}else {
							map.put(headers[j],record.get(j));
						}
					}
					
					lm1.add(map);
					
				}else {
					lm1.add(record.toMap());	
				}
				
			}
			
			return lm1;
			
			
	}
	public List<Map<String, String>> parse(String filePath, boolean withHeader, String[] headers, Character delimiter, long limitCount) throws IOException {
		return this.parse(filePath, withHeader, headers, delimiter, limitCount, 0);
	}
	
	public List<Map<String, String>> parse(String filePath, boolean withHeader, String[] headers, Character delimiter, long limitCount, long skipRow) throws IOException  {
		
		FileReader fileReader = null;
		CSVParser csvFileParser = null;

		//Create the CSVFormat object with the header mapping
		CSVFormat csvFileFormat = CSVFormat.DEFAULT;
		
			if (delimiter!=null) 
				csvFileFormat=csvFileFormat.withDelimiter(delimiter);
			
			if (withHeader)
				csvFileFormat = csvFileFormat.withHeader();

			//initialize FileReader object
			fileReader = new FileReader(filePath);

			try {
				//initialize CSVParser object
				csvFileParser = new CSVParser(fileReader, csvFileFormat);
			}catch (IllegalArgumentException iae) {
				CSVDuplicateHeaderRuntimeException csvDuplicateHeaderRuntimeException = new CSVDuplicateHeaderRuntimeException(iae);
				logger.warn("Duplicate CSV Header ", csvDuplicateHeaderRuntimeException);
				throw csvDuplicateHeaderRuntimeException;
			}
			
			
			List<Map<String, String>> lm1 = new ArrayList<Map<String, String>>();
			
			//Get a list of CSV file records
//			List<CSVRecord> csvRecords = csvFileParser.getRecords();
//			for (int i=0; i<csvRecords.size(); i++) {
			long i =0;
			for (CSVRecord record : csvFileParser) {
				if (i==limitCount) {
					break;
				}
				
				if (i<skipRow) {
					i++;
					continue;
				}
				
				if (headers!=null){
					Map<String,String> map = new HashMap<String, String>();
					for (int j=0;j<headers.length;j++) {
						if (headers[j].equals("_skipcolumn")){
							//skip column
						}else {
							map.put(headers[j],record.get(j));
						}
					}
					
					lm1.add(map);
					
				}else {
					lm1.add(record.toMap());	
				}
				
				i++;
			}
			
			return lm1;
			
			
	}
	
	public Map<String, Integer> getHeader(String filePath, boolean withHeader, Character delimiter) throws IOException {
		FileReader fileReader = null;
		CSVParser csvFileParser = null;

		//Create the CSVFormat object with the header mapping
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader();

		if (delimiter!=null) 
			csvFileFormat=csvFileFormat.withDelimiter(delimiter);
		
		if (withHeader)
			csvFileFormat = csvFileFormat.withHeader();
		
			//initialize FileReader object
			fileReader = new FileReader(filePath);
			
			try {
				//initialize CSVParser object
				csvFileParser = new CSVParser(fileReader, csvFileFormat);
			}catch (IllegalArgumentException iae) {
				CSVDuplicateHeaderRuntimeException csvDuplicateHeaderRuntimeException = new CSVDuplicateHeaderRuntimeException(iae);
				logger.warn("Duplicate CSV Header ", csvDuplicateHeaderRuntimeException);
				throw csvDuplicateHeaderRuntimeException;
			}
			
			return csvFileParser.getHeaderMap();
			
			
	}
	
	public static void main(String[] args)  {
//	public static void main(String[] args) throws IOException {
		CSVUtil csvutil = new CSVUtil();
		try {
			List res =csvutil.parse("/home/andrea/Downloads/IDC Customer Export-2016-03-30 12-05-41 (pl280).csv", true,null, ',');
			System.out.println(res);
			System.out.println("fine");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
