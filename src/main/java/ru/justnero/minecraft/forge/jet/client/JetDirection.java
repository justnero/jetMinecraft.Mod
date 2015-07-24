package ru.justnero.minecraft.forge.jet.client;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.Direction;

public class JetDirection extends Direction {

	public static String[] directions;


	static {
		load();
	}

	public static void load() {
		directions = new String[] {
			I18n.format("direction.south"),
			I18n.format("direction.west"),
			I18n.format("direction.north"),
			I18n.format("direction.east")
		};
	}

}
