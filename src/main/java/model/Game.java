package model;

import engine.Engine;
import engine.MinimaxEngine;

public class Game {
    private String gameState = "on going";
    private Board board;
    private Character player = 'X';

    public static final int rowNumber =  15;
    public static final int columnNumber =  15;

    private int playTimes=0;

    @Override
    public Game clone(){
        Game game = new Game();
        game.board = this.board.clone();
        game.gameState = this.gameState;
        game.player = this.player;
        game.playTimes = 0;
        return game;
    }
    public void setGameState(String state){
        gameState= state;
    }

    public boolean canPlay(Point point){
        if(!gameState.equals("on going"))
            return false;
        if(point.getRow()>rowNumber ||point.getRow()<1)
            return false;
        if(point.getColumn()>columnNumber ||point.getColumn()<1)
            return false;
        return board.canPlay(point);
    }



    private void playX(Point point){
        board.playX(point);
    }
    private void playO(Point point){
        board.playO(point);
    }
    private void switchPlayer(){
        if(player.equals('X'))
            player = 'O';
        else player = 'X';
    }
    public void playMove(Point point){
        if(!canPlay(point))
            return;
        if(player.equals('X'))
            playX(point);
        else playO(point);
        System.out.println(player+"   "+Engine.evaluateInstantStateValueForX(board));
        MinimaxEngine.findBestMove(this);
        switchPlayer();
        playTimes++;
        gameState = getGameStateAfterMove();
//        printState();
    }

    public Game(){
        board = new Board(rowNumber,columnNumber);
    }
    public Board getBoard(){
        return this.board;
    }
    public Character getCellState(Point point){
        return board.getValue(point);
    }

    public static Character getWinState(Board board){
        Character[][] stateArray = board.getStateArray();
        int playTime = 0;
        int rowNumber = board.getStateArray().length-2;
        int columnNumber = board.getStateArray()[0].length -2 ;
        int[] dr ={0,1,1,1};
        int[] dc ={1,1,0,-1};
        for(int dir = 0 ;dir<4;dir++){
            Integer[][] count = new Integer[rowNumber+2][columnNumber+2];
            int di=dr[dir];
            int dj=dc[dir];
            for(int i=0;i<=rowNumber+1;i++) {
                for (int j = 0; j <= columnNumber+1; j++) {
                    count[i][j] = 0;
                }
            }
            for(int i=1;i<=rowNumber;i++){
                for(int j=1;j<=columnNumber;j++){
                    if(stateArray[i][j]=='_'){
                        continue;
                    }
                    playTime++;
                    if(stateArray[i][j].equals(stateArray[i-di][j-dj])) {
                        count[i][j]=count[i-di][j-dj]+1;
                        if(count[i][j]==5)
                            return stateArray[i][j];
                    }
                    else count[i][j]=1;
                }
            }
        }
        if(playTime == rowNumber*columnNumber)
            return 'D';
        return '_';
    }

    private Character checkWin(){
        return getWinState(board);
    }
    private String getGameStateAfterMove(){
        char c = checkWin();
        if(c=='X')
            return "player X is winning";
        if(c=='O')
            return "player O is winning";
        if(playTimes == rowNumber*columnNumber)
            return "draw";
        return "on going";
    }
    public String getGameState(){
        return this.gameState;
    }
    public void printState(){
        System.out.println();
        for(int i=1;i<=rowNumber;i++){
            for(int j=1;j<=columnNumber;j++){
                System.out.print(board.getValue(new Point(i,j))+" ");
            }
            System.out.println();
        }
    }
    public Character getPlayer(){
        return this.player;
    }

}
