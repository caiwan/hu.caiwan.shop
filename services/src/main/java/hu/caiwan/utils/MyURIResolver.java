package hu.caiwan.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class MyURIResolver implements URIResolver {
	
	private static DocumentBuilderFactory dFactory ;
	static {
		dFactory = DocumentBuilderFactory
	            .newInstance();
		dFactory.setNamespaceAware(true);
	}
	
	@Override
	public Source resolve(String href, String base) throws TransformerException {
		DocumentBuilder dBuilder;
		try {
			dBuilder = dFactory.newDocumentBuilder();
			ClassLoader cl = this.getClass().getClassLoader();
			InputStream in = cl.getResourceAsStream("xsl/" + href);
			InputSource xslInputSource = new InputSource(in);
			Document xslDoc = dBuilder.parse(xslInputSource);
			DOMSource xslDomSource = new DOMSource(xslDoc);
			xslDomSource.setSystemId("xsl/" + href);
			return xslDomSource;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}