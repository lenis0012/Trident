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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import net.tridentsdk.Coordinates;
import net.tridentsdk.base.Tile;
import net.tridentsdk.entity.Entity;
import net.tridentsdk.entity.LivingEntity;
import net.tridentsdk.entity.projectile.Projectile;
import net.tridentsdk.util.Vector;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * An entity that has health
 *
 * @author The TridentSDK Team
 */
public abstract class TridentLivingEntity extends TridentEntity implements LivingEntity {
    /**
     * Whether the entity is dead
     */
    protected final boolean dead;
    /**
     * Whether the entity can pick up items
     */
    protected final boolean canPickup = true;
    /**
     * The entity health
     */
    protected final AtomicDouble health = new AtomicDouble(0.0);
    /**
     * The maximum available health
     */
    protected double maxHealth;
    /**
     * Ticks since the entity was last set on fire and when the fire burned out
     */
    protected volatile AtomicDouble fireTicks = new AtomicDouble(0.0);
    /**
     * The amount of air the entity has, depleted when in water
     */
    protected final AtomicDouble airTicks = new AtomicDouble(0.0);

    /**
     * Describes projectile logic
     */
    public final List<WeakReference<Projectile>> hit = Collections.synchronizedList(
            Lists.<WeakReference<Projectile>>newArrayList());

    /**
     * Inherits from {@link TridentEntity}
     * <p/>
     * <p>The entity is immediately set "non-dead" after {@code super} call</p>
     */
    public TridentLivingEntity(UUID id, Coordinates spawnLocation) {
        super(id, spawnLocation);

        this.dead = false;
    }

    @Override
    public double getHealth() {
        return this.health.get();
    }

    @Override
    public void setHealth(double health) {
        this.health.set(health);
    }

    @Override
    public double getMaxHealth() {
        return this.maxHealth;
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public Coordinates getEyeLocation() {
        return this.getLocation().getRelative(new Vector(0.0d, 1.0d, 0.0d));
    }

    @Override
    public long getRemainingAir() {
        return (long) this.airTicks.get();
    }

    @Override
    public void setRemainingAir(long ticks) {
        this.airTicks.set((double) ticks);
    }

    @Override
    public boolean canPickupItems() {
        return this.canPickup;
    }

    @Override
    public boolean isDead() {
        return this.dead;
    }

    @Override
    public boolean isImpaledEntity() {
        return this.hit != null;
    }

    @Override
    public boolean isImpaledTile() {
        return false;
    }

    @Override
    public Entity impaledEntity() {
        if (!this.isImpaledEntity())
            return null;
        return this;
    }

    @Override
    public Tile impaledTile() {
        return null;
    }

    @Override
    public void put(Projectile projectile) {
        this.hit.add(new WeakReference<>(projectile));
    }

    @Override
    public boolean remove(Projectile projectile) {
        return this.hit.remove(new WeakReference<>(projectile));
    }

    @Override
    public void clear() {
        // TODO remove the projectile entities
        this.hit.clear();
    }

    @Override
    public List<Projectile> projectiles() {
        return new ImmutableList.Builder<Projectile>().addAll(Lists.transform(this.hit,
                new Function<WeakReference<Projectile>,
                        Projectile>() {
                    @Override
                    public Projectile apply(WeakReference<Projectile> projectileWeakReference) {
                        return projectileWeakReference.get();
                    }
                })).build();
    }
}
