package by.maribo.file_chooser.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveListener implements ActionListener {
    private FileController fileChooser;

    public SaveListener(FileController fileChooser){
        this.fileChooser = fileChooser;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        fileChooser.setPressed();
        fileChooser.closeDialog();
    }
}
