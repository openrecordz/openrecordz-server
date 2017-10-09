//package shoppino.search.solr.util;
//
//public class QueryUtil {
//
//	public static String cleanAndProcessQuery(String query) {
//		String returnQuery = query;
//		
//		//clean
//		if (returnQuery.contains("!"))
//			returnQuery = returnQuery.replace("!", "");
//		
//		
//		if (!returnQuery.equals("")) {
//			String starredQuery = returnQuery +"*";
//			if (starredQuery.contains(" "))
//				starredQuery = starredQuery.replace(" ", "* ");
//			
//			returnQuery = returnQuery + " OR " + starredQuery;
//			
//			return returnQuery;
//		}else
//			return "";
//	}
//	
//	
//	
//	public static void main(String[] args) {
//		System.out.println(QueryUtil.cleanAndProcessQuery(""));
//		System.out.println(QueryUtil.cleanAndProcessQuery("*"));
//		System.out.println(QueryUtil.cleanAndProcessQuery("!"));
//		System.out.println(QueryUtil.cleanAndProcessQuery("!query"));
//		System.out.println(QueryUtil.cleanAndProcessQuery("test query"));
//		System.out.println(QueryUtil.cleanAndProcessQuery("test query1 query2"));
//		
//	}
//}
