package myFileChooser.tree;

import myFileChooser.view.Interface;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by Maria on 24.06.2017.
 */
public class TreePanel {

    private JPanel mainPanel;
    private ArrayList<Directory> allNodes;
    DefaultMutableTreeNode root;
    DefaultTreeModel treeModel;
    JTree tree;
    File selectFile;
    Interface view;
    DefaultMutableTreeNode homeRoot;
    int homeIndex;
    int delta;
    List <File> selectedFiles = new ArrayList<>();
    TreePath[] treePaths;

    public JPanel getTree(Interface view){
        this.view = view;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        allNodes = new ArrayList<>();
        root = new DefaultMutableTreeNode("Мой компьютер", true);

        int counter = 0;
        for (File f: File.listRoots())
            if (f.isDirectory()){
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(f, true);
                if (node.toString().equals("C:\\")) {
                    homeRoot = node;
                    homeIndex = counter;
                }
                counter++;
                root.add(node);
                Directory newDirectoryNode = new Directory(node, f);
                allNodes.add(newDirectoryNode);
            }

        treeModel = new DefaultTreeModel(root, true);
        tree = new JTree(treeModel);

        tree.setRootVisible(false);
        tree.setCellRenderer(new tree.MyCellRenderer());

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)event.getPath().getLastPathComponent();
                for (Directory node : allNodes)
                    if (node.getNode().toString().equalsIgnoreCase(selectedNode.toString())) {
                    node.showChildrens(allNodes, treeModel);
                        break;
                    }
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
            }
        });

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
            boolean select = false;
            for (Directory node : allNodes) {
                DefaultMutableTreeNode tempCheck2 = node.getNode();
                DefaultMutableTreeNode tempCheck1 = selectedNode;
                int check = selectedNode.getLevel();
                if (!tempCheck2.toString().equals(tempCheck1.toString())) continue;
                for (; check > 0; check--){
                    tempCheck1 = (DefaultMutableTreeNode)tempCheck1.getParent();
                    tempCheck2 = (DefaultMutableTreeNode)tempCheck2.getParent();
                    if (tempCheck2.toString().equals(tempCheck1.toString())) {
                        select = true;
                    } else {
                        select = false;
                        break;
                    }
                }
                if (select && (selectFile == null || !(selectFile.getAbsolutePath().equals(node.getFile().getAbsolutePath())))){
                    selectFile = node.getFile();
                    System.out.println("Выбран файл/папка: " + selectFile.getAbsolutePath());
                    view.addPath(selectFile.getAbsolutePath());
                    break;
                }
            }
            if (selectFile.isFile()) view.hideButton(false);
            else view.hideButton(true);
        });

        JScrollPane mainPanelScroll = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tree.setBackground(Color.DARK_GRAY);
        mainPanel.add(mainPanelScroll);
        return mainPanel;
    }

    public File getFile(){
        return selectFile;
    }

    public List<File> getFiles(){
        if (!tree.isSelectionEmpty()) treePaths = tree.getSelectionPaths();
        else return null;
        if (!selectedFiles.isEmpty()) selectedFiles.clear();
        for(TreePath treePath : treePaths) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            boolean select = false;
            for (Directory node : allNodes) {
                DefaultMutableTreeNode tempCheck2 = node.getNode();
                DefaultMutableTreeNode tempCheck1 = selectedNode;
                int check = selectedNode.getLevel();
                if (!tempCheck2.toString().equals(tempCheck1.toString())) continue;
                for (; check > 0; check--){
                    tempCheck1 = (DefaultMutableTreeNode)tempCheck1.getParent();
                    tempCheck2 = (DefaultMutableTreeNode)tempCheck2.getParent();
                    if (tempCheck2.toString().equals(tempCheck1.toString())) {
                        select = true;
                    } else {
                        select = false;
                        break;
                    }
                }
                if (select){
                    selectedFiles.add(node.getFile());
                    break;
                }
            }
        }
        return selectedFiles;
    }

    public void delNode(File file){
        for (Directory node : allNodes) {
            if (file.getAbsolutePath() == node.getFile().getAbsolutePath()){
                node.getNode().removeFromParent();
                allNodes.remove(node);
                break;
            }
        }
        tree.updateUI();
    }

    public void insertNode(String path) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
        for (Directory node : allNodes)
            if (node.getNode().toString().equalsIgnoreCase(selectedNode.toString())) {
                node.addNode(allNodes, path, selectedNode, treeModel);
                break;
            }
    }

    public void goHome(){
        tree.clearSelection();
        //for(int row = tree.getRowCount() - 1; row >= 0; row--)
        //    tree.collapseRow(row);
        tree.expandRow(homeIndex);
        for (int count = 0; count < homeRoot.getChildCount(); count++){
            DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) homeRoot.getChildAt(count);
            if (tempNode.toString().equals("Users"))
            {
                tree.expandRow(count + homeIndex + 1);
                for (int temp = 0; temp < tempNode.getChildCount(); temp++){
                    if (tempNode.getChildAt(temp).toString().equals("bogum_000"))
                    {
                        tree.expandRow(temp + count + homeIndex + 2);
                        tree.setSelectionRow(temp + count + homeIndex + 2);
                        break;
                    }
                }
                break;
            }
        }
    }

    public void goDesktop(){
        //for(int row = tree.getRowCount() - 1; row >= 0; row--)
        //    tree.collapseRow(row);
        tree.clearSelection();
        tree.expandRow(homeIndex);
        for (int countUsr = 0; countUsr < homeRoot.getChildCount(); countUsr++){
            DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) homeRoot.getChildAt(countUsr);
            if (tempNode.toString().equals("Users"))
            {
                tree.expandRow(countUsr + homeIndex + 1);
                for (int countBgm = 0; countBgm < tempNode.getChildCount(); countBgm++){
                    if (tempNode.getChildAt(countBgm).toString().equals("bogum_000"))
                    {
                        DefaultMutableTreeNode deskNode = (DefaultMutableTreeNode) tempNode.getChildAt(countBgm);
                        tree.expandRow(countBgm + countUsr + homeIndex + 2);
                        for (int countDesk = 0; countDesk < deskNode.getChildCount(); countDesk++){
                            if (deskNode.getChildAt(countDesk).toString().equals("Desktop"))
                            {
                                tree.expandRow(countDesk + countBgm + countUsr + homeIndex + 3);
                                tree.setSelectionRow(countDesk + countBgm + countUsr + homeIndex + 3);
                                break;
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    public void goAway(String fullpath){

        if (selectFile != null && selectFile.getAbsolutePath().equals(fullpath)) return;
        String fileName = "";
        for (int charNumber = 0; charNumber < fullpath.length(); charNumber++){
            if (fullpath.charAt(charNumber) != '\\') fileName += fullpath.charAt(charNumber);
            else{
                String cutPath = fullpath.substring(charNumber + 1, fullpath.length());
                for(int row = tree.getRowCount() - 1; row >= 0; row--)
                    tree.collapseRow(row);
                expand(root, fileName + "\\", cutPath, 0);
                break;
            }
        }

    }

    void expand(DefaultMutableTreeNode node, String fileName, String path, int row){
        if (fileName.equals("")) {
            tree.setSelectionRow(row - 1);
            return;
        }
        for (int count = 0; count < node.getChildCount(); count++)
            if (node.getChildAt(count).toString().equals(fileName)) {
                tree.expandRow(count + row);
                row += count;
                node = (DefaultMutableTreeNode) node.getChildAt(count);
                break;
            }

        String cutPath;
        fileName = "";
        for (int charNumber = 0; charNumber < path.length(); charNumber++){
            if (path.charAt(charNumber) != '\\') fileName += path.charAt(charNumber);
            else{
                cutPath = path.substring(charNumber + 1, path.length());
                expand(node, fileName, cutPath, row + 1);
                return;
            }
        }
        expand(node, fileName, "", row + 1);
    }

    public void expandAll(boolean mode){
        int currentRow;
        if (!tree.isSelectionEmpty()) currentRow = tree.getSelectionRows()[0];
        else return;


        if (mode){
            int check = 0;
            int countOfRows = tree.getRowCount();
            delta = tree.getRowCount() - countOfRows;

            while (check <= delta) {
                tree.expandRow(currentRow + check);
                check++;
                delta = tree.getRowCount() - countOfRows;
            }
        }
        else {
            for (int iterator = delta + currentRow; iterator >= currentRow; iterator--)
                tree.collapseRow(iterator);
        }
    }
    public void collapseAll(){
        for (int iterator = tree.getRowCount() - 1; iterator > 0; iterator--)
                tree.collapseRow(iterator);
    }
}
