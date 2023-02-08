package enginev2;

import model.Board;
import model.Game;
import model.Point;
import model.State;

public class Engine {

    private static final Engine engine = new Engine();

    public static Engine getEngine(){
        return  engine;
    }
//    public double evaluateGameForOnePlayer(Character[][] stateArray, Character player){
//        double[] diemLT ={0,1,2,5,9,1e6};
//        double result = 0;
//        int playTime = 0;
//        int rowNumber =stateArray.length-2;
//        int columnNumber = stateArray[0].length-2;
//        int[] dr ={0,1,1,1};
//        int[] dc ={1,1,0,-1};
//        for(int dir = 0 ;dir<4;dir++){
//            Integer[][] count = new Integer[rowNumber+2][columnNumber+2];
//            int di=dr[dir];
//            int dj=dc[dir];
//            for(int i=0;i<=rowNumber+1;i++) {
//                for (int j = 0; j <= columnNumber+1; j++) {
//                    count[i][j] = 0;
//                }
//            }
//            for(int i=1;i<=rowNumber;i++){
//                for(int j=1;j<=columnNumber;j++){
//                    if(!stateArray[i][j].equals(player)){
//                        count[i][j]=0;
//                        continue;
//                    }
//                    playTime++;
//                    count[i][j]=count[i-di][j-dj]+1;
//                    result += diemLT[count[i][j]];
//                }
//            }
//        }
//        if(playTime == rowNumber*columnNumber)
//            return -1;
//        return result;
//    }
    public Character getWinState(Character[][] stateArray){
        int playTime = 0;
        int rowNumber = stateArray.length-2;
        int columnNumber = stateArray[0].length -2 ;
        int[] dr ={0,1,1,1};
        int[] dc ={1,1,0,-1};
        Integer[][] count = new Integer[rowNumber+2][columnNumber+2];
        for(int dir = 0 ;dir<4;dir++){
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
            if(playTime == rowNumber*columnNumber)
                return 'D';
        }
        return '_';
    }
    private double evaluateGame(Game inputGame, Character player){
        return evaluateGame(inputGame.getBoard().getStateArray(),player);
    }
    public double evaluateGame(State state, Character player){
        return evaluateGame(state.getStateArray(),player);
    }
    public Character getWinState(State state){
        return getWinState(state.getStateArray());
    }
    static final Double[] diemLT = {0.0,1.0,2.0,4.0,9.0,1e6};
    private static final Point[] directions = new Point[]{
            new Point(0,1),
            new Point(1,1),
            new Point(1,0),
            new Point(1,-1),
            new Point(0,-1),
            new Point(-1,-1),
            new Point(-1,0),
            new Point(-1,1)
    };
    private double evaluateGame(Character[][] stateArray, Character player){
        Character winState = getWinState(stateArray);
        Character opponent = player.equals('X')?'O':'X';
        if(winState.equals(player)) {
            return 3e6;
        }
        else if(winState.equals(opponent))
            return -3e6;
        else if(winState.equals('D'))
            return -1;
        double temp=
            evaluateGameForOnePlayer(stateArray,'X')-
            evaluateGameForOnePlayer(stateArray,'O');
        if(player.equals('X'))
            return temp;
        else return -temp;
    }
    private double getInstantReward(Character[][] stateArray,Point point,Point direction, Character player){
        double result = 0.0;
        boolean isStartPointPlayer =stateArray[point.getRow()][point.getColumn()].equals(player);

        Character opponent = 'X';
        if(player.equals('X')) opponent ='O';

        Board board = new Board(stateArray.length-2,stateArray[0].length-2);
        board.setState(stateArray);

        double count=0;
        double temp = 0.0;
        boolean isCurrentPointPlayer =false;
        int lt= 0;
        while(count<5 && board.legalPoint(point) && board.getValue(point) != opponent ){
            ++count;
            boolean isPlayerPoint = board.getValue(point).equals(player);
            if(isPlayerPoint) {
                if(isCurrentPointPlayer){
                    temp+=0.25*diemLT[lt];
                }
                temp+=1;
                result += temp * count;
            }
            else {
                temp += 0.1;
                result += temp * count * 0.2;
            }
            isCurrentPointPlayer = isPlayerPoint;
            if(isPlayerPoint) lt ++;
            else lt= 0;
            point.moveBy(direction);
        }
        if(isStartPointPlayer )
            return result;
        else return result*0.2;
    }
    public double evaluateGameForOnePlayer(Character[][] stateArray, Character player){
        double result= 0;
        for(int i=1;i< stateArray.length-1;i++){
            for(int j=1;j<=stateArray[0].length-1;j++){
                for(Point direction : directions){
                    result += getInstantReward(stateArray,new Point(i,j),direction,player);
                }
            }
        }
        return result;
    }
}
