package myFileChooser.view;

import myFileChooser.tree.TreePanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static view.Const.PASSWORD;

/**
 * Created by Maria on 24.06.2017.
 */
class DelFiles {

    private JFrame passwordFrame;
    private JLabel label;
    private JPasswordField passwordField;
    private JButton decline;
    private JButton accept;
    private JFrame frame;
    private File fileToDel;
    private TreePanel panel;

    DelFiles(TreePanel panel, File file, JFrame frame){
        this.panel = panel;
        fileToDel = file;
        this.frame = frame;
        this.frame.setEnabled(false);
        passwordFrame = new JFrame("Ввод пароля");
        label = new JLabel("Будут удален(ы) файл(ы): " + fileToDel.getName());
        passwordField = new JPasswordField(16);
        decline = new JButton("Отклонить");
        accept = new JButton("Принять");
    }

    void delFolder(){
        passwordFrame.setLayout(new FlowLayout());
        passwordFrame.setResizable(false);
        passwordFrame.setEnabled(true);
        passwordFrame.setBackground(Color.DARK_GRAY);
        passwordFrame.add(label);
        passwordFrame.add(passwordField);
        passwordFrame.add(accept);
        passwordFrame.add(decline);
        passwordFrame.pack();

        decline.addActionListener(e -> {
            frame.setEnabled(true);
            passwordFrame.dispose();
        });
        accept.addActionListener(e -> {
            char[] pass = passwordField.getPassword();
            String password = "";
            for (char pas : pass) {
                password += pas;
            }
            if (!password.equals(PASSWORD)){
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Пароль неверный", "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
                passwordField.selectAll();
            }
            else {
                recursiveDelete(fileToDel);
                frame.setEnabled(true);
                passwordFrame.dispose();
            }
        });

        passwordFrame.setVisible(true);
        passwordFrame.setLocationRelativeTo(null);
    }

    private void recursiveDelete(File file) {
        panel.delNode(file);
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();
        System.out.println("Удаленный файл/папка: " + file.getAbsolutePath());
    }
}
