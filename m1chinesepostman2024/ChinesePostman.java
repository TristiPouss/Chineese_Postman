package m1chinesepostman2024;

import m1graphs2024.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private List<Pair<Node, Node>> redEdges = new ArrayList<>();
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
            if (dev) System.out.println(redEdges);
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
        // Do not close the scanner, else the main function scanner will crash.
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        
        // Demander à l'utilisateur quelle stratégie utiliser
        System.out.println("\nChoose a strategy :");
        System.out.println("  1 - Greedy strategy");
        System.out.println("  2 - Exhaustive matching strategy");
        System.out.print("Your choice (1 or 2) : ");
    
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1 || choice == 2) {
                    break;
                } else {
                    System.out.print("You must use 1 or 2 : ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number (1 or 2) : ");
            }
        }
        //scanner.close();
        // Appliquer la stratégie choisie
        if (choice == 1) {
            System.out.println("\nUsing Greedy strategy.");
            greedyDuplicateEdgesStrategy(graph);
        } else {
            System.out.println("\nUsing Exhaustive matching strategy .");
            duplicateEdgesStrategy(graph);
            
        }
    
        // Étape 5 : Générer le circuit eulérien avec le graphe modifié
        if (dev) System.out.println("Modified graph: " + graph.toDotString());
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

    private int[][] floydWarshall(UndirectedGraphChinesePostman g) {
        int n = g.getAllNodes().size();
        int[][] dist = new int[n][n];
        int[][] next = new int[n][n];
    
        // Initialisation
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            Arrays.fill(next[i], -1);
            dist[i][i] = 0;
        }
    
        for (Edge e : g.getAllEdges()) {
            int u = g.getAllNodes().indexOf(e.from());
            int v = g.getAllNodes().indexOf(e.to());
            dist[u][v] = e.getWeight();
            dist[v][u] = e.getWeight();
            next[u][v] = v;
            next[v][u] = u;
        }
    
        // Calcul de Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    
        return next; // Retourne la matrice des "next nodes"
    }
    


    private List<Pair<Node, Node>> findMinimalLengthMatching(List<Node> oddNodes, int[][] shortestPaths) {
        List<Pair<Node, Node>> bestMatching = null;
        int bestMatchingWeight = Integer.MAX_VALUE;

        // Generate all pairwise matchings
        List<List<Pair<Node, Node>>> allMatchings = listPairs(oddNodes, new ArrayList<>(), new ArrayList<>());

        // Evaluate each matching
        for (List<Pair<Node, Node>> matching : allMatchings) {
            int weight = calculateMatchingWeight(matching, shortestPaths);
            if (weight < bestMatchingWeight) {
                bestMatching = matching;
                bestMatchingWeight = weight;
            }
        }

        return bestMatching;
    }

    private List<List<Pair<Node, Node>>> listPairs(List<Node> nodes, List<Pair<Node, Node>> currentListOfPairs, List<List<Pair<Node, Node>>> listsOfPairs) {
        if (nodes.isEmpty()) {
            listsOfPairs.add(new ArrayList<>(currentListOfPairs));
        } else {
            Node x = nodes.get(0);
            for (int i = 1; i < nodes.size(); i++) {
                Node y = nodes.get(i);
                currentListOfPairs.add(new Pair<>(x, y));
                List<Node> remainingNodes = new ArrayList<>(nodes);
                remainingNodes.remove(x);
                remainingNodes.remove(y);
                listPairs(remainingNodes, currentListOfPairs, listsOfPairs);
                currentListOfPairs.remove(currentListOfPairs.size() - 1);
            }
        }
        return listsOfPairs;
    }

    private int calculateMatchingWeight(List<Pair<Node, Node>> matching, int[][] shortestPaths) {
        int weight = 0;
        for (Pair<Node, Node> pair : matching) {
            int u = graph.getAllNodes().indexOf(pair.getFirst());
            int v = graph.getAllNodes().indexOf(pair.getSecond());
            weight += shortestPaths[u][v];
        }
        return weight;
    }

    private void exhaustiveMatchingStrategy(UndirectedGraphChinesePostman g) {
        // Step 1: Identify odd-degree nodes
        List<Node> oddNodes = getOddDegreeNodes();
        if (oddNodes.size() % 2 != 0) {
            throw new IllegalArgumentException("Graph must have an even number of odd degree nodes.");
        }

        // Step 2: Compute shortest paths between all pairs of nodes
        int[][] shortestPaths = floydWarshall(g);

        // Step 3: Find minimal-length pairwise matching
        List<Pair<Node, Node>> bestMatching = findMinimalLengthMatching(oddNodes, shortestPaths);

        // Step 4: Duplicate edges for the best matching
        for (Pair<Node, Node> pair : bestMatching) {
            int u = graph.getAllNodes().indexOf(pair.getFirst());
            int v = graph.getAllNodes().indexOf(pair.getSecond());
            duplicateEdgesUsingShortestPath(u, v, shortestPaths);
            System.out.println(redEdges);
        }
    }

    private void duplicateEdgesStrategy(UndirectedGraphChinesePostman g) {
        exhaustiveMatchingStrategy(g);
        
    }

    

    private void duplicateEdgesUsingShortestPath(int u, int v, int[][] shortestPaths) {
        List<Integer> path = reconstructShortestPath(u, v, shortestPaths);
        if (dev) System.out.println("Duplicating path between " + u + " and " + v + ": " + path);
        
        for (int i = 0; i < path.size() - 1; i++) {
            int start = path.get(i);
            int end = path.get(i + 1);
            Node startNode = graph.getAllNodes().get(start);
            Node endNode = graph.getAllNodes().get(end);
    
            // Récupérer toutes les arêtes entre startNode et endNode
            List<Edge> originalEdges = graph.getEdges(startNode, endNode);
            if (!originalEdges.isEmpty()) {
                // Utiliser le poids de la première arête comme référence
                int weight = originalEdges.get(0).getWeight();
    
                // Ajouter une nouvelle arête avec le poids récupéré
                graph.addEdge(startNode, endNode, weight);
                if (startNode.getId()>endNode.getId()){
                    redEdges.add(new Pair<>(endNode,startNode));
                }else{
                    redEdges.add(new Pair<>(startNode, endNode));
                }
                // Ajouter à la liste des arêtes dupliquées (optionnel, pour le suivi)
                
                
            } else {
                throw new IllegalStateException("Aucune arête trouvée entre les nœuds " + start + " et " + end);
            }
            
        }
    }
    

    private List<Integer> reconstructShortestPath(int u, int v, int[][] shortestPaths) {
        List<Integer> path = new ArrayList<>();
        path.add(u);
        while (u != v) {
            u = nextNodeInPath(u, v, shortestPaths);
            path.add(u);
        }
        return path;
    }

    private int nextNodeInPath(int u, int v, int[][] shortestPaths) {
        if (u < 0 || u >= shortestPaths.length || v < 0 || v >= shortestPaths.length) {
            throw new IllegalArgumentException("Indices u or v are out of bounds.");
        }
    
        int nextNode = shortestPaths[u][v];
        if (nextNode < 0 || nextNode >= shortestPaths.length) {
            throw new IllegalStateException("Invalid next node in shortestPaths.");
        }
    
        return nextNode;
    }



    private void greedyDuplicateEdgesStrategy(UndirectedGraphChinesePostman g) {
        // Étape 1 : Identifier les nœuds de degré impair
        List<Node> oddNodes = getOddDegreeNodes();
        if (oddNodes.size() % 2 != 0) {
            throw new IllegalArgumentException("Graph must have an even number of odd degree nodes");
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
                if (dev) System.out.println("Edge duplicated : " + u + " -- " + v);
    
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
        if (dev) System.out.println(duplicatePairs);
        return duplicatePairs;
    }
    
    
    /**
     * for exporting the result graph as a file in the DOT syntax
     * @param filename a String. The absolute path to the DOT file with no extension
     * @param extension a String, The extension of the file
    * @throws IOException 
    */
    public void toDotFile(String filename) throws IOException {
        Files.createDirectories(Paths.get("processed\\"));
        String _filename = "processed\\" + filename + "-processed.gv";
        try {
            FileWriter dotFileWriter = new FileWriter(_filename);
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
        var redEdgesCopy = new ArrayList<>(redEdges);

        for (Node n : graph.getAllNodes()){
            if(graph.getList().get(n).isEmpty()){
                if(!usedNodes.contains(n)){
                    dotString += "\n\t" + n;
                }
            }else{
                for (Edge e : graph.getOutEdges(n)){
                    if(e.from().getId() <= e.to().getId()){
                        dotString += "\n\t" + e.from() + " -- " + e.to();
                        if(e.isWeighted()) {
                            dotString += " [label=" + e.getWeight() + ", len=" + e.getWeight();
                            var p = new Pair<Node, Node>(e.from(), e.to());
                            if (redEdgesCopy.contains(p)) {
                                dotString += ", color=red, fontcolor=red";
                                redEdgesCopy.remove(p);
                            } 
                            dotString += "]";
                        }
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
        
        // Process of length/cost
        int trailSize = 0;
        int extraCost = 0;
        var redEdgesCopy2 = new ArrayList<>(redEdges);
        for(Edge e : trailCircuit){
            // Total Length
            trailSize += e.getWeight();
            // Extra cost
            for(var v : redEdgesCopy2){
                if(v.getFirst().getId() == e.from().getId()
                && v.getSecond().getId() == e.to().getId()){
                    extraCost += e.getWeight();
                    redEdgesCopy2.remove(v);
                    break;
                }
            }
        }

        // Total length
        dotString += "\n\tTotal length : " + trailSize;
        
        // Extra cost if chinese circuit
        if(type == Type.NON_EULERIAN){
            dotString += "\n\tExtra cost : " + extraCost;
        }

        dotString +="\"";

        dotString += "\n}";
        return dotString;
    }
    
}