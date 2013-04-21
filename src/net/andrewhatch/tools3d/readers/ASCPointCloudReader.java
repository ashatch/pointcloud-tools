package net.andrewhatch.tools3d.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import net.andrewhatch.tools3d.model.RGBPoint;
import net.andrewhatch.tools3d.model.RGBPointCloud;

public class ASCPointCloudReader {
private static final Logger log = Logger.getLogger(ASCPointCloudReader.class.getName());
	
	public static RGBPointCloud read(File f, boolean csv, boolean ignoreFirstLine) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		return read(fis, csv, ignoreFirstLine);
	}

	public static RGBPointCloud read(InputStream is, boolean csv, boolean ignoreFirstLine) throws IOException {
		log.info("Reading VTK header.");
		
		String splitBy = " ";
		if(csv) splitBy = ",";
		
		RGBPointCloud cloud = new RGBPointCloud();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;

		if(ignoreFirstLine) line = readLine(is);
		
		RGBPoint point;
		
		while((line = br.readLine()) != null) {
			String[] parts = line.split(splitBy);
			point = new RGBPoint();
			point.x = Double.parseDouble(parts[0].trim());
			point.y = Double.parseDouble(parts[0].trim());
			point.z = Double.parseDouble(parts[0].trim());
			point.r = 1;
			point.g = 1;
			point.b = 1;
		}
		
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
}
