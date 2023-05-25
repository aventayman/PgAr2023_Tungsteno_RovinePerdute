package it.tungsteno.fp.rovinePerdute;

import it.tungsteno.fp.rovinePerdute.parsing.Reader;
import it.tungsteno.fp.rovinePerdute.parsing.Writer;
import it.tungsteno.fp.rovinePerdute.pathfinding.Node;
import it.tungsteno.fp.rovinePerdute.pathfinding.RoadMap;

import javax.xml.stream.XMLStreamException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        RoadMap map = Reader.createRoadMapFromXML(Reader.getInputFilePath());
        /*
        for (int key : map.getCities().keySet()) {
            System.out.println(map.getCities().get(key));
            System.out.println(map.getCitiesConnections().get(key));
        }
         */

        List<Node> pathToniatuh = map.findShortestPathForTonatiuh();
        List<Node> pathMetztli = map.findShortestPathForMetztli();

        /*
        System.out.println(map.findShortestPathForMetztli());
        System.out.println(map.findShortestPathForTonatiuh());
         */
        Writer.output(pathToniatuh, pathMetztli, map.getGasTonatiuh(pathToniatuh), map.getGasMetztli(pathMetztli));
    }
}
