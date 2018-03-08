package by.maribo;

import by.maribo.file_chooser.controller.FileController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Maria on 15.06.2017.
 */
public class FileChooser {
    public static void main(String[] args) {
        try {
            String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(systemLookAndFeelClassName);
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Данное оформление не поддерживается на данной платформе");
        } catch (Exception e) {
            System.err.println("Невозможно применить данную тему оформления");
        }
        FileController controller = new FileController();

        JFrame frame = new JFrame("Выберите операцию:");
        frame.setSize(new Dimension(100,85));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.DARK_GRAY);
        JButton saveButton, openButton;
        saveButton = new JButton("Сохранить");
        openButton = new JButton("Открыть");
        frame.add(saveButton, BorderLayout.NORTH);
        frame.add(openButton, BorderLayout.SOUTH);
        saveButton.addActionListener(e -> controller.createDialog(0));
        openButton.addActionListener(e -> controller.createDialog(1));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
