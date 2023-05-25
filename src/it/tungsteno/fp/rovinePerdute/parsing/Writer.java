package it.tungsteno.fp.rovinePerdute.parsing;

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

    public static void output (List<Node> tonatiuhNodeList, List<Node> metztliNodeList, double gasTonatiuh, double gasMetztli) throws XMLStreamException {
        //Inizializzazione
        XMLOutputFactory xmlOutputFactory;
        XMLStreamWriter xmlStreamWriter = null;


        try {
            xmlOutputFactory = XMLOutputFactory.newInstance();
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream(OUTPUT_PATH), "utf-8");
            xmlStreamWriter.writeStartDocument("utf-8", "1.0");
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del writer:");
            System.out.println(e.getMessage());
        }

        try{
            assert xmlStreamWriter != null;
            xmlStreamWriter.writeStartElement("routes");//Scrittura del tag dell'xml
            xmlStreamWriter.writeStartElement("route");
            xmlStreamWriter.writeAttribute("team", TONATIUH);
            xmlStreamWriter.writeAttribute("cost", Integer.toString((int) gasTonatiuh));
            xmlStreamWriter.writeAttribute("cities", Integer.toString(tonatiuhNodeList.size()));

            for (Node node : tonatiuhNodeList) {
                xmlStreamWriter.writeStartElement("city");
                xmlStreamWriter.writeAttribute("id", Integer.toString(node.getId()));
                xmlStreamWriter.writeAttribute("name", node.getName());
                xmlStreamWriter.writeEndElement();
            }
            xmlStreamWriter.writeEndElement();


            xmlStreamWriter.writeStartElement("route");
            xmlStreamWriter.writeAttribute("team", METZTLI);
            xmlStreamWriter.writeAttribute("cost", Integer.toString((int) gasMetztli));
            xmlStreamWriter.writeAttribute("cities", Integer.toString(metztliNodeList.size()));

            for (Node node : metztliNodeList) {
                xmlStreamWriter.writeStartElement("city");
                xmlStreamWriter.writeAttribute("id", Integer.toString(node.getId()));
                xmlStreamWriter.writeAttribute("name", node.getName());
                xmlStreamWriter.writeEndElement();
            }
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();

            xmlStreamWriter.flush(); // svuota il buffer
            xmlStreamWriter.close(); // chiusura del documento e delle risorse impiegate

        } catch (Exception e){
            System.out.println("Errore nella scrittura");
        }

    }
}
