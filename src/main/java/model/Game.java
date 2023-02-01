package model;

public class Game {
    private String gameState = "on going";
    private Board board;
    private String player = "X";

    private final int rowNumber =  30;
    private final int columnNumber =  50;

    private int playTimes=0;

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
        System.out.println("1");
        board.playX(point);
    }
    private void playO(Point point){
        board.playO(point);
    }
    private void switchPlayer(){
        if(player.equals("X"))
            player = "O";
        else player = "X";
    }
    public void playMove(Point point){
        if(!canPlay(point))
            return;
        if(player.equals("X"))
            playX(point);
        else playO(point);
        switchPlayer();
        playTimes++;
        gameState = getGameStateAfterMove();
        printState();
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

    private Character checkWin(){
        Character[][] stateArray= board.getStateArray();
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
                    if(stateArray[i][j].equals(stateArray[i-di][j-dj])) {
                        count[i][j]=count[i-di][j-dj]+1;
                        if(count[i][j]==5)
                            return stateArray[i][j];
                    }
                    else count[i][j]=1;
                }
            }
        }
        return '_';
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
    public String getPlayer(){
        return this.player;
    }
}
