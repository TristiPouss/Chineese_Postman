# Chineese_Postman
A representation of the Chineese Postman problem in Java

# Authors
Anton Dolard & Tristan de Saint Gilles

# About
### How to launch and compile
First launch the Main.java, the application should start and ask you for the name of the graph file.

This file has to be in the test directory and has to be a DOT file in .gv (Some example graphs are already in the directory).

The graph should now be loaded in the application. 

Then the application will check the type of the graph and act in consequences.

- Eulerian : The application will compute the Eulerian Trail.
- Semi-Eulerian : The application will compute the Eulerian Circuit.
- Non-Eulerian : You will be able to choose between the two strategies (Exhaustive matching or Greedy). Then the application will compute the Chinese Circuit of the graph.

Finally the solution will be exported as DOT file in the processed directory of the project.

After that you can either exit the application or re-process a graph.

### This section is for the teacher that will review this project
