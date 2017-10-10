package openrecordz.service;

import openrecordz.exception.ShoppinoException;

public interface SocialService {
	
	public void updatePhotoFromSocial(String username) throws ShoppinoException;
}
