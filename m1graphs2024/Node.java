package m1graphs2024;

import java.util.*;

/**
 * The class Node is used to define the Node of a Graph
 * @author Tristan de Saint Gilles
 * @author Renaud Joffrin
 */
public class Node implements Comparable<Node>{

    /* Attributes */

    /**
     * id is an identifier
     * Two nodes in the same graph can't have the same id
     * Two nodes in different graphs can have the same id 
     */
    private final int id;

    /**
     * A node is not obligated to have a name, it can have it or not depend of the constructor 
     */
    @SuppressWarnings("unused")
    private String name;

    /**
     * graphHolder is a reference to the graph where the node belongs
     */
    private final Graph graphHolder;

    /* Constructors */

    /**
     * Constructor for a node without a name
     * @param id            The id of the Node
     * @param graphHolder   The reference to the graph where the node belongs
     * If the graph already contain a Node with this id the node isn't created. 
     * If the graph don't have the id, the programm create a node and add it to the graph
     */
    public Node(int id, Graph graphHolder){
        this.id = id;
        this.graphHolder = graphHolder;
    }
    
    /**
     * Constructor for a node with a name
     * @param id            The id of the Node
     * @param name          The name of the node
     * @param graphHolder   The reference to the graph where the node belongs
     * If the graph already contain a Node with this id the node isn't created. 
     * If the graph don't have the id, the programm create a node and add it to the graph
     */
    public Node(int id, String name, Graph graphHolder){
        this.id = id;
        this.name = name;
        this.graphHolder = graphHolder;
    }

    /**
     * Constructor for a node without a name and the id specified
     * @param graphHolder
     * Create a node in the graph with the addition between the largest node id in the graph and 1 as the id.
     */
    public Node(Graph graphHolder){
        this.graphHolder = graphHolder;
        this.id = graphHolder.largestNodeId() + 1;
    }
    
    /* API */

    /**
     * Getter for id
     * @return an int
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for graphHolder
     * @return a graph
     */
    public Graph getGraph() {
        return graphHolder;
    }

    /**
     * The goal is getting a list without duplicates of the successors (or neighbours in the undirected case) of node this.
     * @return a list of all the successors without duplicate
     */
    public List<Node> getSuccessors(){
        return this.graphHolder.getSuccessors(this);
    }

    /**
     * The goal is getting a list with possible duplicates of the successors (or neighbours in the undirected case) of node this. 
     * @return a list of all the successors with possible duplicates
     */
    public List<Node> getSuccessorsMulti(){
        return this.graphHolder.getSuccessorsMulti(this);
    }

    /**
     * Test if a Node is adjacent to this node. 
     * @param u is a node 
     * @return true if u is adjacent to this node, false else
     */
    public boolean adjacent(Node u){
        return this.graphHolder.adjacent(this, u);
    }

    /**
     * Test if a Node is adjacent to this node. 
     * @param id is the id of a node 
     * @return true if u is adjacent to this node, false else
     */
    public boolean adjacent(int id){
        return this.graphHolder.adjacent(this.id, id);
    }

    /**
     * for knowing the in-degree of this node. 
     * @return an int
     */
    public int inDegree(){
        return this.graphHolder.inDegree(this);
    }
    
    /**
     * for knowing the out-degree this node. 
     * @return an int
     */
    public int outDegree(){
        return this.graphHolder.outDegree(this);
    }

    /**
     * for knowing the degree of this node. 
     * @return an int
     */
    public int degree(){
        return this.graphHolder.degree(this);
    }

    /**
     * For getting the list of edges leaving the Node
     * @return the list of edges
     */
    public List<Edge> getOutEdges(){
        return this.graphHolder.getOutEdges(this);
    }   

    /**
     * For getting the list of edges entering the Node
     * @return the list of edges
     */
    public List<Edge> getInEdges(){
        return this.graphHolder.getInEdges(this);
    }

    /**
     * for getting the list of all edges incident to this node. 
     * This is the union of the out and in edges
     * @return the list of edges entering and leaving the node 
     */
    public List<Edge> getIncidentEdges(){
        return this.graphHolder.getIncidentEdges(this);
    }

    /**
     * for getting the list of all edges going from this node to node u. 
     * N.B. Theoretically, this is the intersection of the out edges from this node and the in edges to u; but computing this intersection
     * is not efficient so it is better to simply get among the out edges from this node the ones that lead to u.
     * @param u a Node
     * @return the list of all edges going from this node to node u. 
     */
    public List<Edge> getEdgesTo(Node u){
        return this.graphHolder.getEdges(this, u);
    }

    /* Overrides */

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(this.getClass() != obj.getClass()){
            return false;
        }
        Node other = (Node) obj;
        return this.id == other.getId() && this.graphHolder.equals(other.getGraph());
    }

    @Override
    public int hashCode(){
        int hash = 1;
        hash = hash * 17 * id;
        hash = hash * 17 * (graphHolder != null ? graphHolder.hashCode() : 0);
        return hash;
    }


    @Override
    public int compareTo(Node o) {
        return id - o.getId();
    }

    @Override
    public String toString() {
        return "" + id;
    }

    
}