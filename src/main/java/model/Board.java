package model;

public class Board {
    Character[][] state ;

    public Board(int rowNumber, int columnNumber){
        state = new Character[rowNumber+2][columnNumber+2];
        for(Character[] row : state){
            for(int i=1;i<=columnNumber;i++){
                row[i]='_';
            }
        }
    }

    public boolean canPlay(Point point){
        return state[point.getRow()][point.getColumn()] == '_';
    }

    public char getValue(Point point){
        return state[point.getRow()][point.getColumn()];
    }


    public void playX(Point point){
        state[point.getRow()][point.getColumn()] = 'X';
    }
    public void playO(Point point){
        state[point.getRow()][point.getColumn()] = 'O';
    }
    public Character[][] getStateArray(){
        return state;
    }
}
