package angrybirdsreload.settings;

import com.google.inject.Singleton;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

@Singleton
public class GameSettings implements ISettings {
    private HashMap<String, String> settings;
    private String pathname = "/settings.xml";

    private HashMap<String, String> getSettings() {
        return settings;
    }

    private void setSettings(HashMap<String, String> settings) {
        this.settings = settings;
    }

    @Override
    public void load() {
        File file = new File(getClass().getResource(pathname).getFile());
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        Document document = null;

        setSettings(new HashMap<>());

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            if (documentBuilder != null) {
                document = documentBuilder.parse(file);
            }
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        Element docEle = null;
        if (document != null) {
            docEle = document.getDocumentElement();
        }

        NodeList nl = null;
        if (docEle != null) {
            nl = docEle.getChildNodes();
        }

        System.out.println("Scanning settings...");

        if (nl != null) {
            for(int index = 0; index < nl.getLength(); index++) {
                if (nl.item(index).getNodeType() == Node.ELEMENT_NODE) {
                    getSettings().put(nl.item(index).getNodeName(), nl.item(index).getTextContent());
                    System.out.println("Settings <" + nl.item(index).getNodeName() + "> added with value : \"" + nl.item(index).getTextContent() + "\"");
                }
            }
        }
    }

    @Override
    public void set(String key, String value) {
        getSettings().put(key, value);
    }

    @Override
    public String get(String key) {
        return getSettings().get(key);
    }

    @Override
    public void save() {
        Document dom;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
            Element rootEle = dom.createElement("settings");

            getSettings().forEach((key, value) -> {
                Element e = dom.createElement(key);
                e.appendChild(dom.createTextNode(value));
                rootEle.appendChild(e);
            });

            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(getClass().getResource(pathname).getFile())));
            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }
}
