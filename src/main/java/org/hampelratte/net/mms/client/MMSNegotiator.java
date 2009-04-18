package org.hampelratte.net.mms.client;

import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.client.listeners.MMSMessageListener;
import org.hampelratte.net.mms.client.listeners.MMSPacketListener;
import org.hampelratte.net.mms.data.MMSHeaderPacket;
import org.hampelratte.net.mms.data.MMSPacket;
import org.hampelratte.net.mms.messages.MMSMessage;
import org.hampelratte.net.mms.messages.client.Connect;
import org.hampelratte.net.mms.messages.client.ConnectFunnel;
import org.hampelratte.net.mms.messages.client.OpenFile;
import org.hampelratte.net.mms.messages.client.Pong;
import org.hampelratte.net.mms.messages.client.ReadBlock;
import org.hampelratte.net.mms.messages.client.StreamSwitch;
import org.hampelratte.net.mms.messages.server.Ping;
import org.hampelratte.net.mms.messages.server.ReportConnectedEX;
import org.hampelratte.net.mms.messages.server.ReportConnectedFunnel;
import org.hampelratte.net.mms.messages.server.ReportOpenFile;
import org.hampelratte.net.mms.messages.server.ReportReadBlock;
import org.hampelratte.net.mms.messages.server.ReportStreamSwitch;

/**
 * Negotiator class, which connects to the server and negotiates some connection
 * properties
 * 
 * TODO all messages with HRESULT should throw an exception, if HRESULT != 0
 */
public class MMSNegotiator implements MMSMessageListener, MMSPacketListener {

    private MMSClient client;
    private IoSession session;
    boolean running = false;
    boolean waitingForHeader = false;
    
    // client requests
    protected Connect connect;
    protected ConnectFunnel connectFunnel;
    protected OpenFile openFile;
    protected ReadBlock readBlock;
    protected StreamSwitch streamSwitch;
    
    // server responses
    private ReportOpenFile reportOpenFile;
    // TODO ...
    
    /**
     * @return the Connect object used to establish the connection
     */
    public Connect getConnect() {
        return connect;
    }

    /**
     * @param connect
     *            the Connect object used to establish the
     *            connection
     */
    public void setConnect(Connect connect) {
        this.connect = connect;
    }

    public ConnectFunnel getConnectFunnel() {
        return connectFunnel;
    }

    public void setConnectFunnel(ConnectFunnel connectFunnel) {
        this.connectFunnel = connectFunnel;
    }

    public OpenFile getOpenFile() {
        return openFile;
    }

    public void setOpenFile(OpenFile openFile) {
        this.openFile = openFile;
    }

    public ReadBlock getReadBlock() {
        return readBlock;
    }

    public void setReadBlock(ReadBlock readBlock) {
        this.readBlock = readBlock;
    }

    public StreamSwitch getStreamSwitch() {
        return streamSwitch;
    }

    public void setStreamSwitch(StreamSwitch streamSwitch) {
        this.streamSwitch = streamSwitch;
    }

    public void start(IoSession session) {
        this.session = session;
        running = true;
        client.sendRequest(connect);
    }
    
    public void stop() {
        running = false;
    }
    
    // TODO check, if the protocol is implemented correctly
    public void messageReceived(MMSMessage message) {
        if(!running) {
            return;
        }
        
        if(message instanceof Ping) {
            client.sendRequest(new Pong());
        } else if(message instanceof ReportConnectedEX) {
            client.sendRequest(getConnectFunnel());
        } else if(message instanceof ReportConnectedFunnel) {
            client.sendRequest(getOpenFile());
        } else if(message instanceof ReportOpenFile) {
            reportOpenFile = (ReportOpenFile) message;
           
            // save the ReportOpenFile in the session, so that
            // the packet length is accessable, if a media packet
            // is received
            session.setAttribute(ReportOpenFile.class, reportOpenFile);
            ReadBlock rb = getReadBlock();
            rb.setOpenFileId(reportOpenFile.getOpenFileId());
            client.sendRequest(getReadBlock());
        } else if(message instanceof ReportReadBlock) {
            waitingForHeader = true;
        } else if(message instanceof ReportStreamSwitch) {
            // everything worked great, we are ready to start the streaming
        } 
    }

    public void packetReceived(MMSPacket packet) {
        if(!running) {
            return;
        }
        
        if(packet instanceof MMSHeaderPacket) {
            if(waitingForHeader) {
                client.sendRequest(getStreamSwitch());
                waitingForHeader = false;
            }
        }
    }

    public MMSClient getClient() {
        return client;
    }

    public void setClient(MMSClient client) {
        this.client = client;
    }

    public ReportOpenFile getReportOpenFile() {
        return reportOpenFile;
    }

    public void setReportOpenFile(ReportOpenFile reportOpenFile) {
        this.reportOpenFile = reportOpenFile;
    }
    
    public boolean isResumeSupported() {
        if(reportOpenFile == null) {
            return false;
        }
        
        int fileAttributes = reportOpenFile.getFileAttributes();
        int CANSEEK = ReportOpenFile.FILE_ATTRIBUTE_MMS_CANSEEK;
        
        return (fileAttributes & CANSEEK) == CANSEEK;
    }
}
