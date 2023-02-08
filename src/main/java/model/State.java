package model;

import java.util.Objects;

public class State {
    private Character[][] stateArray;

    private State(){

    }
    public Character[][] getStateArray(){
        return this.stateArray;
    }
    public void playX(int i, int j){
        stateArray[i][j]='X';
    }
    public void playO(int i, int j){
        stateArray[i][j]='O';
    }
    public void removePlay(int i, int j){
        stateArray[i][j]='_';
    }
    public void play(Point move, Character player){
        stateArray[move.getRow()][move.getColumn()]=player;
    }
    public State(Character[][] stateArray){
        this.stateArray= stateArray;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        for(int i=1;i<stateArray.length;i++){
            for(int j=1;j<stateArray[0].length;j++){
                if(stateArray[i][j]!=((State) o).getStateArray()[i][j])
                    return false;
            }
        }

        return true;
    }
    @Override
    public int hashCode(){
        Integer[] code = new Integer[stateArray.length];
        for(int i=0;i<stateArray.length;i++){
            code[i]= Objects.hash((Object[]) stateArray[i] );
        }
        return Objects.hash((Object[]) code);
    }
    @Override
    public State clone()  {
        Character[][] temp = new Character[stateArray.length][stateArray[0].length];
        for(int i=0;i<temp.length;i++){
            System.arraycopy(stateArray[i], 0, temp[i], 0, temp[0].length);
        }
        return new State(temp);

    }
    public State clonePlayX(Point move){
        State result = clone();
        result.playX(move.getRow(),move.getColumn());
        return result;
    }
    public State clonePlayO(Point move){
        State result = clone();
        result.playO(move.getRow(),move.getColumn());
        return result;
    }
    public State clonePlay(Point move,Character player){
        State result = clone();
        result.play(move,player);
        return result;
    }
}
