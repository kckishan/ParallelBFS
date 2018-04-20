We would like to collect a specific amount of water in a bottle when we have k water bottles with prespecified capacity and only the following operations are allowed:
* Fill up either bottle completely.
* Empty either bottle completely.
* Pour water from one bottle to the other until the poured bottle becomes empty or the other bottle becomes full.


Water Jug Puzzle was solved using Breadth-First Search in 

* Sequential and 
* Master-worker parallel flow. 

Test case: two bottles of size 5, 3 and target amount of 4. 

Solution given by Sequential and cluster implementation:
```
The solution to water jug puzzle with 2 bottles of size [5, 3] is: [0, 0]->[5, 0]->[2, 3]->[2, 0]->[0, 2]->[5, 2]->[4, 3].
```

To compare sequential and cluster implementation, 10 bottles of size 3,5,7,13,19,29,31,37,39,41 with target amount 21 was considered.

Solution: 
```
[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]-> [0, 0, 0, 0, 19, 0, 0, 0, 0, 0]-> [0, 0, 0, 0, 19, 0, 0, 0, 0, 41]-> [0, 0, 0, 0, 0, 0, 0, 0, 19, 41]-> [0, 0, 0, 0, 0, 0, 0, 0, 39, 21].
```


 Sequential time | Parallel time| Speedup
---|---|---
7019|4340|7019/4340 = 1.6
 
 


To run Sequential program,

* Compile the java file
```
javac waterJugPuzzle.java
```
* Execute compiled java class file
```
java waterJugPuzzle
```

To run parallel program
* Compile the parallel program
```
javac WaterJugClu.java
```

* Make jar file from compiled class file
```
jar cf WaterJugClu.jar *.class
```

* Execute the jar created
```
java pj2 jar-WaterJugClu.jar WaterJugClu 
```
