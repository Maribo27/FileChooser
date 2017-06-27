package view;

import tree.DirectoryTreeModel;
import tree.MyCellRenderer;
import tree.NodeDirectoryTree;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;


/**
 * Created by Maria on 15.06.2017.
 */
public class Interface {
    private JFrame mainWindow;
    private JPanel topPanel, mainPanel, bottomPanel;
    private JButton homeButton, desktopButton, newFolderButton, delButton, hideButton, okButton, cancelButton, updateButton;
    private JComboBox<String> pathComboBox;
    JTree tree;
    boolean passCheck = true;
    JTextField pathField;
    NodeDirectoryTree node;
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
        pathPanel.setLayout(new FlowLayout());
        pathPanel.add(pathComboBox);
        pathPanel.setBackground(Color.DARK_GRAY);

        mainPanel = new JPanel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(500,60));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        mainPanel.setLayout(new BorderLayout());

        DirectoryTreeModel directoryTreeModel = new DirectoryTreeModel();
        tree = new JTree(directoryTreeModel);
        tree.setBackground(Color.DARK_GRAY);
        tree.setRootVisible(false);


        tree.addTreeSelectionListener(e -> {
            node = (NodeDirectoryTree)e.getPath().getLastPathComponent();
            System.out.println(node.getFullPath());
            tree.getSelectionPaths();
        });
        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                node = (NodeDirectoryTree)event.getPath().getLastPathComponent();
                pathComboBox.addItem(node.getFullPath());
                pathComboBox.setSelectedIndex(pathComboBox.getItemCount() - 1);
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {

            }
        });

        tree.setCellRenderer(new MyCellRenderer());

        homeButton = new JButton("Дом");
        homeButton.setEnabled(false);

        desktopButton = new JButton("Cтол");
        desktopButton.setEnabled(false);

        newFolderButton = new JButton("Папка");
        newFolderButton.addActionListener((ActionEvent e) -> {
            String path = pathComboBox.getItemAt(0);
            String folderName = createFolder();
            if (folderName == null) return;
            new File(path + folderName).mkdir();
            System.out.println("Созданная папка: " + path + folderName);
        });

        delButton = new JButton("Удалить");
        delButton.addActionListener(e -> {
            DelFiles del = new DelFiles(mainWindow, node);
            del.delFolder();
        });

        hideButton = new JButton("Скрыть");
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

        okButton = new JButton("ОК");
        okButton.setEnabled(false);

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> System.exit(0));

        updateButton = new JButton("Обновить");
        updateButton.setEnabled(false);

        JScrollPane mainPanelScroll = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanelScroll.setBackground(Color.DARK_GRAY);

        mainPanelScroll.getVerticalScrollBar().setOpaque(true);
        mainPanel.add(mainPanelScroll);
        mainPanel.setBackground(Color.DARK_GRAY);

        homeButton.setBorderPainted(false);

        buttonPanel.add(homeButton);

        buttonPanel.add(desktopButton);
        buttonPanel.add(newFolderButton);
        buttonPanel.add(delButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(hideButton);
        buttonPanel.setBackground(Color.DARK_GRAY);

        topPanel.add(buttonPanel);
        topPanel.add(pathPanel);
        topPanel.setBackground(Color.DARK_GRAY);

        bottomPanel.setPreferredSize(new Dimension(500,50));
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);
        bottomPanel.setBackground(Color.DARK_GRAY);

        mainWindow.add(topPanel, BorderLayout.NORTH);
        mainWindow.add(mainPanelScroll, BorderLayout.CENTER);
        mainWindow.add(bottomPanel, BorderLayout.SOUTH);
    }

    private String createFolder(){
        JOptionPane optionPane = new JOptionPane();
        return JOptionPane.showInputDialog(
                optionPane,
                "Введите имя папки",
                "Создание папки",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
