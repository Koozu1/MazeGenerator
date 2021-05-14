package net.koozumaa.mazegenerator;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MazeCommand implements CommandExecutor {
    public MazeGenerator plugin;
    Algorithm algorithm = new Algorithm(MazeGenerator.instance);

    public MazeCommand(MazeGenerator plugin) {
        this.plugin = plugin;
    }
    ;

    Location pos1;
    Location pos2;
    int length = 0;
    String nullmsg;
    //int blocksPerSecond = 2000;
    //int blocksPerSecondStock = 2000;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        nullmsg = "Missing variables: ";
        boolean isAnyNull = false;
        Player player = (Player) sender;

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "pos1":
                    pos1 = player.getLocation().getBlock().getLocation();
                    player.sendMessage(MazeGenerator.commandPrefix + "Sijainti 1 asetettu");
                    break;
                case "pos2":
                    pos2 = player.getLocation().getBlock().getLocation();
                    player.sendMessage(MazeGenerator.commandPrefix + "Sijainti 2 asetettu");
                    break;
                case "variables":
                    variableMessage(player);
                    break;
                case "solve":
                    /*
                    if (algorithm.solution.isEmpty()){
                        player.sendMessage(MazeGenerator.commandPrefix + "Generoi ensin labyrintti!");
                        break;
                    }
                    algorithm.solution.forEach(loc -> loc.getBlock().setType(Material.BLUE_TERRACOTTA));

                     */
                    algorithm.solveMaze(pos1, pos2);
                    player.sendMessage(MazeGenerator.commandPrefix + "Ratkaistu");
                    break;
                case "generate":
                    if (pos1 == null) {
                        nullmsg += "pos1,";
                        isAnyNull = true;
                    }
                    if (pos2 == null) {
                        nullmsg += "pos2,";
                        isAnyNull = true;
                    }
                    if (isAnyNull) {
                        player.sendMessage(MazeGenerator.commandPrefix + nullmsg);
                        return true;
                    }

                    if (!isEvenLocation(pos1, pos2)) {
                        player.sendMessage(MazeGenerator.commandPrefix + "Alueen reunojen on oltavat parilliset");
                        return true;
                    }
                    KoozuPair<Location, Location> koozuPair = new KoozuPair<>(pos1, pos2);
                    algorithm.generate2(player, koozuPair.getKey(), koozuPair.getValue());
                    player.sendMessage(MazeGenerator.commandPrefix + "pos2: " + pos2.getBlockX() + "x ja z: " + pos2.getBlockZ());
                    pos1 = null;
                    pos2 = null;
                    break;
                case "clearroofmaterials":
                    if (plugin.roofMaterial.isEmpty()){
                        player.sendMessage(MazeGenerator.commandPrefix + "Katon materiaalit olivat jo tyhjät!");
                        break;
                    }
                    player.sendMessage(MazeGenerator.commandPrefix + "Tyhjennetty katon materiaalit!");
                    plugin.roofMaterial.clear();
                    break;
                case "clearwallmaterials":
                    if (plugin.wallMaterials.isEmpty()){
                        player.sendMessage(MazeGenerator.commandPrefix + "Seinien materiaalit olivat jo tyhjät!");
                        break;
                    }
                    player.sendMessage(MazeGenerator.commandPrefix + "Tyhjennetty seinien materiaalit!");
                    plugin.wallMaterials.clear();
                    break;
                case "clearfloormaterials":
                    if (plugin.floorMaterial.isEmpty()){
                        player.sendMessage(MazeGenerator.commandPrefix + "Lattian materiaalit olivat jo tyhjät!");
                        break;
                    }
                    player.sendMessage(MazeGenerator.commandPrefix + "Tyhjennetty lattian materiaalit!");
                    plugin.floorMaterial.clear();
                    break;
            }
            return true;
        } else if (args.length == 2) {
            Material mainHandMaterial = player.getInventory().getItemInMainHand().getType();
            switch (args[0].toLowerCase()) {
                case "addmaterial":
                    player.sendMessage(MazeGenerator.commandPrefix + "Lisätty materiaali: §9" + mainHandMaterial);
                    switch (args[1].toLowerCase()) {
                        case "roof":
                            plugin.roofMaterial.add(mainHandMaterial);
                            break;
                        case "wall":
                            plugin.wallMaterials.add(mainHandMaterial);
                            break;
                        case "floor":
                            plugin.floorMaterial.add(mainHandMaterial);
                            break;
                    }
                    break;
                case "removeMaterial":
                    player.sendMessage(MazeGenerator.commandPrefix + "Poistettu materiaali: §9" + mainHandMaterial);
                    switch (args[1].toLowerCase()) {
                        case "roof":
                            plugin.roofMaterial.remove(mainHandMaterial);
                            break;
                        case "wall":
                            plugin.wallMaterials.remove(mainHandMaterial);
                            break;
                        case "floor":
                            plugin.floorMaterial.remove(mainHandMaterial);
                            break;
                    }
                    break;
                case "blockspersecond":
                    try {
                        plugin.blocksPerSecond = Integer.parseInt(args[1]);
                    }catch (NumberFormatException exception){
                        player.sendMessage(MazeGenerator.commandPrefix + args[1] + " ei ole numero! Asetettu vakioksi §9" + plugin.blocksPerSecondStock);
                        plugin.blocksPerSecond = plugin.blocksPerSecondStock;
                        return true;
                    }
                    player.sendMessage(MazeGenerator.commandPrefix + "Kuutiot sekunnissa asetettu arvoon §9" + plugin.blocksPerSecond);
            }
        }
        return true;
    }


    public boolean isEvenLocation(Location pos1, Location pos2) {
        int doubleCheck = 0;
        if (pos1.getBlockX() > pos2.getBlockX()) {
            if ((pos1.getBlockX() - pos2.getBlockX()) % 2 == 0) {
                doubleCheck += 1;
            }
        } else {
            if ((pos2.getBlockX() - pos1.getBlockX()) % 2 == 0) {
                doubleCheck += 1;
            }
        }
        if (pos1.getBlockZ() > pos2.getBlockZ()) {
            if ((pos1.getBlockZ() - pos2.getBlockZ()) % 2 == 0) {
                doubleCheck += 1;
            }
        } else {
            if ((pos2.getBlockZ() - pos1.getBlockZ()) % 2 == 0) {
                doubleCheck += 1;
            }
        }
        return doubleCheck == 2;
    }


    public void variableMessage(Player player) {
        player.sendMessage(MazeGenerator.commandPrefix + "Muuttujat:");
        if (pos1 == null){
            player.sendMessage("§9| §7pos1 = §5null");
        }else {
            player.sendMessage("§9| §7pos1 = §9" + pos1.getBlockX() + "x, " + pos1.getBlockY() + "y, " + pos1.getBlockZ() + "z");
        }
        if (pos2 == null){
            player.sendMessage("§9| §7pos2 = §5null");
        }else {
            player.sendMessage("§9| §7pos2 = §9" + pos2.getBlockX() + "x, " + pos2.getBlockY() + "y, " + pos2.getBlockZ() + "z");
        }
        player.sendMessage("§9| §7Palikoita sekunissa: §9" + plugin.blocksPerSecond);

        String roofMat = "§9| [Katon materiaalit]";
        String roofTitle = "§9Katon materiaalit:§7";
        TextComponent roofComponent = new TextComponent(roofMat);
        roofComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMaterialString(plugin.roofMaterial, roofTitle)).create()));
        player.spigot().sendMessage(roofComponent);

        String wallMat = "§9| [Seinien materiaalit]";
        String wallTitle = "§9Seinien materiaalit:§7";
        TextComponent wallComponent = new TextComponent(wallMat);
        wallComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMaterialString(plugin.wallMaterials, wallTitle)).create()));
        player.spigot().sendMessage(wallComponent);

        String floorMat = "§9| [Lattian materiaalit]";
        String floorTitle = "§9Lattian materiaalit:§7";
        TextComponent floorComponent = new TextComponent(floorMat);
        floorComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMaterialString(plugin.floorMaterial, floorTitle)).create()));
        player.spigot().sendMessage(floorComponent);

    }
    public String getMaterialString(ArrayList<Material> mat, String title){
        String wallMats = title;
        for (Material material : mat) {
            wallMats += "\n" + material;
        }
        return wallMats;
    }



}
