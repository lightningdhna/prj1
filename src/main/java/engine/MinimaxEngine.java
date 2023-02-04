package engine;

import model.Board;
import model.Game;
import model.Point;

import java.util.HashSet;
import java.util.Set;


public class MinimaxEngine extends Engine{

    private static Set<Point> logicalMove(Game game){
        Set<Point> moves = new HashSet<Point>();
        Board board = game.getBoard();
        for(int i=1;i<=board.getRowNum();i++){
            for(int j=1;j<=board.getRowNum();j++){
                if(board.getValue(new Point(i,j)).equals('_'))
                    continue;
                int dis = 3;
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
    public static Point findBestMove(Game inputGame){
        Game game = inputGame.clone();
        Set<Point> moves = logicalMove(game);
        System.out.println(moves.size());
        for(Point move : moves){
            System.out.println(move.getRow()+" "+move.getColumn());
        }

        return new Point(1,1);
    }

}
