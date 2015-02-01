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
import net.tridentsdk.Position;
import net.tridentsdk.server.data.PositionWritable;
import net.tridentsdk.server.netty.Codec;
import net.tridentsdk.server.netty.packet.OutPacket;

public class PacketPlayOutUpdateSign extends OutPacket {
    protected Position loc;
    protected String[] messages;

    @Override
    public int id() {
        return 0x33;
    }

    public Position location() {
        return this.loc;
    }

    public String[] messages() {
        return this.messages;
    }

    @Override
    public void encode(ByteBuf buf) {
        new PositionWritable(this.loc).write(buf);

        for (int i = 0; i < 4; i++) {
            Codec.writeString(buf, this.messages[i]);
        }
    }
}
