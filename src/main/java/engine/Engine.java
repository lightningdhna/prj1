package engine;

import model.Board;
import model.Game;
import model.Point;

import java.util.HashMap;

public abstract class Engine {

    private static final HashMap<Board,Double> instantReward = new HashMap<>();
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
    static final Double[] diemLT = {0.0,1.0,2.0,4.0,9.0,1e6};

    public static Point findBestMove(Game game) {
        return null;
    }


    private static double getInstantReward(Board board,Point point,Point direction, Character player){
        double result = 0.0;
        boolean isStartPointPlayer =board.getValue(point).equals(player);

        Character opponent = 'X';
        if(player.equals('X')) opponent ='O';


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
    private static double evaluateInstantStateValue(Board board, Character player) {
        double result = 0.0;

        Character opponent = 'X';
        if(player.equals('X')) opponent ='O';
        Character gameState = Game.getWinState(board);
        if(gameState.equals(player))
            return 1e6;
        else if(gameState.equals(opponent))
            return -1e6;
        else if(gameState.equals('D'))
            return 0;
        for(int i=1;i<= board.getRowNum();i++){
            for(int j=1;j<=board.getColumnNum();j++){
                for(Point direction : directions){
                    result += getInstantReward(board,new Point(i,j),direction,player);
                }
            }
        }
        return result;
    }
    public static double evaluateInstantStateValueForX(Board inputBoard){
        Board board = inputBoard.clone();
        if(instantReward.containsKey(board))
            return instantReward.get(board);
        double result = evaluateInstantStateValue(board,'X') - evaluateInstantStateValue(board,'O');
        instantReward.put(board,result);
        return result;
    }
    public static double evaluateInstantStateValueForO(Board board){
        return -evaluateInstantStateValueForX(board);
    }

    public static void main(String... args ) {
        Board board = new Board(1,5);
        instantReward.put(board,1.0);
        board.getStateArray()[1][1]='B';
//        board.getStateArray()[1][1]='_';
        System.out.println(instantReward.get(board.clone()));
    }
    public static Point[] getDirections(){
        return directions;
    }
}
