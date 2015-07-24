package ru.justnero.minecraft.forge.jet.proxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;

import ru.justnero.minecraft.forge.jet.client.GuiManipulator;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        super.init();
        initEvents();
    }

    private void initEvents() {
    	MinecraftForge.EVENT_BUS.register(new GuiManipulator());
    }

}
