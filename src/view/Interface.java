package view;

import tree.DirectoryTreeModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Maria on 15.06.2017.
 */
public class Interface {
    private JFrame mainWindow;
    private JPanel topPanel, mainPanel, bottomPanel;
    private JButton homeButton, desktopButton, newFolderButton, delButton, hideButton, okButton, cancelButton;
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
        mainWindow.pack();
        mainWindow.setResizable(false);
        mainWindow.setEnabled(true);
    }

    private void initPanels(){
        mainPanel = new JPanel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();



        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(500,100));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        homeButton = new JButton("Домой");
        desktopButton = new JButton("На рабочий стол");
        newFolderButton = new JButton("Новая папка");
        delButton = new JButton("Удалить");
        hideButton = new JButton("Скрыть путь");
        okButton = new JButton("ОК");
        cancelButton = new JButton("Отмена");

        mainPanel.setLayout(new BorderLayout());
        DirectoryTreeModel directoryTreeModel = new DirectoryTreeModel();
        JTree tree = new JTree(directoryTreeModel);
        JScrollPane mainPanelScroll = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        mainPanel.add(mainPanelScroll);

        buttonPanel.add(homeButton);
        buttonPanel.add(desktopButton);
        buttonPanel.add(newFolderButton);
        buttonPanel.add(delButton);
        buttonPanel.add(hideButton);

        topPanel.add(buttonPanel);

        bottomPanel.setPreferredSize(new Dimension(500,50));
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);

        mainWindow.add(topPanel, BorderLayout.NORTH);
        mainWindow.add(mainPanelScroll, BorderLayout.CENTER);
        mainWindow.add(bottomPanel, BorderLayout.SOUTH);
    }
}
