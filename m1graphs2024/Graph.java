package m1graphs2024;

import java.io.*;
import java.util.*;

/**
 * The class Graph is a representation of a graph with an Adjacency Edge list
 * @author Tristan de Saint Gilles
 * @author Renaud Joffrin
 */
public class Graph {
    /**
     * Variable used in developpement to print debug information or anything related to testing
     * @hidden SET TO FALSE BEFORE EXPORT
     */
    public static final boolean dev = false; 
    
    /**
     * Int used to have a reference time in the DFS With Infos function
     */
    static int time = 0;

    /* Attributes */

    /*
     * The Adjencent Edge List implemented as a Map
     */
    private Map<Node, List<Edge>> adjEdList;
    @SuppressWarnings("unused")
    private String name;

    /* Constructors */

    /**
     * Constructor for empty graph
     */
    public Graph() {
        adjEdList = new HashMap<>();
    }

    /**
     * Constructor for empty graph with a name
     * @param name the name of the graph
     */
    public Graph(String name) {
        this.name = name;
        adjEdList = new HashMap<>();
    }
    
    /**
     * Constructor from Successor array
     * @param sa unknown number of int as successor array
     */
    public Graph(int ... sa) {
        adjEdList = new HashMap<>();

        int x = 1;
        for (int i = 0; i < sa.length; i++) {
            addNode(x);
            if(sa[i] != 0) {
                if (dev) System.out.println("Adding Edge from " + (x) + " to " + sa[i]);
                addEdge(x, sa[i]);
            }
            else {
                x++;
            }
        }
    }

    /* API */

    /**
     * Getter for AdjEdList
     * @return the adjEdList Map of Node : List Edge
     */
    public Map<Node, List<Edge>> getList(){
        return adjEdList;
    }

    /**************************
     *                        *
     * Nodes related methods  *
     *                        *
     **************************/

    /**
     * @return the number of nodes in the graph
     */
    public int nbNodes(){
        return getAllNodes().size();
    }

    /**
     * 
     * @param n a node 
     * @return true if the id is already used in this graph or false if not
     */
    public boolean usesNode(Node n){
        return this.getNode(n.getId()) != null;
    }

    /**
     * 
     * @param id a int
     * @return true if the id is used in the graph or false if not
     */
    public boolean usesNode(int id){
        return this.getNode(id) != null;
    }

    /**
     * 
     * @param n a node
     * @return true if the node is in the graph or false if not
     */
    public boolean holdsNode(Node n){
        return (this.usesNode(n) && this == n.getGraph());
    }

    /**
     * @param id an int
     * @return the node with the corresponding id or null if it doesnt exist in this graph
     */
    public Node getNode(int id){
        for (Node n : getAllNodes()){
            if (n.getId() == id){
                return n;
            }
        }
        return null;
    }

    
    /** 
     * Add a node to this graph
     * @param n a Node
     * @return false if the node already exists, true else
     */
    public boolean addNode(Node n){
        if(this.usesNode(n)){
            return false;
        }
        adjEdList.put(n, new ArrayList<>());
        return true;
    }

    
    /** 
     * Add a node to this graph
     * @param id an int
     * @return false if the node already exists, true else
     */
    public boolean addNode(int id){
        if(this.usesNode(id)){
            return false;
        }
        if (dev) System.out.println("Adding Node " + id);
        adjEdList.put(new Node(id, this), new ArrayList<>());
        if (dev) System.out.println("Adjacency List : " + adjEdList);
        return true;
    }

    /** 
     * Remove a node from this graph
     * @param n a Node
     * @return false if the node is not in the graph, true else
     */
    public boolean removeNode(Node n){
        if(!this.holdsNode(n)){
            return false;
        }
        List<Edge> allInEdges = getInEdges(n);
        ListIterator<Edge> inIterator = allInEdges.listIterator();
        while(inIterator.hasNext()){
            removeEdge(inIterator.next());
        }
        adjEdList.remove(n);
        return true;
    }

    /** 
     * Remove a node from this graph
     * @param id an int
     * @return false if the node is not in the graph, true else
     */
    public boolean removeNode(int id){
        if(!this.usesNode(id)){
            return false;
        }
        List<Edge> allInEdges = getInEdges(id);
        ListIterator<Edge> inIterator = allInEdges.listIterator();
        while(inIterator.hasNext()){
            removeEdge(inIterator.next());
        }
        adjEdList.remove(new Node(id, this));
        return true;
    }

