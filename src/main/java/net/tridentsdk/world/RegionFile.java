/*
 * Copyright (c) 2014, The TridentSDK Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *     3. Neither the name of TridentSDK nor the names of its
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
package net.tridentsdk.world;

import java.io.*;

/**
 * Represents a Region File (in region/ directory) in memory
 */
public class RegionFile {

    private RandomAccessFile file;
    private int[] locations = new int[1024];
    private int[] timestamps = new int[1024];

    public RegionFile(File path) throws IOException {
        file = new RandomAccessFile(path, "rw");

        // Packing to default size of 8192 if it isn't already that size
        // (this should really never happen, but I'll take my changes)
        if (file.length() < 8192) {
            file.seek(file.length());

            long diff = 8192 - file.length();
            for (long l = 0; l < diff; l++) {
                file.write(0);
            }
        }
        
        // Packing if length is not a multiple of 4096
        
        if((file.length() & 4095) != 0){
            long diff = file.length() - (file.length() & 4095);
            
            for(long l = 0; l < diff; l++){
                file.write(0);
            }
        }
        // Jump to beginning
        file.seek(0);
        
        // Read Locations
        for(int i = 0; i < 1024; i++){
            locations[i] = file.readInt();
        }
        
        // Read Timestamps
        for(int i = 0; i < 1024; i++){
            timestamps[i] = file.readInt();
        }
        
        
    }

}