package com.zinibu.porkplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PorkPlugin extends JavaPlugin {
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (label.equalsIgnoreCase("hello")) {
            String msg = "Hello, I am a message from PorkPlugin";
            getServer().broadcastMessage(msg);
            return true;
//        }
//        return false;
    }

}
