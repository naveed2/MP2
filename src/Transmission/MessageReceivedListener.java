package Transmission;

import java.io.IOException;
import java.net.UnknownHostException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Used in observer mode.
 */
public interface MessageReceivedListener {
    public void onReceivingMessage(EventMessage m) throws ParserConfigurationException, TransformerException, UnknownHostException, IOException ;
}
