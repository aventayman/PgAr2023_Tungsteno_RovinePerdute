package it.tungsteno.fp.rovinePerdute;

import it.tungsteno.fp.rovinePerdute.parsing.Reader;
import it.tungsteno.fp.rovinePerdute.parsing.Writer;
import it.tungsteno.fp.rovinePerdute.pathfinding.RoadMap;
import it.tungsteno.fp.rovinePerdute.teams.HeightDistanceTeam;
import it.tungsteno.fp.rovinePerdute.teams.LinearDistanceTeam;

import javax.xml.stream.XMLStreamException;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        RoadMap map = Reader.createRoadMapFromXML(Reader.getInputFilePath());

        var team1 = new LinearDistanceTeam("Tonatiuh");
        var team2 = new HeightDistanceTeam("Metztli");

        Writer.output(map, team1, team2);
    }
}
