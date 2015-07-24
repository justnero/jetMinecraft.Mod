package ru.justnero.minecraft.forge.jet.client.gui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.Charsets;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import ru.justnero.minecraft.forge.jet.tweak.Jet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

public class JetMainMenu extends GuiMainMenu {

	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final Random rand = new Random();

	private static int drawCount = 0;

    private final String colorChar = String.valueOf((char) 167);

	private int panoramaTimer;
	private String splashText;
	private DynamicTexture viewportTexture;
	private ResourceLocation textureLocation;
	private ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png")
	};
	private GuiButton buttonVK;
	private GuiButton buttonDeveloper;
	private GuiButton buttonForum;
	private GuiButton buttonSite;
	private GuiButton buttonSingleplayer;
	private GuiButton buttonQuit;
	private GuiButton buttonOptions;
	private GuiButton buttonMultiplayer;

	public JetMainMenu() {
		this.splashText = "empty";
        BufferedReader bufferedreader = null;

        try {
            ArrayList<String> arraylist = new ArrayList<String>();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(),Charsets.UTF_8));
            String s;

            while((s = bufferedreader.readLine()) != null) {
                s = s.trim();
                if(!s.isEmpty()) {
                    arraylist.add(s);
                }
            }

            if(!arraylist.isEmpty()) {
                do {
                    this.splashText = (String)arraylist.get(rand.nextInt(arraylist.size()));
                } while(this.splashText.hashCode() == 125780783);
            }
        } catch (IOException ioexception1) {

        } finally {
            if(bufferedreader != null) {
                try {
                    bufferedreader.close();
                } catch (IOException ioexception) {}
            }
        }
	}

	@SuppressWarnings("unchecked")
	public void initGui() {
		this.viewportTexture = new DynamicTexture(256, 256);
        this.textureLocation = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(Calendar.MONTH)+1 == 10 && calendar.get(Calendar.DAY_OF_MONTH) == 6) {
            this.splashText = "С Днём Рождения, Nero!";
        } else if (calendar.get(Calendar.MONTH)+1 == 11 && calendar.get(Calendar.DAY_OF_MONTH) == 3) {
            this.splashText = "С Днём Рождения, Lex_13!";
        } else if (calendar.get(Calendar.MONTH)+1 == 11 && calendar.get(Calendar.DAY_OF_MONTH) == 21) {
            this.splashText = "С Днём Рождения, kirimy!";
        }
        int i = this.height / 4 + 48;
        this.buttonList.add(buttonOptions = new GuiButton(0, width - 102, i + 48, 100, 20, I18n.format("menu.options")));
        this.buttonList.add(buttonQuit = new GuiButton(1, width - 102, i + 72, 100, 20, I18n.format("menu.quit")));
        this.buttonList.add(buttonSingleplayer = new GuiButton(2, width - 102, i, 100, 20, I18n.format("menu.singleplayer")));
		this.buttonList.add(buttonMultiplayer = new GuiButton(3, this.width - 102, i + 24, 100, 20, I18n.format("menu.multiplayer") ));
        if((buttonMultiplayer != null) && ((mc.getSession().getSessionID().isEmpty()) || (mc.getSession().getSessionID().equalsIgnoreCase("-")))) {
            buttonMultiplayer.enabled = false;
        }
        this.buttonList.add(buttonSite = new GuiButton(4, 2, i, 100, 20, I18n.format("menu.site")));
        this.buttonList.add(buttonForum = new GuiButton(5, 2, i + 24, 100, 20, I18n.format("menu.forum")));
        this.buttonList.add(buttonVK = new GuiButton(6, 2, i + 48, 100, 20, I18n.format("menu.vk")));
        this.buttonList.add(buttonDeveloper = new GuiButton(7, 2, i + 72, 100, 20, I18n.format("menu.developer")));
	}

	protected void actionPerformed(GuiButton btn) {
		switch(btn.id) {
	        case 0:
	            mc.displayGuiScreen(new GuiOptions(this,mc.gameSettings));
	            break;
	        case 1:
	            mc.shutdown();
	            break;
	        case 2:
	            mc.displayGuiScreen(new GuiSelectWorld(this));
	            break;
	        case 3:
	            mc.displayGuiScreen(new GuiMultiplayer(this));
	            break;
	        case 4:
	            launchBrowser(Jet.urlSite());
	            break;
	        case 5:
	            launchBrowser(Jet.urlForum());
	            break;
	        case 6:
	            launchBrowser(Jet.urlVK());
	            break;
	        case 7:
	            launchBrowser(Jet.urlDeveloper());
	            break;
	        default:
	            break;
	    }
	}

    private void launchBrowser(String url) {
        if(Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if(desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(url));
                } catch(IOException exception) {

                } catch(URISyntaxException exception) {}
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        renderSkybox(par1,par2,par3);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        Tessellator tessellator = Tessellator.instance;

        tessellator.setColorOpaque_I(16777215);
        GL11.glPushMatrix();

    	if(drawCount < 24) {
    		drawCount++;
    	}
        float percent = Math.min(1.0F,drawCount/24.0F);
        int i = this.height / 4 + 44;
        drawGradientRect(0, i, ((int) (106.0F * percent)), i+100, -1072689136, -804253680);
        buttonSingleplayer.xPosition = width - ((int) (106.0F * percent)) + 4;
        buttonMultiplayer.xPosition = width - ((int) (106.0F * percent)) + 4;
        buttonOptions.xPosition = width - ((int) (106.0F * percent)) + 4;
        buttonQuit.xPosition = width - ((int) (106.0F * percent)) + 4;

        drawGradientRect(width - ((int) (106.0F * percent)), i, width, i+100, -1072689136, -804253680);
        buttonSite.xPosition = ((int) (106.0F * percent)) - 104;
        buttonForum.xPosition = ((int) (106.0F * percent)) - 104;
        buttonVK.xPosition = ((int) (106.0F * percent)) - 104;
        buttonDeveloper.xPosition = ((int) (106.0F * percent)) - 104;


        GL11.glTranslatef(width/2,75.0F,0.0F);
        float f1 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
        f1 = f1 * 100.0F / (float)(fontRendererObj.getStringWidth(splashText) + 32) * 2.0F;
        GL11.glScalef(f1,f1,f1);
        drawCenteredString(fontRendererObj,splashText,0,-8,16776960);
        GL11.glPopMatrix();

        ForgeHooksClient.renderMainMenu(this,fontRendererObj,width,height);
        String copyrightStr = colorChar+"ejetMinecraft 3.0 by Nero (C) 2014";
        drawCenteredString(fontRendererObj,copyrightStr,width/2,height-10,-1);
        drawCenteredString(fontRendererObj,colorChar+"e[ " + mc.getSession().getUsername() + " ]",width/2,5,0);

        int k;
        for(k = 0;k<this.buttonList.size();++k) {
            ((GuiButton)this.buttonList.get(k)).drawButton(this.mc,par1,par2);
        }
        for(k = 0;k<this.labelList.size();++k) {
            ((GuiLabel)this.labelList.get(k)).func_146159_a(this.mc,par1,par2);
        }
    }


    private void drawPanorama(int par1, int par2, float par3) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        byte b0 = 8;

        for (int k = 0;k<b0*b0;++k) {
            GL11.glPushMatrix();
            float f1 = ((float)(k % b0) / (float)b0 - 0.5F) / 64.0F;
            float f2 = ((float)(k / b0) / (float)b0 - 0.5F) / 64.0F;
            float f3 = 0.0F;
            GL11.glTranslatef(f1, f2, f3);
            GL11.glRotatef(MathHelper.sin(((float)this.panoramaTimer + par3) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-((float)this.panoramaTimer + par3) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0;l<6;++l) {
                GL11.glPushMatrix();

                if(l == 1) {
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if(l == 2) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if(l == 3) {
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if(l == 4) {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if(l == 5) {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[l]);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
                float f4 = 0.0F;
                tessellator.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + f4), (double)(0.0F + f4));
                tessellator.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - f4), (double)(0.0F + f4));
                tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - f4), (double)(1.0F - f4));
                tessellator.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + f4), (double)(1.0F - f4));
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, false);
        }

        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox(float par1) {
        this.mc.getTextureManager().bindTexture(this.textureLocation);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        byte b0 = 3;

        for(int i = 0;i<b0;++i) {
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(i + 1));
            int j = this.width;
            int k = this.height;
            float f1 = (float)(i - b0 / 2) / 256.0F;
            tessellator.addVertexWithUV((double)j, (double)k, (double)this.zLevel, (double)(0.0F + f1), 1.0D);
            tessellator.addVertexWithUV((double)j, 0.0D, (double)this.zLevel, (double)(1.0F + f1), 1.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(1.0F + f1), 0.0D);
            tessellator.addVertexWithUV(0.0D, (double)k, (double)this.zLevel, (double)(0.0F + f1), 0.0D);
        }

        tessellator.draw();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int par1, int par2, float par3) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GL11.glViewport(0, 0, 256, 256);
        this.drawPanorama(par1, par2, par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f1 = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float f2 = (float)this.height * f1 / 256.0F;
        float f3 = (float)this.width * f1 / 256.0F;
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.width;
        int l = this.height;
        tessellator.addVertexWithUV(0.0D, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F + f3));
        tessellator.addVertexWithUV((double)k, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F - f3));
        tessellator.addVertexWithUV((double)k, 0.0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F - f3));
        tessellator.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F + f3));
        tessellator.draw();
    }

    public void updateScreen() {
        ++this.panoramaTimer;
    }

}
