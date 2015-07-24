package ru.justnero.minecraft.forge.jet.client.gui;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import ru.justnero.minecraft.forge.jet.client.JetServerList;

@SideOnly(Side.CLIENT)
public class JetMultiplayer extends GuiMultiplayer {

    private final OldServerPinger oldPinger = new OldServerPinger();
    private GuiScreen _parent;
    private JetServerSelectionList selectionList;
    private JetServerList serverList;
    private GuiButton selectButton;
    private boolean isServerListLoaded1;
	private GuiButton selectedButton;

    public JetMultiplayer(GuiScreen parent) {
    	super(parent);
        this._parent = parent;
        FMLClientHandler.instance().setupServerList();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
	public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();

        if(!isServerListLoaded1) {
            isServerListLoaded1 = true;
            serverList = new JetServerList(this.mc);
            serverList.loadServerList();

            selectionList = new JetServerSelectionList(this, mc, width, height, 32, height - 64, 36);
            selectionList.func_148195_a(serverList);
        } else {
            selectionList.func_148122_a(width, height, 32, height - 64);
        }

        buttonList.add(selectButton = new GuiButton(0, width / 2 - 202, height - 28, 200, 20, I18n.format("selectServer.select")));
        buttonList.add(new GuiButton(1, width / 2 + 2, height - 28, 100, 20, I18n.format("selectServer.refresh")));
        buttonList.add(new GuiButton(2, width / 2 + 106, height - 28, 100, 20, I18n.format("gui.cancel")));
        func_146790_a(selectionList.func_148193_k());
    }

    public void updateScreen() {
        oldPinger.func_147223_a();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        oldPinger.func_147226_b();
    }

    protected void actionPerformed(GuiButton btn) {
        if (btn.enabled) {
        	switch(btn.id) {
	        	case 0:
	        		select();
	        		break;
	        	case 1:
	        		refresh();
	        		break;
	        	case 2:
	        		mc.displayGuiScreen(_parent);
	        		break;
        		default:
        			break;
        	}
        }
    }

    private void refresh() {
        mc.displayGuiScreen(new GuiMultiplayer(_parent));
    }

