package m1chinesepostman2024;

import m1graphs2024.*;
import java.util.*;

public class ChinesePostman {
    /**
     * Variable used in developpement to print debug information or anything related to testing
     * @hidden SET TO FALSE BEFORE EXPORT
     */
    public static final boolean dev = true;
    private List<Pair<Node, Node>> redEdges = new ArrayList<>();

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
            System.out.println(redEdges);
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
        // Vérifier si le graphe est déjà eulérien
        if (isEulerian()) {
            System.out.println("Graph is already Eulerian.");
            computeEulerianCircuit();
            return;
        }
    
        if (graph.getAllNodes().size() < 10) { // Par exemple, seuil pour utiliser la stratégie complète
            System.out.println("Utilisation de la stratégie de couplage minimal (exhaustive).");
            duplicateEdgesStrategy(graph);
        } else {
            System.out.println("Utilisation de la stratégie gloutonne.");
            greedyDuplicateEdgesStrategy(graph);
        }
    
        // Étape 5 : Générer le circuit eulérien avec le graphe modifié
        System.out.println("Modified graph: " + graph.toDotString());
        computeEulerianCircuit();
    }
    
    

    private List<Node> eulerianTrail(UndirectedGraphChinesePostman g, Node start) {
        List<Node> trail = new ArrayList<>();
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(start);
    
        while (stack.isEmpty()) {
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

    

    
    
 
    
    private void duplicateEdges(int u, int v, int[][] shortestPaths) {
        List<Integer> path = reconstructPath(u, v, shortestPaths);
        System.out.println("Duplicating path between " + u + " and " + v + ": " + path);
        for (int i = 0; i < path.size() - 1; i++) {
            Node from = graph.getAllNodes().get(path.get(i));
            Node to = graph.getAllNodes().get(path.get(i + 1));
            int weight = shortestPaths[path.get(i)][path.get(i + 1)];
            graph.addEdge(from, to, weight);
            redEdges.add(new Pair<>(from, to));

        }
    }




    
    private List<Integer> reconstructPath(int u, int v, int[][] shortestPaths) {
        List<Integer> path = new ArrayList<>();
        path.add(u);
        if (u != v) path.add(v);
        return path;
    }

    private void duplicateEdgesStrategy(UndirectedGraphChinesePostman g) {
        // Étape 1 : Identifier les nœuds de degré impair
        List<Node> oddNodes = getOddDegreeNodes();
        if (oddNodes.size() % 2 != 0) {
            throw new IllegalArgumentException("Le graphe doit avoir un nombre pair de nœuds impairs.");
        }
    
        // Étape 2 : Trouver les paires de nœuds impairs et les dupliquer
        List<Pair<Node, Node>> duplicatePairs = findExistingEdgesToDuplicate(oddNodes);
    
        // Étape 3 : Dupliquer les arêtes existantes
        while (!oddNodes.isEmpty()) {  // Continuer tant qu'il y a des nœuds impairs
            Pair<Node, Node> pair = null;
            for (Pair<Node, Node> candidate : duplicatePairs) {
                Node u = candidate.getFirst();
                Node v = candidate.getSecond();
                // Si les deux nœuds de la paire sont impairs, les dupliquer
                if (oddNodes.contains(u) && oddNodes.contains(v)) {
                    pair = candidate;
                    break;
                }
            }
            
            if (pair != null) {
                Node u = pair.getFirst();
                Node v = pair.getSecond();
                assert (!graph.getEdges(u, v).isEmpty());
    
                // Dupliquer l'arête entre u et v
                Edge e = graph.getEdges(u, v).get(0);
                graph.addEdge(u, v, e.getWeight());  // Ajouter une arête dupliquée
                redEdges.add(pair);
                System.out.println("Arête dupliquée : " + u + " -- " + v);
    
                // Mettre à jour les nœuds impairs
                if (u.degree() % 2 == 0) {
                    oddNodes.remove(u);
                }
                if (v.degree() % 2 == 0) {
                    oddNodes.remove(v);
                }
            } else {
                // Si aucune paire n'est disponible, on peut arrêter (cas étrange)
                break;
            }
        }
    }
    
    
    private List<Pair<Node, Node>> findExistingEdgesToDuplicate(List<Node> oddNodes) {
        List<Pair<Node, Node>> duplicatePairs = new ArrayList<>();
    
        // Pour chaque paire de nœuds impairs, cherchez une arête existante
        for (int i = 0; i < oddNodes.size(); i++) {
            for (int j = i + 1; j < oddNodes.size(); j++) {
                Node u = oddNodes.get(i);
                Node v = oddNodes.get(j);
                // Si une arête existe déjà entre u et v, on l'ajoute à la liste des arêtes à dupliquer
                if (graph.getNeighbors(u).contains(v)) {
                    duplicatePairs.add(new Pair<>(u, v));
                }
            }
        }
        System.out.println(duplicatePairs);
        return duplicatePairs;
    }
    
    
    
    
    private void greedyDuplicateEdgesStrategy(UndirectedGraphChinesePostman g) {
        // Étape 1 : Identifier les nœuds de degré impair
        List<Node> oddNodes = getOddDegreeNodes();
        if (oddNodes.size() % 2 != 0) {
            throw new IllegalArgumentException("Le graphe doit avoir un nombre pair de nœuds impairs.");
        }
    
        // Étape 2 : Calculer les plus courts chemins entre chaque paire de nœuds impairs
        int[][] shortestPaths = floydWarshall(g);
        List<Integer> oddNodeIndices = new ArrayList<>();
        for (Node node : oddNodes) {
            oddNodeIndices.add(g.getAllNodes().indexOf(node));
        }
    
        // Étape 3 : Construire une liste de paires triées par coût croissant
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        for (int i = 0; i < oddNodeIndices.size(); i++) {
            for (int j = i + 1; j < oddNodeIndices.size(); j++) {
                int u = oddNodeIndices.get(i);
                int v = oddNodeIndices.get(j);
                pairs.add(new Pair<>(u, v));
            }
        }
        // Trier les paires par le coût des plus courts chemins
        pairs.sort(Comparator.comparingInt(pair -> shortestPaths[pair.getFirst()][pair.getSecond()]));
    
        // Étape 4 : Appariement des nœuds
        boolean[] matched = new boolean[g.getAllNodes().size()];
        for (Pair<Integer, Integer> pair : pairs) {
            int u = pair.getFirst();
            int v = pair.getSecond();
            if (!matched[u] && !matched[v]) {
                duplicateEdges(u, v, shortestPaths);
                matched[u] = true;
                matched[v] = true;
            }
        }
    }
    
    
}