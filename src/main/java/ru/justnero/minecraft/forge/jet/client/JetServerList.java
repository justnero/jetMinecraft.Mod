package ru.justnero.minecraft.forge.jet.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import ru.justnero.minecraft.forge.jet.core.JetServerData;
import ru.justnero.minecraft.forge.jet.tweak.Jet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

public class JetServerList extends ServerList {

    private List<JetServerData> servers = new ArrayList<JetServerData>();

    public JetServerList(Minecraft minecraft) {
		super(minecraft);
	}

    public void loadServerList() {
    	if(servers == null) {
    		servers = new ArrayList<JetServerData>();
    	}
        try {
            servers.clear();
            servers.addAll(JetServerData.convert(Jet.listServer(Jet.gameID())));
        } catch(IOException ex) {
        	LogManager.getLogger().error("Couldn\'t load server list",ex);
        }
    }

    public void saveServerList() {}

    public JetServerData getServerData(int index) {
        return servers.get(index);
    }

    public void removeServerData(int par1) {}

    public void addServerData(JetServerData par1ServerData) {}

    public int countServers() {
        return this.servers.size();
    }

    public void swapServers(int par1, int par2) {}

    public void func_147413_a(int p_147413_1_, ServerData p_147413_2_) {}

}
