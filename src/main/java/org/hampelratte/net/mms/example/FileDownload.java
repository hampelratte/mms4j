package org.hampelratte.net.mms.example;

import java.io.File;
import java.net.URI;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.hampelratte.net.mms.client.Client;
import org.hampelratte.net.mms.client.listeners.MMSMessageListener;
import org.hampelratte.net.mms.io.MMSFileDumper;
import org.hampelratte.net.mms.messages.MMSMessage;
import org.hampelratte.net.mms.messages.server.ReportEndOfStream;
import org.hampelratte.net.mms.messages.server.ReportStreamSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileDownload {

    private static transient Logger logger = LoggerFactory.getLogger(FileDownload.class);

    MMSFileDumper dumper;

    public FileDownload() throws Exception {
        // final URI uri = new URI("http://a1014.v1252931.c125293.g.vm.akamaistream.net"
        // + "/7/1014/125293/v0001/wm.od.origin.zdf.de.gl-systemhaus.de/none/zdf/11/09/110919_1340_hko_vh.wmv");
        final URI uri = new URI(
                "mms://a1014.v1252931.c125293.g.vm.akamaistream.net/7/1014/125293/v0001/wm.od.origin.zdf.de.gl-systemhaus.de/none/3sat/11/09/110908_sendung_bauerfeind_vh.wmv");
        // final URI uri = new URI("mms://apasf.apa.at/cms-worldwide"
        // + "/2011-09-18_1100_sd_02_ZIB_____2913225__o__0000823759__s2917803___hr_ORF2HiRes_10595712P_11051213P.wmv");
        // final URL url = new URL("http://a1014.v1252931.c125293.g.vm.akamaistream.net"
        // + "/7/1014/125293/v0001/wm.od.origin.zdf.de.gl-systemhaus.de/none/zdf/11/09/110910_sendung_rdm_vh.wmv");

        final Client client = new Client(uri);
        dumper = new MMSFileDumper(new File("/tmp/out.wmv"));
        client.addPacketListener(dumper);
        client.addMessageListener(dumper);
        client.addMessageListener(new MMSMessageListener() {
            @Override
            public void messageReceived(MMSMessage message) {
                if (message instanceof ReportStreamSwitch) {
                    ReportStreamSwitch rss = (ReportStreamSwitch) message;
                    logger.debug("StreamSwitch result: {}", rss.getHr());

                    // great, we can start the streaming
                    long startPacket = 0;

                    // start the streaming
                    client.startStreaming(startPacket);
                } else if (message instanceof ReportEndOfStream) {
                    logger.info("{}%. End of stream. Closing connection.", client.getProgress());
                    client.disconnect(new IoFutureListener<IoFuture>() {
                        @Override
                        public void operationComplete(IoFuture future) {
                            logger.info("Connection closed.");
                        }
                    });
                }
            }
        });
        client.connect(null);
    }

    public static void main(String[] args) throws Exception {
        new FileDownload();
    }
}
