package com.zinibu.porkplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PorkPlugin extends JavaPlugin {

    public static Logger log = Logger.getLogger("Minecraft");
    public static Location origin = null;
    public static boolean firstHouse = true;

    @Override
    public void onEnable() {
        log.info("PorkPluging startup");
    }

    @Override
    public void onDisable() {
        log.info("PorkPluging stopping");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("hello")) {
            String msg = "Hello, I am a message from PorkPlugin";
            getServer().broadcastMessage(msg);
            return true;
        }

        if (label.equalsIgnoreCase("buildahouse")) {
            if (sender instanceof Player) {
                Player me = (Player) sender;
                origin = me.getLocation();
                firstHouse = true;
                PorkHouse.build_me();
                return true;
            }
        }
        return false;
    }

    public static void makeCube(int offsetX, int offsetY, int offsetZ, int width, int height, Material what) {
        int i, j, k;
        Location loc = new Location(origin.getWorld(), 0,0,0);

        // Base is i X j, k goes up for height
        for (i=0; i< width; i++) {
            for (j=0; j<width; j++) {
                for (k=0; k< height; k++) {
                    loc.setX(origin.getX() + offsetX + i);
                    loc.setZ(origin.getZ() + offsetZ + j);
                    loc.setY(origin.getY() + offsetY + k);
                    origin.getWorld().getBlockAt(loc).setType(what);
                }
            }
        }
    }

    public static void buildMyHouse(int width, int height) {

        if (width < 5) {
            width = 5;
        }

        if (height < 5) {
            height = 5;
        }

        if (firstHouse) {
            // Center the first house on the player
            origin.setY(origin.getY() - 1);
            origin.setZ(origin.getZ() - (width/2));
            origin.setX(origin.getX() - (width/2));
            firstHouse = false;
        }

        // Set the whole area to wood
        makeCube(0,0,0,width, height, Material.WOOD);
        // Set the inside of the cube to air
        makeCube(1,1,1,width-2, height-2, Material.AIR);

        // Pop a door in one wall
        Location door = new Location(origin.getWorld(), origin.getX() + (width/2), origin.getY(), origin.getZ());

        // The door is two high, with a torch over the door
        door.setY(door.getY() +1);
        Block bottom = origin.getWorld().getBlockAt(door);
        door.setY(door.getY() +1);
        Block top = origin.getWorld().getBlockAt(door);
        door.setY(door.getY() +1);
        door.setZ(door.getZ() +1);
        Block over = origin.getWorld().getBlockAt(door);

        // Magic values to establish top and bottom of door
        top.setData((byte)0x8);
        bottom.setData((byte)0x4);
        // And normal material constants
        top.setType(Material.WOODEN_DOOR);
        bottom.setType(Material.WOODEN_DOOR);
        over.setType(Material.TORCH);

        // Move over to make next house if called in a loop.
        origin.setX(origin.getX() + width);

    }

}