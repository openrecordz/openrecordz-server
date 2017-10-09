package openrecordz.util;

public class LocationUtils {

	public static boolean isValidLocation(Double latitude, Double longitude) {
		boolean clientLocationAvailable=false;
		
		if (latitude != null && longitude != null) {
			clientLocationAvailable=true;
		}
		return clientLocationAvailable;
	}
}
