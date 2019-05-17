package org.softc.armoryexpansion.integration.plugins.yoyos;

import net.minecraftforge.common.config.Property;
import org.softc.armoryexpansion.integration.plugins.tinkers_construct.TiCMaterial;

import java.util.Map;

public class YoyosMaterial extends TiCMaterial implements IYoyosMaterial{
    private boolean isYoyoMaterial = false;
    private boolean isCordMaterial = false;
    private boolean isBodyMaterial = false;
    private boolean isAxleMaterial = false;

    public YoyosMaterial(String identifier, String itemName, int color) {
        super(identifier, itemName, color);
    }

    @Override
    public boolean isYoyoMaterial() {
        return isYoyoMaterial;
    }

    @Override
    public IYoyosMaterial setYoyoMaterial(boolean yoyoMaterial) {
        isYoyoMaterial = yoyoMaterial;
        return this;
    }

    @Override
    public boolean isCordMaterial() {
        return isCordMaterial;
    }

    @Override
    public IYoyosMaterial setCordMaterial(boolean cordMaterial) {
        isCordMaterial = cordMaterial;
        return this;
    }

    @Override
    public boolean isBodyMaterial() {
        return isBodyMaterial;
    }

    @Override
    public IYoyosMaterial setBodyMaterial(boolean bodyMaterial) {
        isBodyMaterial = bodyMaterial;
        return this;
    }

    @Override
    public boolean isAxleMaterial() {
        return isAxleMaterial;
    }

    @Override
    public IYoyosMaterial setAxleMaterial(boolean axleMaterial) {
        isAxleMaterial = axleMaterial;
        return this;
    }

    @Override
    public IYoyosMaterial setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    @Override
    public IYoyosMaterial setMagicAffinity(float magicAffinity) {
        this.magicAffinity = magicAffinity;
        return this;
    }

    @Override
    public boolean registerTinkersMaterialStats(Map<String, Property> properties){
        return this.isYoyoMaterial;
    }

    @Override
    public IYoyosMaterial addTrait(String trait, String location) {
        this.traits.add(new TraitHolder(trait, location));
        return this;
    }

    @Override
    public IYoyosMaterial addPrimaryYoyoTrait(String trait) {
        this.addTrait(trait, IYoyosMaterial.BODY);
        return this;
    }

    @Override
    public IYoyosMaterial addSecondaryYoyoTrait(String trait) {
        this.addTrait(trait, IYoyosMaterial.AXLE);
        this.addTrait(trait, IYoyosMaterial.CORD);
        return this;
    }

    @Override
    public IYoyosMaterial addGlobalYoyoTrait(String trait) {
        this.addPrimaryYoyoTrait(trait);
        this.addSecondaryYoyoTrait(trait);
        return this;
    }
}
