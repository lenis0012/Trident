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
package net.tridentsdk.server.entity;

import net.tridentsdk.Coordinates;
import net.tridentsdk.entity.EntityProperties;
import net.tridentsdk.entity.decorate.Impalable;
import net.tridentsdk.entity.living.ProjectileLauncher;
import net.tridentsdk.entity.projectile.Projectile;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Represents an entity that is thrown or launched
 *
 * @author The TridentSDK Team
 */
public abstract class TridentProjectile extends TridentEntity implements Projectile {
    /**
     * The source that fires the projectile
     */
    protected volatile WeakReference<ProjectileLauncher> source;
    /**
     * The impalable that the projectile hit, if any
     */
    protected Impalable impaled;

    /**
     * Inherits UUID and spawnLocation from {@link TridentEntity}
     *
     * @param source the entity which fired the projectile
     */
    public TridentProjectile(UUID uniqueId, Coordinates spawnLocation, ProjectileLauncher source) {
        super(uniqueId, spawnLocation);
        this.source = new WeakReference<>(source);
    }

    @Override
    public boolean isNameVisible() {
        return false;
    }

    @Override
    public abstract void applyProperties(EntityProperties properties);

    @Override
    public void doHit() {
        // TODO Perform impaling logic
        Impalable impalable = null;
        impalable.put(this);
        this.hit();
    }

    /**
     * Performed when the projectile hits something
     */
    protected abstract void hit();

    @Override
    public Impalable getImpaled() {
        return this.impaled;
    }

    @Override
    public void setLauncher(final ProjectileLauncher shooter) {
        super.executor.addTask(new Runnable() {
            @Override
            public void run() {
                TridentProjectile.this.source = new WeakReference<>(shooter);
            }
        });
    }

    @Override
    public ProjectileLauncher getLauncher() {
        return this.source.get();
    }
}
