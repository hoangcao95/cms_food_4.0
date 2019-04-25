package vn.vano.cms.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import vn.yotel.commons.model.LinkModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SaaJSoapClient {
    private static final Logger logger = LoggerFactory.getLogger(vn.yotel.commons.util.SaaJSoapClient.class);
    private String prefix;
    private String prefixForChildElement;
    private String uri;
    private SOAPMessage soapResponse;
    private String soapMessageRequest;
    private String soapMessageResponse;
    private String url;
    private String action;
    private List<LinkModel> childElements;
    private List<LinkModel> addedHeaders = new ArrayList();
    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public SaaJSoapClient() {
    }

    public SaaJSoapClient(String url, String prefix, String prefixForChildElement, String uri, String action, List<LinkModel> childElements) {
        this.url = url;
        this.prefix = prefix;
        this.prefixForChildElement = prefixForChildElement;
        this.uri = uri;
        this.action = action;
        this.childElements = childElements;
    }

    public SOAPMessage createSoapRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPHeader soapHeader = soapMessage.getSOAPHeader();
        SOAPBody soapBody = envelope.getBody();
        envelope.removeNamespaceDeclaration(envelope.getPrefix());
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        envelope.setPrefix("soapenv");
        envelope.addNamespaceDeclaration(this.prefix, this.uri);
        Iterator var7 = this.getAddedHeaders().iterator();

        while (var7.hasNext()) {
            LinkModel childElement = (LinkModel) var7.next();
            soapMessage.getMimeHeaders().addHeader(childElement.getText(), childElement.getValue());
        }

        soapHeader.setPrefix("soapenv");
        soapBody.setPrefix("soapenv");
        SOAPElement soapBodyElem = soapBody.addChildElement(this.action, this.prefix);
        Iterator var15 = this.childElements.iterator();

        while (var15.hasNext()) {
            LinkModel childElement = (LinkModel) var15.next();
            SOAPElement soapElemByBodyElem;
            if (!this.prefixForChildElement.isEmpty()) {
                soapElemByBodyElem = soapBodyElem.addChildElement(childElement.getText(), this.prefixForChildElement);
                soapElemByBodyElem.addTextNode(childElement.getValue());
            } else {
                soapElemByBodyElem = soapBodyElem.addChildElement(childElement.getText());
                soapElemByBodyElem.addTextNode(childElement.getValue());
            }
        }

        soapMessage.saveChanges();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapMessage.getSOAPPart().getContent();
        logger.debug("Request SOAP Message = ");
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        transformer.transform(sourceContent, result);
        this.soapMessageRequest = sw.toString();
        logger.debug("SOAPRequest : " + this.soapMessageRequest);
        return soapMessage;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public SOAPMessage SOAPRequest() throws Exception {
        return this.createSoapRequest();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String SOAPResponseXML(SOAPMessage soapResponse) throws Exception {
        return this.parseSoapResponseXML(soapResponse);
    }

    public String parseSoapResponseXML(SOAPMessage soapResponse) throws Exception {
        StringWriter sw = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        StreamResult result = new StreamResult(sw);
        transformer.transform(sourceContent, result);
        logger.info("SOAPResponseXML : " + sw.toString());
        return sw.toString();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public SOAPMessage SOAPCall() throws Exception {
        return this.processSoapCall();
    }

    public SOAPMessage processSoapCall() throws Exception {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        SOAPMessage soapResponse = soapConnection.call(this.createSoapRequest(), this.url);
        this.soapResponse = soapResponse;
        this.soapMessageResponse = this.parseSoapResponseXML(soapResponse);
        soapConnection.close();
        return soapResponse;
    }

    public String processSoapCallAndResultByTagName(String tagName) throws Exception {
        this.processSoapCall();
        return this.getValueByTagNameFromResponse(tagName);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String SOAPCall(String tagName) throws Exception {
        return this.processSoapCallAndResultByTagName(tagName);
    }

    public Document loadXMLString() throws Exception {
        DocumentBuilder db = this.dbf.newDocumentBuilder();
        return db.parse(new ByteArrayInputStream(this.soapMessageResponse.getBytes()));
    }

    public List<String> getValuesByTagNameFromResponse(String tagName) throws Exception {
        Document xmlDoc = this.loadXMLString();
        NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
        List<String> ids = new ArrayList(nodeList.getLength());

        for (int i = 0; i < nodeList.getLength(); ++i) {
            org.w3c.dom.Node x = nodeList.item(i);
            ids.add(x.getFirstChild().getNodeValue());
        }

        return ids;
    }

    public String getValueByTagNameFromResponse(String tagName) throws Exception {
        Document xmlDoc = this.loadXMLString();
        NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
        if (nodeList.getLength() <= 0) {
            return "";
        } else {
            Node x = nodeList.item(0);
            return x != null && x.getFirstChild() != null ? x.getFirstChild().getNodeValue() : null;
        }
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefixForChildElement() {
        return this.prefixForChildElement;
    }

    public void setPrefixForChildElement(String prefixForChildElement) {
        this.prefixForChildElement = prefixForChildElement;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<LinkModel> getChildElements() {
        return this.childElements;
    }

    public void setChildElements(List<LinkModel> childElements) {
        this.childElements = childElements;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SOAPMessage getSoapResponse() {
        return this.soapResponse;
    }

    public void setSoapResponse(SOAPMessage soapResponse) {
        this.soapResponse = soapResponse;
    }

    public String getSoapMessageResponse() {
        return this.soapMessageResponse;
    }

    public void setSoapMessageResponse(String soapMessageResponse) {
        this.soapMessageResponse = soapMessageResponse;
    }

    public List<LinkModel> getAddedHeaders() {
        return this.addedHeaders;
    }

    public void setAddedHeaders(List<LinkModel> addedHeaders) {
        this.addedHeaders = addedHeaders;
    }

    public String getSoapMessageRequest() {
        return this.soapMessageRequest;
    }

    public void setSoapMessageRequest(String soapMessageRequest) {
        this.soapMessageRequest = soapMessageRequest;
    }
}
