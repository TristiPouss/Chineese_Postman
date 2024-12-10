
import java.io.IOException;
import java.util.Scanner;

import m1chinesepostman2024.*;

public class Main {
    public static void main(String[] args) {
        boolean dev = false;
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("\033\143");
            System.out.println("Welcome to the Chinese Postman Solver\n");

            UndirectedGraphChinesePostman graph = null;

            // DOT input handling
            System.out.print("Enter the name of the DOT file: ");
            String filename = scanner.nextLine();

            try {
                System.out.println("\nLoading graph...");
                graph = UndirectedGraphChinesePostman.fromDotFile(filename);

                if (dev) System.out.println(graph.toDotString());

            } catch (Exception e) {
                System.err.println("Error loading graph: " + e.getMessage());
            }
            System.out.println("\nSolving graph :");
            ChinesePostman cpp = new ChinesePostman(graph);
            cpp.solve();
            System.out.println("\nExporting solution to 'processed/" + filename + "-processed.gv'");
            try {
                cpp.toDotFile(filename);
            } catch (IOException e) {
                System.err.println("Error exporting solution: " + e.getMessage());
            }

            System.out.println("\nWould you like to solve another graph ? (YES/no)");
            String stop = scanner.nextLine();
            
            if (stop.equals("no")) break;
        }
        scanner.close();
        System.out.print("\033\143");
        System.out.println("Application Closed.");
    }
}
