package it.tungsteno.fp.rovinePerdute.pathfinding;

import java.util.List;
import java.util.Map;

public class RoadMap {
    private final Map<Integer, Node> cities;
    private final Map<Integer, List<Integer>> citiesConnections;

    public RoadMap(Map<Integer, Node> cities, Map<Integer, List<Integer>> citiesConnections) {
        this.cities = cities;
        this.citiesConnections = citiesConnections;
    }

    public Map<Integer, Node> getCities() {
        return cities;
    }

    public Map<Integer, List<Integer>> getCitiesConnections() {
        return citiesConnections;
    }
}
