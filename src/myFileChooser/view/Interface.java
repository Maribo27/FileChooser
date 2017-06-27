package myFileChooser.view;

import myFileChooser.tree.TreePanel;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.List;


/**
 * Created by Maria on 15.06.2017.
 */
public class Interface {
    private JFrame mainWindow;
    private TreePanel treePanel = new TreePanel();
    private JComboBox<String> pathComboBox;
    private JPanel topPanel;
    private JPanel bottomPanel;
    JPanel newPanel;
    JButton newFolderButton;
    boolean collapse = true;

    public Interface(){
        initMainFrame();
        initPanels();
        mainWindow.setVisible(true);
        mainWindow.setLocationRelativeTo(null);
    }

    private void initMainFrame(){
        mainWindow = new JFrame("Выбор файла");
        mainWindow.setLayout(new BorderLayout());
        mainWindow.setPreferredSize(new Dimension(500,600));
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setBackground(Color.DARK_GRAY);
        mainWindow.pack();
        mainWindow.setResizable(false);
        mainWindow.setEnabled(true);
    }

    private void initPanels(){

        JPanel pathPanel = new JPanel();
        pathComboBox = new JComboBox<>();
        pathComboBox.setPreferredSize(new Dimension(450,25));
        pathComboBox.setBackground(Color.DARK_GRAY);
        pathComboBox.setForeground(Color.DARK_GRAY);
        DefaultListCellRenderer listCellRenderer = new DefaultListCellRenderer();
        listCellRenderer.setBackground(Color.DARK_GRAY);

        pathComboBox.setBackground(Color.DARK_GRAY);

        pathComboBox.addActionListener(e -> treePanel.goAway(pathComboBox.getSelectedItem().toString()));
        pathPanel.setLayout(new FlowLayout());
        pathPanel.add(pathComboBox);
        pathPanel.setBackground(Color.DARK_GRAY);

        topPanel = new JPanel();
        bottomPanel = new JPanel();

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(500,60));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));


        JButton homeButton = new JButton("Дом");
        homeButton.addActionListener(e -> treePanel.goHome());

        JButton desktopButton = new JButton("Cтол");
        desktopButton.addActionListener(e -> treePanel.goDesktop());


        newFolderButton = new JButton("Папка");
        newFolderButton.addActionListener(e -> {
            File selectedFile = treePanel.getFile();
            String folderName = createFolder();
            String path = selectedFile.getAbsolutePath() + "\\" + folderName;
            if (folderName == null || !selectedFile.isDirectory()) return;
            new File(path).mkdir();
            treePanel.insertNode(path);
            System.out.println("Созданная папка: " + path);
        });

        JButton delButton = new JButton("Удалить");
        delButton.addActionListener(e -> {
            List<File> files = treePanel.getFiles();
            if (files == null) return;
            for (File file : files){
                DelFiles del = new DelFiles(treePanel, file, mainWindow);
                del.delFolder();
            }
            mainWindow.repaint();
            mainWindow.revalidate();
        });

        JButton hideButton = new JButton("Скрыть");
        hideButton.addActionListener(e -> {
            if (pathPanel.isVisible()){
                hideButton.setText("Показать");
                topPanel.setPreferredSize(new Dimension(600, 30));
            } else {
                hideButton.setText("Скрыть");
                topPanel.setPreferredSize(new Dimension(600, 60));
            }
            pathPanel.setVisible(!pathPanel.isVisible());
        });

        JButton okButton = new JButton("ОК");
        okButton.setEnabled(false);

        JButton minButton = new JButton("+");
        minButton.addActionListener(e -> {
            if (collapse) hideButton.setText("+");
            else hideButton.setText("-");
            treePanel.expandAll(collapse);
            collapse = !collapse;
        });

        JButton projectButton = new JButton("Проект");
        projectButton.addActionListener(e -> {
            treePanel.goAway(System.getProperty("user.dir"));
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> System.exit(0));

        JButton collapseButton = new JButton("Закрыть");
        collapseButton.addActionListener(e -> treePanel.collapseAll());

        homeButton.setBorderPainted(false);

        buttonPanel.add(homeButton);

        buttonPanel.add(desktopButton);
        buttonPanel.add(newFolderButton);
        buttonPanel.add(delButton);
        buttonPanel.add(collapseButton);
        buttonPanel.add(hideButton);
        buttonPanel.add(minButton);
        buttonPanel.add(projectButton);
        buttonPanel.setBackground(Color.DARK_GRAY);

        topPanel.add(buttonPanel);
        topPanel.add(pathPanel);
        topPanel.setBackground(Color.DARK_GRAY);

        bottomPanel.setPreferredSize(new Dimension(500,50));
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);
        bottomPanel.setBackground(Color.DARK_GRAY);

        mainWindow.add(topPanel, BorderLayout.NORTH);
        mainWindow.add(bottomPanel, BorderLayout.SOUTH);
        newPanel = treePanel.getTree(this);
        mainWindow.add(newPanel, BorderLayout.CENTER);
    }

    private String createFolder(){
        JOptionPane optionPane = new JOptionPane();
        return JOptionPane.showInputDialog(
                optionPane,
                "Введите имя папки",
                "Создание папки",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void hideButton(boolean state){
        newFolderButton.setEnabled(state);
    }

    public void addPath(String path){
        for (int item = 0; item <= pathComboBox.getItemCount(); item++)
            if (path.equals(pathComboBox.getItemAt(item))){
                pathComboBox.setSelectedIndex(item);
                return;
            }
        pathComboBox.addItem(path);
        pathComboBox.setSelectedIndex(pathComboBox.getItemCount() - 1);
    }
}