    /**
     * For getting the list of all the nodes of the graph
     * @return a List of all the Nodes of the graph
     */
    public List<Node> getAllNodes(){
        Set<Node> keys = adjEdList.keySet();
        List<Node> allNodes = new ArrayList<>(keys.size());
        allNodes.addAll(keys);
        Collections.sort(allNodes); // Sort for the sake of reproductibility
        return allNodes;
    }

    /**
     * Get the list of all the Nodes in every edges of the graph 
     * @return the list of all the nodes
     */
    public List<Node> getAllNodesInEdges(){
        List<Node> allNodes = new ArrayList<>();
        for(Edge e : getAllEdges()){
            if(!allNodes.contains(e.to())){
                allNodes.add(e.to());
            }
            if(!allNodes.contains(e.from())){
                allNodes.add(e.from());
            }
        }
        return allNodes;
    }

    /**
     * Get the largest node Id
     * @return an int
     */
    public int largestNodeId(){
        Iterator<Node> ite = getAllNodes().iterator();        
        int max = 0;
        while(ite.hasNext()){
            Node curr = ite.next();
            if(curr.getId() > max){
                max = curr.getId();
            }
        }
        return max;
    }

    /**
     * For knowing the smallest id used by a node in the graph
     * @return an int 
     */
    public int smallestNodeId(){
        Iterator<Node> ite = getAllNodes().iterator();
        int min = 0;
        while(ite.hasNext()){
            Node curr = ite.next();
            if(min == 0){
                min = curr.getId();
            }else{
                if(curr.getId() < min){
                    min = curr.getId();
                }
            }
        }
        return min;
    }

    /**
     * For getting a list without duplicates of the successor of node n
     * @param n a Node
     * @return a List of Node
     */
    public List<Node> getSuccessors(Node n){
        List<Node> result = new ArrayList<>();
        List<Edge> allEdges = getOutEdges(n);
        ListIterator<Edge> ite = allEdges.listIterator();
        while(ite.hasNext()){
            Edge curr = ite.next();
            if(result.indexOf(curr.to()) == -1){
                result.add(curr.to());
            }
        }
        return result;
    }

    /**
     * For getting a list without duplicates of the successor of node n
     * @param id an int
     * @return a List of Node
     */
    public List<Node> getSuccessors(int id ){
        return getSuccessors(getNode(id));
    }

    /**
     * For getting a list with possible duplicates of the successors 
     * @param n a Node
     * @return a List of Node
     */
    public List<Node> getSuccessorsMulti(Node n){
        List<Node> result = new ArrayList<>();
        List<Edge> allEdges = getOutEdges(n);
        ListIterator<Edge> ite = allEdges.listIterator();
        while(ite.hasNext()){
            result.add(ite.next().to());
        }
        return result;
    }

    /**
     * For getting a list with possible duplicates of the successors 
     * @param id an int
     * @return a List of Node
     */
    public List<Node> getSuccessorsMulti(int id){
        return getSuccessorsMulti(getNode(id));
    }

    /**
     * For knowing whether nodes u and v are adjacent in the graph
     * @param u a Node 
     * @param v a Node
     * @return true if u and v are adjacent, false else
     */
    public boolean adjacent(Node u, Node v){
        return existsEdge(u, v);
    }

    /**
     * For knowing whether nodes u and v are adjacent in the graph
     * @param u an int 
     * @param v an int
     * @return true if u and v are adjacent, false else
     */
    public boolean adjacent(int u, int v){
        return existsEdge(u, v);
    }

    /**
     * for knowing the in-degree of node n. 
     * @param n a Node
     * @return an int
     */
    public int inDegree(Node n){
        return getInEdges(n).size();
    }

    /**
     * for knowing the in-degree of node n. 
     * @param n an int
     * @return an int
     */
    public int inDegree(int n){
        return getInEdges(n).size();
    }

    /**
     * for knowing the out-degree of node n. 
     * @param n a Node
     * @return an int
     */
    public int outDegree(Node n){
        return getOutEdges(n).size();
    }

    /**
     * for knowing the out-degree of node n. 
     * @param n an int
     * @return an int
     */
    public int outDegree(int n){
        return getOutEdges(n).size();
    }

    /**
     * for knowing the degree of node n. 
     * @param n a Node
     * @return an int
     */
    public int degree(Node n){
        return getIncidentEdges(n).size();
    }

