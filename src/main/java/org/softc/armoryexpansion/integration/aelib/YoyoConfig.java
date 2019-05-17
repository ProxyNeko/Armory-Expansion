package org.softc.armoryexpansion.integration.aelib;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.softc.armoryexpansion.integration.plugins.tinkers_construct.ITiCMaterial;
import org.softc.armoryexpansion.integration.plugins.yoyos.IYoyosMaterial;

import java.util.HashMap;
import java.util.Map;

public class YoyoConfig extends Config {
    private static final String CATEGORY_YOYO = "yoyo";
    public YoyoConfig(Configuration configuration) {
        super(configuration);
    }

    public void syncConfig(Map<String, ? extends ITiCMaterial> materials) { // Gets called from preInit
        try {
            // Load config
            configuration.load();
            for (ITiCMaterial material:materials.values())
            {
                IYoyosMaterial mat = (IYoyosMaterial) material;
                Map<String, Property> materialProperties = new HashMap<>();
                if(!properties.containsKey(mat.getIdentifier())){
                    configuration.getCategory(mat.getIdentifier());
                    materialProperties.put(CATEGORY_MATERIAL, addMaterialProperty(mat));
                    materialProperties.put(TRAIT, addMaterialTraitProperty(mat));

                    if(mat.isYoyoMaterial()){
                        putSubcategoryProperty(materialProperties, CATEGORY_YOYO, IYoyosMaterial.BODY, mat);
                        putSubcategoryProperty(materialProperties, CATEGORY_YOYO, IYoyosMaterial.AXLE, mat);
                        putSubcategoryProperty(materialProperties, CATEGORY_YOYO, IYoyosMaterial.CORD, mat);
                    }
                }
                properties.put(material.getIdentifier(), materialProperties);
            }
        } catch (final Exception e) {
            // Failed reading/writing, just continue
        } finally {
            // Save props to config IF config changed
            if (configuration.hasChanged()) configuration.save();
        }
    }
}
