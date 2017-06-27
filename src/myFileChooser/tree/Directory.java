package myFileChooser.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Maria on 20.06.2017.
 */
public class Directory {
    private DefaultMutableTreeNode defaultMutableTreeNode;
    private File file;

    public Directory(DefaultMutableTreeNode defaultMutableTreeNode, File file){
        this.defaultMutableTreeNode = defaultMutableTreeNode;
        this.file = file;
    }

    public void showChildrens(ArrayList<Directory> allNodes, DefaultTreeModel model){
        if (file == null) return;
        for (File f : file.listFiles())
            if (f.isDirectory() && f.listFiles() != null && !f.isHidden() && !isExist(allNodes, f)){
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(f.getName(), true);
                Directory newDirectoryNode = new Directory(newNode, f);
                allNodes.add(newDirectoryNode);
                model.insertNodeInto(newNode, defaultMutableTreeNode, defaultMutableTreeNode.getChildCount());
            }
        for (File f : file.listFiles())
            if (!f.isHidden() && !isExist(allNodes, f) && (f.isFile() || (f.isDirectory() && f.listFiles() == null))){
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(f.getName(), false);
                Directory newDirectoryNode = new Directory(newNode, f);
                allNodes.add(newDirectoryNode);
                model.insertNodeInto(newNode, defaultMutableTreeNode, defaultMutableTreeNode.getChildCount());
            }
    }

    public DefaultMutableTreeNode getNode() {
        return defaultMutableTreeNode;
    }

    public File getFile(){
        return file;
    }

    public String getFullPath(){
        return file.getAbsolutePath();
    }


    public boolean isExist(ArrayList<Directory> allNodes, File file){
        for (Directory directory : allNodes){
            if (directory.getFile() != null && directory.getFile().equals(file))
                return true;
        }
        return false;
    }

    public void addNode(ArrayList<Directory> allNodes, String path, DefaultMutableTreeNode parent, DefaultTreeModel model){
        File file = new File(path);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file.getName(), true);
        Directory newDirectoryNode = new Directory(newNode, file);
        allNodes.add(newDirectoryNode);
        boolean insert = false;
        for (int nodeCount = 0; nodeCount < parent.getChildCount() - 1; nodeCount++){
            String prevNode = parent.getChildAt(nodeCount).toString();
            String nextNode = parent.getChildAt(nodeCount + 1).toString();
            int t = file.getName().compareToIgnoreCase(prevNode);
            int lol = file.getName().compareToIgnoreCase(nextNode);
            if (file.getName().compareToIgnoreCase(prevNode) > 0 && file.getName().compareToIgnoreCase(nextNode) < 0){
                model.insertNodeInto(newNode, parent, nodeCount + 1);
                insert = true;
                break;
            }
        }
        if (!insert) model.insertNodeInto(newNode, parent, parent.getChildCount());
    }
}
