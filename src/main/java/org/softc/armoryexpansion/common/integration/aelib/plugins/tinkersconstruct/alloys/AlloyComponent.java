package org.softc.armoryexpansion.common.integration.aelib.plugins.tinkersconstruct.alloys;


import net.minecraft.nbt.NBTTagCompound;

public class AlloyComponent implements IAlloyComponent {
    private String fluid;
    private int amount;

    public AlloyComponent(String fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    @Override
    public String getFluid() {
        return this.fluid;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public NBTTagCompound getFluidTag(){
        NBTTagCompound fluid = new NBTTagCompound();
        fluid.setString("FluidName", this.fluid);
        fluid.setInteger("Amount", this.amount);
        return fluid;
    }
}
