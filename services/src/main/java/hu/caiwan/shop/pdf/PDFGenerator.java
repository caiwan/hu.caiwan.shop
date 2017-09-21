package hu.caiwan.shop.pdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;

import hu.caiwan.utils.MyURIResolver;

public abstract class PDFGenerator {

	protected Document document;
	protected Source xslt;

	private static FopFactory fopFactory;
	private static TransformerFactory factory;

	private static URIResolver uriResolver;

	static {
		fopFactory = FopFactory.newInstance(new File(".").toURI());
		factory = TransformerFactory.newInstance();

		uriResolver = new MyURIResolver();

		factory.setURIResolver(uriResolver);
	}

	abstract Document generateDocument();

	abstract Source getXslt() throws IOException;

	public void generate() throws IOException {
		document = generateDocument();
		xslt = getXslt();
	}

	public void generate(Object obj, URL xsltURL) throws NullPointerException, IOException {
		if (obj == null || xsltURL == null)
			throw new NullPointerException(String.format("obj (%b) or xsltURL (%b) is null", obj == null, xsltURL == null));

		// XSLT
		InputStream in;
		in = xsltURL.openStream();

		xslt = new StreamSource(in);

		try {
			// Generate xml from obj
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.newDocument();

			JAXBContext ctx = null;
			StringWriter writer = new StringWriter();

			ctx = JAXBContext.newInstance(obj.getClass());
			ctx.createMarshaller().marshal(obj, document);

			// dbg
			ctx.createMarshaller().marshal(obj, writer);
			System.out.println("object as XML");
			System.out.println(writer);

		} catch (ParserConfigurationException e) {

			e.printStackTrace();
			throw new RuntimeException(e);

		} catch (JAXBException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		}
	}

	public String getDocument() throws ParserConfigurationException, TransformerException {
		DOMSource domSource = new DOMSource(document);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		return writer.toString();
	}

	public void writePdf(OutputStream out) throws IOException, FOPException, TransformerException {
		Source src;
		Result res;
		Transformer transformer;
		try {
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

			transformer = factory.newTransformer(xslt);

			src = new DOMSource(document);
			res = new SAXResult(fop.getDefaultHandler());

			transformer.transform(src, res);

			StringWriter writer = new StringWriter();
			transformer.transform(src, new StreamResult(writer));
			String str = writer.toString();
			System.out.println(str);

		} finally {
			out.close();
		}
	}

}