    /**
     * for knowing the degree of node n. 
     * @param n an int
     * @return an int
     */
    public int degree(int n){
        return getIncidentEdges(n).size();
    }

    /**************************
     *                        *
     * Edges related methods  *
     *                        *
     **************************/

    /**
     * 
     * @return the number of edges in this graph
     */
    public int nbEdges() {
        int size = 0;
        for (var l : adjEdList.values()){
            for (Edge e : l){
                if (dev) System.out.println(e.from().getId());
                size ++;
            }
        }
        return size;
    }

    /**
     * For knowing whether an edge exists in this graph between nodes u and v.
     * @param u any Node 
     * @param v any Node
     * @return  false if any node isn't in the graph or if no edges exists between the two,
     *          true else.
     */
    public boolean existsEdge(Node u, Node v){
        return holdsNode(u) && holdsNode(v) && adjEdList.containsKey(u) && adjEdList.get(u).contains(new Edge(u, v, this));
    }

    /**
     * For knowing whether an edge exists in this graph between nodes u and v.
     * @param u any Node id
     * @param v any Node id
     * @return  false if any node isn't in the graph or if no edges exists between the two,
     *          true else.
     */
    public boolean existsEdge(int u, int v){
        return usesNode(u) && usesNode(v) && adjEdList.get(getNode(u)).contains(new Edge(u, v, this));
    }

    /**
     * For knowing whether an edge exists in this graph between nodes u and v.
     * @param e Edge reference
     * @return  true if the edge is in the graph,
     *          false else.
     */
    public boolean existsEdge(Edge e){
        return holdsNode(e.from()) && holdsNode(e.to()) && adjEdList.get(e.from()).contains(e);
    }

    /**
     * Add an edge in the graph, if any Node does not exist, they have to
     * be created first.
     * @param from a Node
     * @param to a Node
     */
    public void addEdge(Node from, Node to){
        addNode(from);
        addNode(to);

        adjEdList.get(from).add(new Edge(from, to, this));
    }

    /**
     * Add an edge in the graph, if any Node does not exist, they have to
     * be created first.
     * @param from a Node
     * @param to a Node
     * @param weight the weight of the edge
     */
    public void addEdge(Node from, Node to, int weight){
        addNode(from);
        addNode(to);

        adjEdList.get(from).add(new Edge(from, to, weight, this));
    }

    /**
     * Add an edge in the graph, if any Node does not exist, they have to
     * be created first.
     * @param from a Node id
     * @param to a Node id
     */
    public void addEdge(int from, int to){
        addNode(from);
        addNode(to);
        adjEdList.get(getNode(from)).add(new Edge(from, to, this));
    }

    /**
     * Add an edge in the graph, if any Node does not exist, they have to
     * be created first.
     * @param from a Node id
     * @param to a Node id
     * @param weight the weight of the edge
     */
    public void addEdge(int from, int to, int weight){
        addNode(from);
        addNode(to);

        adjEdList.get(getNode(from)).add(new Edge(from, to, weight, this));
    }

    /**
     * Add an edge in the graph
     * @param e Edge reference
     */
    public void addEdge(Edge e){
        addNode(e.from());
        addNode(e.to());

        if(!e.isWeighted()) adjEdList.get(e.from()).add(new Edge(e.from(), e.to(), this));
        else adjEdList.get(e.from()).add(new Edge(e.from(), e.to(), e.getWeight(), this));
    }

    /**
     * Remove the edge between @from and @to from this graph 
     * @param from a Node
     * @param to a Node
     * @return true if removal was a success
     *         false else.
     */
    public boolean removeEdge(Node from, Node to){
        if (!existsEdge(from, to)) return false;

        adjEdList.get(from).remove(new Edge(from, to, this));

        return true;
    }

    /**
     * Remove the edge between @from and @to from this graph 
     * @param from a Node
     * @param to a Node
     * @param weight the weight of the edge
     * @return true if removal was a success
     *         false else.
     */
    public boolean removeEdge(Node from, Node to, int weight){
        if (!existsEdge(from, to)) return false;

        adjEdList.get(from).remove(new Edge(from, to, weight, this));

        return true;
    }

    /**
     * Remove the edge between @from and @to from this graph 
     * @param from a Node id
     * @param to a Node id
     * @return true if removal was a success
     *         false else.
     */
    public boolean removeEdge(int from, int to){
        if (!existsEdge(from, to)) return false;

        adjEdList.get(getNode(from)).remove(new Edge(from, to, this));

        return true;
    }

