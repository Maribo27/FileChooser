package myFileChooser.tree;

import myFileChooser.controller.FileController;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Maria on 20.06.2017.
 */
class Directory {
    private DefaultMutableTreeNode defaultMutableTreeNode;
    private File file;
    private FileController controller;

    Directory(DefaultMutableTreeNode defaultMutableTreeNode, File file, FileController controller){
        this.controller = controller;
        this.defaultMutableTreeNode = defaultMutableTreeNode;
        this.file = file;
    }

    void showChildrens(ArrayList<Directory> allNodes, DefaultTreeModel model, boolean fileMode){
        if (file == null) return;
        for (File f : file.listFiles()) {
            boolean directory = f.isDirectory() && f.listFiles() != null && (!f.isHidden() || (f.isHidden() && fileMode)) && !isExist(allNodes, f);
            if (directory){
                buildDirNode(allNodes, model, f, f.getName(), true, controller, defaultMutableTreeNode, defaultMutableTreeNode.getChildCount());
            }
        }
        for (File f : file.listFiles()) {
            boolean file = (!f.isHidden() || (f.isHidden() && fileMode)) && !isExist(allNodes, f) && (f.isFile() || (f.isDirectory() && f.listFiles() == null));
            if (file){
                buildDirNode(allNodes, model, f, f.getName(), false, controller, defaultMutableTreeNode, defaultMutableTreeNode.getChildCount());
            }
        }
    }

    private void buildDirNode(ArrayList<Directory> allNodes, DefaultTreeModel model, File f, String name, boolean allowsChildren, FileController controller, DefaultMutableTreeNode defaultMutableTreeNode, int childCount) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name, allowsChildren);
        Directory newDirectoryNode = new Directory(newNode, f, controller);
        allNodes.add(newDirectoryNode);
        model.insertNodeInto(newNode, defaultMutableTreeNode, childCount);
    }

    DefaultMutableTreeNode getNode() {
        return defaultMutableTreeNode;
    }

    File getFile(){
        return file;
    }

    private boolean isExist(ArrayList<Directory> allNodes, File file){
        for (Directory directory : allNodes){
            if (directory.getFile() != null && directory.getFile().equals(file))
                return true;
        }
        return false;
    }

    void addNode(ArrayList<Directory> allNodes, String path, DefaultMutableTreeNode parent, DefaultTreeModel model){
        File file = new File(path);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file.getName(), true);
        Directory newDirectoryNode = new Directory(newNode, file, controller);
        allNodes.add(newDirectoryNode);
        boolean insert = false;
        for (int nodeCount = 0; nodeCount < parent.getChildCount() - 1; nodeCount++){
            String prevNode = parent.getChildAt(nodeCount).toString();
            String nextNode = parent.getChildAt(nodeCount + 1).toString();
            if (file.getName().compareToIgnoreCase(prevNode) > 0 && file.getName().compareToIgnoreCase(nextNode) < 0){
                model.insertNodeInto(newNode, parent, nodeCount + 1);
                insert = true;
                break;
            }
        }
        if (!insert) model.insertNodeInto(newNode, parent, parent.getChildCount());
    }
}
