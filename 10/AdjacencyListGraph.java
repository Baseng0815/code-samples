package zettel10;

import java.util.*;

public class AdjacencyListGraph<T> implements Graph<T> {

    private enum NodeColor {
        UNCOLORED,
        RED,
        GREEN
    }

    private NodeColor complement(NodeColor color) {
        // this should never be called with NodeColor.UNCOLORED
        return color == NodeColor.RED ? NodeColor.GREEN : NodeColor.RED;
    }

    Map<T, List<T>> map;

    public AdjacencyListGraph() {
        map = new HashMap<>();
    }

    @Override
    public boolean addNodeElement(T element) {
        if (map.containsKey(element)) {
            return false;
        }
        map.put(element, new LinkedList<>());
        return true;
    }

    @Override
    public boolean removeNodeElement(T element) {
        if (map.remove(element) == null) {
            return false;
        }
        for (List<T> list : map.values()) {
            list.remove(element);
        }
        return true;
    }

    @Override
    public void addEdge(T from, T to) throws InvalidEdgeException {
        if (!map.containsKey(from) || !map.containsKey(to)) {
            throw new InvalidEdgeException();
        }
        List<T> list = map.get(from);
        if (!list.contains(to)) {
            list.add(to);
        }
    }

    @Override
    public boolean removeEdge(T from, T to) throws InvalidNodeException {
        if (!map.containsKey(from) || !map.containsKey(to)) {
            throw new InvalidNodeException();
        }
        return map.get(from).remove(to);
    }

    AdjacencyMatrixGraph<T> convert() {
        AdjacencyMatrixGraph<T> graph = new AdjacencyMatrixGraph<>();
        for (T node : map.keySet()) {
            graph.addNodeElement(node);
        }

        for (T node : map.keySet()) {
            for (T other : map.get(node)) {
                try {
                    graph.addEdge(node, other);
                } catch (InvalidEdgeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return graph;
    }

    public AdjacencyListGraph<T> undirected() {
        final AdjacencyListGraph<T> graph = new AdjacencyListGraph<>() {
            @Override
            public void addEdge(T from, T to) throws InvalidEdgeException {
                super.addEdge(from, to);
                super.addEdge(to, from);
            }

            @Override
            public boolean removeEdge(T from, T to) throws InvalidNodeException {
                return super.removeEdge(from, to) && super.removeEdge(to, from);
            }
        };

        for (T e : map.keySet()) {
            graph.addNodeElement(e);
        }

        for (T from : map.keySet()) {
            for (T to : map.get(from)) {
                try {
                    graph.addEdge(from, to);
                } catch (InvalidEdgeException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return graph;
    }

    public boolean undirectedGraphIsBipartit() {
        AdjacencyListGraph<T> undirected = undirected();
        Collection<T> nodes = undirected.map.keySet();
        Map<T, AdjacencyListGraph.NodeColor> coloredNodes = new HashMap<>(nodes.size());

        for (T node : nodes) {
            coloredNodes.put(node, NodeColor.UNCOLORED);
        }

        final NodeColor startingColor = NodeColor.RED;

        for (T node : nodes) {
            AdjacencyListGraph.NodeColor nodeColor = coloredNodes.get(node);
            if (nodeColor == NodeColor.UNCOLORED) {
                coloredNodes.put(node, startingColor);

                Queue<T> queue = new ArrayDeque<>();
                queue.add(node);

                // BFS traversal (any traversal works, but breadth first should abort earlier)
                while (!queue.isEmpty()) {
                    T bfsNode = queue.poll();
                    NodeColor bfsColor = coloredNodes.get(bfsNode);
                    NodeColor expectedAdjacentColor = complement(bfsColor);
                    for (T adjacentNode : undirected.map.get(bfsNode)) {
                        NodeColor adjacentColor = coloredNodes.get(adjacentNode);
                        if (adjacentColor == NodeColor.UNCOLORED) {
                            coloredNodes.put(adjacentNode, expectedAdjacentColor);
                            queue.add(adjacentNode);
                        } else if (adjacentColor != expectedAdjacentColor) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    void print() {
        for (T node : map.keySet()) {
            for (T other : map.get(node)) {
                System.out.println(node + " -> " + other);
            }
        }
    }

    public static void main(String[] args) throws InvalidEdgeException, InvalidNodeException {
        var graph = new AdjacencyListGraph<Character>();

        graph.addNodeElement('1');
        graph.addNodeElement('2');
        graph.addNodeElement('3');
        graph.addNodeElement('4');
        graph.addNodeElement('5');
        graph.addNodeElement('A');
        graph.addNodeElement('B');
        graph.addNodeElement('C');
        graph.addNodeElement('E');

        graph.addEdge('1', 'A');
        graph.addEdge('1', 'E');
        graph.addEdge('2', 'C');
        graph.addEdge('3', 'A');
        graph.addEdge('3', 'B');
        graph.addEdge('4', 'C');
        graph.addEdge('4', 'E');
        graph.addEdge('5', 'E');

        graph.addEdge('1', '3');

        graph.print();
        System.out.println();

        assert !graph.undirectedGraphIsBipartit();

        graph.removeEdge('1', '3');

        assert graph.undirectedGraphIsBipartit();
    }
}
