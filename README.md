# Pokemon_Game

ex2 Readme

This project is about the Pokemons game<br>
that built of 2 parts:<br><br>
In the first part:
deals with the construction of directional weighted graphstarting from the creation of the nodes and edges in the graph,<br>
continues with the creation of the graph itself (such as connecting the nodes in the graph and more) and basic algorithms related to the graph<br>
( paths in the parent link graph and more).<br>
A graph is made up of four interfaces arranged according to the hierarchy from the creation of a node to the execution of an algorithm on the graph. <br>
And the second part: <br>
Engaged in creating the Pokemon game from building the game itself graphics, actions performed in the game, placing the agents and building a victory strategy.<br>


**Introduction:**<br>

The second part:Pokemon game.

Goal of the game: Collect as many Pokemon as possible.
The game has 23 different stages.
The game is built from:
Game board - a graph is displayed according to the stage.
Pokemon - Pokemon that are on the graph by eating get a score.
Agents - The players who run on the graph and eat the Pokemon.
For each stage of the game: a set time, a suitable graph, and a different number of Pokemon and agents.
The more Pokemon you collect with the help of the agents, the higher the score, depending on the specific value of the Pokemon.
In order to reach the maximum score, a victory strategy was built in the department: MainGame
Which uses the shortestPath function - which returns a list of nodes of the shortest route (defined in the DWGraph_Algo class).
This is how the shortest route to each Pokemon in the game to which the agent is sent is calculated.
When a situation arises where an agent in a game is stuck at the edge we will make a low break and a high number of moves so that he can eat the Pokmon and advance to the next edge.

The first part is made up of four classes:

 *NodeData class:*<br>
This implements node_data node in a graph consists of a<br>
info, tag, location and weight.
In this class you can perform operations on a node in a graph such as a
 constructor and get and set operations on the node fields.

 *DWGraph_DS class:*<br>
This implementation directed_weighted_graph
A graph is made up of a <br>
Vertices, Neighbors, countMC and edgeSize.
In this class there are several functions that can be done in the graph such as:
 constructor, add node, delete node and edge, make a connection between 2 nodes, get a collection of all the node and neighbors and more.
And operations that can be performed in edge: constructor and get and set operations to edge fields that are src, des, weight, info, tag.

 *Graph_Algo class:*<br>
This implementation dw_graph_algorithms
This class represents a number of algorithms that can be made on a graph<br>
Such as: init- creates a pointer to the graph.
copy- creates a copy of the graph by deep copying.
isConnected- checks whether the graph is linked.
shortestPathDist- Returns the length of the shortest path.
shortestPath- Returns a list of nodes in the shortest path.
save and load- Save and load the graph.
Dijkstra - an algorithm for finding the shortest route.
BFS - an algorithm that marks the graph nodes and helped to check whether the graph is linked.

Point3D class
This implementation geo_location
that represents a geo location <x,y,z>.
In this class the number of functions that can be performed in a location.

**Data Structure:**<br>
HashMap-It is used because it allows you to get data based on key in O(1).
ArrayList-Because it has the ability to create a list in the desired order.
PriorityQueue-Because it has the ability to adjust the position of the object by definition.
