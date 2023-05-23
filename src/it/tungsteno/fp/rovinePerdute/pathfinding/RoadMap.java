package it.tungsteno.fp.rovinePerdute.pathfinding;

import java.util.*;

/**
 * Classe che contiene una mappa che collega ogni nodo (città) al suo indice e un'altra che collega un integer,
 * corrispondente all'indice di un nodo, e una lista di integer, indici di altri nodi, rappresentanti
 * i collegamenti che il nodo possiede con altre città
 */
public class RoadMap {

    // indice -> nodo
    private final Map<Integer, Node> cities;

    // indice -> indici
    private final Map<Integer, List<Integer>> citiesConnections;
    private List<List<Edge>> adjacencyList;

    public RoadMap(Map<Integer, Node> cities, Map<Integer, List<Integer>> citiesConnections, int citiesAmount) {
        this.cities = cities;
        this.citiesConnections = citiesConnections;
        this.adjacencyList = createAdjacencyList();
    }

    public Map<Integer, Node> getCities() {
        return cities;
    }

    public Map<Integer, List<Integer>> getCitiesConnections() {
        return citiesConnections;
    }

    public int getCitiesAmount() {
        return cities.size();
    }

    private List<List<Edge>> createAdjacencyList() {
        List<List<Edge>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            List<Edge> edgeList = new LinkedList<>();
            for (int j = 0; j < citiesConnections.get(i).size(); j++) {
                edgeList.add(new Edge(getNodeById(i), getNodeById(citiesConnections.get(i).get(j))));
            }
            adjacencyList.add(edgeList);
        }

        return adjacencyList;
    }

    private Node getNodeById(int id) {
        return cities.get(id);
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }
}
