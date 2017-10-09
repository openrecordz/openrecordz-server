package shoppino.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import shoppino.service.UrlService;
import shoppino.url.UrlDAO;

public class UrlServiceImpl implements UrlService {

	@Autowired
	UrlDAO urlDAO;
	
	@Override
	public String save(String url) {
		urlDAO.save(url);
		return urlDAO.getByUrl(url);
	}

	@Override
	public String getByUID(String uid) {
		return urlDAO.getByUID(uid);
	}

}
