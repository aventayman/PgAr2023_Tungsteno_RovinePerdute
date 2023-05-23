package it.tungsteno.fp.rovinePerdute;

import it.tungsteno.fp.rovinePerdute.parsing.Reader;
import it.tungsteno.fp.rovinePerdute.pathfinding.RoadMap;

import javax.xml.stream.XMLStreamException;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        RoadMap map = Reader.createRoadMapFromXML(Reader.getInputFilePath());
        for (int key : map.getCities().keySet()) {
            System.out.println(map.getCities().get(key));
            System.out.println(map.getCitiesConnections().get(key));
        }

        System.out.println(map.findShortestPath());
    }
}
