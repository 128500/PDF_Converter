package com.alex.kudin.owndevelopment.pdfconvertor.launcher;

import com.alex.kudin.owndevelopment.pdfconvertor.gui.ProgramMainFrame;

import javax.swing.*;

/**
 * This class is an entry point of the program.
 * @Author ALEKSANDR KUDIN
 */
public class Launcher {

    public static void main(String[] args) {

        try{

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        } catch( ClassNotFoundException | UnsupportedLookAndFeelException  | InstantiationException | IllegalAccessException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка настройки интерфейса приложения", JOptionPane.INFORMATION_MESSAGE);
        }
        finally {
            SwingUtilities.invokeLater( () ->{
                ProgramMainFrame frame = new ProgramMainFrame();
                frame.setVisible(true);
            });
        }
    }
}
