/*
 * Copyright (c) 2014, TridentSDK Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of TridentSDK nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.tridentsdk.data;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.api.Location;

/**
 * Represents a writable form of a {@link net.tridentsdk.api.Location}
 *
 * @author The TridentSDK Team
 */
public class Position implements Writable {
    private Location loc;

    /**
     * Creates a new position based from an existing location
     *
     * @param loc the location to wrap with writable format
     */
    public Position(Location loc) {
        this.loc = loc;
    }

    /**
     * Gets the wrapped, original location
     *
     * @return the location passed in by constructor or by {@link #setLoc(net.tridentsdk.api.Location)}
     */
    public Location getLoc() {
        return this.loc;
    }

    /**
     * Sets the wrapped position
     *
     * <p>This does not change the value of already written locations. This is purely for purposes of performance, but
     * removes concurrency.</p>
     *
     * @param loc the location to wrap with writable format
     */
    public void setLoc(Location loc) {
        this.loc = loc;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong((long) ((int) this.loc.getX() & 0x3FFFFFF) << 38 |
                (long) ((int) this.loc.getY() & 0xFFF) << 26 |
                (int) this.loc.getZ() & 0x3FFFFFF);
    }
}
