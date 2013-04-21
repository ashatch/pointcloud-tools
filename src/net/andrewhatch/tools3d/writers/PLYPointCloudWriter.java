package net.andrewhatch.tools3d.writers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import net.andrewhatch.tools3d.model.RGBPoint;
import net.andrewhatch.tools3d.model.RGBPointCloud;

public class PLYPointCloudWriter {

	private static final Logger log = Logger.getLogger(PLYPointCloudWriter.class.getName());

	public static void write(RGBPointCloud cloud, File f) throws IOException {
		FileOutputStream fos = new FileOutputStream(f);
		write(cloud, fos);
	}

	public static void write(RGBPointCloud cloud, OutputStream os) throws IOException {
		log.info("Writing PLY header.");
		os.write("ply\n".getBytes());
		os.write("format ascii 1.0\n".getBytes());
		os.write(("element vertex " + cloud.points.size() + "\n").getBytes());
		os.write("property float x\n".getBytes());
		os.write("property float y\n".getBytes());
		os.write("property float z\n".getBytes());
		os.write("property uchar red\n".getBytes());                 
		os.write("property uchar green\n".getBytes());
		os.write("property uchar blue\n".getBytes());
		os.write("end_header\n".getBytes());
		int points = cloud.points.size();
		log.info("Writing point cloud data: " + points + " points.");
		int progress = 0;
		int lastprogress = -1;		
		for(int i = 0; i < points; i++) {
			writePoint(cloud.points.get(i), os);
			progress = (int)(((double)i / (double) points) * 100.0);
			if(progress % 10 == 0 && lastprogress != progress) {
				log.info(progress + "%");
				lastprogress = progress;
			}
		}
		progress = 0;
		lastprogress = -1;
		os.close();
		log.info("100% - PLY write complete.");
	}

	private static void writePoint(RGBPoint point, OutputStream os) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append((point.x + " "));
		sb.append((point.y + " "));
		sb.append((point.z + " "));
		sb.append((point.r + " "));
		sb.append((point.g + " "));
		sb.append((point.b + "\n"));
		os.write(sb.toString().getBytes());
	}
}
