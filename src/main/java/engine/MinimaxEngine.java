package engine;

import model.Board;
import model.Game;
import model.Point;

import java.util.*;


public class MinimaxEngine extends Engine{

    private static ArrayList<Point> findLogicalMoveSort(Board board, Character player){
        Set<Point> moves = logicalMove(board);
        ArrayList<Point> result = new ArrayList<>(moves);
        Map<Point,Double> reward = new HashMap<>();




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
//        ArrayList<Point> moves = findLogicalMoveSort(board,'O');
        Set<Point> moves = logicalMove(board);
        Point result = new Point(1,1);
        double max=-4e6;
        System.out.println(moves.size());
        positionCounter=0;
        endPositionCounter=0;
        for(Point move: moves){
//            double cur =Engine.evaluateInstantStateValueForO(board);
            double cur =minimaxEvaluate(board.cloneMoveO(move),'X',0,-3e6,+3e6);
            System.out.println(positionCounter);
            if(cur>max) {
                max=cur;
                result = move;
            }

        }
        System.out.println(positionCounter);
        System.out.println(endPositionCounter);
        int cnt=0;
        for(int i=0;i<maxDepth;i++)
            cnt+=minimaxValue[i].size();
        System.out.println(cnt);
        return result;
    }


    static int positionCounter = 0;
    static int endPositionCounter = 0;
    final private static int maxDepth = 4;
    private static final Map<Board,Double>[] minimaxValue = new HashMap[50] ;

    public static double minimaxEvaluate(Board board, Character player, int depth, double alpha, double beta){
        if(minimaxValue[depth] ==null){
            minimaxValue[depth] = new HashMap<>();
        }
        if(minimaxValue[depth].containsKey(board))
            return minimaxValue[depth].get(board);

        Character winState = Game.getWinState(board);
        if(winState.equals('O')){
            minimaxValue[depth].put(board,3e6);
            return 3e6;
        }
        if(winState.equals('X')){
            minimaxValue[depth].put(board,-3e6);
            return -3e6;
        }
        if(winState.equals('D')){
            minimaxValue[depth].put(board,-1.0);
            return -1;
        }
        ++positionCounter;
        if(depth == maxDepth){
            ++endPositionCounter;
            double temp = Engine.evaluateInstantStateValueForO(board);
            minimaxValue[depth].put(board,temp);
            return temp;
        }
        Character opponent;
        ArrayList<Point> moves = findLogicalMoveSort(board, player);
//        Set<Point> moves = logicalMove(board);
        ArrayList<Integer> reward = new ArrayList<>();

        if(player.equals('O')){
            opponent = 'X';
            double maxEval = -3e6;
            for(Point move: moves){
                board.playO(move);
                maxEval = Math.max( maxEval , minimaxEvaluate(board, opponent, depth+1, alpha,beta));
                board.undoMove(move);
                alpha = Math.max(alpha,maxEval);
                if(alpha>=beta)
                    break;

            }
            minimaxValue[depth].put(board,maxEval);
            return maxEval;

        }
        else {
            opponent = 'O';
            double minEval = 3e6;
            for(Point move: moves){
                board.playX(move);
                minEval = Math.min( minEval , minimaxEvaluate(board, opponent, depth+1, alpha,beta));
                board.undoMove(move);
                beta = Math.min(beta,minEval);
                if(alpha>=beta)
                    break;
            }
            minimaxValue[depth].put(board,minEval);
            return minEval;
        }
    }

}
