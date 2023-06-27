package zettel10;

import java.util.*;

public class AdjacencyMatrixGraph<T> implements Graph<T> {
    public static final int INITIAL_CAPACITY = 10;

    private final Map<T, Integer> nodesToPos;
    private final List<T> nodes;
    private boolean[][] matrix;

    public AdjacencyMatrixGraph() {
        matrix = new boolean[INITIAL_CAPACITY][INITIAL_CAPACITY];
        nodes = new ArrayList<>();
        nodesToPos = new HashMap<>();
    }

    @Override
    public boolean addNodeElement(T element) {
        if (nodesToPos.containsKey(element)) {
            return false;
        }

        int pos = nodesToPos.size();
        nodesToPos.put(element, pos);
        nodes.add(element);

        if (pos >= matrix.length) {
            growMatrix();
        }

        return true;
    }

    private void growMatrix() {
        int size = matrix.length * 2;
        boolean[][] newMatrix = new boolean[size][size];

        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix.length);
        }

        matrix = newMatrix;
    }

    @Override
    public boolean removeNodeElement(T element) {
        if (!nodesToPos.containsKey(element)) {
            return false;
        }

        Integer pos = nodesToPos.remove(element);
        nodes.set(pos, nodes.get(nodes.size() - 1));
        nodes.remove(nodes.size() - 1);

        cleanPos(pos);

        return true;
    }

    private void cleanPos(int pos) {
        int last = nodes.size();
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][pos] = matrix[i][last];
            matrix[pos][i] = matrix[last][i];
            matrix[i][last] = false;
            matrix[last][i] = false;
        }
    }

    @Override
    public void addEdge(T from, T to) throws InvalidEdgeException {
        Integer posFrom = nodesToPos.get(from);
        Integer posTo = nodesToPos.get(to);

        if (posFrom == null || posTo == null) {
            throw new InvalidEdgeException("At least one node does not exist.");
        }

        matrix[posFrom][posTo] = true;
    }

    @Override
    public boolean removeEdge(T from, T to) throws InvalidNodeException {
        Integer posFrom = nodesToPos.get(from);
        Integer posTo = nodesToPos.get(to);

        if (posFrom == null || posTo == null) {
            throw new InvalidNodeException("At least one node does not exist.");
        }

        if (matrix[posFrom][posTo]) {
            matrix[posFrom][posTo] = false;
            return true;
        }

        return false;
    }

    public AdjacencyListGraph<T> convert() {
        AdjacencyListGraph<T> graph = new AdjacencyListGraph<>();

        for (T node : nodes) {
            graph.addNodeElement(node);
        }

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (matrix[i][j]) {
                    try {
                        graph.addEdge(nodes.get(i), nodes.get(j));
                    } catch (InvalidEdgeException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return graph;
    }
}
