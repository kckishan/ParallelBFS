/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package waterJugPuzzle;

import java.util.*;

/**
 *
 * @author kishan_kc
 */
public class waterJugPuzzle {
    private final int N, target_amount;
    private final int size[];
    //Define List of list to store nodes
    private final List<List<Integer>> adj = new ArrayList<>();
    //Define List of list to store visited nodes
    private final List<List<Integer>> visited = new ArrayList<>();
    
    //Define constructor to define number of bottles, target amount and size of each bottles
    waterJugPuzzle(int n, int t, int[] s){
        N = n;
        target_amount = t;
        size = s;
    }
     
    //Transfer from one bottle to another bottle
    List<Integer> transfer(List<Integer> node, int from, int to){
        int transfer_amount;
        List<Integer> step = new ArrayList(node);
        transfer_amount = Math.min(step.get(from),size[to] - step.get(to) );
        step.set(to, step.get(to) + transfer_amount);
        step.set(from, step.get(from) - transfer_amount);
        return step;
    }
    
    //Fill the first bottle
    List<Integer> fill(List<Integer> node){
        List<Integer> step = new ArrayList(node);
        step.set(0, size[0]);
        return step;
    }
    
    //Empty the last bottle
    List<Integer> empty(List<Integer> node){
        List<Integer> step = new ArrayList(node);
        step.set(size.length - 1, 0);
        return step;
    }
    
    void possible_steps(List<Integer> node){
        visited.add(node);
        List<Integer> step;
        //If first bottle is empty, fill it
        if (node.get(0)==0){
            step = fill(node);
            if(!visited.contains(step))
                adj.add(step);
        }
        //if first bottle is not empty and last bottle is not full, transfer water
        else if (node.get(0)>0 & node.get(N-1)!= size[size.length-1]){
            for(int i = 0; i < N-1;i++){
                step = transfer(node,i,i+1);
                if(!visited.contains(step))
                    adj.add(step);
            }
        }
        //if first bottle is not empty but last bottle is full, empty last bottle
        else if(((node.get(0)>0) && (node.get(N-1) == size[size.length-1]))) {
            step = empty(node);
            if(!visited.contains(step))
                adj.add(step);
        }
        
    }
    
    //Check if target amount is collected
    boolean targetfound(List<List<Integer>> graph){
        List<Integer> list = new ArrayList<>();
        for(int i =0; i< graph.size(); i ++)
            list = graph.get(i);
        return list.contains(target_amount);
    }
    
    public static void main(String[] args){
        // Define number of bottles
        int num_of_bottles = 2;
        
        //Define size of bottles 
        int[] s = {5, 3};
        
        //Define target amount to be collected in tank
        int target = 4;
        
        //Create an object for class Graph
        waterJugPuzzle states = new waterJugPuzzle(num_of_bottles, target, s); 
        List<Integer> start = new ArrayList<Integer>(num_of_bottles);
        for (int i=0;i<num_of_bottles;i++)
            start.add(0);
        states.adj.add(start);
        
        //Define Counter to iterate on list
        int counter = 0;
        //Check if target quantity is collected
        while(states.targetfound(states.adj) == false){
            //Get all possible steps for given node
            states.possible_steps(states.adj.get(counter));
            counter +=1;
        }
        System.out.println("The solution to water jug puzzle with "+ num_of_bottles +" bottles of size " + Arrays.toString(s) + " is:");
        for(int i = 0; i < states.adj.size();i++){
            System.out.print(states.adj.get(i));  
            if(i!=states.adj.size()-1)
                System.out.print("->");
        }
        System.out.print(".");
        System.out.println();
    }
}
