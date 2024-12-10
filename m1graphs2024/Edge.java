package m1graphs2024;

import java.util.Objects;

/**
 * The class Edge aims at physically coding edges as object
 * @author Tristan de Saint Gilles
 * @author Renaud Joffrin
 */
public class Edge implements Comparable<Edge>{

    /* Attributes */
    
    /**
     * The node from where the edge starts
     */
	private Node from;
    /**
     * The node where the edge is going to
     */
    private Node to;
    /**
     * The weight of the edge
     * 
     * 0 and negative values are allowed
     * 
     * Can be null if edge is not weighted
     */
    private Integer weight;
    /**
     * 
     */
    private Graph graphHolder;

    /* Constructors */

    /**
     * Constructor for a non weighted Edge
     * @param from the Node where it begins
     * @param to the Node where it ends
     * @param graphHolder the Graph of this Edge
     * @throws IllegalArgumentException if any of the arguments are null 
     * or if the graph of @from is different from the one of @to
     */
    public Edge(Node from, Node to, Graph graphHolder) {  //
        if(graphHolder == null || from == null || to == null){
            throw new IllegalArgumentException("Invalid arguments for edge.");
        }
        if(graphHolder.getNode(from.getId()) == null) graphHolder.addNode(from.getId());
        if(graphHolder.getNode(to.getId()) == null) graphHolder.addNode(to.getId());
        this.graphHolder = graphHolder;
        this.from = from;
        this.to = to;
        weight = null;
    }

    /**
     * Constructor for a weighted Edge
     * @param from the Node where it begins
     * @param to the Node where it ends
     * @param weight the weight of the Edge
     * @param graphHolder the Graph of this Edge
     * @throws IllegalArgumentException if any of the arguments are null 
     * or if the graph of @from is different from the one of @to
     */
    public Edge(Node from, Node to, int weight, Graph graphHolder) {
        if(graphHolder == null || from == null || to == null){
            throw new IllegalArgumentException("Invalid arguments for edge.");
        }
        if(graphHolder.getNode(from.getId()) == null) graphHolder.addNode(from.getId());
        if(graphHolder.getNode(to.getId()) == null) graphHolder.addNode(to.getId());
        this.graphHolder = graphHolder;
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * Constructor by ids for a non-weighted Edge
     * @param from the Node where it begins
     * @param to the Node where it ends
     * @param graphHolder the Graph of this Edge
     * @throws IllegalArgumentException if any of the arguments are null 
     * or if the graph of @from is different from the one of @to
     */
    public Edge(int from, int to, Graph graphHolder) {
        if(graphHolder == null){
            throw new IllegalArgumentException("Invalid arguments for edge.");
        }
        if(graphHolder.getNode(from) == null) graphHolder.addNode(from);
        if(graphHolder.getNode(to) == null) graphHolder.addNode(to);
        this.graphHolder = graphHolder;
        this.from = graphHolder.getNode(from);
        this.to = graphHolder.getNode(to);
        this.weight = null;
    }

    /**
     * Constructor by ids for a weighted Edge
     * @param from the Node where it begins
     * @param to the Node where it ends
     * @param weight the weight of the Edge
     * @param graphHolder the Graph of this Edge
     * @throws IllegalArgumentException 
     * if any of the arguments are null or
     * if the graph of @from is different from the one of @to
     */
    public Edge(int from, int to, int weight, Graph graphHolder) {
        if(graphHolder == null){
            throw new IllegalArgumentException("Invalid arguments for edge.");
        }
        if(graphHolder.getNode(from) == null) graphHolder.addNode(from);
        if(graphHolder.getNode(to) == null) graphHolder.addNode(to);
        this.graphHolder = graphHolder;
        this.from = graphHolder.getNode(from);
        this.to = graphHolder.getNode(to);
        this.weight = weight;
    }

    /* API */

    /**
     * Getter for from
     * @return a Node
     */
    public Node from(){
        return from;
    }

    /**
     * Getter for to
     * @return a Node
     */
    public Node to(){
        return to;
    }

    /**
     * Getter for to
     * @return a Node
     */
    public Graph getGraph(){
        return graphHolder;
    }

    /**
     * Get symmetric, reverse @from and @to 
     * @return a new Edge
     */
    public Edge getSymmetric(){
        if (!isWeighted()) return new Edge(this.to, this.from, this.graphHolder);
        else return new Edge(this.to, this.from, this.weight, this.graphHolder);
    }

    /**
     * Check if self loop, @from equals @to
     * @return true if self loop, false else
     */
    public boolean isSelfLoop(){
        return this.from.equals(this.to);
    }

    /**
     * Check if weighht is not null
     * @return true if weighted, false else
     */
    public boolean isWeighted(){
        return weight != null;
    }

    /**
     * Get the weight of the Edge
     * @return the Integer @weight
     */
    public Integer getWeight() {
        return weight;
    }

    /* Overrides */

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        
        Edge other = (Edge) obj;

        return Objects.equals(from, other.from()) && Objects.equals(to, other.to())  && 
                ((!isWeighted() && !other.isWeighted()) || Objects.equals(weight, other.getWeight()));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash * (this.from != null ? this.from.hashCode() : 0);
        hash = 31 * hash * (this.to != null ? this.to.hashCode() : 0);
        hash = 31 * hash * (isWeighted() ? this.weight : 0);
        hash = 31 * hash * (this.graphHolder != null ? this.graphHolder.hashCode() : 0);
        return hash;
    }

	@Override
	public int compareTo(Edge o) {
        if (from.getId() != o.from().getId()) return from.getId() - o.from().getId();
        if (to.getId() != o.to().getId()) return to.getId() - o.to().getId(); 
        if (isWeighted() && o.isWeighted() && !Objects.equals(weight, o.getWeight())) return weight - o.getWeight();
        return 0;
	}

    @Override
    public String toString() {
        return from + "->" + to;
    }

}