    /**
     * Remove the edge between @from and @to from this graph 
     * @param from a Node id
     * @param to a Node id
     * @param weight the weight of the edge
     * @return true if removal was a success
     *         false else.
     */
    public boolean removeEdge(int from, int to, int weight){
        if (!existsEdge(from, to)) return false;

        adjEdList.get(getNode(from)).remove(new Edge(from, to, weight, this));

        return true;
    }

    /**
     * Remove the edge @e from this graph 
     * @param e the edge reference to remove
     * @return true if removal was a success
     *         false else.
     */
    public boolean removeEdge(Edge e){
        if (!existsEdge(e.from(), e.to())) return false;

        adjEdList.get(e.from()).remove(e);

        return true;
    }

    /**
     * For getting the list of edges leaving the Node n
     * @param n a Node 
     * @return the list of edges leaving the node 
     */
    public List<Edge> getOutEdges(Node n){
        if (adjEdList.get(n) == null) return new ArrayList<>();
        List<Edge> result = adjEdList.get(n);
        Collections.sort(result);
        return result;
    }

    /**
     * For getting the list of edges leaving the Node @n
     * @param n a Node id
     * @return the list of edges leaving the node 
     */
    public List<Edge> getOutEdges(int n){
        if (getNode(n) == null) return new ArrayList<>();
        Node node = getNode(n);
        if (adjEdList.get(node) == null) return new ArrayList<>();
        List<Edge> result = adjEdList.get(node);
        Collections.sort(result);
        return result;
    }

    /**
     * For getting the list of edges entering the Node @n
     * @param n a Node 
     * @return the list of edges entering the node 
     */
    public List<Edge> getInEdges(Node n){
        List<Edge> res = new ArrayList<>();
        for (List<Edge> edges : adjEdList.values()){
            for (Edge e : edges){
                if (e.to() == n) res.add(e);
            }
        }
        Collections.sort(res);
        return res;
    }

    /**
     * For getting the list of edges entering the Node @n
     * @param n a Node id
     * @return the list of edges entering the node 
     */
    public List<Edge> getInEdges(int n){
        if (getNode(n) == null) return new ArrayList<>();
        Node node = getNode(n);

        List<Edge> res = new ArrayList<>();
        for (List<Edge> edges : adjEdList.values()){
            for (Edge e : edges){
                if (e.to() == node) res.add(e);
            }
        }
        Collections.sort(res);
        return res;
    }

    /**
     * for getting the list of all edges incident to node @n. 
     * This is the union of the out and in edges
     * @param n a Node 
     * @return the list of edges entering and leaving the node 
     */
    public List<Edge> getIncidentEdges(Node n){
        List<Edge> res = new ArrayList<>();
        for (List<Edge> edges : adjEdList.values()){
            for (Edge e : edges){
                if (e.to() == n || e.from() == n) res.add(e);
            }
        }
        return res;
    }

    /**
     * for getting the list of all edges incident to node @n. 
     * This is the union of the out and in edges
     * @param n a Node id
     * @return the list of edges entering and leaving the node 
     */
    public List<Edge> getIncidentEdges(int n){
        if (getNode(n) == null) return new ArrayList<>();
        Node node = getNode(n);

        List<Edge> res = new ArrayList<>();
        for (List<Edge> edges : adjEdList.values()){
            for (Edge e : edges){
                if (e.to() == node || e.from() == node) res.add(e);
            }
        }
        return res;
    }

    /**
     * for getting the list of all edges going from node u to node v. 
     * N.B. Theoretically, this is the intersection of the out edges from u and the in edges to v; but computing this intersection
     * is not efficient so it is better to simply get among the out edges from u the ones that lead to v.
     * @param u a Node
     * @param v a Node
     * @return the list of all edges going from node u to node v. 
     */
    public List<Edge> getEdges(Node u, Node v){
        List<Edge> res = new ArrayList<>();
        for (List<Edge> edges : adjEdList.values()){
            for (Edge e : edges){
                if (e.from() == u && e.to() == v) res.add(e);
            }
        }
        return res;
    }

    /**
     * for getting the list of all edges going from node u to node v. 
     * N.B. Theoretically, this is the intersection of the out edges from u and the in edges to v; but computing this intersection
     * is not efficient so it is better to simply get among the out edges from u the ones that lead to v.
     * @param u a Node id
     * @param v a Node id
     * @return the list of all edges going from node u to node v. 
     */
    public List<Edge> getEdges(int u, int v){
        if (getNode(u) == null || getNode(v) == null) return new ArrayList<>();
        Node nodeU = getNode(u);
        Node nodeV = getNode(v);

        List<Edge> res = new ArrayList<>();
        for (List<Edge> edges : adjEdList.values()){
            for (Edge e : edges){
                if (e.from() == nodeU && e.to() == nodeV) res.add(e);
            }
        }
        return res;
    }

