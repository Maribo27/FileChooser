package by.maribo.file_chooser.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnterListener implements ActionListener {
    private FileController fileChooser;

    public EnterListener(FileController fileChooser){
        this.fileChooser = fileChooser;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        fileChooser.setPressed();
        fileChooser.closeDialog();
    }
}
