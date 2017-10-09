package shoppino.util;

public class ThumbnailUtil {

	public static int[] fitSize(float[] origSize, float[] destinatioSize) {
		float[] scaledSize = new float[2];
		scaledSize[0]=origSize[0];
		scaledSize[1]=origSize[1];
	   
	    // first scale on width
	    float hScaleFactor;
	    if (scaledSize[0]> destinatioSize[0]) {
	        hScaleFactor = destinatioSize[0] / origSize[0];
	        scaledSize[0] = (origSize[0] * hScaleFactor);
	        scaledSize[1] = (origSize[1] * hScaleFactor);
	    }
	   
	    // then scale on height
	    float vScaleFactor;
	    if (scaledSize[1]> destinatioSize[1]) {
	        vScaleFactor = destinatioSize[1] / scaledSize[1];
	        scaledSize[1] = (scaledSize[1] * vScaleFactor);
	        scaledSize[0] = (scaledSize[0] * vScaleFactor);
	    }
	    int[] returns = new int[2];
	    returns[0] =  Math.round(scaledSize[0]);
	    returns[1] =  Math.round(scaledSize[1]);
	    return returns;
	}
	
	
	public static void main (String[] args) {
		float[] orgi = {950,712};
		float[] dest = {300,300};
		int[] newsize= fitSize(orgi, dest);
		
		float[] orgi2 = {600,600};
		float[] dest2 = {640,640};
		newsize= fitSize(orgi2, dest2);
		
		
		
		float[] orgi3 = {720,960};
		float[] dest3 = {640,640};
		newsize= fitSize(orgi3, dest3);
		//480*640
		System.out.println(newsize);
	}
}
