package engine;

import model.Board;
import model.Game;
import model.Point;

import java.util.*;


public class MinimaxEngine extends Engine{

    private static ArrayList<Point> findLogicalMoveSort(Board board, Character player){
        Set<Point> moves = logicalMove(board);
        ArrayList<Point> result = new ArrayList<>(moves);


        result.sort((Point a, Point b) -> {
            int valueDif = (int) (Engine.evaluateInstantStateValueForX(board.cloneMoveO(a))
                                -Engine.evaluateInstantStateValueForX(board.cloneMoveO(b)));
            if(player.equals('O')){
                return valueDif;
            }
            else {
                return -valueDif;
            }
        });

        return result;
    }

    private static Set<Point> logicalMove(Board board){
        Set<Point> moves = new HashSet<>();
        for(int i=1;i<=board.getRowNum();i++){
            for(int j=1;j<=board.getRowNum();j++){
                if(board.getValue(new Point(i,j)).equals('_'))
                    continue;
                int dis = 2;
                for(int ii = Math.max(1,i-dis);ii<=Math.min(board.getRowNum(),i+dis);ii++ ){
                    for(int jj = Math.max(1,j-dis);jj<=Math.min(board.getColumnNum(),j+dis);jj++){
                        if(board.getValue(new Point(ii,jj)).equals('_')){
                            moves.add(new Point(ii,jj));
                        }
                    }
                }
            }
        }

        return moves;
    }
    public static Point findBestMove(final Game inputGame){
        Game game = inputGame.clone();
        Board board = game.getBoard();
        ArrayList<Point> moves = findLogicalMoveSort(board,'O');
        Point result = new Point(1,1);
        double min=4e6;
        System.out.println(moves.size());
        for(Point move: moves){
            positionCounter=0;
//            double cur =Engine.evaluateInstantStateValueForO(board);
            double cur =minimaxEvaluate(board.cloneMoveX(move),'X',0,-3e6,+3e6);
            if(cur<min) {
                min=cur;
                result = move;
            }

        }
        System.out.println(positionCounter);
        return result;
    }


    static int positionCounter = 0;
    final private static int maxDepth = 2;
    private static final ArrayList<HashMap<Board,Double>> minimaxValueO = new ArrayList<>() ;
    private static final ArrayList<HashMap<Board,Double>> minimaxValueX = new ArrayList<>() ;

    public static double minimaxEvaluate(Board board, Character player, int depth, double alpha, double beta){

        if(depth == maxDepth){
            return Engine.evaluateInstantStateValueForO(board);
        }
        Character winState = Game.getWinState(board);
        if(winState.equals('O')){
            return 3e6;
        }
        if(winState.equals('X')){
            return -3e6;
        }
        if(winState.equals('D')){
            return -1;
        }
        ++positionCounter;
        Character opponent;
//        ArrayList<Point> moves = findLogicalMoveSort(board, player);
        Set<Point> moves = logicalMove(board);
        ArrayList<Integer> reward = new ArrayList<>();

        if(player.equals('O')){
            opponent = 'X';
            double maxEval = -3e6;
            for(Point move: moves){
                board.playX(move);
                maxEval = Math.max( maxEval , minimaxEvaluate(board, opponent, depth+1, alpha,beta));
                board.undoMove(move);
                alpha = Math.max(alpha,maxEval);
                if(alpha>=beta)
                    break;

            }
            return maxEval;

        }
        else {
            opponent = 'O';
            double minEval = 3e6;
            for(Point move: moves){
                minEval = Math.min( minEval , minimaxEvaluate(board.cloneMoveO(move), opponent, depth+1, alpha,beta));
                beta = Math.min(beta,minEval);
                if(alpha>=beta)
                    break;
            }
            return minEval;
        }
    }

}
