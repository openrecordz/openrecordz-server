package openrecordz.service;

import openrecordz.exception.OpenRecordzException;

public interface SocialService {
	
	public void updatePhotoFromSocial(String username) throws OpenRecordzException;
}
