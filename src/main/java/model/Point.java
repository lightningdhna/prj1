package model;

import java.util.Objects;

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
    @Override
    public boolean equals(Object o){
        if (o == null || getClass() != o.getClass())
            return false;
        return row == ((Point) o).row && column == ((Point) o).column;
    }
    @Override
    public int hashCode(){
        return Objects.hash(row,column);
    }
}
