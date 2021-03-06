/*
 * Trident - A Multithreaded Server Alternative
 * Copyright 2014 The TridentSDK Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tridentsdk.server.packets.play.out;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.server.netty.Codec;
import net.tridentsdk.server.netty.packet.OutPacket;
import net.tridentsdk.world.ChunkLocation;

public class PacketPlayOutChunkData extends OutPacket {

    protected final byte[] data = {};
    protected ChunkLocation chunkLocation;
    protected boolean continuous;
    protected short primaryBitMap;

    @Override
    public int getId() {
        return 0x21;
    }

    public ChunkLocation getChunkLocation() {
        return this.chunkLocation;
    }

    public boolean isContinuous() {
        return this.continuous;
    }

    public short getPrimaryBitMap() {
        return this.primaryBitMap;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(this.chunkLocation.getX());
        buf.writeInt(this.chunkLocation.getZ());

        buf.writeBoolean(this.continuous);
        buf.writeByte((int) this.primaryBitMap);

        Codec.writeVarInt32(buf, this.data.length);
        buf.writeBytes(this.data);
    }
}
