package engine;

import model.Board;
import model.Point;

public abstract class Engine {
    public abstract Point findBestMove();
    public double evaluate(Board board){
        return 0.0;
    }
}
