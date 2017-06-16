package controller;

import model.Model;
import view.Interface;

/**
 * Created by Maria on 15.06.2017.
 */
public class Controller {
    Interface view;
    Model model;
    public Controller(){
        view = new Interface();
        model = new Model();
    }

    public void runProgram(){

    };
}
