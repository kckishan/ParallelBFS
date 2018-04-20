import java.util.*;

/**
 *
 * @author kishan_kc
 */
public class waterJugPuzzle {

    private final int N, target_amount;
    private final int size[];
    //Define List of list to store nodes
    //Define List of list to store visited nodes
    private final List<List<Integer>> visited = new ArrayList<>();
    private final List<List<Integer>> parent = new ArrayList<>();
    private final List<List<Integer>> nodes_list = new ArrayList<>();

    //Define constructor to define number of bottles, target amount and size of each bottles
    public waterJugPuzzle(int n, int t, int[] s) {
        N = n;
        target_amount = t;
        size = s;
    }

    //Transfer from one bottle to another bottle
    public List<Integer> transfer(List<Integer> node, int from, int to) {
        int transfer_amount;
        List<Integer> step = new ArrayList(node);
        transfer_amount = Math.min(step.get(from), size[to] - step.get(to));
        step.set(to, step.get(to) + transfer_amount);
        step.set(from, step.get(from) - transfer_amount);
        return step;
    }

    //Fill the first bottle
    public List<Integer> fill(List<Integer> node, int jug) {
        List<Integer> step = new ArrayList(node);
        step.set(jug, size[jug]);
        return step;
    }

    //Empty the last bottle
    public List<Integer> empty(List<Integer> node, int jug) {
        List<Integer> step = new ArrayList(node);
        step.set(jug, 0);
        return step;
    }

    public List<List<Integer>> possible_steps(List<Integer> node) {
        List<List<Integer>> next_nodes = new ArrayList<>();
        List<Integer> step;
        List<Integer> temp = new ArrayList<>(node);
        //If first bottle is empty, fill it
        for (int i = 0; i < N; i++) {
            step = fill(temp, i);
            if (!visited.contains(step) & !next_nodes.contains(step) & !step.isEmpty()) {
                parent.add(node);
                next_nodes.add(step);
            }
        }
        //if first bottle is not empty and last bottle is not full, transfer water
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i != j) {
                    step = transfer(temp, i, j);
                    if (!visited.contains(step) & !next_nodes.contains(step) & !step.isEmpty()) {
                        parent.add(node);
                        next_nodes.add(step);
                    }
                }
            }
        }
        //if first bottle is not empty but last bottle is full, empty last bottle
        for (int i = 0; i < N; i++) {
            step = empty(temp, i);
            if (!visited.contains(step) & !next_nodes.contains(step) & !step.isEmpty()) {
                parent.add(node);
                next_nodes.add(step);
            }
        }
        return next_nodes;
    }

    //Check if target amount is collected
    public boolean targetfound(List<List<Integer>> graph) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < graph.size(); i++) {
            list = graph.get(i);
        }
        return list.contains(target_amount);
    }

    public static void main(String[] args) {
        // Define number of bottles

        long startTime = System.currentTimeMillis();
        //Define size of bottles 
        int[] s = {3, 5, 7, 13, 19, 29, 31, 37, 39, 41};
        //Define size of bottles 
        //Define target amount to be collected in tank
        int num_of_bottles = s.length;
        //Define target amount to be collected in tank
        int target = 40;

        List<List<Integer>> adj = new ArrayList<>();
        List<List<Integer>> path = new ArrayList<>();
        List<Integer> child_node = new ArrayList<>();
        int child_index;

        //Create an object for class Graph
        waterJugPuzzle states = new waterJugPuzzle(num_of_bottles, target, s);
        List<Integer> start = new ArrayList<Integer>(num_of_bottles);
        for (int i = 0; i < num_of_bottles; i++) {
            start.add(0);
        }

        states.nodes_list.add(start);
        states.parent.add(start);
        states.visited.add(start);
        //Define Counter to iterate on list
        int counter = 0;
        //Check if target quantity is collected
        while (states.targetfound(adj) == false) {
            adj = states.possible_steps(states.nodes_list.get(counter));
            System.out.println(adj);
            for (int i = 0; i < adj.size(); i++) {
                states.nodes_list.add(adj.get(i));
                states.visited.add(adj.get(i));
            }
            counter += 1;
        }

        // Find the path from start to target point
        int solution = states.nodes_list.size() - 1;
        child_node = states.nodes_list.get(solution);
        path.add(child_node);
        while (child_node != start) {
            child_index = states.nodes_list.indexOf(child_node);
            child_node = states.parent.get(child_index);
            path.add(child_node);
//            parent_index = ;
        }
        System.out.println("The solution to water jug puzzle with " + num_of_bottles + " bottles of size " + Arrays.toString(s) + " is:");

        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i));
            if (i != 0) {
                System.out.print("->");
            }
        }
        System.out.print(".");
        System.out.println("");
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total Sequential time=" + totalTime);

    }
}
