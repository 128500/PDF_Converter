package com.alex.kudin.owndevelopment.pdfconvertor.gui;

import com.alex.kudin.owndevelopment.pdfconvertor.pdf.PDFConverter;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * This class creates the main frame of the program and
 * contains methods to dynamically change the content of
 * the panels.
 *
 * @Author ALEKSANDR KUDIN
 */
public class ProgramMainFrame extends JFrame {

    /*Panel that contains mini pictures of chosen files*/
    private JPanel imagePanel;

    /*Scroll panel that contains image panel (see above)*/
    private JScrollPane scrollPane;

    /*Text area that shows the information about chosen files, paths of saved pdf files, errors, etc.*/
    private JTextPane textPane;

    private List<File> chosenFiles;
    private List<File> unacceptableFiles;

    /*Class constructor*/
    public ProgramMainFrame(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 600);
        setResizable(false);
        setTitle("Конвертер JPEG/JPG/PNG/BMP/TIF -> PDF    **(ALEX KUDIN \u00A9)**");
        setLocationRelativeTo(null);

        add(createButtonsPanel(), BorderLayout.SOUTH);
        add(createImageScrollPanel(5), BorderLayout.WEST);
        add(createTextScrollPanel(), BorderLayout.CENTER);
    }


    /**
     * Creates a panel with buttons
     * @return created panel
     */
    private JPanel createButtonsPanel(){
        JPanel panel = new JPanel();

        JButton choose = new JButton("Выбрать файлы");
        choose.addActionListener(new FileChooseListener()); // see below

        JButton save = new JButton("Сохранить как PDF");
        save.addActionListener(new SaveAsPDFListener()); // see below

        panel.add(choose);
        panel.add(save);

        return panel;
    }


    /**
     * Creates scrollable panel with text area
     * @return created scroll panel
     */
    private JScrollPane createTextScrollPanel(){
        textPane = createTextAreaPanel(); // see below
        JScrollPane pane  = new  JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(500, 600));
        return pane;

    }

    /**
     * Creates scrollable panel with text area
     * @param rows quantity of images that will be placed on the panel
     * @return created scroll panel
     */
    private JScrollPane createImageScrollPanel(int rows){

        imagePanel = createImagePanel(rows); // see below

        /*By default on the panel are placed document icons*/
        URL resource = getClass().getClassLoader().getResource("images/doc_img.png");
        for(int i = 0; i < rows; i++){
            assert resource != null;
            imagePanel.add(new JLabel(new ImageIcon(resource)));
        }
        scrollPane = new  JScrollPane(imagePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 600));
        return scrollPane;
    }

    /**
     * Creates a panel on witch there will be placed mini pictures of chosen files
     * @param rows quantity of elements (pictures) that will be placed
     * @return created panel
     */
    private JPanel createImagePanel(int rows){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, 1,5, 5));
        panel.setBackground(new Color(177,181,174));
        return panel;
    }


    /**
     * Creates a panel on witch there will be placed text messages:
     * names of the chosen files, names of inappropriate files (extension of witch unacceptable),
     * path to witch was saved converted PDF file, errors, etc.
     * @return created panel
     */
    private JTextPane createTextAreaPanel(){
        JTextPane area = new JTextPane();
        area.setPreferredSize(new Dimension(500, 600));
        return textPane = area;
    }


    /**
     * Fills the text area with the information about chosen files
     */
    private void fillTextPane(){

        if(!chosenFiles.isEmpty()) {
            appendToPane(textPane, "\n\nБыли выбраны следующие файлы:\n", Color.BLACK);
            for (File f : chosenFiles) {
                appendToPane(textPane, " - " + f.getName() + ";\n", Color.BLACK);
            }
        }
        if(!unacceptableFiles.isEmpty()) {
            appendToPane(textPane, "\n\n", Color.WHITE);
            appendToPane(textPane, "*************************************************", Color.BLACK);
            appendToPane(textPane, "\n\n", Color.WHITE);

            appendToPane(textPane, "Следующие файлы имеют недопустимое разрешение:\n", Color.BLACK);
            for (File f : unacceptableFiles) {
                appendToPane(textPane, " - " + f.getName() + ";\n", Color.RED);
            }
        }
    }


    /**
     * Shows an error on the text area panel in red color
     * @param msg a massage to show
     */
    private void showError(String msg){
        appendToPane(textPane, "\n\n" + msg, Color.RED);
    }


    /**
     * Shows a massage on the text area panel about successful saving of a file (after converting to pdf)
     * @param msg a  massage to show
     */
    private void showSuccessfulSave(String msg){
        appendToPane(textPane, msg, Color.BLACK);
    }

    /**
     * Adds a text message to a text panel
     * @param tp a panel to witch the message must be added
     * @param msg a text of the message
     * @param c color in witch it must be painted
     */
    private void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 14);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }


    /**
     * Checks the name of the file that user input for unacceptable sings
     * @param fileName a file name to check
     * @return true if the file name is valid, or else - false
     */
    private boolean validateFileName(String fileName) {
        return fileName.matches("^[^.\\\\/:*?\"<>|]?[^\\\\/:*?\"<>|]*")
                && getValidFileName(fileName).length()>0;
    }

    private String getValidFileName(String fileName) {
        String newFileName = fileName.replace("^\\.+", "").replaceAll("[\\\\/:*?\"<>|]", "");
        if(newFileName.length()==0)
            throw new IllegalStateException(
                    "File Name " + fileName + " results in an empty fileName!");
        return newFileName;
    }


    /**
     * An action listener for 'choose files' button. Calls a  file chooser window.
     */
    private class FileChooseListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            int result = chooser.showOpenDialog(null);
            if(result == JFileChooser.CANCEL_OPTION || result == JFileChooser.ERROR_OPTION) return;
            File[] files = chooser.getSelectedFiles();

            textPane.setText("");
            chosenFiles = new ArrayList<>();
            unacceptableFiles = new ArrayList<>();
            imagePanel.removeAll();
            imagePanel  = createImagePanel(files.length);

            for(File f : files) {
               String extension = f.getName().substring(f.getName().lastIndexOf('.')+ 1);
               if(extension.toLowerCase().matches("(jpeg)|(jpg)|(png)|(tiff)|(tif)|(gif)|(bmp)")){
                chosenFiles.add(f);
                Image image = new ImageIcon(f.getAbsolutePath()).getImage().getScaledInstance(128,170,Image.SCALE_DEFAULT);
                ImageIcon icon = new ImageIcon(image);
                imagePanel.add(new JLabel(icon));
               }
               else unacceptableFiles.add(f);
            }

            fillTextPane();
            textPane.repaint();
            scrollPane.setViewportView(imagePanel);
            scrollPane.revalidate();
            scrollPane.repaint();
        }
    }


    /**
     * An action listener for 'save as PDF' button. Converts to pdf the
     * chosen files and shows information about saving, or error if it failed.
     */
    private class SaveAsPDFListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(chosenFiles == null || chosenFiles.isEmpty()){
                textPane.setText("");
                showError("Не выбрано ни одного файла!!!");
                textPane.repaint();
                return;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result  = chooser.showSaveDialog(null);
            if(result == JFileChooser.CANCEL_OPTION || result == JFileChooser.ERROR_OPTION) return;
            File directory = chooser.getSelectedFile();
            if(!validateFileName(directory.getName())) {
                showError("Имя сохраняемого файла содержит недопустимые символы:\n" +
                "'\\', '/', ':', '\"', '*', '?', '<', '>', '|'");
                return;
            }
            textPane.setText("");

            boolean saveResult = new PDFConverter().convertFiles(chosenFiles, directory.getAbsolutePath()+".pdf");
            if(saveResult) showSuccessfulSave("\n\nФайл PDF успешно сохранен в следующей директории : \n" + directory.getAbsolutePath() +".pdf");
            else showError("\n\nФайл не удалось сохранить по причине ошибки!!!");
        }
    }
}