    /**
     * for getting the list of all the edges of the graph.
     * @return the list of all edges in the graph.
     */
    public List<Edge> getAllEdges(){
        List<Edge> res = new ArrayList<>();
        for (List<Edge> edges : adjEdList.values()) {
            for (Edge e : edges) {
                res.add(e);
            }
        }
        Collections.sort(res);
        return res;
    }

    /**************************
     *                        *
     *   Representation and   *
     * Transformation methods *
     *                        *
     **************************/

     /**
      * for obtaining a representation of the graph in the SA (successor array) formalism
      * For every nodes, iterate through its adjacency edge list, and for each edge,
      * add the 'to' Node id in the result array
      * After checking every edge of a node, simply add a 0 in the result array
      * @return an array of int
      */
    public int[] toSuccessorArray(){
        int[] result = new int[nbEdges() + nbNodes()];
        int count = 0;
        for (Node curr : getAllNodes()) {
            List<Edge> successors = getOutEdges(curr);
            Iterator<Edge> iteEdge = successors.iterator();
            while(iteEdge.hasNext()){
                result[count] = iteEdge.next().to().getId();
                count++;
            }
            result[count] = 0;
            count++;
        }

        if(dev){
            System.out.print("Print of the successor array : [");
            int ite = 0;
            for(; ite < result.length; ite++){
                System.out.print(result[ite] + " | ");
            }
            System.out.print("]");
        }

        return result;
    }

    /**
     * for obtaining a representation of the graph as an adjacency matrix. Multigraphs are
     * allowed, so the elements in the matrix may be greater than 1, indicating the number of edges between any two
     * nodes. Also graphs with self-loops are allowed, thus allowing nonzero diagonal elements
     * The method consist to iterate through every edge of the graph and increment by one the number 
     * at the position [from-1][to-1] of the matrix
     * @return a matrix of int
     */
    public int[][] toAdjMatrix(){
        int[][] adjMatrix = new int[nbNodes()][nbNodes()];
        for (Edge e : getAllEdges()){
            adjMatrix[e.from().getId()-1][e.to().getId()-1]++;
        }
        if (dev) {
            for (int row = 0; row < nbNodes(); row++){
                System.out.println("");
                for (int col = 0; col < nbNodes(); col++){
                    System.out.print(adjMatrix[row][col] + " ");
                }   
            }
            System.out.println("");
        }
        return adjMatrix;
    }

    /**
     * for computing in a new graph the reverse (G−1) of the graph
     * @return a Graph
     */
    public Graph getReverse(){
        Graph reverse = new Graph();

        for (Node n : getAllNodes()) {
            reverse.addNode(n);
        }

        for (Edge e : getAllEdges()) {
            reverse.addEdge(e.getSymmetric());
        }

        return reverse;
    }

    /**
     * for computing in a new graph the transitive closure of the graph
     * https://en.wikipedia.org/wiki/Transitive_closure
     * For each node 'u' of the graph the algorithm do a traversal and every
     * encountered node 'v' becomes a destination of the node u (an edge from u to v is added)
     * @return a Graph
     */
    public Graph getTransitiveClosure(){
        Graph transClosure = new Graph();

        for (Node u : getAllNodes()){
            List<Node> visited = new ArrayList<>();
            Stack<Node> toVisit = new Stack<>();
            toVisit.add(u);
            transClosure.addNode(u.getId());
            while (!toVisit.isEmpty()) {
                Node current = toVisit.pop();
                if(!visited.contains(current)){
                    for (Edge v : getOutEdges(current)){ // here 'v' is an edge but the v from the algorithm is actually the @to of that edge  
                        if(u.getId() != v.to().getId()){
                            if(!transClosure.existsEdge(u.getId(), v.to().getId())){
                                transClosure.addEdge(u.getId(), v.to().getId());
                            }
                            toVisit.remove(v.to());
                            toVisit.add(v.to());
                        }
                    }
                    visited.add(current);
                }
            }
        }

        return transClosure;
    }

