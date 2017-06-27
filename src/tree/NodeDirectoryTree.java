package tree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maria on 17.06.2017.
 */
public class NodeDirectoryTree {
    private String fullPath;
    private List<NodeDirectoryTree> sonsList;
    private boolean sonsIsCreate;

    NodeDirectoryTree(String fullPath){
        this.fullPath = fullPath;
    }

    public String getFullPath(){
        return fullPath;
    }

    public String toString(){
        String name = "";

        int n = fullPath.length();
        for (int i = n-1; i >=0; i--) {
            if (fullPath.charAt(i) == '/' && i!=n-1){
                break;
            }
            name = fullPath.charAt(i) + name;
        }
        return name;
    }

    Object getChild(int index) {
        if (!sonsIsCreate){
            createSonsList();
        }
        return sonsList.get(index);
    }

    int getChildCount() {
        if (!sonsIsCreate){
            createSonsList();
        }
        return sonsList.size();
    }

    boolean isLeaf(){
        File f = new File(fullPath);
        return f.isFile();
    }

    int getIndexOfChild(Object node){
        if (!sonsIsCreate){
            createSonsList();
        }
        NodeDirectoryTree son = (NodeDirectoryTree)node;
        return sonsList.indexOf(son);
    }

    private void createSonsList(){
        sonsIsCreate = true;
        sonsList = new ArrayList<>();
        List<NodeDirectoryTree> fileList = new ArrayList<>();
        File[] listOfFiles;
        if (fullPath.equals("/home")){
            listOfFiles = File.listRoots();
        } else{
            File folder = new File(fullPath);
            listOfFiles = folder.listFiles();
        }

        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isDirectory()) {
                if (fullPath.equals("/home")) {
                    String name = "";
                    String path = listOfFile.getAbsolutePath();
                    int m = path.length();
                    for (int j = 0; j < m - 1; j++) {
                        name = name + path.charAt(j);
                    }
                    sonsList.add(new NodeDirectoryTree("/" + name + "/"));
                } else {
                    sonsList.add(new NodeDirectoryTree(fullPath + "/" + listOfFile.getName()));
                }
            } else if (listOfFile.isFile())
            {
                if (fullPath.equals("/home")) {
                    String name = "";
                    String path = listOfFile.getAbsolutePath();
                    int m = path.length();
                    for (int j = 0; j < m - 1; j++) {
                        name = name + path.charAt(j);
                    }
                    fileList.add(new NodeDirectoryTree("/" + name + "/"));
                } else {
                    fileList.add(new NodeDirectoryTree(fullPath + "/" + listOfFile.getName()));
                }
            }
        }
        for (NodeDirectoryTree tempNode: fileList) {
            sonsList.add(tempNode);
        }
    }
}
