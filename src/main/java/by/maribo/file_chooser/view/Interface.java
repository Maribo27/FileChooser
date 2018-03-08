package by.maribo.file_chooser.view;

import by.maribo.file_chooser.controller.CancelListener;
import by.maribo.file_chooser.controller.EnterListener;
import by.maribo.file_chooser.controller.FileController;
import by.maribo.file_chooser.controller.SaveListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Vector;


/**
 * Created by Maria on 15.06.2017.
 */
public class Interface {
	private final static String DESKTOP = "\\Desktop";
	private static final String DISK = "^\\D:\\$";
	private static final String USER_HOME = "user.home";

	private FileController controller;
    private JDialog mainWindow;
    private JComboBox<String> pathComboBox;
    private JPanel topPanel;
	private JPanel treePanel;
    private JTextField textField;
    private JButton okButton;
    private JButton newFolderButton;
    private JButton delButton;

    private boolean fileMode = false;
    private boolean press = false;

    private int mode;

    public Interface(FileController controller){
        this.controller = controller;
    }

    public void createSaveDialog(){
        mode = 0;
        press = false;
        initMainFrame(new JFrame(), "Сохранить файл как:");
        initPanels();
        initBottomPanel(mode);
        mainWindow.setVisible(true);
    }

    public void createOpenDialog(){
        press = false;
        initMainFrame(new JFrame(), "Открыть файл:");
        initPanels();
        mode = 1;
        initBottomPanel(mode);
        mainWindow.setVisible(true);
    }

    private void initMainFrame(JFrame frame, String mode){
        mainWindow = new JDialog(frame, mode, true);
        mainWindow.setLayout(new BorderLayout());
        mainWindow.setSize(new Dimension(500,600));
        mainWindow.setBackground(Color.DARK_GRAY);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setResizable(false);
    }

    private void initPanels(){

        JPanel pathPanel = new JPanel();

        pathComboBox = new JComboBox<>();
        pathComboBox.setPreferredSize(new Dimension(450,25));
        pathComboBox.setBackground(Color.DARK_GRAY);
        pathComboBox.addActionListener(e -> {
            String fullPath = Objects.requireNonNull(pathComboBox.getSelectedItem()).toString();
            controller.goAway(fullPath);
        });

        pathPanel.setLayout(new FlowLayout());
        pathPanel.add(pathComboBox);
        pathPanel.setBackground(Color.DARK_GRAY);

        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(600,70));

        JToolBar buttonPanel = new JToolBar("Панель быстрого доступа", JToolBar.HORIZONTAL);
        buttonPanel.setFloatable(false);
        buttonPanel.setOpaque(false);

        JButton homeButton = new JButton(getIcon("home.png"));
        homeButton.setToolTipText("Домашняя папка");
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.addActionListener(e -> controller.goAway(System.getProperty(USER_HOME)));

        JButton desktopButton = new JButton(getIcon("desktop.png"));
        desktopButton.setToolTipText("Рабочий стол");
        desktopButton.setBorderPainted(false);
        desktopButton.setFocusPainted(false);
        desktopButton.setContentAreaFilled(false);
        desktopButton.addActionListener(e -> controller.goAway(System.getProperty(USER_HOME) + DESKTOP));

        newFolderButton = new JButton(getIcon("createFolder.png"));
        newFolderButton.setEnabled(false);
        newFolderButton.setToolTipText("Создать папку");
        newFolderButton.setBorderPainted(false);
        newFolderButton.setFocusPainted(false);
        newFolderButton.setContentAreaFilled(false);
        newFolderButton.addActionListener(e -> {
            File selectedFile = controller.getFile();
            String folderName = createFolder();
            String path;
	        boolean disk = selectedFile.getAbsolutePath().matches(DISK);
	        if (disk) {
	            path = selectedFile.getAbsolutePath() + folderName;
            } else {
	            path = selectedFile.getAbsolutePath() + "\\" + folderName;
            }

            if (folderName == null || !selectedFile.isDirectory()) {
	            return;
            }
            if (new File(path).exists()) {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Такая папка уже существует", "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            new File(path).mkdir();
            controller.insertNode(path);
            System.out.println("Создана папка: " + path);
        });

        delButton = new JButton(getIcon("delete.png"));
        delButton.setToolTipText("Удалить");
        newFolderButton.setEnabled(false);
        delButton.setBorderPainted(false);
        delButton.setFocusPainted(false);
        delButton.setContentAreaFilled(false);
        delButton.addActionListener(e -> {
            List<File> files = controller.getFiles();
            if (files == null) return;
            for (File file : files){
                DelFiles del = new DelFiles(controller, file);
                del.delFolder();
            }
            mainWindow.repaint();
            mainWindow.revalidate();
        });

        JButton hidePathButton = new JButton(getIcon("hiddenDir.png"));
        hidePathButton.setToolTipText("Скрыть/показать путь");
        hidePathButton.setBorderPainted(false);
        hidePathButton.setFocusPainted(false);
        hidePathButton.setContentAreaFilled(false);
        hidePathButton.addActionListener(e -> {
            if (pathPanel.isVisible()){
                topPanel.setPreferredSize(new Dimension(600, 35));
                topPanel.setSize(new Dimension(600, 35));
                topPanel.setMaximumSize(new Dimension(600, 35));
            } else {
                topPanel.setPreferredSize(new Dimension(600, 70));
                topPanel.setSize(new Dimension(600, 70));
                topPanel.setMaximumSize(new Dimension(600, 70));
            }
            pathPanel.setVisible(!pathPanel.isVisible());
            mainWindow.repaint();
            mainWindow.revalidate();
        });

        JButton okButton = new JButton("ОК");
        okButton.setEnabled(false);

        JButton minButton = new JButton(getIcon("minimize.png"));
        minButton.setToolTipText("Закрыть/открыть папки");
        minButton.setBorderPainted(false);
        minButton.setFocusPainted(false);
        minButton.setContentAreaFilled(false);
        minButton.addActionListener(e -> controller.expandAll());

        JButton projectButton = new JButton(getIcon("project.png"));
        projectButton.setToolTipText("Расположение проекта");
        projectButton.setBorderPainted(false);
        projectButton.setFocusPainted(false);
        projectButton.setContentAreaFilled(false);
        projectButton.addActionListener(e -> controller.goAway(System.getProperty("user.dir")));

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> System.exit(0));

