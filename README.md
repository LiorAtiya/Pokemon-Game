<h1>Pokemon Game</h1>
<p align="center">
<img src="https://sm.ign.com/t/ign_il/screenshot/default/c89b6bc7-0673-4720-b761-bab17c7d53fa-xoq7fo_119x.1280.jpg" width="900" height="400">
</p>    

<h2>Description</h2>
that built of 2 parts:<br>

>* firrst part:<br>
>deals with the construction of directional weighted graphstarting from the creation of the nodes and edges in the graph,<br>
>continues with the creation of the graph itself (such as connecting the nodes in the graph and more) and basic algorithms related to the graph
>(paths in the parent link graph and more).
>A graph is made up of four interfaces arranged according to the hierarchy from the creation of a node to the execution of an algorithm on the graph.
>* Second part:<br> 
>Engaged in creating the Pokemon game from building the game itself graphics, actions performed in the game, placing the agents and building a victory strategy.<br>


The first part:
---------------

is made up of four classe<br>
**NodeData class:**
This implements node_data node in a graph consists of a<br>
info, tag, location and weight.
In this class you can perform operations on a node in a graph such as a
constructor and get and set operations on the node fields.

**DWGraph_DS class:**<br>
This implementation directed_weighted_graph<br>
A graph is made up of a <br>
Vertices, Neighbors, countMC and edgeSize.<br>
In this class there are several functions that can be done in the graph such as:<br>
 constructor, add node, delete node and edge, make a connection between 2 nodes, get a collection of all the node and neighbors and more.<br>
And operations that can be performed in edge: constructor and get and set operations to edge fields that are src, des, weight, info, tag.<br>

**Graph_Algo class:**<br>
This implementation dw_graph_algorithms<br>
This class represents a number of algorithms that can be made on a graph<br>
Such as: init- creates a pointer to the graph.<br>
copy- creates a copy of the graph by deep copying.<br>
isConnected- checks whether the graph is linked.<br>
shortestPathDist- Returns the length of the shortest path.<br>
shortestPath- Returns a list of nodes in the shortest path.<br>
save and load- Save and load the graph.<br>
Dijkstra - an algorithm for finding the shortest route.<br>
BFS - an algorithm that marks the graph nodes and helped to check whether the graph is linked.<br>

**Point3D class:**<br>
This implementation geo_location<br>
that represents a geo location <x,y,z>.<br>
In this class the number of functions that can be performed in a location.<br>

**Data Structure:**<br>
HashMap-It is used because it allows you to get data based on key in O(1).<br>
ArrayList-Because it has the ability to create a list in the desired order.<br>
PriorityQueue-Because it has the ability to adjust the position of the object by definition.<br>


The second part: Pokemon game <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQomLc61qIjwYvlfH3NwFWGE-sI5U6NmSPSXQ&usqp=CAU" width="25" height="25">
---------------
is made up of eight classe<br>

**Agent class:**<br>
Class representing an agent in a graph for each agent Multiple fields:.<br>
EPS, Count, _Seed, _Hand, _Pos, _Speed, _Sedge_Course, _Shoot_Course, _gg ,_Fruit_Course, _Sg_DT,.<br>
This class advises a number of actions that can be performed on an agent such as:.<br>
builds, get and set actions for the class fields,<br>
update - gets json file and updates the agents in the graph<br> 
toJson - converts the agents to json file<br>
setNectNode- Defines the next node for the agent.<br>
getNextNode- Returns the node to which the agent is intended<br>
set_SDT- Defines the time it will take for the agent to move from the node he is on to the Pokemon.<br>
And more.

**Arena class:**<br>
This class represents the data of each stage of the game<br>
Allows you to get and configure data for the game<br>
The class is made up of the fields: EPS1, _gg, _agents, _pokemons, _info, timer, MIN, MAX.<br>
In this class you can perform a number of operations on the game such as: get and set operations for the class variables,
init- Initializes the game.
getAgent- Returns the agents that belong to the phase.
json2Pokemons- Gets a string of Pokemon and returns a list containing them.
updateEdge- Updates the locations of Pokemon to edges.<br>
isOnEdge-<br>
isOnEdge-<br>
isOnEdge-<br>
GraphRange-<br>
and more.

**Ex2 class:**<br>
In this class the main is defined through which the game can be run.

**GameFrame class:**<br>

**GamePanel class:**<br>
This class defines the GUI of the game.<br>
Class variables: _ ar, _w2f.<br>
This class defines the GUI interface of the interface using the following functions:<br>
paintComponent- Builder for the game window.<br>
importImage-<br>
drawHeader- Defines the text that appears in the game window.<br>
updateFrame-<br>
drawGraph- receives the game graph and displays it.<br>
drawPokemons- Displays the Pokemon according to the stage.<br>
drawAgants- Displays the agents according to the stage.<br>
drawNode- Displays the graph nodes.<br>
drawEdge- Displays the nodes in the graph.

**LoginGameFrame class:**<br>
This class defines the game login window using the following functions:<br>
LoginGameFrame- A method that defines the graphical parts of the login window such as type, button text box and more.<br>
initLoginGame-<br>
windowCenter - places the login screen in the middle of the screen.<br>

**MainGame class:**<br>
The department where the game takes place,<br>
Department fields: _win, _ar, scenario_num, id, dt.<br>
The game is run using the following functions:<br>
MainGame- Defines the id and stage number in the game.<br>
run- the function through which the game runs and calls are made to the functions that are part of the game.<br>
moveAgents- A function in which the move is made to each agent and a route is assigned to the agent.<br>
nextNode- A function that returns the node to which the agent needs to reach according to his route.<br>
strategy2- victory strategy was built in the department: MainGame
Which uses the shortestPath function - which returns a list of nodes of the shortest route (defined in the DWGraph_Algo class).
This is how the shortest route to each Pokemon in the game to which the agent is sent is calculated.
When a situation arises where an agent in a game is stuck at the edge we will make a low break and a high number of moves so that he can eat the Pokmon and advance to the next edge.
init-<br>
initArena-<br>
createGUI-

**Pokemon class:**<br>
This class defines pokemon<br>
Class fields: _edge, _value, _type, _pos, min_dist, min_ro, id.<br>
In this class you can do various actions on Pokemon such as: get and set actions for the class variables, builder, and more.

><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQomLc61qIjwYvlfH3NwFWGE-sI5U6NmSPSXQ&usqp=CAU" width="25" height="25">  **Goal of the game:** Collect as many Pokemon as possible.<br>
>The more Pokemon you collect with the help of the agents, the higher the score, depending on the specific value of the Pokemon.
>In order to reach the maximum score, a victory strategy was built in the department: MainGame
>Which uses the shortestPath function - which returns a list of nodes of the shortest route (defined in the DWGraph_Algo class).
>This is how the shortest route to each Pokemon in the game to which the agent is sent is calculated.
>When a situation arises where an agent in a game is stuck at the edge we will make a low break and a high number of moves so that he can eat the Pokmon and advance to the next edge.

<p align="center">
<img src="https://media.giphy.com/media/Cfyg66IOQyNAZq3Zkv/giphy.gif" width="50" height="50">
</p>    

The game can be played from:
<img src="http://up419.siz.co.il/up3/zwmomgyy2ykj.png" width="400" height="300">      <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/IntelliJ_IDEA_Logo.svg/1024px-IntelliJ_IDEA_Logo.svg.png" width="50" height="50"> 
<img src="https://sdtimes.com/wp-content/uploads/2019/03/jW4dnFtA_400x400.jpg" width="50" height="50" background="white">  
