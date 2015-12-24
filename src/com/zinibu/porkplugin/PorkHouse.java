package com.zinibu.porkplugin;

import org.bukkit.Material;

public class PorkHouse {
    public static void build_me(Material whatOut, Material whatIn) {
        int width = 6;
        int height = 8;
        PorkPlugin.buildMyHouse(width, height, whatOut, whatIn);
    }
}
