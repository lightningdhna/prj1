package view;

import animatefx.animation.*;

import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class MainPage extends Page {

    @FXML
    Pane bigTaskBar, smallTaskBar;
    @FXML
    AnchorPane rootPane;
    @FXML
    JFXButton smb1,smb2,smb3;
    @FXML
    JFXButton playButton,evaluateButton,settingButton;
    @FXML
    Label pageInfoLabel;
    JFXButton [] buttons;
    JFXButton smButtons[];
    Page[] pages;
    String [] pageName = {"Play","Evaluate","Setting"};


    public void showBigTaskBar(){
        if(!rootPane.getChildren().contains(bigTaskBar)) {
            rootPane.getChildren().add(bigTaskBar);
            new FadeIn(bigTaskBar).play();
        }
    }
    public void unShowBigTaskBar(){
        rootPane.getChildren().remove(bigTaskBar);
//        bigTaskBar.toBack();
    }
    public void abc(){
        System.out.println("abc");
    }


    public int getSourceIndexButtons(MouseEvent mouseEvent){
        JFXButton button = (JFXButton) mouseEvent.getSource();
        for(int i=0;i<buttons.length;i++){
            if(button==buttons[i]){
                return i;
            }
        }
        for(int i=0;i<smButtons.length;i++){
            if(button==smButtons[i]){
                return i;
            }
        }
        return -1;
    }

    public void showPage(int index){
        if(index<0 || index >= pages.length)
            return;
        Page page = pages[index];
        switchToPage(page, 100,100);
        new FadeIn(page.getRoot()).play();
        bigTaskBar.toFront();
        pageInfoLabel.setText(pageName[index]);
    }

    EventHandler<MouseEvent> buttonEventHandler = mouseEvent -> {
        int index = getSourceIndexButtons(mouseEvent);
        if(mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED){
            if(pages[index]==null){
                System.out.println(pages[index].getClass().toString());
            }
            showPage(index);
        }
        unShowBigTaskBar();
    };

    public MainPage(){
        super();
        rootPane.getChildren().remove(bigTaskBar);
        bigTaskBar.setLayoutX(0);


        PlayPage playPage = new PlayPage();
        SettingPage settingPage = new SettingPage();
        EvaluatePage evaluatePage = new EvaluatePage();

        smButtons= new JFXButton[]{smb1,smb2,smb3};
        buttons = new JFXButton[]{ playButton,evaluateButton,settingButton};
        pages   =  new Page[]    { playPage, evaluatePage,settingPage };

        for(var button : buttons){
            button.addEventHandler(MouseEvent.MOUSE_CLICKED,buttonEventHandler);
        }
        for(var button : smButtons){
            button.addEventHandler(MouseEvent.MOUSE_CLICKED,buttonEventHandler);
        }
    }

}
