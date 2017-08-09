package myFileChooser.view;

import myFileChooser.controller.FileController;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by Maria on 24.06.2017.
 */
class DelFiles {

    private JDialog passwordFrame;
    private JLabel label;
    private JPasswordField passwordField;
    private JButton decline;
    private JButton accept;
    private File fileToDel;
    private FileController controller;

    DelFiles(FileController controller, File file){
        this.controller = controller;
        fileToDel = file;
        JFrame frame = null;
        passwordFrame = new JDialog(frame, "Ввод пароля", true);
        label = new JLabel("Будут удален файл/папка: " + fileToDel.getName());
        passwordField = new JPasswordField(16);
        decline = new JButton("Отклонить");
        accept = new JButton("Принять");
    }

    void delFolder(){
        passwordFrame.setLayout(new BorderLayout());
        controller.disabledFrame(false);
        passwordFrame.setResizable(false);
        passwordFrame.setEnabled(true);
        JPanel passPanel = new JPanel();
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.Y_AXIS));
        label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JLabel label2 = new JLabel("Введите пароль");
        label2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label2.setFont(new Font("Arial", Font.PLAIN, 14));
        passPanel.add(label);
        passPanel.add(label2);
        passPanel.add(passwordField);
        JPanel butPanel = new JPanel();
        butPanel.setLayout(new BoxLayout(butPanel, BoxLayout.X_AXIS));
        butPanel.add(accept);
        butPanel.add(decline);
        passPanel.add(butPanel);
        passwordFrame.add(passPanel, BorderLayout.CENTER);
        passwordFrame.pack();

        decline.addActionListener(e -> {
            controller.disabledFrame(true);
            passwordFrame.dispose();
        });
        accept.addActionListener(e -> {
            char[] pass = passwordField.getPassword();
            String password = "";
            for (char pas : pass) {
                password += pas;
            }
            if (!password.equals(Const.PASSWORD)){
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Пароль неверный", "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
                passwordField.selectAll();
            }
            else {
                recursiveDelete(fileToDel);
                controller.disabledFrame(true);
                passwordFrame.dispose();
            }
        });
        passwordFrame.setLocationRelativeTo(null);
        passwordFrame.setVisible(true);
    }

    private void recursiveDelete(File file) {
        controller.delNode(file);
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }

        if (file.delete())
            System.out.println("Удаленный файл/папка: " + file.getAbsolutePath());
        else
            System.out.println("Невозможно удалить файл/папку: " + file.getAbsolutePath());
    }
}
