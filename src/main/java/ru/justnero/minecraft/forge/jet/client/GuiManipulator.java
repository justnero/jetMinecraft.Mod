package ru.justnero.minecraft.forge.jet.client;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import ru.justnero.minecraft.forge.jet.tweak.Jet;
import ru.justnero.minecraft.forge.jet.client.gui.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;

@SideOnly(Side.CLIENT)
public class GuiManipulator {

	private static final ArrayList<IIngameRender> ingameRenders = new ArrayList<IIngameRender>();

	private boolean first = true;
	private String language = "";

	@SubscribeEvent
	public void onGuiOpenEvent(GuiOpenEvent event) {
		if(first) {
			Display.setTitle(Jet.title());
			Minecraft.getMinecraft().ingameGUI = new JetIngame(Minecraft.getMinecraft());
			first = false;
		}
		if(!language.equals(Minecraft.getMinecraft().gameSettings.language)) {
			JetDirection.load();
			language = Minecraft.getMinecraft().gameSettings.language;
		}
		if(event.gui != null) {
			String className = event.gui.getClass().getName();
			if(className.equals("net.minecraft.client.gui.GuiMainMenu")) {
				event.gui = new JetMainMenu();
			} else if(className.equals("net.minecraft.client.gui.GuiIngameMenu")) {
				event.gui = new JetIngameMenu();
			} else if(className.equals("net.minecraft.client.gui.GuiMultiplayer")) {
				String fieldName = "field_146798_g";
				GuiScreen parent = ReflectionHelper.getPrivateValue(GuiMultiplayer.class,(GuiMultiplayer) event.gui,fieldName);
				event.gui = new JetMultiplayer(parent);
			}
		}
	}

	public static void addIngameRender(IIngameRender render) {
		if(!ingameRenders.contains(render)) {
			ingameRenders.add(render);
		}
	}

	public static void fireIngameRender(Gui gui, FontRenderer fontrenderer, int width, int height) {
        /*ProtectManager pm = ProtectManager.instance();
        Protect currRegion = pm.getPlayerPositionCollision();
        if (currRegion == null)
            currRegion = pm.getPlayerLookCollision(false);
        if (currRegion != null) {
            String color = colorChar+(pm.playerCanBuildOnView() ? "2" : "4");
            String currRegName = new StringBuilder().append(colorChar).append("f[ ").append(color).append(currRegion.getName()).append(" ").append(colorChar).append("f]").toString();
            drawString(fontrenderer,currRegName,k/2-fontrenderer.getStringWidth(currRegName)/2,2,0);
        }*/

		for(IIngameRender render : ingameRenders) {
			render.draw(gui,fontrenderer,width,height);
		}
	}

}
