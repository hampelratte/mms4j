package org.hampelratte.net.mms.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hampelratte.net.mms.client.listeners.MMSMessageListener;
import org.hampelratte.net.mms.client.listeners.MMSPacketListener;
import org.hampelratte.net.mms.data.MMSPacket;
import org.hampelratte.net.mms.messages.MMSMessage;
import org.hampelratte.net.mms.messages.server.ReportEndOfStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listens for mms header and media packets and writes them to the given file.
 * If the dumper receives an EndOfMedia command, the file will be closed automatically
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class MMSFileDumper implements MMSPacketListener, MMSMessageListener {

    private static transient Logger logger = LoggerFactory.getLogger(MMSFileDumper.class);
    
    private FileOutputStream fout;
    
    private File file;
    
    public MMSFileDumper(File file) throws FileNotFoundException {
        fout = new FileOutputStream(file);
        this.file = file;
    }
    
    public void packetReceived(MMSPacket packet) {
        try {
            fout.write(packet.getData());
        } catch (IOException e) {
            logger.error("Couldn't write mms packet to file " + file.getAbsolutePath(), e);
        }
    }

    public void messageReceived(MMSMessage command) {
        if(command instanceof ReportEndOfStream) {
            try {
                logger.debug("Received EndOfMedia, closing file now");
                fout.close();
            } catch (IOException e) {
                logger.error("Couldn't close file " + file.getAbsolutePath(), e);
            }
        }
    }

}
