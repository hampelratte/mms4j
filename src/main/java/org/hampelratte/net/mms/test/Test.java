package org.hampelratte.net.mms.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mina.nio.MessageSentDumpFilter;
import org.hampelratte.net.mms.asf.io.ASFInputStream;
import org.hampelratte.net.mms.asf.objects.ASFObject;
import org.hampelratte.net.mms.asf.objects.ASFToplevelHeader;
import org.hampelratte.net.mms.client.MMSClient;
import org.hampelratte.net.mms.client.MMSNegotiator;
import org.hampelratte.net.mms.client.listeners.MMSMessageListener;
import org.hampelratte.net.mms.client.listeners.MMSPacketListener;
import org.hampelratte.net.mms.data.MMSHeaderPacket;
import org.hampelratte.net.mms.data.MMSPacket;
import org.hampelratte.net.mms.io.MMSFileDumper;
import org.hampelratte.net.mms.messages.MMSMessage;
import org.hampelratte.net.mms.messages.client.Connect;
import org.hampelratte.net.mms.messages.client.ConnectFunnel;
import org.hampelratte.net.mms.messages.client.OpenFile;
import org.hampelratte.net.mms.messages.client.ReadBlock;
import org.hampelratte.net.mms.messages.client.StreamSwitch;
import org.hampelratte.net.mms.messages.server.ReportStreamSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test implements MMSMessageListener, MMSPacketListener {

    private static transient Logger logger = LoggerFactory.getLogger(Test.class);

    private MMSClient client;

    public Test() throws FileNotFoundException {
        // zdf lang
        // mms://ondemand.msmedia.zdf.newmedia.nacamar.net/zdf/data/msmedia/zdf/07/03/070313_pipeline_htc_vh.wmv
         String host = "ondemand.msmedia.zdf.newmedia.nacamar.net";
         String filepath = "zdf/data/msmedia/zdf/07/03/";
         String filename = "070313_pipeline_htc_vh.wmv";

        // zdf kurz
        // mms://ondemand.msmedia.zdf.newmedia.nacamar.net/zdf/data/msmedia/3sat/09/05/090524_sendung2_neues_vh.wmv
//         String host = "ondemand.msmedia.zdf.newmedia.nacamar.net";
//         String filepath = "zdf/data/msmedia/3sat/09/05/";
//         String filename = "090524_sendung2_neues_vh.wmv";

        // arte
        // mms://a1091.v397591.c39759.g.vm.akamaistream.net/7/1091/39759/0b48fbf5073e3b715dad199ef8df723d/artegeie.download.akamai.com/39759/mfile/arteprod/A7_SGT_ENC_04_039360-000-A_PG_HQ_DE.wmv
//        String host = "a311.v397595.c39759.g.vm.akamaistream.net";
//        String filepath = "7/1091/39759/0b48fbf5073e3b715dad199ef8df723d/artegeie.download.akamai.com/39759/mfile/arteprod/";
//        String filename = "A7_SGT_ENC_04_039360-000-A_PG_HQ_DE.wmv";

        // rtl
        // String host = "217.118.170.35";
        // String filepath = "vod/57/";
        // String filename =
        // "RTL_achtunghartwich_080309_700k.wmv?ssid=a18561c909ba12eae66388c47059170f&cid=1205786160&rd=1&rnd=7387286";

        MMSNegotiator negotiator = new MMSNegotiator();
        client = new MMSClient(host, 1755, negotiator);
        negotiator.setClient(client);

        // configure the negotiator
        // connect
        Connect connect = new Connect();
        connect.setPlayerInfo("NSPlayer/7.0.0.1956");
        connect.setGuid(UUID.randomUUID().toString());
        connect.setHost(host);
        negotiator.setConnect(connect);
        // connect funnel
        ConnectFunnel cf = new ConnectFunnel();
        cf.setIpAddress("192.168.0.1");
        cf.setProtocol("TCP");
        cf.setPort("1037");
        negotiator.setConnectFunnel(cf);
        // open file
        OpenFile of = new OpenFile();
        of.setFileName(filepath + filename);
        negotiator.setOpenFile(of);
        // read block
        ReadBlock rb = new ReadBlock();
        negotiator.setReadBlock(rb);
        // stream switch
        StreamSwitch ss = new StreamSwitch();
        ss.addStreamSwitchEntry(ss.new StreamSwitchEntry(0xFFFF, 1, 0));
        ss.addStreamSwitchEntry(ss.new StreamSwitchEntry(0xFFFF, 2, 0));
        negotiator.setStreamSwitch(ss);

        // dump the received header and media packets to a file
        File dump = new File("/tmp", filename);
        MMSFileDumper dumper = new MMSFileDumper(dump);
        client.addMessageListener(dumper);
        client.addMessageListener(this);
        client.addPacketListener(dumper);
        client.addPacketListener(this);

        client.addAdditionalIoHandler(new IoHandlerAdapter() {
            @Override
            public void sessionCreated(IoSession session) throws Exception {
                super.sessionCreated(session);
                session.getFilterChain().addLast("sentDump", new MessageSentDumpFilter(new File("/tmp/out.txt")));
            }

            @Override
            public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
                super.exceptionCaught(session, cause);
                CloseFuture cf = session.close(true);
                cf.addListener(new IoFutureListener<IoFuture>() {
                    @Override
                    public void operationComplete(IoFuture future) {
                        System.exit(1);
                    }
                });
            }
        });

        // finally start the download, the negotiator
        // will start automatically when the connection is
        // established
        try {
            client.connect();
        } catch (Exception e) {
            System.err.println("Couldn't connect to host");
            e.printStackTrace(System.err);
        }
    }

    /**
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        new Test();
    }

    public void messageReceived(MMSMessage command) {
        if (command instanceof ReportStreamSwitch) {
            // great, we can start the streaming
            long startPacket = 0;

            // start the streaming
            client.startStreaming(startPacket);
        }
    }

    @Override
    public void packetReceived(MMSPacket mmspacket) {
        if (mmspacket instanceof MMSHeaderPacket) {
            MMSHeaderPacket hp = (MMSHeaderPacket) mmspacket;
            ByteArrayInputStream bin = new ByteArrayInputStream(hp.getData());
            ASFInputStream asfin = new ASFInputStream(bin);

            ASFObject asfo = null;
            int count = 0;
            while( !(asfo instanceof ASFToplevelHeader) && count++ < 5) {
                try {
                    asfo = asfin.readASFObject();
                } catch (Exception e) {
                    logger.warn("Ignoring unknown ASF header object", e);
                }   
            }

            if(asfo != null) {
                ASFToplevelHeader asfHeader = (ASFToplevelHeader) asfo;
                logger.debug("ASF header: {}", asfHeader);
            }
        }
    }
}