package it.tungsteno.fp.rovinePerdute.pathfinding;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ShortestPath {
    private static final double EPS = 1e-6;

    private static Comparator<Node> comparator =
            new Comparator<Node>() {
                @Override
                public int compare(Node node1, Node node2) {
                   if (Math.abs(node1.getMinDistance() - node2.getMinDistance()) < EPS) return 0;
                   return (node1.getMinDistance() - node2.getMinDistance()) > 0 ? +1 : -1;
                }
            };

    public static double dijkstra(List<List<Edge>> adjacencyList, Node start, Node destination, boolean withHeight) {
        int citiesAmount = adjacencyList.size();

        //Array di minima distanza dal node di start
        var dist = new double[citiesAmount];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start.getId()] = 0;
        start.setMinDistance(0);

        //Priority queue dove ci sono i nodi piu promettenti
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(2 * citiesAmount, comparator);
        priorityQueue.offer(start);

        boolean[] visited = new boolean[citiesAmount];
        var prev = new Node[citiesAmount];

        while (!priorityQueue.isEmpty()) {
            Node node = priorityQueue.poll();
            visited[node.getId()] = true;

            if (dist[node.getId()] < node.getMinDistance()) continue;

            List<Edge> edges = adjacencyList.get(node.getId());
            for (int i = 0; i < edges.size(); i++) {
                Edge edge = edges.get(i);

                if (visited[edge.getDestinationNode().getId()]) continue;

                double newDist;
                if (withHeight)
                    newDist = dist[edge.getStartNode().getId()] + edge.getHeightCost();
                else
                    newDist = dist[edge.getStartNode().getId()] + edge.getLinearCost();

                if (newDist < dist[edge.getDestinationNode().getId()]) {
                    prev[edge.getDestinationNode().getId()] = edge.getStartNode();
                    dist[edge.getDestinationNode().getId()] = newDist;
                    edge.getDestinationNode().setMinDistance(newDist);
                    priorityQueue.offer(edge.getDestinationNode());
                }
            }
            if (node.getId() == destination.getId()) return dist[destination.getId()];
        }

        return Double.POSITIVE_INFINITY;
    }

}
