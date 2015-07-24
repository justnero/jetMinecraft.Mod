package ru.justnero.minecraft.forge.jet;

import ru.justnero.minecraft.forge.jet.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="jetMinecraft", name="jetMinecraft", version="1.0")
public class JetMinecraft {

	@Instance("jetMinecraft")
    public static JetMinecraft instance;

    @SidedProxy(clientSide="ru.justnero.minecraft.forge.jet.proxy.ClientProxy",serverSide="ru.justnero.minecraft.forge.jet.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit();
    }

}
