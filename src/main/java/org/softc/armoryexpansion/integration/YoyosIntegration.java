package org.softc.armoryexpansion.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jozufozu.yoyos.Yoyos;
import com.jozufozu.yoyos.tinkers.materials.AxleMaterialStats;
import com.jozufozu.yoyos.tinkers.materials.BodyMaterialStats;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.softc.armoryexpansion.ArmoryExpansion;
import org.softc.armoryexpansion.integration.aelib.AbstractIntegration;
import org.softc.armoryexpansion.integration.aelib.Config;
import org.softc.armoryexpansion.integration.aelib.YoyoConfig;
import org.softc.armoryexpansion.integration.plugins.yoyos.IYoyosMaterial;
import org.softc.armoryexpansion.integration.plugins.yoyos.YoyosMaterial;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.tools.TinkerMaterials;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static c4.conarm.lib.materials.ArmorMaterialType.CORE;
import static c4.conarm.lib.materials.ArmorMaterialType.TRIM;
import static org.softc.armoryexpansion.util.Math.clamp;
import static slimeknights.tconstruct.library.materials.MaterialTypes.*;

@Mod(
        modid = YoyosIntegration.MODID,
        name = YoyosIntegration.NAME,
        version = ArmoryExpansion.VERSION,
        dependencies = YoyosIntegration.DEPENDENCIES
)
@Mod.EventBusSubscriber
public class YoyosIntegration extends AbstractIntegration {
    static final String MODID = ArmoryExpansion.MODID + "-" + Yoyos.MODID;
    static final String NAME = ArmoryExpansion.NAME + " - " + Yoyos.NAME;
    static final String DEPENDENCIES =
            "required-after:" + ArmoryExpansion.MODID + "; " +
            "after:" + Yoyos.MODID + "; " +
            "after:*";

    private static final float STAT_MULT = 1.25f;
    private static final int DURA_MIN = 1;
    private static final int DURA_MAX = 120;

    protected Map<String, IYoyosMaterial> materials = new HashMap<>();
    private List<IYoyosMaterial> jsonMaterials = new LinkedList<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.modid = Yoyos.MODID;
        this.logger = event.getModLog();
        Property property = ArmoryExpansion.config
                .get("integrations", modid, true, "Whether integration with " + modid + " should be enabled");
        isEnabled = property == null || property.getBoolean();
        ArmoryExpansion.config.save();
        if(isEnabled){
            this.configHelper = this.getConfigHelper(event);
//            configHelper = new Config(new Configuration(event.getSuggestedConfigurationFile()));
            this.setMaterials(event);
//            this.configHelper.syncConfig(materials);
//            this.registerMaterialStats();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if(isEnabled){
            this.registerMaterialTraits();
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
        this.configHelper.syncConfig(materials);
        this.registerMaterialStats();
    }

    @Override
    protected void loadMaterialsFromSource() {
        for (Material material: TinkerRegistry.getAllMaterials())
        {
            if (this.isConversionAvailable(material)) {
                IYoyosMaterial m = newTiCMaterial(material, TinkerMaterials.iron);
                if (!jsonMaterials.contains(m)){
                    this.addMaterial(m);
                }
            }
        }
    }

    private boolean isConversionAvailable(Material material){
        return this.isBody(material) || this.isAxle(material) || this.isCord(material);
    }

    private boolean isBody(Material material){
        return !material.hasStats(IYoyosMaterial.BODY) && material.hasStats(HEAD);
    }

    private boolean isAxle(Material material){
        return !material.hasStats(IYoyosMaterial.AXLE) && material.hasStats(HANDLE);
    }

    private boolean isCord(Material material){
        return !material.hasStats(IYoyosMaterial.CORD) && material.hasStats(BOWSTRING);
    }

    private IYoyosMaterial newTiCMaterial(Material material, Material baseMaterial){
        return new YoyosMaterial(material.identifier, null, material.materialTextColor)
                .setYoyoMaterial(true)
                .setBodyMaterial(this.isBody(material))
                .setAxleMaterial(this.isAxle(material))
                .setCordMaterial(this.isCord(material))
                .setDurability(calculateDurability(material, baseMaterial))
                .setMagicAffinity(calculateExtraDurability(material, baseMaterial));
    }

    @Override
    protected void loadAlloysFromSource() {
        // Left empty on purpose
        // All the alloys should be added through the JSON file
    }

    @Override
    protected Config getConfigHelper(FMLPreInitializationEvent event){
        return new YoyoConfig(new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/" + ArmoryExpansion.MODID + "/" + modid + ".cfg")));
    }

    private void addMaterial(IYoyosMaterial material){
        if(isMaterialEnabled(material)){
            this.materials.putIfAbsent(material.getIdentifier(), material);
        }
    }

    private int calculateDurability(Material material, BodyMaterialStats body, HeadMaterialStats head){
        if(body == null){
            return 0;
        }
        final HeadMaterialStats materialHead = material.getStats(HEAD);
        //noinspection IntegerDivisionInFloatingPointContext
        return materialHead != null ? (int)clamp(body.durability * materialHead.durability / head.durability / STAT_MULT, DURA_MIN, DURA_MAX): 0;
    }

    private int calculateDurability(Material material, Material baseMaterial){
        return calculateDurability(material, baseMaterial.getStats(IYoyosMaterial.BODY), baseMaterial.getStats(HEAD));
    }

    private float calculateExtraDurability(Material material, AxleMaterialStats axle, ExtraMaterialStats extra){
        if(axle == null){
            return 0;
        }
        final ExtraMaterialStats materialExtra = material.getStats(EXTRA);
        return materialExtra != null ? 2 * axle.modifier * materialExtra.extraDurability / extra.extraDurability / STAT_MULT : 0;
    }

    private float calculateExtraDurability(Material material, Material baseMaterial){
        return calculateExtraDurability(material, baseMaterial.getStats(IYoyosMaterial.AXLE), baseMaterial.getStats(EXTRA));
    }

    @Override
    protected String returnMaterialExample(){
        return  "";
    }

    private void loadMaterials(YoyosMaterial[] jsonMaterials){
        if(jsonMaterials == null){
            return;
        }
        for(YoyosMaterial m:jsonMaterials){
            this.materials.putIfAbsent(m.getIdentifier(), m);
        }
    }

    @Override
    protected void loadMaterialsFromJson(InputStream path){
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().setLenient();
        Gson gson = builder.create();

        YoyosMaterial[] jsonMaterials = gson.fromJson(new BufferedReader(new InputStreamReader(path)), YoyosMaterial[].class);
        this.loadMaterials(jsonMaterials);
    }

    @Override
    protected void loadMaterialsFromJson(String path){
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().setLenient();
        Gson gson = builder.create();

        YoyosMaterial[] jsonMaterials = new YoyosMaterial[0];
        try {
            File input = new File(path);
            jsonMaterials = gson.fromJson(new FileReader(input), YoyosMaterial[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.loadMaterials(jsonMaterials);
    }
}
