package net.andrewhatch.tools3d.readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.andrewhatch.tools3d.model.RGBPoint;
import net.andrewhatch.tools3d.model.RGBPointCloud;

public class VTKPointCloudReader {

	private static final Logger log = Logger.getLogger(VTKPointCloudReader.class.getName());
	
	public static RGBPointCloud read(File f) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		return read(fis);
	}

	public static RGBPointCloud read(InputStream is) throws IOException {
		log.info("Reading VTK header.");
		RGBPointCloud cloud = new RGBPointCloud();
		String line = readLine(is);
		while(!line.startsWith("POINTS")) {
			line = readLine(is);
		}

		long points = getPointCount(line);
		log.info("Reading " + points + " points.");

		byte[] buffer = new byte[12];
		int progress = 0;
		int lastprogress = -1;
		
		double xmin = Double.MAX_VALUE; double xmax = Double.MIN_VALUE;
		double ymin = Double.MAX_VALUE; double ymax = Double.MIN_VALUE;
		double zmin = Double.MAX_VALUE; double zmax = Double.MIN_VALUE;
		
		
		for(int i = 0; i < points; i++) {
			is.read(buffer, 0, 12);
			RGBPoint point = new RGBPoint();
			point.x = toFloat(buffer, 0);
			point.y = toFloat(buffer, 4);
			point.z = toFloat(buffer, 8);
			
			if(point.x < xmin) xmin = point.x; if(point.x > xmax) xmax = point.x;
			if(point.y < ymin) ymin = point.y; if(point.y > ymax) ymax = point.y;
			if(point.z < zmin) zmin = point.z; if(point.z > zmax) zmax = point.z;
			
			cloud.points.add(point);
			progress = (int)(((double)i / (double) points) * 100.0);
			if(progress % 10 == 0 && lastprogress != progress) {
				log.info(progress + "%");
				lastprogress = progress;
			}
		}
		progress = 0;
		lastprogress = -1;
		log.info("100% - VTK read complete.");
		log.info("X range: " + xmin + " -> " + xmax);
		log.info("Y range: " + ymin + " -> " + ymax);
		log.info("Z range: " + zmin + " -> " + zmax);

		readLine(is);
		readLine(is);
		readLine(is);

		byte[] rgb_buffer = new byte[3];

		log.info("Reading colour data for " + points + " points.");
		
		double rmin = Double.MAX_VALUE; double rmax = Double.MIN_VALUE;
		double gmin = Double.MAX_VALUE; double gmax = Double.MIN_VALUE;
		double bmin = Double.MAX_VALUE; double bmax = Double.MIN_VALUE;
		
		RGBPoint t;
		
		for(int i = 0; i < points; i++) {
			is.read(rgb_buffer, 0, 3);
			t = cloud.points.get(i);
			t.r = unsignedByteToInt(rgb_buffer[0]);
			t.g = unsignedByteToInt(rgb_buffer[1]);
			t.b = unsignedByteToInt(rgb_buffer[2]);
			
			if(t.r < rmin) rmin = t.r; if(t.r > rmax) rmax = t.r;
			if(t.g < gmin) gmin = t.g; if(t.g > gmax) gmax = t.g;
			if(t.b < bmin) bmin = t.b; if(t.b > bmax) bmax = t.b;
			
			progress = (int)(((double)i / (double) points) * 100.0);
			if(progress % 10 == 0 && lastprogress != progress) {
				log.info(progress + "%");
				lastprogress = progress;
			}
		}
		
		log.info("100% - read complete.");
		log.info("R range: " + rmin + " -> " + rmax);
		log.info("G range: " + gmin + " -> " + gmax);
		log.info("B range: " + bmin + " -> " + bmax);

		is.close();

		return cloud;
	}

	private static String readLine(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		int c;
		while((c= is.read()) != (int)'\n') {
			sb.append((char)c);
		}
		return sb.toString();
	}

	private static long getPointCount(String line) {
		StringTokenizer st = new StringTokenizer(line, " ");
		st.nextToken();		
		return Long.parseLong(st.nextToken());
	}

	public static float toFloat(byte[] b, int offset)
	{		
		int i = (((b[offset + 0] & 0xff) << 24) | ((b[offset + 1] & 0xff) << 16) |
				((b[offset + 2] & 0xff) << 8) | (b[offset + 3] & 0xff));
		return Float.intBitsToFloat(i);
	}

	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}
}
