package it.tungsteno.fp.rovinePerdute.parsing;

import it.ayman.fp.lib.AnsiColors;
import it.ayman.fp.lib.PrettyStrings;
import it.tungsteno.fp.rovinePerdute.pathfinding.MultiThreader;
import it.tungsteno.fp.rovinePerdute.pathfinding.Node;
import it.tungsteno.fp.rovinePerdute.pathfinding.RoadMap;
import it.tungsteno.fp.rovinePerdute.teams.Team;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Writer {
    private static final String OUTPUT_PATH = "./OutputData/Routes.xml";
    private static final String INITIALIZATION_ERROR =
            PrettyStrings.colorString("Writer initialization error", AnsiColors.RED);
    private static final String WRITING_ERROR = PrettyStrings.colorString("Writing error", AnsiColors.RED);
    private static final String ROUTES = "routes";
    private static final String ROUTE = "route";
    private static final String COST = "cost";
    private static final String TEAM = "team";
    private static final String CITIES = "cities";
    private static final String CITY = "city";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DOCUMENT_ENCODING = "utf-8";
    private static final String DOCUMENT_VERSION = "1.0";

    public static void output (RoadMap map, Team...teams) {
        //Inizializzazione
        XMLOutputFactory xmlOutputFactory;
        XMLStreamWriter xmlStreamWriter = null;

        try {
            xmlOutputFactory = XMLOutputFactory.newInstance();
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream(OUTPUT_PATH), DOCUMENT_ENCODING);
            xmlStreamWriter.writeStartDocument(DOCUMENT_ENCODING, DOCUMENT_VERSION);
        } catch (Exception e) {
            System.out.println(INITIALIZATION_ERROR);
            System.out.println(e.getMessage());
        }

        try{
            assert xmlStreamWriter != null;

            // Scrivo il tag di apertura del file
            xmlStreamWriter.writeStartElement(ROUTES);

            // Creo una lista di thread in base al numero di team che ho
            List<MultiThreader> threaderList = new ArrayList<>(teams.length);

            // Per ogni team aggiungo alla lista dei thread un nuovo thread
            // con al suo interno la mappa e il team in questione
            for (Team team : teams)
                threaderList.add(new MultiThreader(map, team));

            // Faccio partire tutti i thread, così cominciano tutti a calcolare
            for (MultiThreader threader : threaderList) {
                threader.start();
            }

            // Per ogni thread scrivo i dati che ho trovato nell' XML
            for (MultiThreader threader : threaderList) {
                // Prima di cominciare a scrivere i dati il thread in questione deve aver terminato
                // altrimenti la nodeList sarebbe null, quindi attendiamo finché non abbia finito
                // oppure se ha già finito possiamo andare avanti subito
                threader.join();

                List<Node> teamPath = threader.getNodeList();

                xmlStreamWriter.writeStartElement(ROUTE);
                xmlStreamWriter.writeAttribute(TEAM, threader.getTeam().name());
                xmlStreamWriter.writeAttribute(COST, Integer.toString((int) RoadMap.getGas(teamPath, threader.getTeam())));
                xmlStreamWriter.writeAttribute(CITIES, Integer.toString(teamPath.size()));

                for (Node node : teamPath) {
                    xmlStreamWriter.writeStartElement(CITY);
                    xmlStreamWriter.writeAttribute(ID, Integer.toString(node.getId()));
                    xmlStreamWriter.writeAttribute(NAME, node.getName());
                    xmlStreamWriter.writeEndElement();
                }
                xmlStreamWriter.writeEndElement();
            }

            xmlStreamWriter.writeEndDocument();

            xmlStreamWriter.flush(); // svuota il buffer
            xmlStreamWriter.close(); // chiusura del documento e delle risorse impiegate

        } catch (XMLStreamException e){
            System.out.println(WRITING_ERROR);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
