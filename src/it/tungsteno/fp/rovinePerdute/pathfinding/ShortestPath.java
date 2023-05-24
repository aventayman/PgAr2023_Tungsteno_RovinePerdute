package it.tungsteno.fp.rovinePerdute.pathfinding;

import java.util.*;

public class ShortestPath {
    private static final double EPS = 1e-6;

    private static Map<Integer, Node> prev;
    private static Map<Integer, Double> minDistanceMap;

    private static final Comparator<Integer> comparator =
            (index1, index2) -> {
                if (Math.abs(minDistanceMap.get(index1) - minDistanceMap.get(index2)) < EPS) return 0;
                return (minDistanceMap.get(index1) - minDistanceMap.get(index2)) > 0 ? +1 : -1;
            };


    public static List<Node> getShortestPath(RoadMap map, Node start, Node destination, boolean withHeight) {
        double dist = dijkstra(map, start, destination, withHeight);
        List<Node> path = new ArrayList<>();
        if (dist == Double.POSITIVE_INFINITY) return path;
        path.add(destination);
        for (int at = findIndexInPrev(destination); prev.get(at) != null; at = prev.get(at).getId()) {
            path.add(prev.get(at));
        }
        Collections.reverse(path);
        return path;
    }

    private static int findIndexInPrev(Node searchedNode) {
        for (Integer index : prev.keySet()) {
            if (index == searchedNode.getId())
                return index;
        }
        return -1;
    }

    private static double dijkstra(RoadMap map, Node start, Node destination, boolean withHeight) {
        List<List<Edge>> adjacencyList = map.getAdjacencyList();
        int citiesAmount = adjacencyList.size();
        minDistanceMap = new HashMap<>();

        //Array di minima distanza dal node di start
        var dist = new double[citiesAmount];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start.getId()] = 0;
        minDistanceMap.put(start.getId(), 0.0);

        //Priority queue dove ci sono i nodi piu promettenti
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(2 * citiesAmount, comparator);
        priorityQueue.offer(start.getId());

        boolean[] visited = new boolean[citiesAmount];
        prev = new HashMap<>();

        while (!priorityQueue.isEmpty()) {
            Node node = map.getNodeById(priorityQueue.poll());
            visited[node.getId()] = true;

            if (dist[node.getId()] < minDistanceMap.get(node.getId())) continue;

            List<Edge> edges = adjacencyList.get(node.getId());
            for (Edge edge : edges) {
                if (visited[edge.getDestinationNode().getId()]) continue;

                double newDist;
                if (withHeight) {
                    newDist = dist[edge.getStartNode().getId()] + edge.getHeightCost();
                } else {
                    newDist = dist[edge.getStartNode().getId()] + edge.getLinearCost();
                }
                if (newDist < dist[edge.getDestinationNode().getId()]) {
                    prev.put(edge.getDestinationNode().getId(), edge.getStartNode());
                    dist[edge.getDestinationNode().getId()] = newDist;
                    minDistanceMap.put(edge.getDestinationNode().getId(), newDist);
                    priorityQueue.offer(edge.getDestinationNode().getId());
                }
            }
            if (node.getId() == destination.getId()) return dist[destination.getId()];
        }

        return Double.POSITIVE_INFINITY;
    }

}
