package enginev2;

import model.Game;
import model.Point;
import model.State;

import java.util.*;

public class MinimaxEngine extends Engine{
    private MinimaxEngine(){
        for(int i=0;i<=maxDepth;i++)
            map[i]= new HashMap<>();
    }

    private static final MinimaxEngine engine = new MinimaxEngine();
    private static final int maxDepth = 2;
    public static MinimaxEngine getEngine(){
        return engine;
    }


    private Set<Point> generateMove(Character[][] stateArray){
        Set<Point> result = new HashSet<>();
        int rowNum = stateArray.length-2;
        int colNum = stateArray[0].length-2;
        final int dis= 1;
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


    public Point findBestMove(Game inputGame){
        Point result = new Point(1,1);
        double max=-3e6;
        Set<Point> moves = generateMove(inputGame.getBoard().getStateArray());
        Character[][] state = inputGame.getBoard().clone().getStateArray();
        posCounter=0;
        for(var move : moves){
            state[move.getRow()][move.getColumn()]='O';


            double temp = minimaxEvaluate(new State(state),'X',maxDepth,-4e6,+4e6);
            if(temp>max){
                result =move;
                max= temp;
            }


            state[move.getRow()][move.getColumn()]='_';
//            System.out.println(temp);
        }
        System.out.println(posCounter);
        return result;
    }

    int posCounter =0;

    Map<State,Double>[] map  = new HashMap[maxDepth+1];
//    public double minimaxEvaluate2(State inputState, Character player, int depth, double alpha, double beta){
//        Character opponent = player.equals('X')? 'O':'X';
//        State state = inputState.clone();
//        if(map[depth].containsKey(state))
//            return map[depth].get(state);
//        posCounter++;
//        Character[][] arrayState = state.getStateArray();
//        if(depth == 0){
//
//            double temp= Engine.getEngine().evaluateGame(state, opponent);
//            map[depth].put(state,temp);
//            return temp;
//        }
//        Character winState = Engine.getEngine().getWinState(state);
//        if(winState.equals(player)) {
//            map[depth].put(state,1e6);
//            return 1e6;
//        }
//        else if(winState.equals(opponent)) {
//            map[depth].put(state,-1e6);
//            return -1e6;
//        }
//        else if(winState.equals('D')) {
//            map[depth].put(state,-1.0);
//            return -1;
//        }
//
//        Set<Point> moves = generateMove(arrayState);
//        for(var move: moves){
//            state.play(move,player);
//            //evaluate()
//            double eval = - minimaxEvaluate(state, opponent,depth-1,-beta,-alpha);
//            state.removePlay(move.getRow(),move.getColumn());
//            if(eval>=beta){
//                return beta;
//            }
//            alpha = Math.max(alpha,eval);
//        }
//        map[depth].put(state,alpha);
//        return alpha;
//    }
    public double minimaxEvaluate(State inputState,Character player, int depth, double alpha, double beta){
        Character opponent = player.equals('X')? 'O':'X';
        State state = inputState.clone();
        Character winState = Engine.getEngine().getWinState(state);
        if(winState.equals('O')) {
            map[depth].put(state,1e6);
            return 1e6;
        }
        if(winState.equals('X')) {
            map[depth].put(state,-1e6);
            return -1e6;
        }
        if(winState.equals('D')) {
            map[depth].put(state,-1.0);
            return -1;
        }
        if(map[depth].containsKey(state)){
            return map[depth].get(state);
        }
        if(depth ==0 ){
            double temp= Engine.getEngine().evaluateGame(state, 'O');
            map[depth].put(state,temp);
            return temp;
        }
        posCounter++;
        Set<Point> rawMoves = generateMove(state.getStateArray());
        ArrayList<Point> moves = sort(rawMoves,state, player);
//        System.out.println(moves.size());

        double value= 0;
        if(player.equals('O')){
            value= -3e6;
            for(var move:moves){
                value = Math.max(value,minimaxEvaluate(state.clonePlayO(move),'X',depth-1,alpha,beta));
                alpha = Math.max(alpha,value);
                if(value>=beta){
                    break;
                }
            }

        }
        else{
            value = 3e6;
            for(var move:moves){
                value = Math.min(value,minimaxEvaluate(state.clonePlayX(move),'O',depth-1,alpha,beta));
                beta = Math.min(beta,value);
                if(value<=alpha){
                    break;
                }
            }
        }
        map[depth].put(state,value);
        return value;
    }

    public void testMap(){

    }
    public void printState(State state){
        for(int i=0;i<state.getStateArray().length;i++){
            for(int j=0;j<state.getStateArray()[0].length;j++){
                System.out.print(state.getStateArray()[i][j]+" ");
            }
            System.out.println();
        }
    }
    public static void main(String... args){
        Map<State, Double>[] map = new HashMap[maxDepth+1];
        for(int i=0;i<=maxDepth;i++){
            map[i]=new HashMap<>();
        }
        Character[][] temp = new Character[][]{{'A','B'},{'C','D'}};
        State state = new State(temp);
        State state2 = state.clone();

        map[0].put(state.clone(),1.0);
        temp[0][0]='_';
        state2.getStateArray()[0][0]='_';

        System.out.println(map[0].get(state.clone()));
        System.out.println(map[0].get(state2.clone()));
        System.out.println(map[0].get(state));
        System.out.println(map[0].get(state2));
    }
    public ArrayList<Point> sort(Set<Point> moves, State inputState, Character player){
        ArrayList<Point> result = new ArrayList<>(moves);
        Map<Point, Double> point = new HashMap<>();
        State state = inputState.clone();
        for(Point move: result){
            State nexState = state.clonePlay(move,player);
            double temp=0;
            for(int i=0;i<=maxDepth;i++){
                if(map[i].containsKey(nexState)){
                    temp+= map[i].get(nexState);
                }
            }
            if(player.equals('X'))
                point.put(move,temp);
            else point.put(move,-temp);

        }
        result.sort((Point a, Point b)->{
            return (int) (point.get(a)-point.get(b));
        });
        return result;
    }
}
