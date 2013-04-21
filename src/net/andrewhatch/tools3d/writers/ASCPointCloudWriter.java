package net.andrewhatch.tools3d.writers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import net.andrewhatch.tools3d.model.RGBPoint;
import net.andrewhatch.tools3d.model.RGBPointCloud;

public class ASCPointCloudWriter {
	
	private static final Logger log = Logger.getLogger(ASCPointCloudWriter.class.getName());
	
	public static void write(RGBPointCloud cloud, File f) throws IOException {
		write(cloud, new FileOutputStream(f));
	}
	
	public static void write(RGBPointCloud cloud, OutputStream os) throws IOException {
		int points = cloud.points.size();
		log.info("Writing point cloud data: " + points + " points.");
		int progress = 0;
		int lastprogress = -1;	
		RGBPoint t;
		for(int i = 0; i < points; i++) {
			t = cloud.points.get(i);
			os.write(("" + t.x + " " + t.y + " " + t.z + " " + t.r + " " + t.g + " " + t.b).getBytes());
			progress = (int)(((double)i / (double) points) * 100.0);
			if(progress % 10 == 0 && lastprogress != progress) {
				log.info(progress + "%");
				lastprogress = progress;
			}
		}
		os.close();
		progress = 0;
		lastprogress = -1;
		log.info("100% - ASC write complete.");
	}
}
