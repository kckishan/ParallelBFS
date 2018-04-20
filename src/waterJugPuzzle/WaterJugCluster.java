
import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Job;
import edu.rit.pj2.Task;
import static edu.rit.pj2.Task.terminate;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.TupleListener;
import edu.rit.pj2.tuple.EmptyTuple;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kishan_kc
 */
public class WaterJugClu extends Job {

    public void main(String[] args) throws Exception {
        // Log the starting time of the program
        long startTime = System.currentTimeMillis();
        System.out.println("Start time=" + startTime);

        // Define the configuraation for the bottle, visited nodes, target amount and size of bottles
        int num_of_bottles = 10;
        List<Integer> start = new ArrayList<>();
        List<List<Integer>> nodes = new ArrayList<>();
        List<List<Integer>> visited = new ArrayList<>();
        for (int i = 0; i < num_of_bottles; i++) {
            start.add(0);
        }
        nodes.add(start);
        visited.add(start);
        //Define size of bottles 
        String s = "3,5,7,13,19,29,31,37,39,41";
        //Define target amount to be collected in tank
        int target = 21;

        //create some nodes here and pass it to worker thread
        putTuple(new StateTuple(nodes, visited));

        // When a StateTuple is available in the Tuple Space, new Worker task is initiated 
        // the parameter for worker to work is new StateTuple, number of bottles, size of bottles and target amount to be met.
        rule().whenMatch(new StateTuple()).task(SearchTask.class)
                .args("" + num_of_bottles, "" + target, "" + s);
    }

    private static class StateTuple extends Tuple {

        private List<List<Integer>> visited = new ArrayList<>();
        private List<List<Integer>> nodes = new ArrayList<>();

        public StateTuple() {
        }

        public StateTuple(List<List<Integer>> x, List<List<Integer>> w) {
            this.nodes = x;
            this.visited = w;

        }

        @Override
        public void writeOut(OutStream stream) throws IOException {
            stream.writeObject(nodes);
            stream.writeObject(visited);
        }

        @Override
        public void readIn(InStream stream) throws IOException {
            nodes = (List<List<Integer>>) stream.readObject();
            visited = (List<List<Integer>>) stream.readObject();
        }

    }

    private static class SearchTask extends Task {

        private int N;
        private int target_amount;
        volatile boolean found;

        private class State {

            private final int[] size;
            private List<List<Integer>> visited = new ArrayList<>();
            private List<List<Integer>> state = new ArrayList<>();

            public State(List<List<Integer>> a, int[] b, List<List<Integer>> c) {
                state = a;
                size = b;
                visited = c;
            }

            //Fill the first bottle
            List<Integer> fill(List<Integer> node, int jug) {
                List<Integer> step = new ArrayList(node);
                step.set(jug, size[jug]);
                return step;
            }

            // Transfer water from on bottle to another
            List<Integer> transfer(List<Integer> node, int from, int to) {
                int amount;
                List<Integer> step = new ArrayList(node);
                amount = Math.min(step.get(from), size[to] - step.get(to));
                step.set(to, step.get(to) + amount);
                step.set(from, step.get(from) - amount);
                return step;
            }

            //Empty the last bottle
            List<Integer> empty(List<Integer> node, int jug) {
                List<Integer> step = new ArrayList(node);
                step.set(jug, 0);
                return step;
            }
//
//            int[] toIntArray(List<Integer> list) {
//                int[] ret = new int[list.size()];
//                int i = 0;
//                for (Integer e : list) {
//                    ret[i++] = e.intValue();
//                }
//                return ret;
//            }

            boolean targetfound_list(List<Integer> graph) {
                return graph.contains(target_amount);
            }

            void possible_steps() throws IOException {
                List<Integer> step = new ArrayList<>();
                List<List<Integer>> next_nodes = new ArrayList<>();
                for (int k = 0; k < state.size(); k++) {
                    List<Integer> temp = new ArrayList<>(state.get(k));
                    //If first bottle is empty, fill it
                    for (int i = 0; i < N; i++) {
                        step = fill(temp, i);
                        if (!visited.contains(step) & !next_nodes.contains(step) & !step.isEmpty()) {
                            visited.add(step);
                            next_nodes.add(step);
                            if (targetfound_list(step)) {
                                System.out.println(step);
                                putTuple(new EmptyTuple());
                                long endTime = System.currentTimeMillis();
                                System.out.println("End time=" + endTime);
                                terminate(0);
                            }
                        }
                    }
                    //if first bottle is not empty but last bottle is full, empty last bottle
                    for (int i = 0; i < N; i++) {
                        step = empty(temp, i);
                        if (!visited.contains(step) & !next_nodes.contains(step) & !step.isEmpty()) {
                            visited.add(step);
                            next_nodes.add(step);
                            if (targetfound_list(step)) {
                                System.out.println(step);
                                putTuple(new EmptyTuple());
                                long endTime = System.currentTimeMillis();
                                System.out.println("End time=" + endTime);
                                terminate(0);
                            }
                        }
                    }
                    //if first bottle is not empty and last bottle is not full, transfer water
                    for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                            if (i != j) {
                                step = transfer(temp, i, j);
                                if (!visited.contains(step) & !next_nodes.contains(step) & !step.isEmpty()) {
                                    visited.add(step);
                                    next_nodes.add(step);
                                    if (targetfound_list(step)) {
                                        System.out.println(step);
                                        putTuple(new EmptyTuple());
                                        long endTime = System.currentTimeMillis();
                                        System.out.println("End time=" + endTime);
                                        terminate(0);
                                    }
                                }
                            }
                        }
                    }
                }

                int n = 0;
                int total_size = next_nodes.size();
                for (int i = 0; i < ( total_size/ 30) + 1; i += 20) {
                    if (i + 30 > total_size) {
                        n = total_size;
                    } else {
                        n = i + 30;
                    }
                    List<List<Integer>> pass = new ArrayList<>(next_nodes.subList(i, n));
                    putTuple(new StateTuple(pass, visited));
                }
            }
        }

        public static int[] getIntegers(String numbers) {
            StringTokenizer st = new StringTokenizer(numbers, ",");
            int[] intArr = new int[st.countTokens()];
            int i = 0;
            while (st.hasMoreElements()) {
                intArr[i] = Integer.parseInt((String) st.nextElement());
                i++;
            }
            return intArr;
        }

        boolean targetfound(List<List<Integer>> graph) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < graph.size(); i++) {
                list = graph.get(i);
                if (list.contains(target_amount)) {
                    return true;
                }
            }
            return false;
        }

        public void main(String[] args) throws Exception {
            // Parse command line arguments.
            N = Integer.parseInt(args[0]);
            target_amount = Integer.parseInt(args[1]);
            int[] size = getIntegers(args[2]);

            StateTuple tuple = (StateTuple) getMatchingTuple(0);
            new State(tuple.nodes, size, tuple.visited).possible_steps();
        }

        protected static int coresRequired() {
            return 1;
        }
    }
}
