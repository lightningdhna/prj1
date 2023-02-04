package model;

public class Point {
    private int row, column;
    public Point(int row, int column){
        this.row =row;
        this.column =column;
    }
    public int getRow(){
        return row;
    }
    public int getColumn(){
        return column;
    }
    public void moveBy(Point direction){
        row+= direction.getRow();
        column+=direction.getColumn();
    }
}
