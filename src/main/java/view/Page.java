package view;

import animatefx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;


public class Page {

    private Page parentPage = null;
    private Page currentShowed = null;
    private List<Page> childrenPage = new ArrayList<>();
    private AnchorPane root = null;
    private String type = null;

    public void setMovable(){
        Stage stage = (Stage) root.getScene().getWindow();
        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset.set(event.getSceneX());
            yOffset.set(event.getSceneY());
        });

        // Move around here
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset.get());
            stage.setY(event.getScreenY() - yOffset.get());
        });
//        root.setStyle("-fx-background-radius: 15");
    }
    public Page(){
        type = getClass().getSimpleName();
//        System.out.println(type);
        FXMLLoader fxmlLoader = new FXMLLoader( getClass().getResource(type+".fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        root = fxmlLoader.getRoot();
//        setMovable();
    }
    public Page(String type) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(type));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        root = fxmlLoader.getRoot();
        this.type=type;
    }

    public AnchorPane getRoot(){
        return root;
    }
    public Page getParent() {
        return parentPage;
    }
    public void setParent(Page page){
        parentPage = page;
    }
    public void setRoot(AnchorPane anchorPane){
        this.root =anchorPane;
    }
    public List<Page> getChildren(){
        return this.childrenPage;
    }

    public void add(Page page){
        childrenPage.add(page);
        this.root.getChildren().add(page.getRoot());
        page.setParent(this);
    }
    public void add(Page page, int x, int y){

        childrenPage.add(page);
        this.root.getChildren().add(page.getRoot());
        page.root.setLayoutX(x);
        page.root.setLayoutY(y);
        page.setParent(this);
        currentShowed = page;
    }

    public void switchToPage(Page page, int x, int y){
        if(currentShowed == page){
            return;
        }
        if(currentShowed != null)
            remove(currentShowed);
        add(page,x,y);
    }
    public void switchToPage(Page page){
        switchToPage(page,0,0);
    }
    public boolean has(Page page) {
        return childrenPage.contains(page);
    }
    public void toFront() {
        root.toFront();
    }
    public void toBack() {
        root.toBack();
    }
    public void show(Page page) {
        page.toFront();
    }
    public void unShow(Page page){
        page.toBack();
    }
    public void remove(Page page){
        childrenPage.remove(page);
        this.root.getChildren().remove(page.getRoot());
        page.setParent(null);
    }
    public void close(){
        if(parentPage!=null) {
            parentPage.remove(this);
        }
        childrenPage.clear();
        parentPage=null;
        root = null;
        type=null;
    }
    public void back(){
        this.getParent().remove(this);
    }
    public void exit(){
        new FadeOut(getRoot().getScene().getRoot()).play();
        Thread thread  = new Thread(
                ()->{
                    try {
                        Thread.sleep(1000);
                        Platform.runLater(
                                ()-> ((Stage)root.getScene().getWindow()).close()
                        );
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
        );
        thread.start();
    }


}