    /**
     * for knowing if this is a multi-graph (i.e. it has at least one multi-edge) or not
     * @return true if this graph is a multi-graph, false else
     */
    public boolean isMultiGraph(){
        List<Edge> visited = new ArrayList<>();
        for(Edge e : getAllEdges()){
            if(visited.contains(e)){
                return true;
            }
            visited.add(e);
        }
        return false;
    }

    /**
     * for knowing if this has self-loops or not
     * @return true if this graph has self loops, false else
     */
    public boolean hasSelfLoops(){
        for(Edge e : getAllEdges()){
            if(e.from().equals(e.to())){
                return true;
            }
        }
        return false;
    }

    /**
     * for knowing if this is a simple graph (i.e. it has neither self-loop nor multi-edge) or not
     * @return true if this graph is a simple graph, false else
     */
    public boolean isSimpleGraph(){
        return !(isMultiGraph() || hasSelfLoops());
    }

    /**
     * for transforming the (possibly) multi-graph this into a simple one, by removing its
     * self-loops and multi-edges
     * The algorithm is an iteration over every edge, if the from and to are equal it is deleted
     * and if 
     * @return a Graph 
     */
    public Graph toSimpleGraph(){
        Graph result = this.copy();
        List<Edge> visited = new ArrayList<>();
        //boolean delete = false;
        for(Edge e : result.getAllEdges()){
            if(e.to() == e.from()){
                result.removeEdge(e);
            }else{
                if(visited.contains(e)){
                    result.removeEdge(e);
                }else{
                    visited.add(e);
                }
            }
        }
        return result;
    }

    /**
     * to get a copy of this graph into a new graph
     * @return a Graph
     */
    public Graph copy(){
        Graph copy = new Graph();

        for (Node n : getAllNodes()) {
            copy.addNode(new Node(n.getId(), copy));
        }

        for (Edge e : getAllEdges()) {
            if (!e.isWeighted()) copy.addEdge(new Edge(copy.getNode(e.from().getId()), copy.getNode(e.to().getId()), copy));
            else copy.addEdge(new Edge(copy.getNode(e.from().getId()), copy.getNode(e.to().getId()), e.getWeight(), copy));            
        }

        return copy;
    }

    /**************************
     *                        *
     *     Graph Traversal    *
     *                        *
     **************************/

     /**
      * for getting a Depth-First Search traversal of the graph
      * @return the list of visited node in DFS order
      */
    public List<Node> getDFS(){
        if(getAllNodes().isEmpty()){
            return null;
        }
        return getDFS(getAllNodes().get(0));
    }

    /**
     * for getting a Depth-First Search traversal of the graph, starting from node u
     * The algorithm is not recursive 
     * It uses the LIFO Stack toVisit and a list of visited nodes
     * It first adds all the nodes in decreasing order in toVisit
     * Then while toVisit is not empty, it pops the node on top,
     * put it in the visited list, then for each out-edge
     * it takes the node @to and if it is not already visited, it gets 
     * pushed to the top of the toVisit stack
     * @param u the starting Node
     * @return the list of visited node in DFS order
     */
    public List<Node> getDFS(Node u){
        if(u == null){
            return null;
        }
        List<Node> visited = new ArrayList<>();
        Stack<Node> toVisit = new Stack<>();
        var nodes = getAllNodes();
        nodes.sort(Comparator.reverseOrder());
        toVisit.addAll(nodes);
        toVisit.remove(u);
        toVisit.add(u);
        while (!toVisit.isEmpty()) {
            Node current = toVisit.pop();
            visited.add(current);
            List<Edge> li = getOutEdges(current);
            li.sort(Comparator.reverseOrder()); // Reproducibility
            for(Edge e : li){
                if(!visited.contains(e.to())){
                    // Push e.to() to the top of the toVisit stack
                    toVisit.remove(e.to());
                    toVisit.add(e.to());
                }
            }
        }
        return visited;
    }

    /**
     * for getting a Depth-First Search traversal of the graph, starting from node u
     * @param u an int (the starting Node)
     * @return the list of visited node in DFS order
     */
    public List<Node> getDFS(int u){
        return getDFS(getNode(u));
    }

    /**
     * for getting a Breadth-First Search traversal of the graph
     * @return the list of visited node in BFS order
     */
    public List<Node> getBFS() {
        return getBFS(getAllNodes().get(0));
    }

    /**
     * for getting a Breadth-First Search traversal of the graph, starting from node u
     * @param u the starting Node
     * @return the list of visited node in BFS order
     */
    public List<Node> getBFS(Node u) {
        return getBFS(u, new ArrayList<>());
    }

