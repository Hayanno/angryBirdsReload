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
    public enum Scope {
        GAME("game", "/settings.xml"),
        STAGE("stage"),
        LEVEL("level");

        private String name, pathname;
        private HashMap<String, String> settings = new HashMap<>();

        Scope(String name) {
            this.name = name;
        }

        Scope(String name, String pathname) {
            this.name = name;
            this.pathname = pathname;
        }

        public void setPathname(String pathname) {
            this.pathname = pathname;
        }

        public String getName() {
            return name;
        }

        public String getPathname() {
            return pathname;
        }

        public HashMap<String, String> getSettings() {
            return settings;
        }
    }

    @Override
    public void load() {
        setStageLevel(1, 1);

        populateSettings();
    }

    @Override
    public void setStageLevel(int currentStage, int currentLevel) {
        Scope.STAGE.setPathname("/stages/" + currentStage + "/stage.xml");
        Scope.LEVEL.setPathname("/stages/" + currentStage + "/level-" + currentStage + ".xml");
    }

    @Override
    public void set(String key, String value) {
        Scope.GAME.getSettings().put(key, value);
    }

    @Override
    public String get(String scopeName, String key) {
        for(Scope scope : Scope.values()) {
            if(scope.getName().equals(scopeName))
                return scope.getSettings().get(key);
        }

        return null;
    }

    @Override
    public void save() {
        Document dom;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
            Element rootEle = dom.createElement("settings");

            Scope.GAME.getSettings().forEach((key, value) -> {
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

                tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(getClass().getResource(Scope.GAME.getPathname()).getFile())));
            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private void populateSettings() {
        for(Scope scope : Scope.values()) {
            String pathname = scope.getPathname(),
                name = scope.getName();
            HashMap<String, String> settings = scope.getSettings();

            File file = new File(getClass().getResource(pathname).getFile());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = null;
            Document document = null;

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

            System.out.println("Scanning " + name + " settings...");

            if (nl != null) {
                for(int index = 0; index < nl.getLength(); index++) {
                    if (nl.item(index).getNodeType() == Node.ELEMENT_NODE) {
                        settings.put(nl.item(index).getNodeName(), nl.item(index).getTextContent());

                        System.out.println(capitalize(name) + " settings <" + nl.item(index).getNodeName() + "> added with value : \"" + nl.item(index).getTextContent() + "\"");
                    }
                }
            }
        }
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
