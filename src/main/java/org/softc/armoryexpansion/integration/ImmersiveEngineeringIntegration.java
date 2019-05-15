package org.softc.armoryexpansion.integration;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.softc.armoryexpansion.ArmoryExpansion;
import org.softc.armoryexpansion.integration.aelib.JsonIntegration;

@Mod(
        modid = ImmersiveEngineeringIntegration.MODID,
        name = ImmersiveEngineeringIntegration.NAME,
        version = ArmoryExpansion.VERSION,
        dependencies = ImmersiveEngineeringIntegration.DEPENDENCIES
)
@Mod.EventBusSubscriber
public class ImmersiveEngineeringIntegration extends JsonIntegration {
    static final String MODID = ArmoryExpansion.MODID + "-" + ImmersiveEngineering.MODID;
    static final String NAME = ArmoryExpansion.NAME + " - " + ImmersiveEngineering.MODNAME;
    static final String DEPENDENCIES =
            "required-after:" + ArmoryExpansion.MODID + "; " +
            "after:" + ImmersiveEngineering.MODID + "; ";

    public ImmersiveEngineeringIntegration() {
        super(ImmersiveEngineering.MODID, "assets/" + ArmoryExpansion.MODID + "/data/" + ImmersiveEngineering.MODID);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.modid = ImmersiveEngineering.MODID;
        super.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event){
        super.registerBlocks(event);
    }
}
