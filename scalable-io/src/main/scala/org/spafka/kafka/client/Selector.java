package org.spafka.kafka.client;


import org.apache.kafka.common.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.*;
import java.util.*;


public class Selector {

    private static final Logger log = LoggerFactory.getLogger(org.apache.kafka.common.network.Selector.class);

    private final java.nio.channels.Selector nioSelector;

    public Selector() throws IOException {
        this.nioSelector = java.nio.channels.Selector.open();
    }

    private final Map<String, KafkaChannel> channels=new HashMap<>();


    public void connect(String id, InetSocketAddress address, int sendBufferSize, int receiveBufferSize) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Socket socket = socketChannel.socket();
        socket.setKeepAlive(true);
        if (sendBufferSize != Selectable.USE_DEFAULT_BUFFER_SIZE)
            socket.setSendBufferSize(sendBufferSize);
        if (receiveBufferSize != Selectable.USE_DEFAULT_BUFFER_SIZE)
            socket.setReceiveBufferSize(receiveBufferSize);
        socket.setTcpNoDelay(true);
        boolean connected;
        try {
            connected = socketChannel.connect(address);
        } catch (UnresolvedAddressException e) {
            socketChannel.close();
            throw new IOException("Can't resolve address: " + address, e);
        } catch (IOException e) {
            socketChannel.close();
            throw e;
        }
        SelectionKey key = socketChannel.register(nioSelector, SelectionKey.OP_CONNECT);

    }


    public void register(String id, SocketChannel socketChannel) throws ClosedChannelException {
        SelectionKey key = socketChannel.register(nioSelector, SelectionKey.OP_READ);

    }


    public void wakeup() {
        this.nioSelector.wakeup();
    }


    public void poll(long timeout) throws IOException {
        if (timeout < 0)
            throw new IllegalArgumentException("timeout should be >= 0");


        pollSelectionKeys(this.nioSelector.selectedKeys(), false);
        //     pollSelectionKeys(immediatelyConnectedKeys, true);


    }

    private void pollSelectionKeys(Iterable<SelectionKey> selectionKeys, boolean isImmediatelyConnected) throws IOException {
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            KafkaChannel channel = channelOrFail("1",false);


                /* complete any connections that have finished their handshake (either normally or immediately) */
            if (isImmediatelyConnected || key.isConnectable()) {
                if (channel.finishConnect()) {
                } else
                    continue;
            }

                /* if channel is not ready finish prepare */
            if (channel.isConnected() && !channel.ready())
                channel.prepare();

                /* if channel is ready read from any connections that have readable data */
            if (channel.ready() && key.isReadable()) {
                NetworkReceive networkReceive;
                while ((networkReceive = channel.read()) != null) {
                }

                /* if channel is ready write to any sockets that have space in their buffer and for which we have data */
                if (channel.ready() && key.isWritable()) {
                    Send send = channel.write();
                    if (send != null) {

                    }
                }

                /* cancel any defunct sockets */
                if (!key.isValid()) {
                    close(channel);

                }

            }
        }

    }


    private int select(long ms) throws IOException {
        if (ms < 0L)
            throw new IllegalArgumentException("timeout should be >= 0");

        if (ms == 0L)
            return this.nioSelector.selectNow();
        else
            return this.nioSelector.select(ms);
    }

    public void close(String id) {
    }

    private void close(KafkaChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            log.error("Exception closing connection to node {}:", channel.id(), e);
        }

    }

    private KafkaChannel channelOrFail(String id, boolean maybeClosing) {
        KafkaChannel channel = this.channels.get(id);
        if (channel == null && maybeClosing)
        if (channel == null)
            throw new IllegalStateException("Attempt to retrieve channel for which there is no connection. Connection id " + id + " existing connections " + channels.keySet());
        return channel;
    }

}
