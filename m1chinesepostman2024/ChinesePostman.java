package m1chinesepostman2024;

import m1graphs2024.*;
import java.util.*;

public class ChinesePostman {
    private final UndirectedGraph graph;

    public ChinesePostman(UndirectedGraph graph) {
        this.graph = graph;
    }

    public boolean isEulerian() {
        for(Node n : graph.getAllNodes()){
            if(n.degree() % 2 != 0) return false;
        }
        return true;
    }

    public boolean isSemiEulerian() {
        long oddCount = 0;
        for(Node n : graph.getAllNodes()){
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
        // Implement Eulerian Circuit logic here
        System.out.println("Eulerian Circuit computation not yet implemented.");
    }

    private void computeEulerianTrail() {
        // Implement Eulerian Trail logic here
        System.out.println("Eulerian Trail computation not yet implemented.");
    }

    private void computeChinesePostmanCircuit() {
        // Implement Chinese Postman logic here
        System.out.println("Chinese Postman computation not yet implemented.");
    }
}