    public void confirmClicked(boolean par1, int par2) {}

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2) {
        int j = this.selectionList.func_148193_k();
        selectionList.getListEntry(j);

        if(par2 == 63) {
            refresh();
        } else {
            if(j >= 0) {
                /*if(par2 == 200) {
                    if(j > 0) {
                        func_146790_a(selectionList.func_148193_k() - 1);
                        selectionList.scrollBy(-selectionList.getSlotHeight());
                    } else {
                        func_146790_a(-1);
                    }
                } else if(par2 == 208) {
                    if(j < selectionList.getSize()) {
                        func_146790_a(selectionList.func_148193_k() + 1);
                        selectionList.scrollBy(selectionList.getSlotHeight());
                    } else {
                        func_146790_a(-1);
                    }
                } else*/ if(par2 != 28 && par2 != 156) {
                	if(par2 == 1) {
                        mc.displayGuiScreen((GuiScreen)null);
                        mc.setIngameFocus();
                    }
                } else {
                    actionPerformed((GuiButton)buttonList.get(2));
                }
            } else {
            	if(par2 == 1) {
                    mc.displayGuiScreen((GuiScreen)null);
                    mc.setIngameFocus();
                }
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3) {
        drawDefaultBackground();
        selectionList.drawScreen(par1, par2, par3);
        drawCenteredString(fontRendererObj, I18n.format("multiplayer.title"), width / 2, 20, 16777215);

        int k;
        for(k = 0;k<this.buttonList.size();++k) {
            ((GuiButton)this.buttonList.get(k)).drawButton(this.mc,par1,par2);
        }
        for(k = 0;k<this.labelList.size();++k) {
            ((GuiLabel)this.labelList.get(k)).func_146159_a(this.mc,par1,par2);
        }
    }

    public void select() {
        GuiListExtended.IGuiListEntry iguilistentry = this.selectionList.func_148193_k() < 0 ? null : this.selectionList.getListEntry(this.selectionList.func_148193_k());

        if(iguilistentry instanceof ServerListEntryNormal) {
            connect(((ServerListEntryNormal)iguilistentry).func_148296_a());
        }
    }

    private void connect(ServerData data) {
        FMLClientHandler.instance().connectToServer(this,data);
    }

    public void func_146790_a(int p_146790_1_) {
        this.selectionList.func_148192_c(p_146790_1_);
        GuiListExtended.IGuiListEntry iguilistentry = p_146790_1_ < 0 ? null : selectionList.getListEntry(p_146790_1_);
        selectButton.enabled = false;

        if(iguilistentry != null && !(iguilistentry instanceof ServerListEntryLanScan)) {
            selectButton.enabled = true;
        }
    }

    public OldServerPinger func_146789_i() {
        return oldPinger;
    }

    public void func_146793_a(String p_146793_1_) {
    }

    public void func_146796_h() {
        GuiListExtended.IGuiListEntry iguilistentry = this.selectionList.func_148193_k() < 0 ? null : this.selectionList.getListEntry(this.selectionList.func_148193_k());

        if (iguilistentry instanceof ServerListEntryNormal)
        {
            this.connect(((ServerListEntryNormal)iguilistentry).func_148296_a());
        }
        else if (iguilistentry instanceof ServerListEntryLanDetected)
        {
            LanServerDetector.LanServer lanserver = ((ServerListEntryLanDetected)iguilistentry).func_148289_a();
            this.connect(new ServerData(lanserver.getServerMotd(), lanserver.getServerIpPort()));
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3) {
    	if(par3 == 0) {
            for(int l = 0;l<buttonList.size();++l) {
                GuiButton guibutton = (GuiButton)this.buttonList.get(l);

                if(guibutton.mousePressed(mc, par1, par2)) {
                    ActionPerformedEvent.Pre event = new ActionPerformedEvent.Pre(this,guibutton,buttonList);
                    if(MinecraftForge.EVENT_BUS.post(event)) {
                        break;
                    }
                    selectedButton = event.button;
                    event.button.func_146113_a(this.mc.getSoundHandler());
                    actionPerformed(event.button);
                    if(equals(mc.currentScreen))
                        MinecraftForge.EVENT_BUS.post(new ActionPerformedEvent.Post(this,event.button,buttonList));
                }
            }
        }

        selectionList.func_148179_a(par1, par2, par3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
    	if(selectedButton != null && p_146286_3_ == 0) {
            selectedButton.mouseReleased(p_146286_1_, p_146286_2_);
            selectedButton = null;
        }
        selectionList.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_);
    }

    public JetServerList func_146795_p() {
        return this.serverList;
    }

    private class JetServerSelectionList extends GuiListExtended {

    	private final GuiMultiplayer field_148200_k;
        private final List<JetServerListEntryNormal> field_148198_l = Lists.newArrayList();
        private int field_148197_o = -1;

        public JetServerSelectionList(GuiMultiplayer p_i45049_1_, Minecraft p_i45049_2_, int p_i45049_3_, int p_i45049_4_, int p_i45049_5_, int p_i45049_6_, int p_i45049_7_) {
            super(p_i45049_2_, p_i45049_3_, p_i45049_4_, p_i45049_5_, p_i45049_6_, p_i45049_7_);
            this.field_148200_k = p_i45049_1_;
        }

        /**
         * Gets the IGuiListEntry object for the given index
         */
        public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
            return (GuiListExtended.IGuiListEntry)this.field_148198_l.get(p_148180_1_);
        }

        protected int getSize() {
            return field_148198_l.size();
        }

        public void func_148192_c(int p_148192_1_) {
            this.field_148197_o = p_148192_1_;
        }

        /**
         * Returns true if the element passed in is currently selected
         */
        protected boolean isSelected(int p_148131_1_) {
            return p_148131_1_ == this.field_148197_o;
        }

        public int func_148193_k() {
            return this.field_148197_o;
        }

        public void func_148195_a(ServerList p_148195_1_) {
            this.field_148198_l.clear();

            for (int i = 0; i < p_148195_1_.countServers(); ++i) {
                this.field_148198_l.add(new JetServerListEntryNormal(this.field_148200_k, p_148195_1_.getServerData(i)));
            }
        }

        @SuppressWarnings("unused")
		public void func_148194_a(List<?> p_148194_1_) {}

        protected int getScrollBarX() {
            return super.getScrollBarX() + 30;
        }

        public int getListWidth() {
            return super.getListWidth() + 85;
        }

    }

    private class JetServerListEntryNormal extends ServerListEntryNormal {

		protected JetServerListEntryNormal(GuiMultiplayer p_i45048_1_,
				ServerData p_i45048_2_) {
			super(p_i45048_1_, p_i45048_2_);
		}

    }

}