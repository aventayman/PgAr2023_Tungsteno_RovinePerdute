package it.tungsteno.fp.rovinePerdute;

import it.tungsteno.fp.rovinePerdute.parsing.Reader;
import it.tungsteno.fp.rovinePerdute.parsing.Writer;
import it.tungsteno.fp.rovinePerdute.pathfinding.Node;
import it.tungsteno.fp.rovinePerdute.pathfinding.RoadMap;
import it.tungsteno.fp.rovinePerdute.teams.HeightDistanceTeam;
import it.tungsteno.fp.rovinePerdute.teams.LinearDistanceTeam;

import javax.xml.stream.XMLStreamException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        RoadMap map = Reader.createRoadMapFromXML(Reader.getInputFilePath());

        var tonathiu = new LinearDistanceTeam("Tonatiuh");
        var metztli = new HeightDistanceTeam("Metztli");

        Writer.output(map, tonathiu, metztli);
    }
}
