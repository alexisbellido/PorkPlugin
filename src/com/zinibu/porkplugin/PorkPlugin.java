package com.zinibu.porkplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
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
            log.info(Arrays.toString(args));
            //getServer().broadcastMessage(msg);
            return true;
        }

        if (label.equalsIgnoreCase("buildablock")) {
            if (sender instanceof Player) {
                Player me = (Player) sender;
                origin = me.getLocation();
                World world = origin.getWorld();
                Block block;

                Material material;
                if (args.length > 0 && args[0] != null) {
                    material = Material.getMaterial(args[0]);
                } else {
                    material = Material.WOOD;
                }

                int quantity = 1;
                if (args.length > 1 && args[1] != null) {
                    quantity = Integer.parseInt(args[1]);
                }

                for (int i = 0; i < quantity; i++) {
                    block = world.getBlockAt(origin);
                    block.setType(material);
                    origin.setX(origin.getX() + 1);
                }

                return true;
            }
        }

        if (label.equalsIgnoreCase("buildahouse")) {
            if (sender instanceof Player) {
                Player me = (Player) sender;
                origin = me.getLocation();
                firstHouse = true;
                int width = 6;
                int height = 8;

                Material whatOut;
                Material whatIn;

                if (args.length > 0 && args[0] != null) {
                    whatOut = Material.getMaterial(args[0]);
                } else {
                    whatOut = Material.WOOD;
                }

                whatIn = Material.AIR;

                if (args.length > 1 && args[1] != null) {
                    width = Integer.parseInt(args[1]);
                }

                if (args.length > 2 && args[2] != null) {
                    height = Integer.parseInt(args[2]);
                }

                PorkHouse.build_me(width, height, whatOut, whatIn);
                return true;
            }
        }

        if (label.equalsIgnoreCase("spawnacreature")) {
            // TODO extra checks for correct parameter types
            if (sender instanceof Player) {
                Player me = (Player) sender;
                origin = me.getLocation();
                World world = origin.getWorld();

                EntityType creature;
                if (args.length > 0 && args[0] != null) {
                    creature = EntityType.valueOf(args[0]);
                } else {
                    creature = EntityType.HORSE;
                }

                int quantity = 1;
                if (args.length > 1 && args[1] != null) {
                    quantity = Integer.parseInt(args[1]);
                }

                for (int i = 0; i < quantity; i++) {
                    world.spawnEntity(origin, creature);
                }
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

    public static void buildMyHouse(int width, int height, Material whatOut, Material whatIn) {

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
        makeCube(0,0,0,width, height, whatOut);
        // Set the inside of the cube to air
        makeCube(1,1,1,width-2, height-2, whatIn);

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
        //top.setData((byte)0x8);
        //bottom.setData((byte)0x4);

        // And normal material constants
        top.setType(Material.WOODEN_DOOR);
        bottom.setType(Material.WOODEN_DOOR);
        over.setType(Material.TORCH);

        // Move over to make next house if called in a loop.
        origin.setX(origin.getX() + width);

    }

}