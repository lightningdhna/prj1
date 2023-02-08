package enginev2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Mapping {

    private static final Mapping map = new Mapping();

    public static Mapping getMap(){
        return map;
    }
    Map<Integer[], Double>[] mapArray = new Map[20];
    public Mapping(){
        for(int i=0;i<mapArray.length;i++)
            mapArray[i]= new HashMap<>();
    }

    public void add(int depth, Character[][] state, double result){
        mapArray[depth].put(map(state),result);
    }
    public Integer[] map(Character[][] state){
        Integer[] result = new Integer[state.length];
        for(int i=0;i<state.length;i++)
            result[i] = Objects.hash((Object[]) state[i]);
        return result;
    }
    public boolean containKey(int depth, Character[][] state){
        return mapArray[depth].containsKey(map(state));
    }
    public double getReward(int depth, Character[][]state){
        return mapArray[depth].get(map(state));
    }

}
