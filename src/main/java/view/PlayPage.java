package view;

import enginev2.MinimaxEngine;
import enginev2.QLearning;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Game;
import model.Point;

public class PlayPage extends Page{
    public PlayPage(){
        super();
        for(int row=0;row<rowNum;row++){
            for(int column=0;column<colNum;column++){
                cells[row][column]= new Button();
                Button button = cells[row][column];
                button.setPrefSize(30,30);
                button.setOnMouseClicked(eventEventHandler);
                button.setLayoutX(column*30);
                button.setLayoutY(row*30);
            }
        }
    }
    @FXML
    Label gameStateLabel;
    @FXML
    AnchorPane board;

    int rowNum = Game.rowNumber;
    int colNum = Game.columnNumber;
    Button[][] cells = new Button[rowNum][colNum];

    Game game = new Game();

    public void moveByEngine(){
        if(!game.getGameState().equals( "on going"))
            return;
        Point point = QLearning.getEngine().findBestMove(game);
//        Point point = MiniMaxEngineV2.findBestMove(game);
//        while(!game.canPlay(point)){
//            point = MinimaxEngine.findBestMove(game);
//        }

        cells[point.getRow()-1][point.getColumn()-1].setText(game.getPlayer().toString());
        game.playMove(point);
        if(!game.getGameState().equals( "on going")) {
            gameStateLabel.setText(game.getGameState());
        }
    }

    EventHandler<MouseEvent> eventEventHandler = event -> {
        Button button = (Button) event.getSource();
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<colNum;j++)
                if(cells[i][j]==button){
                    if(game.canPlay(new Point(i+1,j+1))){
                        cells[i][j].setText(game.getPlayer().toString());
                        game.playMove(new Point(i+1,j+1));
                        if(!game.getGameState().equals( "on going")){
                            gameStateLabel.setText(game.getGameState());
                        }
                        moveByEngine();

                    }
                }
        }
    };
    private void setButton(Button button, int row, int column){
        button.setText(" ");
        if(!board.getChildren().contains(button))
            board.getChildren().add(button);
    }
    private void initBoard(){
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<colNum;j++){
                setButton(cells[i][j], i,j);
            }
        }
    }

    public void newGame(){
        gameStateLabel.setText("");
        game = new Game();
        initBoard();
    }


}
