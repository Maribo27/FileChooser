package myFileChooser.controller;

import myFileChooser.tree.TreePanel;
import myFileChooser.view.Interface;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * Created by Maria on 15.06.2017.
 */
public class FileController {
    private Interface view;
    private TreePanel model;
    public FileController(){
        model = new TreePanel(this, false);
        view = new Interface(this);
    }

    public void createDialog(int mode){
        if (mode == 0) view.createSaveDialog();
        if (mode == 1) view.createOpenDialog();
    }

    public boolean isPressed(){
        return view.isPressed();
    }

    void setPressed(){
        view.setPressed();
    }

    void closeDialog(){
        view.closeDialog();
    }

    public JPanel getThisTree(){
        return model.getTree();
    }

    public File getFile(){
        return model.getFile();
    }

    public List<File> getFiles(){
        return model.getFiles();
    }

    public void delNode(File file){
        model.delNode(file);
    }

    public void insertNode(String path) {
        model.insertNode(path);
    }

    public void goAway(String fullpath){
        model.goAway(fullpath);
    }

    public void expandAll(){
        model.expandAll();
    }

    public void collapseAll(){
        model.collapseAll();
    }

    public void addPath(String path){
        view.addPath(path);
    }

    public void hideButton(boolean state){
        view.hideButton(state);
    }

    public void hideDelButton(boolean state){
        view.hideDelButton(state);
    }

    public void disabledFrame(boolean dis){
        view.disabledFrame(dis);
    }

    public JPanel updateModel(boolean fileMode){
        model = new TreePanel(this, fileMode);
        return model.getTree();
    }

    public int[] returnSelection(){
        return model.returnSelection();
    }

    public Vector<Integer> returnExpands(){
        return model.returnExpands();
    }

    public void setSelection(int[] rows, Vector<Integer> expands){
        model.setExpands(expands);
        model.setSelection(rows);
    }

    public String getFileName(){
        return view.getFileName();
    }
}
