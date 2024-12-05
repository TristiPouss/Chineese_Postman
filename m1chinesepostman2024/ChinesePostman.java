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
        List<Node> circuit = eulerianTrail(graph, graph.getAllNodes().get(0));
        System.out.println("Eulerian Circuit: " + circuit);
    }

    private void computeEulerianTrail() {
        List<Node> oddNodes = getOddDegreeNodes();
        if (oddNodes.size() != 2) {
            System.out.println("Graph is not Semi-Eulerian.");
            return;
        }
    
        Node start = oddNodes.get(0);
        List<Node> trail = eulerianTrail(graph, start);
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
    
    

    private List<Node> eulerianTrail(UndirectedGraphChinesePostman g, Node start) {
        List<Node> trail = new ArrayList<>();
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(start);
    
        while (!stack.isEmpty()) {
            Node current = stack.peek();
            List<Node> neighbors = g.getNeighbors(current); // Méthode corrigée
    
            if (neighbors.isEmpty()) {
                trail.add(stack.pop());
            } else {
                Node neighbor = neighbors.get(0); // Prendre le premier voisin disponible
                g.removeEdge(current, neighbor);
                stack.push(neighbor);
            }
        }
    
        Collections.reverse(trail); // Si un circuit eulérien est attendu
        return trail;
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
    
}

