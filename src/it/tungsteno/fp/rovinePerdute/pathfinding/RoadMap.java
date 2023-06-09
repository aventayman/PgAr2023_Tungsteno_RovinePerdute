package it.tungsteno.fp.rovinePerdute.pathfinding;

import it.tungsteno.fp.rovinePerdute.teams.Team;

import java.util.*;

/**
 * Classe che contiene una mappa che collega ogni nodo (città) al suo indice e un'altra che collega un integer,
 * corrispondente all'indice di un nodo, e una lista di integer, indici di altri nodi, rappresentanti
 * i collegamenti che il nodo possiede con altre città
 */
public class RoadMap {

    private static final String BASE_CAMP_NAME = "campo base";
    private static final String LOST_RUINS_NAME = "rovine perdute";
    // indice -> nodo
    private final Map<Integer, Node> cities;

    // indice -> lista d'indici collegati
    private final Map<Integer, List<Integer>> citiesConnections;
    private final Map<Integer, List<Edge>> adjacencyMap;
    private final Node campoBase, rovinePerdute;

    /**
     * Costruttore della RoadMap
     * @param cities la mappa che associa l'indice univoco di ogni città al nodo corrispondente
     * @param citiesConnections la mappa che associa l'indice univoco di ogni città alla lista d'indici a essa collegata
     */
    public RoadMap(Map<Integer, Node> cities, Map<Integer, List<Integer>> citiesConnections) {
        this.cities = cities;
        this.citiesConnections = citiesConnections;
        this.adjacencyMap = createAdjacencyMap();
        this.campoBase = findCampoBase();
        this.rovinePerdute = findRovinePerdute();
    }

    /**
     * Questo metodo crea la lista di adiacenza di archi che collegano ogni nodo, ogni città all'interno della mappa
     * ha un indice, e alla posizione dell'id della città viene fornita una lista di edge.
     * @return la lista di adiacenza riferita alla mappa
     */
    private Map <Integer, List<Edge>> createAdjacencyMap() {
        Map <Integer, List<Edge>> adjacencyList = new HashMap<>();
        for (int i = 0; i < cities.size(); i++) {
            List<Edge> edgeList = new ArrayList<>();
            int roadsAmount;
            if (citiesConnections.get(i) != null) {
                roadsAmount = citiesConnections.get(i).size();
                for (int j = 0; j < roadsAmount; j++) {
                    edgeList.add(new Edge(getNodeById(i), getNodeById(citiesConnections.get(i).get(j))));
                }
            }
            adjacencyList.put(i, edgeList);
        }

        return adjacencyList;
    }

    /**
     * Dato un id ritorna il nodo corrispondente
     * @param id id della città
     * @return il nodo corrispondente all'id
     */
    public Node getNodeById(int id) {
        return cities.get(id);
    }

    /**
     * Un metodo che serve per trovare il nodo campo base all'interno della mappa
     * @return il nodo campo base
     */
    private Node findCampoBase() {
        for (int id : cities.keySet()) {
            if (cities.get(id).getName().equalsIgnoreCase(BASE_CAMP_NAME))
                return cities.get(id);
        }
        return null;
    }

    /**
     * Un metodo che serve per trovare le rovine perdute all'interno della mappa
     * @return il nodo delle rovine perdute
     */
    private Node findRovinePerdute() {
        for (int id : cities.keySet()) {
            if (cities.get(id).getName().equalsIgnoreCase(LOST_RUINS_NAME))
                return cities.get(id);
        }
        return null;
    }

    public Node getCampoBase() {
        return campoBase;
    }

    public Node getRovinePerdute() {
        return rovinePerdute;
    }

    public Map<Integer, List<Edge>> getAdjacencyMap() {
        return adjacencyMap;
    }

    public Map<Integer, List<Integer>> getCitiesConnections() {
        return citiesConnections;
    }

    /**
     * Trova il percorso più breve dal campo base fino alle rovine perdute tenendo conto della distanza lineare
     * fra ogni nodo senza contare il dislivello
     * @return la lista dei nodi che sono stati percorsi per andare dal nodo di partenza a quello di arrivo
     */
    public List<Node> findShortestPath(Team team) {
        ShortestPath shortestPath = new ShortestPath();
        return shortestPath.getShortestPath(this, getCampoBase(), getRovinePerdute(), team);
    }

    public static double getGas (List<Node> nodeList, Team team) {
        double gas = 0;
        for (int i = 0; i < nodeList.size() - 1; i++) {
            gas += team.calculateNodeDistance(nodeList.get(i), nodeList.get(i + 1));
        }
        return gas;
    }
    public Map<Integer, Node> getCities() {
        return cities;
    }
}
