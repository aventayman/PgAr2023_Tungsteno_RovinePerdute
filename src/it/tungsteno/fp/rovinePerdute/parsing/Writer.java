package it.tungsteno.fp.rovinePerdute.parsing;

import it.ayman.fp.lib.AnsiColors;
import it.ayman.fp.lib.PrettyStrings;
import it.tungsteno.fp.rovinePerdute.pathfinding.Node;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.util.List;

public class Writer {
    private static final String OUTPUT_PATH = "./OutputData/Routes.xml";
    private static final String TONATIUH = "Tonatiuh";
    private static final String METZTLI = "Metztli";
    private static final String INITIALIZATION_ERROR = PrettyStrings.colorString("Writer initialization error", AnsiColors.RED);
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





    public static void output (List<Node> tonatiuhNodeList, List<Node> metztliNodeList, double gasTonatiuh, double gasMetztli) throws XMLStreamException {
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
            xmlStreamWriter.writeStartElement(ROUTES);//Scrittura del tag dell'xml
            xmlStreamWriter.writeStartElement(ROUTE);
            xmlStreamWriter.writeAttribute(TEAM, TONATIUH);
            xmlStreamWriter.writeAttribute(COST, Integer.toString((int) gasTonatiuh));
            xmlStreamWriter.writeAttribute(CITIES, Integer.toString(tonatiuhNodeList.size()));

            for (Node node : tonatiuhNodeList) {
                xmlStreamWriter.writeStartElement(CITY);
                xmlStreamWriter.writeAttribute(ID, Integer.toString(node.getId()));
                xmlStreamWriter.writeAttribute(NAME, node.getName());
                xmlStreamWriter.writeEndElement();
            }
            xmlStreamWriter.writeEndElement();


            xmlStreamWriter.writeStartElement(ROUTE);
            xmlStreamWriter.writeAttribute(TEAM, METZTLI);
            xmlStreamWriter.writeAttribute(COST, Integer.toString((int) gasMetztli));
            xmlStreamWriter.writeAttribute(CITIES, Integer.toString(metztliNodeList.size()));

            for (Node node : metztliNodeList) {
                xmlStreamWriter.writeStartElement(CITY);
                xmlStreamWriter.writeAttribute(ID, Integer.toString(node.getId()));
                xmlStreamWriter.writeAttribute(NAME, node.getName());
                xmlStreamWriter.writeEndElement();
            }
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();

            xmlStreamWriter.flush(); // svuota il buffer
            xmlStreamWriter.close(); // chiusura del documento e delle risorse impiegate

        } catch (Exception e){
            System.out.println(WRITING_ERROR);
        }

    }
}
