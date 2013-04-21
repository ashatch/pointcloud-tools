package net.andrewhatch.tools3d;

import java.io.File;
import java.io.IOException;

import net.andrewhatch.tools3d.model.RGBPointCloud;
import net.andrewhatch.tools3d.readers.ASCPointCloudReader;
import net.andrewhatch.tools3d.writers.PLYPointCloudWriter;

public class CSVToPLY {
	public static void main(String[] args) throws IOException {
		if(args.length < 2) {
			usage();
			System.exit(1);
		}
		RGBPointCloud cloud = ASCPointCloudReader.read(new File(args[0]), true, true);
		PLYPointCloudWriter.write(cloud, new File(args[1]));
	}

	private static void usage() {
		System.out.println("Please supply a VTK input file and ASC output file as arguments");
		
	}
}
