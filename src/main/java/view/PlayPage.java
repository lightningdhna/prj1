package view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Game;
import model.Point;

public class PlayPage extends Page{
    @FXML
    Label gameStateLabel;
    @FXML
    AnchorPane board;

    int rowNum = 30;
    int colNum = 50;
    Button[][] cells = new Button[rowNum][colNum];

    Game game = new Game();

    EventHandler<MouseEvent> eventEventHandler = event -> {
        Button button = (Button) event.getSource();
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<colNum;j++)
                if(cells[i][j]==button){
                    if(game.canPlay(new Point(i+1,j+1))){
                        cells[i][j].setText(game.getPlayer());
                        game.playMove(new Point(i+1,j+1));
                        if(!game.getGameState().equals( "on going")){
                            gameStateLabel.setText(game.getGameState());
                        }
                    }
                }
        }
    };
    private void setButton(Button button, int row, int column){
        button.setLayoutX(column*30);
        button.setLayoutY(row*30);
        button.setPrefSize(30,30);
        button.setOnMouseClicked(eventEventHandler);
        board.getChildren().add(button);
    }
    private void initBoard(){
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<colNum;j++){
                cells[i][j]= new Button();
                setButton(cells[i][j], i,j);
            }
        }
    }

    public void newGame(){
        gameStateLabel.setText("");
        board.getChildren().clear();
        initBoard();
        game= new Game();
    }
}
