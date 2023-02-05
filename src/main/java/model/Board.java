package model;

import java.util.Objects;

public class Board {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        if(getColumnNum()!=((Board) o).getColumnNum()||getRowNum()!=((Board) o).getRowNum())
            return false;
        for(int i=0;i<state.length;i++){
            for(int j=0;j<state[0].length;j++){
                if(!state[i][j].equals(((Board) o).getStateArray()[i][j])){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        Integer[] code = new Integer[state.length];
        for(int i=0;i<state.length;i++){
            code[i]= Objects.hash((Object[]) state[i] );
        }
        return Objects.hash((Object[]) code);
//        return Objects.hashCode(state);
    }
   private  Character[][] state ;

    private int rowNum, columnNum;
    public boolean legalPoint(Point point){
        int row  = point.getRow();
        int column = point.getColumn();
        return (row>=1 && row<= rowNum && column>=1 && column<=columnNum);
    }
    public Board(int rowNumber, int columnNumber){
        this.rowNum = rowNumber;
        this.columnNum = columnNumber;
        state = new Character[rowNumber+2][columnNumber+2];
        for(Character[] row : state){
            for(int i=0;i<=columnNumber+1;i++){
                row[i]='_';
            }
        }
    }

    public boolean canPlay(Point point){
        return state[point.getRow()][point.getColumn()] == '_';
    }

    public Character getValue(Point point){
        return state[point.getRow()][point.getColumn()];
    }


    public void playX(Point point){
        state[point.getRow()][point.getColumn()] = 'X';
    }
    public void playO(Point point){
        state[point.getRow()][point.getColumn()] = 'O';
    }
    public void undoMove(Point point){
        state[point.getRow()][point.getColumn()] = '_';
    }
    public Character[][] getStateArray(){
        return state;
    }
    public int getRowNum(){
        return this.rowNum;
    }
    public int getColumnNum(){
        return this.columnNum;
    }
    public void setState(Character[][] state){
        this.state = state;
    }
    @Override
    public Board clone()  {
        Board board = new Board(state.length-2,state[0].length-2);
        board.state = new Character[state.length][state[0].length];
        for(int i=0;i<board.state.length;i++){
            board.state[i] = state[i].clone();
        }
        return board;

    }
    public Board cloneMoveX(Point point){
        Board board = clone();
        board.playX(point);
        return board;
    }
    public Board cloneMoveO(Point point){
        Board board = clone();
        board.playO(point);
        return board;
    }

}
