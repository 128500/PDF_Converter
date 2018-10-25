package com.alex.kudin.owndevelopment.pdfconvertor.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * This class uses the PDFbox library. It contains methods to
 * convert the images(jpeg, jpg, png, tif, tiff, gif, bmp) into
 * a PDF document.
 */
public class PDFConverter {

    /**
     * Converts the given images into a PDF document and saves it
     * in the file system
     * @param files a list of the given files (images)
     * @param path a path to save a created PDF file
     * @return true - if the operation of converting and saving file was successful, or false - if not
     */
    public boolean convertFiles(List<File> files, String path){
        PDDocument doc = new PDDocument();

        try {
        for(File f : files){
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDImageXObject image = PDImageXObject.createFromFile(f.getAbsolutePath(), doc);
            PDPageContentStream contents = new PDPageContentStream(doc, page);
            Dimension scaledDim = getScaledDimension(new Dimension(image.getWidth(),  image.getHeight()), createDimension(page.getMediaBox()));
            contents.drawImage(image, 0f,0f, scaledDim.width, scaledDim.height);
            contents.close();
        }
            doc.save(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка !!!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally {
            try {
                doc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Checks if the dimensions of the image received from the file fit to the
     * dimensions of the standard A4 sheet. If it is not - reduces the
     * current dimensions of the image.
     * @param imgSize dimensions of the image received from the file (as a Dimension object)
     * @param boundary dimensions of the the standard A4 sheet (as a Dimension object)
     * @return fitted dimensions (as a Dimension object)
     */
    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }


    /**
     * Creates a Dimension object from a PDRectangle object
     * (for more @see org.apache.pdfbox.pdmodel.common.PDRectangle)
     * @param page a PDRecangle object
     * @return a Dimensions object
     */
    private static Dimension createDimension(PDRectangle page){
        return new Dimension((int)Math.floor((double)page.getWidth()), (int)Math.floor((double)page.getHeight()));
    }

}
