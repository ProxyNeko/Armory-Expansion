package org.softc.armoryexpansion.integration.plugins.yoyos;

import org.softc.armoryexpansion.integration.plugins.tinkers_construct.ITiCMaterial;

public interface IYoyosMaterial extends ITiCMaterial {
    String CORD = "cord";
    String AXLE = "axle";
    String BODY = "body";

    boolean isYoyoMaterial();

    IYoyosMaterial setYoyoMaterial(boolean yoyoMaterial);

    boolean isCordMaterial();

    IYoyosMaterial setCordMaterial(boolean cordMaterial);

    boolean isBodyMaterial();

    IYoyosMaterial setBodyMaterial(boolean bodyMaterial);

    boolean isAxleMaterial();

    IYoyosMaterial setAxleMaterial(boolean axleMaterial);

    IYoyosMaterial setDurability(int durability);

    IYoyosMaterial setMagicAffinity(float magicAffinity);

    IYoyosMaterial addTrait(String trait, String location);

    IYoyosMaterial addPrimaryYoyoTrait(String trait);

    IYoyosMaterial addSecondaryYoyoTrait(String trait);

    IYoyosMaterial addGlobalYoyoTrait(String trait);
}
