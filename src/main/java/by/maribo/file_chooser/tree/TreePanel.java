package by.maribo.file_chooser.tree;

import by.maribo.file_chooser.controller.DragDropListener;
import by.maribo.file_chooser.controller.FileController;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by Maria on 24.06.2017.
 */
public class TreePanel {

    private FileController controller;
    private ArrayList<Directory> allNodes;
    private List <File> selectedFiles = new ArrayList<>();
    private DefaultTreeModel treeModel;
    private JTree tree;
    private File selectFile;
    private boolean fileMode;
    private int delta;

    public TreePanel(FileController controller, boolean fileMode) {
        this.controller = controller;
        this.fileMode = fileMode;
    }

    public JPanel getTree(){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        allNodes = new ArrayList<>();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Мой компьютер", true);

        for (File f: File.listRoots()) {
	        if (f.isDirectory()){
	            DefaultMutableTreeNode node = new DefaultMutableTreeNode(f, true);
	            root.add(node);
	            Directory newDirectoryNode = new Directory(node, f, controller);
	            allNodes.add(newDirectoryNode);
	        }
        }

        treeModel = new DefaultTreeModel(root, true);
        tree = new JTree(treeModel);

        tree.setRootVisible(false);
        tree.setCellRenderer(new CellRenderer());

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)event.getPath().getLastPathComponent();
                for (Directory node : allNodes) {
	                if (node.getNode().toString().equalsIgnoreCase(selectedNode.toString())) {
	                    node.showChildren(allNodes, treeModel, fileMode);
	                    break;
	                }
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
	            int check = selectedNode.getLevel();
	            boolean selected = !tempCheck2.toString().equals(selectedNode.toString());
	            if (selected) {
	                continue;
                }
	            select = isSelect(select, tempCheck2, selectedNode, check);
	            boolean fileSelected = select && (selectFile == null || !(selectFile.getAbsolutePath().equals(node.getFile().getAbsolutePath())));
	            if (fileSelected){
                    selectFile = node.getFile();
                    System.out.println("Выбран файл/папка: " + selectFile.getAbsolutePath());
                    controller.addPath(selectFile.getAbsolutePath());
                    break;
                }
            }
            if (tree.isSelectionEmpty()) {
                controller.hideDelButton(false);
            } else {
                controller.hideDelButton(true);
            }

	        boolean fileSelected = tree.isSelectionEmpty() || selectFile.isFile() || Objects.requireNonNull(tree.getSelectionRows()).length != 1;
	        if (fileSelected) {
                controller.hideButton(false);
            } else {
                controller.hideButton(true);
            }
        });

        JScrollPane mainPanelScroll = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tree.setBackground(Color.DARK_GRAY);
        mainPanel.add(mainPanelScroll);

        DragDropListener myDragDropListener = new DragDropListener(controller);
        new DropTarget(mainPanel, myDragDropListener);
        return mainPanel;
    }

	public File getFile(){
        return selectFile;
    }

    public List<File> getFiles(){
        TreePath[] treePaths;
        if (!tree.isSelectionEmpty()) {
	        treePaths = tree.getSelectionPaths();
        } else {
	        return null;
        }

        if (!selectedFiles.isEmpty()) {
	        selectedFiles.clear();
        }

        for (TreePath treePath : Objects.requireNonNull(treePaths)) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
	        for (Directory node : allNodes) {
                DefaultMutableTreeNode tempCheck2 = node.getNode();
	            int check = selectedNode.getLevel();
                if (!tempCheck2.toString().equals(selectedNode.toString())) {
	                continue;
                }
		        boolean select = isSelect(false, tempCheck2, selectedNode, check);
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
            if (Objects.equals(file.getAbsolutePath(), node.getFile().getAbsolutePath())){
                node.getNode().removeFromParent();
                allNodes.remove(node);
                break;
            }
        }
        tree.updateUI();
    }

    public void insertNode(String path) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
        for (Directory node : allNodes) {
	        if (node.getNode().toString().equalsIgnoreCase(selectedNode.toString())) {
	            node.addNode(allNodes, path, selectedNode, treeModel);
	            break;
	        }
        }
    }

    public void goAway(String fullPath){
	    boolean fileSelected = selectFile != null && selectFile.getAbsolutePath().equals(fullPath);
	    if (fileSelected) {
	        return;
        }
        String fileName = "";
        for (int charNumber = 0; charNumber < fullPath.length(); charNumber++){
            if (fullPath.charAt(charNumber) != '\\') {
	            fileName += fullPath.charAt(charNumber);
            } else {
                String cutPath = fullPath.substring(charNumber + 1, fullPath.length());
                expand(fileName + "\\", cutPath);
                break;
            }
        }
    }

    private void expand(String fileName, String path){
        if (fileName.equals("")) {
            return;
        }
        for (int count = 0; count < tree.getRowCount(); count++) {
            if (tree.getPathForRow(count).getLastPathComponent().toString().equals(fileName)) {
                tree.setSelectionRow(count);
                tree.expandRow(count);
                break;
            }
        }
        String cutPath;
        fileName = "";
        for (int charNumber = 0; charNumber < path.length(); charNumber++){
            if (path.charAt(charNumber) != '\\') {
	            fileName += path.charAt(charNumber);
            } else {
                cutPath = path.substring(charNumber + 1, path.length());
                expand(fileName, cutPath);
                return;
            }
        }
        expand(fileName, "");
    }

    public void expandAll(){
        int currentRow;
        if (!tree.isSelectionEmpty()) {
	        currentRow = Objects.requireNonNull(tree.getSelectionRows())[0];
        } else {
	        return;
        }


        if (!tree.isExpanded(currentRow)){
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
            for (int iterator = delta + currentRow; iterator >= currentRow; iterator--) {
	            tree.collapseRow(iterator);
            }
        }
    }

    public void collapseAll(){
        for (int iterator = tree.getRowCount() - 1; iterator >= 0; iterator--)
            tree.collapseRow(iterator);
    }

    public int[] returnSelection(){
        return tree.getSelectionRows();
    }

    public Vector<Integer>  returnExpands(){
        Vector<Integer> expandedRows = new Vector<>();
        for (int iterator = 0; iterator < tree.getRowCount(); iterator++)
            if (tree.isExpanded(iterator)) expandedRows.add(iterator);
        return expandedRows;
    }

    public void setExpands(Vector<Integer> expandedRows){
        for (int iterator = 0; iterator < expandedRows.size(); iterator++)
            tree.expandRow(expandedRows.elementAt(iterator));
    }

    public void setSelection(int[] selection){
        tree.setSelectionRows(selection);
    }

	private boolean isSelect(boolean select, DefaultMutableTreeNode tempCheck2, DefaultMutableTreeNode tempCheck1, int check) {
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
		return select;
	}
}
