package ru.justnero.minecraft.forge.jet.client.gui;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.DEBUG;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.TEXT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import ru.justnero.minecraft.forge.jet.client.GuiManipulator;
import ru.justnero.minecraft.forge.jet.client.JetDirection;
import ru.justnero.minecraft.forge.jet.tweak.Jet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;

public class JetIngame extends GuiIngameForge {

	private static final int WHITE = 0xFFFFFF;
    private static final String colorChar = String.valueOf((char) 167);
    public static final List<String> debugInfo = new ArrayList<String>();
	public static final Map<String,List<IIngameTrigger>> debugTriggers = new HashMap<String,List<IIngameTrigger>>();

    private ScaledResolution res = null;
    private FontRenderer fontrenderer = null;
    private RenderGameOverlayEvent eventParent;

	public JetIngame(Minecraft mc) {
		super(mc);
	}

	@Override
    public void renderGameOverlay(float partialTicks, boolean hasScreen, int mouseX, int mouseY) {
		res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        eventParent = new RenderGameOverlayEvent(partialTicks, res, mouseX, mouseY);
        fontrenderer = mc.fontRenderer;

        super.renderGameOverlay(partialTicks, hasScreen, mouseX, mouseY);
    }

	@Override
	protected void renderHUDText(int width, int height) {
	    mc.mcProfiler.startSection("forgeHudText");
	    OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
	    ArrayList<String> left = new ArrayList<String>();
	    ArrayList<String> right = new ArrayList<String>();

	    int playerX = MathHelper.floor_double(mc.thePlayer.posX);
	    int playerY = MathHelper.floor_double(mc.thePlayer.posY);
	    int playerZ = MathHelper.floor_double(mc.thePlayer.posZ);

	    left.add(colorChar+"2"+Jet.project()+" by Nero");
	    left.add(new StringBuilder().append(colorChar).append("9[ ").append(playerX).append(" ; ").append(playerY).append(" ; ").append(playerZ).append(" ]").toString());

		if(this.mc.gameSettings.showDebugInfo && !pre(DEBUG)) {
			mc.mcProfiler.startSection("debug");
			GL11.glPushMatrix();

			left.add(I18n.format("direction.title",JetDirection.directions[(MathHelper.floor_double(mc.thePlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3)]));
			left.add(new StringBuilder().append(colorChar).append("3").append(mc.debug.toUpperCase().substring(0,mc.debug.length()-8)).append("S").toString());
			left.add(null);

			for(int i=0;i<debugInfo.size();i++) {
            	String key = debugInfo.get(i);
            	String line;
            	if(debugTriggers.containsKey(key) && debugTriggers.get(key).size() > 0) {
            		Object args[] = new String[debugTriggers.get(key).size()];
            		for(int j=0;j<debugTriggers.get(key).size();j++) {
            			IIngameTrigger trigger = debugTriggers.get(key).get(j);
            			args[j] = trigger.execute();
            		}
            		line = I18n.format(key,args);
            	} else {
            		line = I18n.format(key);
            	}
            	int offset = 2;
            	if(line.startsWith("!")) {
            		offset = 4;
            		line = line.substring(1);
            	}
            	drawString(fontrenderer,line,offset,52 + 10 * i,0);
            	left.add(null);
            }

			long max = Runtime.getRuntime().maxMemory();
			long total = Runtime.getRuntime().totalMemory();
			long free = Runtime.getRuntime().freeMemory();
			long used = total - free;

			right.add(used / 1024L / 1024L + "MB (" + used * 100L / max + "%) of " + max / 1024L / 1024L + "MB");
			right.add(total * 100L / max + "% (" + total / 1024L / 1024L + "MB)");


		    GL11.glPopMatrix();
		    mc.mcProfiler.endSection();
		    post(DEBUG);
		}

		RenderGameOverlayEvent.Text event = new RenderGameOverlayEvent.Text(eventParent, left, right);
		if(!MinecraftForge.EVENT_BUS.post(event)) {
		    for(int x = 0; x < left.size(); x++) {
		        String msg = left.get(x);
		        if(msg == null) continue;
		        fontrenderer.drawStringWithShadow(msg, 2, 2 + x * 10, WHITE);
		    }

		    for(int x = 0; x < right.size(); x++){
		        String msg = right.get(x);
		        if(msg == null) continue;
		        int w = fontrenderer.getStringWidth(msg);
		        fontrenderer.drawStringWithShadow(msg, width - w - 10, 2 + x * 10, WHITE);
		    }
		}

		GuiManipulator.fireIngameRender(this,fontrenderer,width,height);

		mc.mcProfiler.endSection();
		post(TEXT);
    }

	private boolean pre(ElementType type) {
        return MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Pre(eventParent, type));
    }

    private void post(ElementType type) {
        MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(eventParent, type));
    }

}
