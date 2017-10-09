package shoppino.service.impl;

import openrecordz.domain.Searchable;

public class CategoryQuerySolrUtil {

	public static String createQuery(String category) {
		//end with /
		if (category.endsWith("/"))
			return Searchable.TENANTS_FIELD+":("+ category+ " OR "+ category+"* )";
		else
			return Searchable.TENANTS_FIELD+":("+ category+ " OR "+ category+"/* )";
	}
}
