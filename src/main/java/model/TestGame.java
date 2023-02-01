package model;

public class TestGame {

    public static void main(String[] args){
        Game game = new Game();
        game.playMove(new Point(1,1));
        game.playMove(new Point(1,2));
        game.playMove(new Point(2,1));
        game.playMove(new Point(2,2));
        game.playMove(new Point(3,1));
        game.playMove(new Point(3,2));
        game.playMove(new Point(4,1));
        game.playMove(new Point(4,2));
        game.playMove(new Point(5,1));
        game.playMove(new Point(5,2));
        System.out.println(game.getGameState());
    }
}
