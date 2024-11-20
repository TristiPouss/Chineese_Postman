import java.io.File;
import java.util.Scanner;

import m1chinesepostman2024.*;
import m1graphs2024.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Chinese Postman Solver");

        UndirectedGraph graph = new UndirectedGraph();

        // Example DOT input handling
        System.out.print("Enter the path to the DOT file: ");
        String path = scanner.nextLine();

        try {
            graph = UndirectedGraph.fromDotFile(path);
            System.out.println("Loading graph...");
            // Example: graph.addEdge(...);

            System.out.println(graph.toDotString());

            ChinesePostman cpp = new ChinesePostman(graph);
            cpp.solve();
        } catch (Exception e) {
            System.err.println("Error loading graph: " + e.getMessage());
        }

        scanner.close();
    }
}
