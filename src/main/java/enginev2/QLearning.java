 package enginev2;

import model.Game;
import model.Point;
import model.State;

import java.util.*;

 public class QLearning {
    private final HashMap<State, Point> piMap = new HashMap<>();
    private final HashMap<State, Double> vMap = new HashMap<>();
    private final HashMap<State, Double> evalMap = new HashMap<>();


    private final double epsilon = 0.2;
    private final double  gamma = 0.8;
    private final double alpha = 0.3;

    private final int maxDepth = 4;

    private final int maxIntegration = 10000;

    private static final QLearning engine = new QLearning();
    public static QLearning getEngine(){
        return engine;
    }
     private Set<Point> generateMove(Character[][] stateArray){
         Set<Point> result = new HashSet<>();
         int rowNum = stateArray.length-2;
         int colNum = stateArray[0].length-2;
         final int dis= 2;
         for(int i=1;i<=rowNum;i++){
             for(int j=1;j<=colNum;j++){
                 if(!stateArray[i][j].equals('_')){
                     for(int ii=Math.max(1,i-dis);ii<=Math.min(rowNum,i+dis);ii++){
                         for(int jj=Math.max(1,j-dis);jj<=Math.min(colNum,j+dis);jj++){
                             if(stateArray[ii][jj].equals('_'))
                                 result.add(new Point(ii,jj));
                         }
                     }
                 }
             }
         }

         return result;
     }
    public ArrayList<Point> generateSequence(State state0){

        int depth = 0;
        ArrayList<Point> result = new ArrayList<>();
        State state = state0.clone();
        Point move;

        Character winState = Engine.getEngine().getWinState(state.getStateArray());
        Character player = 'O';
        while(depth++<maxDepth && winState.equals('_')){
            if(Math.random()<epsilon && piMap.containsKey(state)){
                move = piMap.get(state);
            }
            else{
                Set<Point> moves = generateMove(state.getStateArray());
//                Map<Point, Double> evalMove = new HashMap<>();

//                double sum = 0;
//                for(Point temp: moves){
//                    double tempEval = eval(state.clonePlay(temp,player));
//                    evalMove.put(temp, tempEval);
//                    sum+=tempEval;
//                }
//                move = null;
//                for(Point temp:moves){
//                    if(Math.random() <= evalMove.get(temp)/sum){
//                        move= temp;
//                    }
//                }
                move=null;
                if(move==null){
                    ArrayList<Point> move2 = new ArrayList<>(moves);
                    move = move2.get(new Random().nextInt(move2.size()));
                }

            }

            result.add(move);
            state.play(move,player);
            player = player.equals('X')?'O':'X';
            winState = Engine.getEngine().getWinState(state.getStateArray());

        }
        return result;
    }
    public double eval(State state){
        if(evalMap.containsKey(state))
            return evalMap.get(state);

        double result =0;

        result = enginev2.Engine.getEngine().evaluateGame(state,'O');

        evalMap.put(state,result);
        return result;
    }

    public double getValue(State state){
        if(vMap.containsKey(state))
            return vMap.get(state);
        else {
            vMap.put(state,eval(state));
            return 0.0;
        }
    }
    public void updatePi(State state, Point action){
        if (piMap.containsKey(state)) {
            piMap.replace(state, action);
        }
        else piMap.put(state,action);
    }

    private Double qlearning(State state, int id, Character player, ArrayList<Point> moves){

        if(id==moves.size()) {
            return getValue(state);
        }

        Point action = moves.get(id);
        Character opponent = player.equals('X') ? 'O' :'X';

        double result = getValue(state);
        double curMax = result;

        if(player.equals('X')){
            State nexState = state.clonePlayX(action);
            result = (1-alpha)*result+
                    alpha * (  eval(nexState)- eval(state) +
                            gamma * Math.min(getValue(nexState), qlearning(nexState,id+1,opponent,moves))
                    );
            if( result <= curMax){
                updatePi(state,action);
            }
        }
        else{
            State nexState = state.clonePlayO(action);
            result = (1-alpha)*result+
                    alpha * (  eval(nexState)- eval(state) +
                            gamma *  Math.max(getValue(nexState), qlearning(nexState,id+1,opponent,moves))
                    );
            if( result >= curMax){
                updatePi(state,action);
            }
        }

        vMap.replace(state, result);
        return result;

    }

    public Thread training(final State state){
        Thread thread = new Thread(
                ()->{
//                    for(int i=0;i<maxIntegration;i++){
//                        ArrayList<Point> moves = generateSequence(state);
//                        updateV(state,0,'O',moves);
//                    }
                    while(!Thread.interrupted()){
                        ArrayList<Point> moves = generateSequence(state);
                        qlearning(state,0,'O',moves);
                    }
                }
        );
        thread.start();
        return thread;
    }

    public Point findBestMove(Game inputGame){
        State state =new State(inputGame.getBoard().getStateArray());
        int agentsNum = 1;
        Thread[] agents = new Thread[agentsNum];
        for(int i=0;i<agentsNum;i++) {
            agents[i] = training(state);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i<agentsNum;i++){
            agents[i].interrupt();
        }
        System.out.println(evalMap.size());
        return piMap.get(state);
    }

}
