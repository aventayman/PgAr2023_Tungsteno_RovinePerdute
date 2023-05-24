package it.tungsteno.fp.rovinePerdute.pathfinding;

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

    // indice -> lista di indici collegati
    private final Map<Integer, List<Integer>> citiesConnections;
    private List<List<Edge>> adjacencyList;

    /**
     * Costruttore della RoadMap
     * @param cities la mappa che associa l'indice univoco di ogni città al nodo corrispondente
     * @param citiesConnections la mappa che associa l'indice univoco di ogni città alla lista d'indici a essa collegata
     */
    public RoadMap(Map<Integer, Node> cities, Map<Integer, List<Integer>> citiesConnections) {
        this.cities = cities;
        this.citiesConnections = citiesConnections;
        this.adjacencyList = createAdjacencyList();
    }

    /**
     * Questo metodo crea la lista di adiacenza di archi che collegano ogni nodo, ogni città all'interno della mappa
     * ha un indice, e alla posizione dell'id della città viene fornita una lista di edge.
     * @return la lista di adiacenza riferita alla mappa
     */
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
    private Node getCampoBase() {
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
    public Node getRovinePerdute() {
        for (int id : cities.keySet()) {
            if (cities.get(id).getName().equalsIgnoreCase(LOST_RUINS_NAME))
                return cities.get(id);
        }
        return null;
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Trova il percorso più breve dal campo base fino alle rovine perdute tenendo conto della distanza lineare
     * fra ogni nodo senza contare il dislivello
     * @return la lista dei nodi che sono stati percorsi per andare dal nodo di partenza a quello di arrivo
     */
    public List<Node> findShortestPathForTonatiuh() {
        adjacencyList = createAdjacencyList();
        return ShortestPath.getShortestPath(this, getCampoBase(), getRovinePerdute(), false);
    }

    /**
     * Trova il percorso più breve dal campo base fino alle rovine perdute tenendo conto del dislivello fra ogni
     * nodo, non tiene conto della distanza lineare fra i due punti
     * @return la lista dei nodi che sono stati percorsi per andare dal nodo di partenza a quello di arrivo
     */
    public List<Node> findShortestPathForMetztli() {
        adjacencyList = createAdjacencyList();
        return ShortestPath.getShortestPath(this, getCampoBase(), getRovinePerdute(), true);
    }
}
