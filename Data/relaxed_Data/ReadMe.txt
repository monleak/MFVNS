Instance name = TSPLIB name + {"C", "R"} + "-" + numberOfGroup + "-" + d-relaxed. 
Below is a description of the format of the text file that defines each problem instance. All constants are integers. 
Customer number 1 denotes the depot, where all vehicles must start and finish.

N is the number of customer.
G is the number of group.
d is the d-relaxed number.

Next is a distance matrix.
Next g lines describes the priority information. (i n S)
	- The first number is the id of group. 
	- The second number is the number of node with priority i
	- Next is the list of node with priority i
_____________________________________________________________________
N
G d
Distance Matrix (NxN)
0 <Size Of S0> <Array of S0>
1 <Size Of S1> <Array of S1>
...
G-1  <Size Of SG-1> <Array of SG-1>