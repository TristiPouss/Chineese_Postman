import java.io.File;
import java.util.Scanner;

import m1chinesepostman2024.*;

public class Main {
    public static void main(String[] args) {
        boolean dev = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Chinese Postman Solver");

        UndirectedGraphChinesePostman graph = null;

        // DOT input handling
        System.out.print("Enter the path to the DOT file: ");
        String path = scanner.nextLine();

        try {
            System.out.println("Loading graph...");
            graph = UndirectedGraphChinesePostman.fromDotFile(path);

            if (dev) System.out.println(graph.toDotString());

        } catch (Exception e) {
            System.err.println("Error loading graph: " + e.getMessage());
        }

        ChinesePostman cpp = new ChinesePostman(graph);
        cpp.solve();

        scanner.close();
    }
}
