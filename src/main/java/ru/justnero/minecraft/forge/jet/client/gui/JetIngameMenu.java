package ru.justnero.minecraft.forge.jet.client.gui;

import org.lwjgl.opengl.GL11;

import ru.justnero.minecraft.forge.jet.tweak.Jet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;

public class JetIngameMenu extends GuiIngameMenu {

	private final String colorChar = String.valueOf((char) 167);

	@SuppressWarnings("unchecked")
	@Override
    public void initGui() {
        buttonList.clear();
        if(mc.isIntegratedServerRunning()) {
            buttonList.add(new GuiButton(0, 2, height - 22, 102, 20, I18n.format("menu.returnToMenu")));
        } else {
            buttonList.add(new GuiButton(0, 2, height - 22, 102, 20, I18n.format("menu.disconnect")));
        }
        buttonList.add(new GuiButton(1, 2, height - 44, 102, 20, I18n.format("menu.options")));
        buttonList.add(new GuiButton(2, 2, height - 66, 102, 20, I18n.format("gui.stats")));
        buttonList.add(new GuiButton(3, 2, height - 88, 102, 20, I18n.format("gui.achievements")));
        buttonList.add(new GuiButton(4, 2, height - 110, 102, 20, I18n.format("menu.returnToGame")));
    }

	@Override
    protected void actionPerformed(GuiButton btn) {
        switch (btn.id) {
            case 0:
                btn.enabled = false;
                mc.theWorld.sendQuittingDisconnectingPacket();
                mc.loadWorld((WorldClient)null);
                mc.displayGuiScreen(new GuiMainMenu());
                break;
            case 1:
                mc.displayGuiScreen(new GuiOptions(this,mc.gameSettings));
                break;
            case 2:
                if(mc.thePlayer != null) {
                    mc.displayGuiScreen(new GuiStats(this,mc.thePlayer.getStatFileWriter()));
                }
                break;
            case 3:
                if(mc.thePlayer != null) {
                    mc.displayGuiScreen(new GuiAchievements(this,mc.thePlayer.getStatFileWriter()));
                }
                break;
            case 4:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                break;
            default:
                break;
        }
    }

	@Override
    public void drawScreen(int par1, int par2, float par3) {
        drawGradientRect(0, height - 132, 108, height, -1072689136, -804253680);
        drawString(fontRendererObj, colorChar+"7"+Jet.project()+"  ", 24, height - 110 - 15, 16777215);

        GL11.glPushMatrix();
        GL11.glTranslatef(width / 2 + 40, height / 2 - 50, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float var8 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime()% 1000L) / 1000.0F * 3.141593F * 2.0F) * 0.1F);
        var8 = var8 * 100.0F / (fontRendererObj.getStringWidth(I18n.format("menu.title")) + 32);
        GL11.glScalef(var8, var8, var8);
        drawCenteredString(fontRendererObj, colorChar+"7"+Jet.project(), 0, 0, 16776960);
        GL11.glRotatef(40.0F, 0.0F, 0.0F, 1.0F);
        drawCenteredString(fontRendererObj, colorChar+"8"+I18n.format("menu.title"), -8, -15, 16776960);
        GL11.glPopMatrix();

        int k;
        for(k = 0;k<this.buttonList.size();++k) {
            ((GuiButton)this.buttonList.get(k)).drawButton(this.mc,par1,par2);
        }
        for(k = 0;k<this.labelList.size();++k) {
            ((GuiLabel)this.labelList.get(k)).func_146159_a(this.mc,par1,par2);
        }
    }

}
