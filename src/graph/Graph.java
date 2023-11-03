package graph;

/**
 * Represents an unordered graph with an adjacency matrix.
 */
public class Graph {

    /** 
     * The adjacency matrix of this graph
     */
    private final boolean[][] matrix;

    /**
     * Create a new Graph object with the specified order.
     * @param order The graph order
     */
    public Graph(int order) {
        // init value is false
        matrix = new boolean[order][order];
    }

    /**
     * Check if x and y are adjacent in this graph.
     * @param x
     * @param y
     * @return true if x and y are adjacent, false otherwise.
     */
    public boolean adjacent(int x, int y) {
        return matrix[x][y];
    }

    /**
     * Get the number of neighbors of v.
     * @param v The vertex.
     * @return The number of neighbors.
     */
    private int nbNeighbors(int v) {
        int res = 0;
        for(int w=0;w< matrix.length;w++)
            if(adjacent(v, w)) res++;
        return res;
    }


    /**
     * Get all the neighbors of vertex v.
     * @param v The vertex.
     * @return An array of the neighbors of v.
     */
    public int[] neighbors(int v) {
        int i=0;
        int[] res = new int[nbNeighbors(v)];

        for(int w=0;w< matrix.length;w++)
            if(adjacent(v,w)) res[i++] = w;

        return res;
    }

    /**
     * Create an edge between x and y.
     * @param x The first vertex
     * @param y The second vertex
     */
    public void addEdge(int x, int y) {
        matrix[x][y] = true;
        matrix[y][x] = true;
    }

    /**
     * Remove the edge between x and y.
     * @param x The first vertex.
     * @param y The second vertex.
     */
    public void removeEdge(int x, int y) {
        matrix[x][y] = false;
        matrix[y][x] = false;
    }
}
