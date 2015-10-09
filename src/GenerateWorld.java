import java.io.FileOutputStream;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GenerateWorld {
	public static void main(String[] args) {
		String filename = "RandomWorld.xml";
		int width = 500;
		int height = 500;
		int time = 100;
		boolean debug = true;

		generate(filename, width, height, time, debug);
	}
	
	public static void generate(String filename, int width, int height, int time, boolean debug) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			Element mainRootElement = document.createElementNS("http://perceptualscience.rutgers.edu/flocking", "world");
			
			//set the attributes of the world
			mainRootElement.setAttribute("width", width + "");
			mainRootElement.setAttribute("height", height + "");
			mainRootElement.setAttribute("time", time + "");
			mainRootElement.setAttribute("debug", debug + "");
			
			//set the flocker defaults
			Element defaults = document.createElement("defaults");
			Element flocker = document.createElement("flocker");
				flocker.setAttribute("align", "true");
				flocker.setAttribute("debug", "true");
				flocker.setAttribute("clear", "true");
				flocker.setAttribute("evade", "true");
				flocker.setAttribute("center", "true");
				flocker.setAttribute("follow", "true");
			defaults.appendChild(flocker);
			mainRootElement.appendChild(defaults);
			

			Random random = new Random();
	
			//generate obstacles
			int numObstacle = random.nextInt(6);
			for(int i=0; i<numObstacle; i++) {
				mainRootElement.appendChild(newRock(document, width, height));
			}
			
			//generate lights
			int numLights = random.nextInt(4);
			for(int i=0; i<numLights; i++) {
				mainRootElement.appendChild(newLight(document, width, height));
			}
			
			//place flockers
			int numFlockers = random.nextInt(4) +1;
			for(int i=0; i<numFlockers; i++) {
				mainRootElement.appendChild(newFlocker(document, width, height));
			}
			
			//place predator
			int numPredator = random.nextInt(2);
			for(int i=0; i<numPredator; i++) {
				mainRootElement.appendChild(newPredator(document, width, height));
			}
			
			//append root to document
			document.appendChild(mainRootElement);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StreamResult file = new StreamResult(new FileOutputStream(filename));
			transformer.transform(source, file);
			
			System.out.println("XML doc created successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Node newRock(Document document, int width, int height) {

		Random random = new Random();
		int x = random.nextInt(width);
		int y = random.nextInt(width);
		
		Element rock = document.createElement("rock");
		rock.setAttribute("x", x + "");
		rock.setAttribute("y", y + "");
		
		return rock;
	}
	
	private static Node newLight(Document document, int width, int height) {

		Random random = new Random();
		int x = random.nextInt(width);
		int y = random.nextInt(width);
		
		Element light = document.createElement("light");
		light.setAttribute("x", x + "");
		light.setAttribute("y", y + "");
		
		return light;
	}
	
	private static Node newFlocker(Document document, int width, int height) {

		Random random = new Random();
		int x = random.nextInt(width);
		int y = random.nextInt(width);
		
		Element boid = document.createElement("flocker");
		boid.setAttribute("x", x + "");
		boid.setAttribute("y", y + "");
		boid.setAttribute("heading", "0");
		
		return boid;
	}
	
	private static Node newPredator(Document document, int width, int height) {

		Random random = new Random();
		int x = random.nextInt(width);
		int y = random.nextInt(width);
		
		Element predator = document.createElement("model-predator");
		predator.setAttribute("x", x + "");
		predator.setAttribute("y", y + "");
		predator.setAttribute("heading", "0");
		
		return predator;
	}
}