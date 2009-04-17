package org.hampelratte.net.mina.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mms.MMSObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSentDumpFilter extends IoFilterAdapter {
    
    private static transient Logger logger = LoggerFactory.getLogger(MessageSentDumpFilter.class);
    
    private FileOutputStream out;
    
    private MMSObjectEncoder enc;
    
    private ProtocolEncoderOutput output;
    
    public MessageSentDumpFilter(File outputFile) {
        enc = new MMSObjectEncoder();
        output = new ProtocolEncoderOutput() {

            @Override
            public WriteFuture flush() {
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void mergeAll() {
                throw new RuntimeException("Operation not implemented");
            }

            @Override
            public void write(Object encodedMessage) {
                try {
                    out.getChannel().write( ((IoBuffer)encodedMessage).buf() );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            openFile(outputFile);
        } catch (FileNotFoundException e) {
            logger.error("Couldn't open stream dump file", e);
        }
    }
    
    private void openFile(File outputFile) throws FileNotFoundException {
        out = new FileOutputStream(outputFile);
    }

    @Override
    public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
//        out.write(writeRequest.getMessage().getClass().toString().getBytes());
        enc.encode(session, writeRequest.getMessage(), output);
//        out.write("\n".getBytes());
        nextFilter.messageSent(session, writeRequest);
    }
}