    /**
     * for getting a Breadth-First Search traversal of the graph, starting from node u
     * @param u an int (the starting Node)
     * @return the list of visited node in BFS order
     */
    public List<Node> getBFS(int u) {
        return getBFS(getNode(u));
    }

    /**
     * for getting a Breadth-First Search traversal of the graph, starting from node u
     * This is the recursive function for BFS
     * @param u an int (the starting Node)
     * @param visited the list of visited nodes
     * @return the list of visited node in BFS order
     */
    public List<Node> getBFS(Node u, List<Node> visited) {
        visited.add(u);
        List<Node> next = new ArrayList<>();
        next.add(u);
        while(!next.isEmpty()){
            Node curr = next.get(0);
            next.remove(0);
            List<Edge> neighbor = adjEdList.get(curr);
            for (Edge e : neighbor) {
                if(!visited.contains(e.to())){
                    visited.add(e.to());
                    next.add(e.to());
                }
            }
        }

        if(visited.size() < getAllNodes().size()){
            for (Node n1 : getAllNodes()) {
                if(!visited.contains(n1)){
                    visited = getBFS(n1, visited);
                }
            }
        }
        return visited;
    }

    /**
     * Get a detailled DFS : characterizing the nodes by their colour (white, gray, black ); 
     * their predecessor in the traversal; their discovery and finish timestamps;
     * the edges by their type (tree, backward, forward or cross edge).
     * The algorithm here is the one from the lecture, a recursive DFS with colour, time and predecessor informations 
     * @param nodeVisit a map of Node, NodeVisitInfo (NodeVisitInfo is a class that encapsulates the colour of a node 
     * (of type enum NodeColour {WHITE, GRAY,BLACK}), its predecessor (of type Node), its discovery and finished timestamps (of type Integer))
     * @param edgeVisit a map of Edge, EdgeVisitType (EdgeVisitType is simply an enum: {TREE, BACKWARD, FORWARD, CROSS})
     * @return the list of visited node in DFS order
     */
    public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) {
        if(getAllNodes().isEmpty()){
            return null;
        }
        return getDFSWithVisitInfo(getAllNodes().get(0), nodeVisit, edgeVisit);
    }

    /**
     * Get a detailled DFS starting from node u : characterizing the nodes by their colour (white, gray, black ); 
     * their predecessor in the traversal; their discovery and finish timestamps;
     * the edges by their type (tree, backward, forward or cross edge).
     * The algorithm here is the one from the lecture, a recursive DFS with colour, time and predecessor informations 
     * @param u the starting Node
     * @param nodeVisit a map of Node, NodeVisitInfo (NodeVisitInfo is a class that encapsulates the colour of a node 
     * (of type enum NodeColour {WHITE, GRAY,BLACK}), its predecessor (of type Node), its discovery and finished timestamps (of type Integer))
     * @param edgeVisit a map of Edge, EdgeVisitType (EdgeVisitType is simply an enum: {TREE, BACKWARD, FORWARD, CROSS})
     * @return the list of visited node in DFS order
     */
    public List<Node> getDFSWithVisitInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) {
        if(u == null){
            return null;
        }
        for(Node n : getAllNodes()){
            nodeVisit.get(n).colour = NodeColour.WHITE;
            nodeVisit.get(n).predecessor = null;
        }
        time = 0;
        List<Node> visited = new ArrayList<>();
        getDFSWithVisitInfo_Visit(u, visited, nodeVisit, edgeVisit);
        time = 0;
        return visited;
    }

    /**
     * Function used for recursivity in the detailled dfs traversal
     * @param u Current Node
     * @param visited the Visited Node Array used as return in the main function
     * @param nodeVisit the map of Node and NodeVisitInfo
     * @param edgeVisit the map of Edge and EdgeVisitType
     */
    public void getDFSWithVisitInfo_Visit(Node u, List<Node> visited, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) {
        time++;
        nodeVisit.get(u).discovery = time;
        nodeVisit.get(u).colour = NodeColour.GRAY;
        visited.add(u);

        List<Edge> li = getOutEdges(u);
        li.sort(Comparator.reverseOrder());
        for(Edge e : li){
            switch(nodeVisit.get(e.to()).colour){
                case BLACK:
                    // Forward or Cross
                    // Not Done : differenciate forward and cross 
                    edgeVisit.put(e, EdgeVisitType.FORWARD);
                    //edgeVisit.put(e, EdgeVisitType.CROSS);
                    break;
                case GRAY:
                    // Backward
                    edgeVisit.put(e, EdgeVisitType.BACKWARD);
                    break;
                case WHITE:
                    // Tree
                    edgeVisit.put(e, EdgeVisitType.TREE);
                    nodeVisit.get(e.to()).predecessor = u;
                    getDFSWithVisitInfo_Visit(e.to(), visited, nodeVisit, edgeVisit);
                    break;
                default:
                    break;
                
            }
        }

        nodeVisit.get(u).colour = NodeColour.BLACK;
        time++;
        nodeVisit.get(u).finished = time;
    }

    /**************************
     *                        *
     *      Graph Import      *
     *       and Export       *
     *                        *
     **************************/

     /**
      * for importing a file in the restricted DOT format.
      * The extension is assumed to be ’.gv’.
      * @param filename a String. The absolute path to the DOT file with no extension
      * @return a Graph
      */
    public static Graph fromDotFile(String filename) {
        return fromDotFile(filename, ".gv");
    }

    /**
      * for importing a file in the restricted DOT format
      * @param filename a String. The absolute path to the DOT file with no extension
      * @param extension a String, The extension of the file
      * @return a Graph
      */
    public static Graph fromDotFile(String filename, String extension) {
        if(!(extension.equals(".gv") || extension.equals(".dot"))){
            return null;
        }
        Graph result = null;
        File newFile = new File(filename + extension);
        try{
            try (Scanner parser = new Scanner(newFile)) {
                while(parser.hasNextLine()){
                    String curr = parser.nextLine().trim();

                    if(curr.charAt(0) == '#' || curr.isEmpty()){
                        continue;
                    }

                    String[] token = curr.split("\\s+");
                    if(curr.contains("{")){
                        if(token.length == 3){
                            if(Objects.equals(token[2], "{")){
                                if(token[0].equals("digraph")){
                                    result = new Graph(token[1]);
                                }else{
                                    return null;
                                }
                            }
                        }else{
                            if(token[0].equals("digraph")){
                                result = new Graph();
                            }else{
                                return null;
                            }
                        }
                    }

                    if(token[token.length - 1].equals("}")){
                        return result;
                    }
                    
                    if(result != null){
                        if(token.length >= 3){
                            if(token[1].equals("->")){
                                int node1 = Integer.parseInt(token[0]);
                                int node2 = Integer.parseInt(token[2]);
                                result.addNode(node1);
                                result.addNode(node2);

                                if(token.length > 3){
                                    result.addEdge(node1, node2, Integer.parseInt(token[token.length - 1].split("=")[1].replace("]", "")));
                                }else{
                                    result.addEdge(node1, node2);
                                }
                            }
                        }else{
                            if(token.length == 1 && token[0].matches("[0-9]+")){
                                result.addNode(Integer.parseInt(token[0]));
                            }
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }catch(FileNotFoundException f){
            throw new RuntimeException(f);
        }
        return result;
    }

    /**
     * for exporting the graph as a String in the DOT syntax
     * @return a String
     */
    public String toDotString() {
        String dotString = "digraph G {";

        List<Node> usedNodes = getAllNodesInEdges();

        for (Node n : getAllNodes()){
            if(adjEdList.get(n).isEmpty()){
                if(!usedNodes.contains(n)){
                    dotString += "\n\t" + n;
                }
            }else{
                for (Edge e : getOutEdges(n)){
                    dotString += "\n\t" + e.from() + " -> " + e.to();
                    if(e.isWeighted()) dotString += " [label=" + e.getWeight() + ", len=" + e.getWeight() + "]";
                }
            }
        }

        dotString += "\n}";
        return dotString;
    }

    /**
     * for exporting the graph as a file in the DOT syntax
     * The extension is assumed to be ’.gv’.
     * @param filename a String. The absolute path to the DOT file with no extension
     */
    public void toDotFile(String filename){
        toDotFile(filename, ".gv");
    }

    /**
     * for exporting the graph as a file in the DOT syntax
     * @param filename a String. The absolute path to the DOT file with no extension
     * @param extension a String, The extension of the file
     */
    public void toDotFile(String filename, String extension) {
        filename += extension;
        try {
            FileWriter dotFileWriter = new FileWriter(filename);
            dotFileWriter.write(toDotString());
            dotFileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred. Could not create the file.");
            e.printStackTrace();
        }
    }
}