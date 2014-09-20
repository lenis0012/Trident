/*
 * Copyright (c) 2014, The TridentSDK Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     1. Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. Neither the name of the The TridentSDK Team nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL The TridentSDK Team BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.tridentsdk.server.netty.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import net.tridentsdk.api.Trident;
import net.tridentsdk.server.TridentServer;
import net.tridentsdk.server.netty.Codec;
import net.tridentsdk.server.netty.client.ClientConnection;
import net.tridentsdk.server.netty.protocol.Protocol;

import java.util.List;

/**
 * Channel handler that decodes the packet data sent from the stream in the form of the byte buffer. This is needed to
 * interpret the data sent correctly, and make sure that the data maintains its transmission integrity. <p/> <p>Note
 * this is not shareable. It must be thread confined, or create a new instance for each channel.</p>
 *
 * @author The TridentSDK Team
 */
public class PacketDecoder extends ReplayingDecoder<PacketDecoder.State> {
    
    private int rawLength;

    /**
     * Creates the decoder and initializes the state
     */
    public PacketDecoder() {
        super(State.LENGTH);
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buf, List<Object> objects) throws Exception {
        
        switch (this.state()) {
        /* Reading the length of sent packet */
        case LENGTH:
            if(ClientConnection.encryptionEnabled(context)) {
                rawLength = buf.readableBytes();
            } else {
                rawLength = Codec.readVarInt32(buf);
            }

            this.checkpoint(State.DATA);
            //NOTE: Not meant to break;

        /* Reading the packet */
        case DATA:
            // read amount of data stated by rawLength
            ByteBuf data = buf.readBytes(rawLength);

            // read the length of the packet if encrypted, as would be ignored
            if(ClientConnection.encryptionEnabled(context)) {
                Codec.readVarInt32(buf);
            }

            // add packet data to objects to be handled
            objects.add(new PacketData(data));

            // read the next packet (i.e repeat)
            this.checkpoint(State.LENGTH);
            break;
            
        }
     
    }

    /**
     * The current read state of the decoder
     *
     * @author The TridentSDK Team
     */
    enum State {
        LENGTH, DATA
    }
}
