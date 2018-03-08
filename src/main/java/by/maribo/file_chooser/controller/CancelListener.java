package by.maribo.file_chooser.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CancelListener implements ActionListener{
    private FileController fileChooser;

    public CancelListener(FileController fileChooser){
        this.fileChooser = fileChooser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fileChooser.closeDialog();
    }
}