package m1chinesepostman2024;

import m1graphs2024.*;

import java.io.*;
import java.util.*;

/** 
 * @author Tristan de Saint Gilles
 * @author Anton Dolard
 */
public class ChinesePostman {
    /**
     * Variable used in developpement to print debug information or anything related to testing
     * @hidden SET TO FALSE BEFORE EXPORT
     */
    public static final boolean dev = false;

    private final UndirectedGraphChinesePostman graph;
    private Type type;
    private List<Edge> trailCircuit;

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
            type = Type.EULERIAN;
            computeEulerianCircuit();
        } else if (isSemiEulerian()) {
            System.out.println("Graph is Semi-Eulerian.");
            type = Type.SEMI_EULERIAN;
            computeEulerianTrail();
        } else {
            System.out.println("Graph is Non-Eulerian.");
            type = Type.NON_EULERIAN;
            computeChinesePostmanCircuit();
        }
    }

    private void computeEulerianCircuit() {
        List<Edge> circuit = eulerianTrail(graph.getAllNodes().get(0));
        System.out.println("Eulerian Circuit: " + circuit);
    }

    private void computeEulerianTrail() {
        List<Node> oddNodes = getOddDegreeNodes();
        if (type != Type.SEMI_EULERIAN) {
            System.out.println("Graph is not Semi-Eulerian.");
            return;
        }
    
        Node start = oddNodes.get(0);
        List<Edge> trail = eulerianTrail(start);
        System.out.println("Eulerian Trail: " + trail);
    }
    

    private void computeChinesePostmanCircuit() {
        List<Node> oddNodes = getOddDegreeNodes();
        if (oddNodes.isEmpty()) {
            System.out.println("Graph is already Eulerian.");
            computeEulerianCircuit();
            return;
        }
    
        // Étape 1 : Calcul des plus courts chemins
        int size = graph.getAllNodes().size();
        int[][] shortestPaths = floydWarshall(graph);
    
        // Étape 2 : Associer les sommets de degré impair
        List<Integer> oddIndexes = new ArrayList<>();
        for (Node oddNode : oddNodes) {
            oddIndexes.add(graph.getAllNodes().indexOf(oddNode));
        }
    
        int[] match = findMinCostMatching(oddIndexes, shortestPaths);
    
        // Étape 3 : Dupliquer les arêtes pour les sommets appariés
        for (int i = 0; i < match.length; i++) {
            if (match[i] > i) {
                duplicateEdges(oddIndexes.get(i), oddIndexes.get(match[i]), shortestPaths);
            }
        }
    
        // Étape 4 : Trouver un circuit eulérien
        computeEulerianCircuit();
    }
    
    
    private List<Edge> eulerianTrail(Node start) {
        if (dev) System.out.println("\nLoading Circuit/Trail...\n");
        UndirectedGraphChinesePostman copy = graph.copy();
        if (dev) System.out.println(copy.getList());
        List<Edge> trail = new ArrayList<Edge>();
        
        eulerianTrailRecur(trail, copy, copy.getNode(start.getId()));

        Collections.reverse(trail);
        trailCircuit = trail;

        return trail;

    }

    private void eulerianTrailRecur(List<Edge> trail, UndirectedGraphChinesePostman g, Node u){
        if (dev) System.out.println(" Curr : " + u);
        List<Node> neighbors = g.getNeighbors(u);
        if (dev) System.out.println("  Neighbors : " + neighbors);

        for(Node v : neighbors){
            if(isValidNextEdge(g, u, v)){
                Edge e = g.getEdges(u, v).get(0);
                if (dev) System.out.println("  "+e);
                g.removeEdge(e);
                if (dev) System.out.println("  "+g.getList());
                eulerianTrailRecur(trail, g, v);
                trail.add(e);
            }
        }
    }

    private boolean isValidNextEdge(UndirectedGraphChinesePostman g, Node u, Node v) {
        // The edge u-v is valid in one of the
        // following two cases:
 
        // 1) If v is the only adjacent vertex of u
        // ie size of adjacent vertex list is 1
        if (g.degree(u) == 1) {
            return true;
        }
 
        // 2) If there are multiple adjacents, then
        // u-v is not a bridge Do following steps
        // to check if u-v is a bridge
        // 2.a) count of vertices reachable from u
        int count1 = g.getDFS(u).size();

        // 2.b) Remove edge (u, v) and after removing
        //  the edge, count vertices reachable from u
        if(g.getEdges(u, v).isEmpty()) return false;
        Edge e = g.getEdges(u, v).get(0);
        g.removeEdge(e);
        int count2 = g.getDFS(u).size();
 
        // 2.c) Add the edge back to the graph
        g.addEdge(e);
        return (count1 > count2) ? false : true;
    }

    private int[][] floydWarshall(UndirectedGraph graph) {
        int size = graph.getAllNodes().size();
        int[][] dist = new int[size][size];
    
        // Initialiser les distances
        for (int i = 0; i < size; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            dist[i][i] = 0;
        }
    
        for (Edge edge : graph.getAllEdges()) {
            int u = graph.getAllNodes().indexOf(edge.from());
            int v = graph.getAllNodes().indexOf(edge.to());
            dist[u][v] = dist[v][u] = edge.getWeight(); // Ajouter poids des arêtes
        }
    
        // Floyd-Warshall
        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE) {
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
        Arrays.fill(match, -1);
    
        // Approche brute-force pour trouver le couplage minimal
        int minCost = Integer.MAX_VALUE;
        int[] bestMatch = null;
    
        List<int[]> permutations = generatePermutations(oddNodes);
        for (int[] permutation : permutations) {
            int cost = 0;
            for (int i = 0; i < permutation.length; i += 2) {
                cost += shortestPaths[permutation[i]][permutation[i + 1]];
            }
            if (cost < minCost) {
                minCost = cost;
                bestMatch = permutation;
            }
        }
    
        // Convertir permutation en couplage
        for (int i = 0; i < bestMatch.length; i += 2) {
            match[bestMatch[i]] = bestMatch[i + 1];
            match[bestMatch[i + 1]] = bestMatch[i];
        }
    
        return match;
    }
    private List<int[]> generatePermutations(List<Integer> oddNodes) {
        List<int[]> permutations = new ArrayList<>();
        permute(oddNodes, 0, permutations);
        return permutations;
    }
    
    private void permute(List<Integer> nodes, int start, List<int[]> result) {
        if (start == nodes.size()) {
            // Ajouter une copie de la permutation actuelle
            result.add(nodes.stream().mapToInt(Integer::intValue).toArray());
            return;
        }
        for (int i = start; i < nodes.size(); i++) {
            // Échanger les éléments
            Collections.swap(nodes, start, i);
            permute(nodes, start + 1, result);
            // Restaurer l'ordre original
            Collections.swap(nodes, start, i);
        }
    }
    
    
    
 
    
    private void duplicateEdges(int u, int v, int[][] shortestPaths) {
       
        System.out.println("Duplicating edges between " + u + " and " + v);
    }

    /**
     * for exporting the result graph as a file in the DOT syntax
     * @param filename a String. The absolute path to the DOT file with no extension
     * @param extension a String, The extension of the file
     */
    public void toDotFile(String filename) {
        filename += "-processed.gv";
        try {
            FileWriter dotFileWriter = new FileWriter(filename);
            dotFileWriter.write(toDotString());
            dotFileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred. Could not create the file.");
            e.printStackTrace();
        }
    }
    
    private String toDotString() {
        String dotString = "graph {";

        List<Node> usedNodes = graph.getAllNodesInEdges();

        for (Node n : graph.getAllNodes()){
            if(graph.getList().get(n).isEmpty()){
                if(!usedNodes.contains(n)){
                    dotString += "\n\t" + n;
                }
            }else{
                for (Edge e : graph.getOutEdges(n)){
                    if(e.from().getId() <= e.to().getId()){
                        dotString += "\n\t" + e.from() + " -- " + e.to();
                        if(e.isWeighted()) dotString += " [label=" + e.getWeight() + ", len=" + e.getWeight() + "]";
                    }
                }
            }
        }

        // Type
        dotString += "\n\tlabel=\"Type : " + type;
        
        // Circuit / Trail
        String s = "";
        switch (type) {
            case EULERIAN:
                s = "Eulerian Circuit";
                break;
            case SEMI_EULERIAN:
                s = "Eulerian Trail";
                break;
            case NON_EULERIAN:
                s = "Chinese Circuit";
                break;
        }
        dotString += "\n\t" + s + " : [";
        int count = 0;
        Iterator<Edge> iterator = trailCircuit.iterator();
        while(iterator.hasNext()){
            Edge e = iterator.next();
            if(count == 4) {
                dotString+="\n\t";
                count = 0;
            }
            if(iterator.hasNext()){
                dotString+= e.from() + "-(" + e.getWeight() + ")-" + e.to() + ", ";
            } else {
                dotString+= e.from() + "-(" + e.getWeight() + ")-" + e.to();
            }
            count++;
        }
        dotString += "]";
        
        // Total length
        dotString += "\n\tTotal length : " + trailCircuit.size();
        
        // Extra cost if chinese circuit
        if(type == Type.NON_EULERIAN){
            dotString += "\n\tExtra cost : " + 0;
        }

        dotString +="\"";

        dotString += "\n}";
        return dotString;
    }
}