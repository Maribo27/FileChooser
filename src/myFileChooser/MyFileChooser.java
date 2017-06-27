package myFileChooser;

import myFileChooser.controller.Controller;

import javax.swing.*;

/**
 * Created by Maria on 15.06.2017.
 */
public class MyFileChooser {
    public static void main(String[] args) {
        try {
            String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(systemLookAndFeelClassName);
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Данное оформление не поддерживается на данной платформе");
        } catch (Exception e) {
            System.err.println("Невозможно применить данную тему оформления");
        }


        Controller controller = new Controller();
        controller.runProgram();
    }

}
