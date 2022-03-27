/*
 * Copyright ou © ou Copr. Serge Rosmorduc (2004-2020) 
 * serge.rosmorduc@cnam.fr
 * Copyright ou ou Copr. 2022 Ralph Giles <giles@thaumas.net>

 * Ce logiciel est régi par la licence CeCILL-C soumise au droit français et
 * respectant les principes de diffusion des logiciels libres : "http://www.cecill.info".

 * This software is governed by the CeCILL-C license 
 * under French law : "http://www.cecill.info". 
 */
package jsesh.demo;

/**
 * How to use JSesh to render Manuel de Codage files as images.
 * compile: javac -cp .:/FOLDER_CONTAINING/jsesh.jar MDCToPNG.java
 * run: java -cp .:/FOLDER_CONTAINING/jsesh.jar MDCToPNG
 * 
 * Various other packages like jseshGlyphs.jar and jvectClipboard-1.0.jar
 * should be in the same folder as jsesh.jar, as in the installer packages.
 * (normally, there is no need to add them explicitely to the class path,
 * as jsesh.jar contains the necessary information in its manifest.)
 *
 * Or, to build and run the demo from the top level of the source tree
 * compile: mvn package
 * run: java -cp jseshDemos/target/jseshDemos-7.5.5.jar:jsesh/target/jsesh-7.5.5.jar:cupruntime/target/cupruntime-7.5.5.jar:qenherkhopeshefUtils/target/qenherkhopeshefUtils-7.5.5.jar:jseshGlyphs/target/jseshGlyphs-7.5.5.jar jsesh.demo.MDCToPNG file.gly
 */

import javax.imageio.ImageIO;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.awt.image.*;

import jsesh.mdc.constants.TextDirection;
import jsesh.mdc.constants.TextOrientation;
import jsesh.mdcDisplayer.preferences.*;
import jsesh.mdcDisplayer.draw.*;
import jsesh.mdc.*;

public class MDCToPNG {

	public static BufferedImage buildImage(String mdcText)
			throws MDCSyntaxError {
		// Create the drawing system:
		MDCDrawingFacade drawing = new MDCDrawingFacade();

		// Change the scale, choosing the cadrat height in pixels.
		drawing.setCadratHeight(60);

                // Allow longer rendered lines
                drawing.setMaxSize(5000,4000);

		// Change a number of parameters
		DrawingSpecification drawingSpecifications = new DrawingSpecificationsImplementation();
		PageLayout pageLayout = new PageLayout();
		pageLayout.setLeftMargin(5);
		pageLayout.setTopMargin(5);
                drawingSpecifications.setPageLayout(pageLayout);
		drawingSpecifications.setTextDirection(TextDirection.RIGHT_TO_LEFT);
		drawingSpecifications.setTextOrientation(TextOrientation.HORIZONTAL);	
		drawing.setDrawingSpecifications(drawingSpecifications);
		// Create the picture
		BufferedImage result = drawing.createImage(mdcText);
		return result;
	}

        // Replace or append a filename extension.
        static String setExtension(String str, String ext) {
          int ext_start = str.lastIndexOf(".");
          if (ext_start > 0) {
            return str.substring(0, ext_start) + "." + ext;
          } else {
            return str + "." + ext;
          }
        }

	public static void main(String args[]) throws MDCSyntaxError, FileNotFoundException, IOException {
          for (String arg : args) {
            System.out.println("Converting '" + arg + "'...");
            // Read the input file; expecting .gly for MdC text.
            String mdc = new String(Files.readAllBytes(Paths.get(arg)));
            // Create the picture
            BufferedImage img = buildImage(mdc);
            // Open the output file.
            Path out_path = Paths.get(setExtension(arg, "png"));
            System.out.println("   opening '" + out_path + "'...");
            OutputStream out_stream = Files.newOutputStream(out_path);
            // Save the image.
            ImageIO.write(img, "png", out_stream);
            out_stream.close();
	  }
        }
}
