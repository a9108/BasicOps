package Network;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Images {
	public static void saveJPG(String weburl,String path){
		BufferedImage image = null;  
	    try {
	    	URL url = new URL(weburl);  
	        image = ImageIO.read(url);  

	        ImageIO.write(image, "jpg", new File(path));  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    System.out.println("Done");  
	}
}
