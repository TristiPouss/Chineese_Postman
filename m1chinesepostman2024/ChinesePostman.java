package m1chinesepostman2024;

import m1graphs2024.*;
import java.util.*;

public class ChinesePostman {
    /**
     * Variable used in developpement to print debug information or anything related to testing
     * @hidden SET TO FALSE BEFORE EXPORT
     */
    public static final boolean dev = true;

    private final UndirectedGraphChinesePostman graph;

    public ChinesePostman(UndirectedGraphChinesePostman graph) {
        this.graph = graph;
    }

    public boolean isEulerian() {
        if (dev) System.out.println("isEulerian");
        for(Node n : graph.getAllNodes()){
            if (dev) System.out.println("  "+ n +" : "+ n.degree());
            if(n.degree() % 2 != 0) return false;
        }
        return true;
    }

    public boolean isSemiEulerian() {
        if (dev) System.out.println("isSemiEulerian");
        long oddCount = 0;
        for(Node n : graph.getAllNodes()){
            if (dev) System.out.println("  "+ n +" : "+ n.degree());
            if(n.degree() % 2 != 0) oddCount++;
        }
        return oddCount == 2;
    }

    public List<Node> getOddDegreeNodes() {
        List<Node> oddNodes = new ArrayList<>();
        for (Node node : graph.getAllNodes()) {
            if (graph.degree(node) % 2 != 0) {
                oddNodes.add(node);
            }
        }
        return oddNodes;
    }

    public void solve() {
        if (isEulerian()) {
            System.out.println("Graph is Eulerian.");
            computeEulerianCircuit();
        } else if (isSemiEulerian()) {
            System.out.println("Graph is Semi-Eulerian.");
            computeEulerianTrail();
        } else {
            System.out.println("Graph is Non-Eulerian.");
            computeChinesePostmanCircuit();
        }
    }

    private void computeEulerianCircuit() {
        System.out.println("Computing Eulerian Circuit...");
        /*List<Integer> circuit = new ArrayList<>();
        Map<Pair<Integer, Integer>, Boolean> visitedEdges = new HashMap<>(eul);
    
        for (Pair<Integer, Integer> edge : graph.getEdges()) {
            visitedEdges.put(edge, false);
        }
    
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(graph.getAllNodes().get(0)); // Start from any node.
    
        while (!stack.isEmpty()) {
            int current = stack.peek();
    
            // Find an unvisited edge from the current node
            boolean found = false;
            for (Pair<Integer, Integer> neighbor : graph.adjacencyList.get(current)) {
                Pair<Integer, Integer> edge = new Pair<>(Math.min(current, neighbor.getFirst()), Math.max(current, neighbor.getFirst()));
                if (!visitedEdges.get(edge)) {
                    stack.push(neighbor.getFirst());
                    visitedEdges.put(edge, true);
                    found = true;
                    break;
                }
            }
    
            if (!found) {
                circuit.add(stack.pop());
            }
        }
    
        System.out.println("Eulerian Circuit: " + circuit);*/
    }
    

    private void computeEulerianTrail() {
        System.out.println("Computing Eulerian Trail...");
        /*List<Integer> trail = new ArrayList<>();
        Map<Pair<Integer, Integer>, Boolean> visitedEdges = new HashMap<>();
    
        for (Pair<Integer, Integer> edge : graph.getEdges()) {
            visitedEdges.put(edge, false);
        }
    
        // Find the two odd degree nodes
        List<Integer> oddNodes = getOddDegreeNodes();
        int startNode = oddNodes.get(0); // Start from any of the odd degree nodes.
    
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(startNode);
    
        while (!stack.isEmpty()) {
            int current = stack.peek();
    
            // Find an unvisited edge from the current node
            boolean found = false;
            for (Pair<Integer, Integer> neighbor : graph.adjacencyList.get(current)) {
                Pair<Integer, Integer> edge = new Pair<>(Math.min(current, neighbor.getFirst()), Math.max(current, neighbor.getFirst()));
                if (!visitedEdges.get(edge)) {
                    stack.push(neighbor.getFirst());
                    visitedEdges.put(edge, true);
                    found = true;
                    break;
                }
            }
    
            if (!found) {
                trail.add(stack.pop());
            }
        }
    
        System.out.println("Eulerian Trail: " + trail);*/
    }
    

    private void computeChinesePostmanCircuit() {
        System.out.println("Computing Chinese Postman Circuit...");
        
        /*List<Integer> oddNodes = getOddDegreeNodes();
        int n = oddNodes.size();
    
        // If already Eulerian
        if (n == 0) {
            computeEulerianCircuit();
            return;
        }
    
        // Compute shortest path between all odd nodes (Floyd-Warshall)
        int[][] shortestPaths = floydWarshall(graph);
    
        // Find minimum-cost pairing of odd nodes
        int[] match = findMinCostMatching(oddNodes, shortestPaths);
    
        // Duplicate edges to make graph Eulerian
        for (int i = 0; i < match.length; i += 2) {
            int u = oddNodes.get(match[i]);
            int v = oddNodes.get(match[i + 1]);
            duplicateEdges(u, v, shortestPaths);
        }
    
        // Now compute the Eulerian Circuit
        computeEulerianCircuit();*/
    }
    
    private int[][] floydWarshall(UndirectedGraph graph) {
        int size = graph.getAllNodes().size();
        int[][] dist = new int[size][size];
    
        // Initialize distances
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        for (int i = 0; i < size; i++) dist[i][i] = 0;
    
        for (Pair<Integer, Integer> edge : graph.getEdges()) {
            int u = edge.getFirst(), v = edge.getSecond();
            dist[u][v] = dist[v][u] = graph.getEdgeWeight(u, v);
        }
    
        // Floyd-Warshall algorithm
        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (dist[i][k] < Integer.MAX_VALUE && dist[k][j] < Integer.MAX_VALUE) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }
        return dist;
    }
    
    private int[] findMinCostMatching(List<Integer> oddNodes, int[][] shortestPaths) {
        int n = oddNodes.size();
        int[] match = new int[n];
    
        // Use a dynamic programming algorithm or brute force for small n
        // Implementation depends on n. For simplicity:
        // Match nodes with greedy or similar approach.
    
        // Placeholder: Implement pairing logic here.
        return match;
    }
    
    private void duplicateEdges(int u, int v, int[][] shortestPaths) {
        // Add edges along the shortest path between u and v to the graph.
        // Placeholder: Implement edge duplication logic.
        System.out.println("Duplicating edges between " + u + " and " + v);
    }
    
}

