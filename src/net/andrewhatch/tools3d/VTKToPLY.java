package net.andrewhatch.tools3d;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.andrewhatch.tools3d.model.RGBPointCloud;
import net.andrewhatch.tools3d.readers.VTKPointCloudReader;
import net.andrewhatch.tools3d.writers.PLYPointCloudWriter;

public class VTKToPLY {

	private File in = null;
	private File out = null;
	private boolean gfxMode = false;

	private VTKToPLY(String[] args) throws IOException {
		if(args.length < 2) {
			gfxMode  = true;
			ask();
		}else{
			in = new File(args[0]);
			out = new File(args[1]);
		}

		if(in != null && out != null) {
			RGBPointCloud cloud = VTKPointCloudReader.read(in);
			PLYPointCloudWriter.write(cloud, out);
		}else{
			String msg = "Did not specify files to work on.";
			if(gfxMode) {				
				JOptionPane.showMessageDialog(new JFrame(), msg);
			}else{
				System.out.println(msg);
			}
		}
		System.exit(0);
	}



	private void ask() {
		FileDialog d = new FileDialog(new JFrame(), "Select .vtk file");
		d.setFile("*.vtk");
		d.setDirectory(".");
		d.setVisible(true);
		String openFile;
		if((openFile = d.getFile()) != null) {
			in = new File(d.getDirectory(), openFile);
		}

		if(in != null) {
			d = new FileDialog(new JFrame(), "Convert " + in.getName() + " to PLY", FileDialog.SAVE);
			int dotPos = in.getName().lastIndexOf(".");
			d.setFile(in.getName().substring(0, dotPos) + ".ply");
			d.setDirectory(".");
			d.setVisible(true);
			String saveFile;
			if((saveFile = d.getFile()) != null) {
				out = new File(d.getDirectory(), saveFile);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new VTKToPLY(args);
	}
}
