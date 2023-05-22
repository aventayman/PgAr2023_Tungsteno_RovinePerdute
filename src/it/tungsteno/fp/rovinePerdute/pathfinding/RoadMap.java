package it.tungsteno.fp.rovinePerdute.pathfinding;

import java.util.List;
import java.util.Map;

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
    private final int citiesAmount;

    public RoadMap(Map<Integer, Node> cities, Map<Integer, List<Integer>> citiesConnections, int citiesAmount) {
        this.cities = cities;
        this.citiesConnections = citiesConnections;
        this.citiesAmount = citiesAmount;
    }

    public Map<Integer, Node> getCities() {
        return cities;
    }

    public Map<Integer, List<Integer>> getCitiesConnections() {
        return citiesConnections;
    }

    public int getCitiesAmount() {
        return citiesAmount;
    }
}
