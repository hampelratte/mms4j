package org.hampelratte.net.mms.http;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.hampelratte.net.mms.MMSObjectDecoder;
import org.hampelratte.net.mms.data.MMSHeaderPacket;
import org.hampelratte.net.mms.data.MMSMediaPacket;
import org.hampelratte.net.mms.io.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decoder for all objects received from a server (messages data packets)
 * 
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class MMSHttpResponseDecoder extends CumulativeProtocolDecoder {

    private static transient Logger logger = LoggerFactory.getLogger(MMSHttpResponseDecoder.class);

    private MMSObjectDecoder mmsDecoder = new MMSObjectDecoder();

    /**
     * This is the state of the connection. If streaming is set to true, we expected only $H, $D etc. packages. We do not have to decode the http part. This is
     * only necessary for the first few loops of the decoding.
     */
    private boolean streaming = false;
    int count = 0;

    @Override
    protected boolean doDecode(IoSession session, IoBuffer b, ProtocolDecoderOutput out) throws Exception {
        logger.trace("Buffer size: {}\n{}", b.remaining(), StringUtils.toHexString(b));
        b.mark();

        while (b.remaining() > 0) {
            if (!streaming) {
                if (!headerComplete(b)) {
                    // header not yet complete
                    logger.trace("Wating for more header data");
                    return false;
                }

                // parse the header
                Map<String, List<String>> header = decodeHeader(b);
                for (String key : header.keySet()) {
                    logger.trace("{} = {}", key, header.get(key));
                }

                // check the http status
                String status = header.get("STATUS").get(0);
                if (!status.toUpperCase().endsWith("200 OK")) {
                    throw new IOException("Server response is: " + status);
                }

                // determine the message body size
                List<String> list = header.get("Content-Length");
                if (list != null && list.size() > 0) {
                    String contentLength = list.get(0);
                    long bodySize = Long.parseLong(contentLength);

                    if (b.remaining() < bodySize) {
                        logger.trace("Wating for more message body data");
                        b.reset();
                        return false;
                    }

                    // header and body complete
                    assert b.remaining() == bodySize;
                    logger.trace("Response complete");
                    logger.debug("HTTP status line: {}", status);
                    logger.debug("Content-Length: {}", bodySize);
                }
                logger.trace("Position {}, Limit {}, Remaining {}", new Object[] { b.position(), b.limit(), b.remaining() });
            }

            streaming = true; // from now on we expect only framing headers to arrive
            b.mark(); // mark the current position, so that we can reset the buffer, if data is missing to decode the framing header + mms packet
            // do we have enough bytes remaining to decode the framing header?
            if (b.remaining() < 4) {
                b.reset();
                return false;
            }

            // check, if the framed packet is complete
            FramingHeader fh = decodeFramingHeader(b);
            if (b.remaining() < fh.getPacketLength() - 4) {
                logger.trace("Waiting for framed packet to complete {}/{}", b.remaining(), fh.getPacketLength());
                b.reset();
                return false;
            }

            // now decode the framed packet $H, $D, etc
            if (fh.getPacketId() == 'H') {
                b.order(ByteOrder.LITTLE_ENDIAN);
                long locationId = b.getUnsignedInt();
                byte playIncarnation = b.get();
                byte afFlags = b.get();
                int mmsPacketLength = b.getUnsignedShort();
                b.order(ByteOrder.BIG_ENDIAN);

                // do we have enough bytes to read in the whole mms packet?
                if (b.remaining() < mmsPacketLength - 8) {
                    logger.trace("Waiting for mms packet to complete {}/{}", b.remaining(), mmsPacketLength);
                    b.reset();
                    return false;
                }

                logger.trace("MMS packet length {}, remaining {}", mmsPacketLength, b.remaining());
                logger.debug("Framing Header: {}", fh);

                // read in the data
                byte[] buf = new byte[mmsPacketLength - 8];
                b.get(buf);

                MMSHeaderPacket packet = new MMSHeaderPacket(locationId, afFlags, mmsPacketLength);
                packet.setData(buf);
                out.write(packet);

                // read in the asf top level header
                // ByteArrayInputStream bin = new ByteArrayInputStream(buf);
                // ASFInputStream asfin = new ASFInputStream(bin);
                // ASFObject asfTopLevelHeader = asfin.readASFObject();
                //
                // // read in the header part of the ASF Data Object
                // GUID objectID = asfin.readGUID();
                // assert "75B22636-668E-11CF-A6D9-00AA0062CE6C".equals(objectID.toString());
                // long dataSize = asfin.readLELong();
                // logger.debug("ASF data size: {}", dataSize);
                // GUID fileID = asfin.readGUID();
                // logger.debug("File ID: {}", fileID);
                // long packetCount = asfin.readLELong();
                // logger.debug("Packet count: {}", packetCount);
                //
                // out.write(asfTopLevelHeader);
            } else if (fh.getPacketId() == 'D') {
                // logger.debug("$D(ata) Packet:\n{}", StringUtils.toHexString(b, 16));
                // logger.debug("Framing Header: {}", fh);
                // logger.debug("Position {}, Limit {}, Remaining {}", new Object[] { b.position(), b.limit(), b.remaining() });

                // do we have enough bytes to read in the mms header?
                if (b.remaining() < 8) {
                    logger.trace("Waiting for mms header to arrive {}/8", b.remaining());
                    b.reset();
                    return false;
                }

                b.order(ByteOrder.LITTLE_ENDIAN);
                long locationId = b.getUnsignedInt();
                byte afflags = b.get(); // AFFLags
                byte playIncarnation = b.get(); // playIncarnation
                // logger.debug("play incarnation {}", playIncarnation);
                int mmsPacketLength = b.getUnsignedShort();
                // logger.debug("frame header size {}", fh.getPacketLength());
                // logger.debug("mms packet size {}", mmsPacketLength);
                b.order(ByteOrder.BIG_ENDIAN);

                // do we have enough bytes to read in the whole mms packet?
                if (b.remaining() < mmsPacketLength - 8) {
                    logger.trace("Waiting for mms packet to complete {}/{}", b.remaining(), mmsPacketLength);
                    b.reset();
                    return false;
                }

                // read in the data
                byte[] buf = new byte[mmsPacketLength - 8];
                b.get(buf);
                logger.debug("Sequence: {}", locationId);
                MMSMediaPacket packet = new MMSMediaPacket(locationId, afflags, mmsPacketLength);
                packet.setData(buf);
                int padding = 8948 - packet.getData().length;
                if (padding > 0) {
                    packet.addPadding(padding);
                }
                out.write(packet);

                // if (count++ == 10) {
                // System.exit(0);
                // }
            }
        }

        return true;
    }

    private FramingHeader decodeFramingHeader(IoBuffer b) {
        // read the WMSP framing header
        FramingHeader fh = new FramingHeader();

        short firstEightBits = b.get();
        // determine the b flag
        fh.setB((firstEightBits & 0x80) == 0x80);

        // determine the frame
        fh.setFrame((short) (firstEightBits & 0x7F));
        assert fh.getFrame() == 0x24;

        // read the packet id
        fh.setPacketId(b.get() & 0xFF);

        // read the packet size
        b.order(ByteOrder.LITTLE_ENDIAN);
        fh.setPacketLength(b.getUnsignedShort());
        b.order(ByteOrder.BIG_ENDIAN);

        /*
         * TODO Check, if the reason field is existent. We have to check, if the next 4 bytes are a valid HRESULT. This would imply, that the reason field is
         * set. Otherwise the MMS Packet starts at this position.
         */

        return fh;
    }

    private boolean headerComplete(IoBuffer buf) {
        while (buf.remaining() > 0) {
            byte b0 = buf.get();
            int position = buf.position();
            if (b0 == 0x0d) {
                byte b1 = buf.get();
                byte b2 = buf.get();
                byte b3 = buf.get();
                if (b1 == 0x0a && b2 == 0x0d && b3 == 0x0a) {
                    buf.reset();
                    return true;
                } else {
                    buf.position(position);
                }
            }
        }
        buf.reset();
        return false;
    }

    private Map<String, List<String>> decodeHeader(IoBuffer b) {
        Map<String, List<String>> header = new HashMap<String, List<String>>();

        while (b.remaining() > 0) {
            String line = readLine(b);
            if (line.trim().isEmpty()) {
                // an empty line is the end of the header
                break;
            } else {
                int delim = line.indexOf(':');
                if (delim > 0) {
                    String key = line.substring(0, delim);
                    String value = line.substring(delim + 1).trim();
                    List<String> values = header.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        header.put(key, values);
                    }
                    values.add(value);
                } else {
                    // this should be the status line
                    List<String> status = new ArrayList<String>();
                    status.add(line);
                    header.put("STATUS", status);
                }
            }
        }

        return header;
    }

    private String readLine(IoBuffer b) {
        int initialPosition = b.position();
        int initialLimit = b.limit();

        int lineEnd = b.limit();
        while (b.remaining() > 0) {
            byte cr = b.get();
            int position = b.position();
            if (cr == 0x0d) {
                byte lf = b.get();
                if (lf == 0x0a) {
                    lineEnd = b.position();
                    break;
                } else {
                    b.position(position);
                }
            }
        }

        // read the line into a byte[]
        b.position(initialPosition);
        b.limit(lineEnd);
        byte[] buf = new byte[b.remaining()];
        b.get(buf);

        // reset the buffer so that the remaining bytes can be read
        b.position(b.limit());
        b.limit(initialLimit);

        // return the line without cr/lf
        return new String(buf, 0, buf.length - 2);
    }
}