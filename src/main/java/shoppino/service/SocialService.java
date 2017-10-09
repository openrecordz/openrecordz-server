package shoppino.service;

import shoppino.exception.ShoppinoException;

public interface SocialService {
	
	public void updatePhotoFromSocial(String username) throws ShoppinoException;
}