        JButton collapseButton = new JButton(getIcon("close.png"));
        collapseButton.setToolTipText("Закрыть все ветви");
        collapseButton.setBorderPainted(false);
        collapseButton.setFocusPainted(false);
        collapseButton.setContentAreaFilled(false);
        collapseButton.addActionListener(e -> controller.collapseAll());

        JButton updateButton = new JButton(getIcon("update.png"));
        updateButton.setToolTipText("Обновить");
        updateButton.setBorderPainted(false);
        updateButton.setFocusPainted(false);
        updateButton.setContentAreaFilled(false);
        updateButton.addActionListener(e -> synchronize(fileMode));

        JButton hideButton = new JButton(getIcon("hideFiles.png"));
        hideButton.setToolTipText("Показать/скрыть скрытые файлы");
        hideButton.setBorderPainted(false);
        hideButton.setFocusPainted(false);
        hideButton.setContentAreaFilled(false);
        hideButton.addActionListener(e -> {
            fileMode = !fileMode;
            synchronize(fileMode);
        });

        buttonPanel.add(homeButton);
        buttonPanel.add(desktopButton);
        buttonPanel.add(projectButton);
        buttonPanel.addSeparator();
        buttonPanel.add(newFolderButton);
        buttonPanel.add(delButton);
        buttonPanel.addSeparator();
        buttonPanel.add(collapseButton);
        buttonPanel.add(minButton);
        buttonPanel.addSeparator();
        buttonPanel.add(updateButton);
        buttonPanel.add(hideButton);
        buttonPanel.add(hidePathButton);
        buttonPanel.setBackground(Color.DARK_GRAY);

        topPanel.add(buttonPanel);
        topPanel.add(pathPanel);
        topPanel.setBackground(Color.DARK_GRAY);

        mainWindow.add(topPanel, BorderLayout.NORTH);
        treePanel = controller.getThisTree();
        mainWindow.add(treePanel, BorderLayout.CENTER);
    }

    private void initBottomPanel(int mode){

        topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        okButton = new JButton("ОК");
        okButton.setEnabled(false);

        textField = new JTextField(20);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new CancelListener(controller));

        bottomPanel.setPreferredSize(new Dimension(500,50));
        if (mode == 0) {
            bottomPanel.add(textField);
            okButton.addActionListener(new SaveListener(controller));
        }
        else okButton.addActionListener(new EnterListener(controller));

        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);
        bottomPanel.setBackground(Color.DARK_GRAY);

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

    public void hideButton(boolean state){
        newFolderButton.setEnabled(state);
        if (mode == 1) state = !state;
        okButton.setEnabled(state);
    }

    public void hideDelButton(boolean state){
        delButton.setEnabled(state);
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

    private void synchronize(boolean fileMode){
        mainWindow.remove(treePanel);
        int[] treeSelectionModel = controller.returnSelection();
        Vector<Integer> expandedRows = controller.returnExpands();
        treePanel = controller.updateModel(fileMode);
        controller.setSelection(treeSelectionModel, expandedRows);
        mainWindow.add(treePanel, BorderLayout.CENTER);
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    public boolean isPressed(){
        return press;
    }

    public void setPressed(){
        press = true;
    }

    public void closeDialog(){
        mainWindow.dispose();
        mainWindow = null;
    }

    public String getFileName(){
        if (!textField.getText().contains(".xml")) {
	        textField.setText(textField.getText() + ".xml");
        }

        for (File f: File.listRoots()) {
	        boolean directory = f.isDirectory() || f.getAbsolutePath().equals(controller.getFile().getAbsolutePath());
	        if (directory) {
		        return controller.getFile().getAbsolutePath() + textField.getText();
	        }
        }
        return controller.getFile().getAbsolutePath() + "\\" + textField.getText();
    }

    public void disabledFrame(boolean dis){
        mainWindow.setEnabled(dis);
        mainWindow.setFocusable(dis);
    }

	private ImageIcon getIcon(String fileName) {
		String folder = "pictures/";
		String path = Objects.requireNonNull(getClass().getClassLoader().getResource(folder + fileName)).getPath();
        path = path.replaceAll("%5b", "[");
        path = path.replaceAll("%5d", "]");
        path = path.replaceAll("%20", " ");
        return new ImageIcon(path);
	}
}