package net.koozumaa.mazegenerator;

import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazegenerator.Utils.PlayerVar;
import net.koozumaa.mazegenerator.Utils.Mode;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class MazeCommand implements CommandExecutor {
    public MazeGenerator plugin;
    Solver solver = new Solver(MazeGenerator.instance);

    public MazeCommand(MazeGenerator plugin) {
        this.plugin = plugin;
    }

    String nullmsg;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        nullmsg = "Missing variables: ";
        boolean isAnyNull = false;
        Player player = (Player) sender;
        PlayerVar pVar;

        if(plugin.players.containsKey(player.getUniqueId())){
            pVar = plugin.players.get(player.getUniqueId());
        }else {
            pVar = new PlayerVar(player.getUniqueId());
            plugin.players.put(player.getUniqueId(), pVar);
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "pos1":
                    pVar.setPos1(plugin.utils.toBlockLocation(player.getLocation().getBlock().getLocation()));
                    player.sendMessage(MazeGenerator.commandPrefix + "Sijainti 1 asetettu");
                    break;
                case "pos2":
                    pVar.setPos2(plugin.utils.toBlockLocation(player.getLocation().getBlock().getLocation()));
                    player.sendMessage(MazeGenerator.commandPrefix + "Sijainti 2 asetettu");
                    break;
                case "variables":
                    variableMessage(pVar);
                    break;
                case "solve":
                    solver.solveMaze(pVar.getPos1(), pVar.getPos2());
                    player.sendMessage(MazeGenerator.commandPrefix + "Ratkaistu");
                    break;
                case "generate":
                    if (pVar.getPos1() == null) {
                        nullmsg += "pos1,";
                        isAnyNull = true;
                    }
                    if (pVar.getPos2() == null) {
                        nullmsg += "pos2,";
                        isAnyNull = true;
                    }
                    if (isAnyNull) {
                        player.sendMessage(MazeGenerator.commandPrefix + nullmsg);
                        return true;
                    }

                    if (!isEvenLocation(pVar.getPos1(), pVar.getPos2())) {
                        player.sendMessage(MazeGenerator.commandPrefix + "Alueen reunojen on oltavat parilliset");
                        return true;
                    }
                    KoozuPair<Location, Location> koozuPair = plugin.utils.getInvertedLocations(pVar.getPos1(), pVar.getPos2(), Bukkit.getPlayer(pVar.getUUID()).getWorld());

                    pVar.setPos1(koozuPair.getKey());
                    pVar.setPos2(koozuPair.getValue());
                    plugin.manager.genMaze(pVar);
                    break;
                case "clearroofmaterials":
                    if (pVar.getRoofMaterials().isEmpty()){
                        player.sendMessage(MazeGenerator.commandPrefix + "Katon materiaalit olivat jo tyhjät!");
                        break;
                    }
                    player.sendMessage(MazeGenerator.commandPrefix + "Tyhjennetty katon materiaalit!");
                    pVar.setRoofMaterials(new ArrayList<>());
                    break;
                case "clearwallmaterials":
                    if (pVar.getWallMaterials().isEmpty()){
                        player.sendMessage(MazeGenerator.commandPrefix + "Seinien materiaalit olivat jo tyhjät!");
                        break;
                    }
                    player.sendMessage(MazeGenerator.commandPrefix + "Tyhjennetty seinien materiaalit!");
                    pVar.setWallMaterials(new ArrayList<>());
                    break;
                case "clearfloormaterials":
                    if (pVar.getFloorMaterials().isEmpty()){
                        player.sendMessage(MazeGenerator.commandPrefix + "Lattian materiaalit olivat jo tyhjät!");
                        break;
                    }
                    player.sendMessage(MazeGenerator.commandPrefix + "Tyhjennetty lattian materiaalit!");
                    pVar.setFloorMaterials(new ArrayList<>());
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
                            pVar.addRoofMaterial(mainHandMaterial);
                            break;
                        case "wall":
                            pVar.addWallMaterial(mainHandMaterial);
                            break;
                        case "floor":
                            pVar.addFloorMaterial(mainHandMaterial);
                            break;
                    }
                    break;
                case "removeMaterial":
                    player.sendMessage(MazeGenerator.commandPrefix + "Poistettu materiaali: §9" + mainHandMaterial);
                    switch (args[1].toLowerCase()) {
                        case "roof":
                            pVar.getRoofMaterials().remove(mainHandMaterial);
                            break;
                        case "wall":
                            pVar.getWallMaterials().remove(mainHandMaterial);
                            break;
                        case "floor":
                            pVar.getFloorMaterials().remove(mainHandMaterial);
                            break;
                    }
                    break;
                case "blockspersecond":
                    try {
                        pVar.setBps(Integer.parseInt(args[1]));
                    }catch (NumberFormatException exception){
                        player.sendMessage(MazeGenerator.commandPrefix + args[1] + " ei ole numero! Asetettu vakioksi §9" + plugin.blocksPerSecondStock);
                        pVar.setBps(plugin.blocksPerSecondStock);
                        return true;
                    }
                    player.sendMessage(MazeGenerator.commandPrefix + "Kuutiot sekunnissa asetettu arvoon §9" + pVar.getBps());
                    break;
                case "mode":
                    Optional<Mode> mode = Arrays.stream(Mode.values()).filter(gM -> gM.name().equalsIgnoreCase(args[1])).findFirst();

                    if (!mode.isPresent()){
                        player.sendMessage(MazeGenerator.commandPrefix + "Tätä tilaa ei ole olemassa.");
                        break;
                    }
                    pVar.setGenMode(mode.get());
                    player.sendMessage(MazeGenerator.commandPrefix + pVar.getGenMode().name() + "-tila asetettu");
                    break;
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


    public void variableMessage(PlayerVar pVar) {
        Player player = Bukkit.getPlayer(pVar.getUUID());
        player.sendMessage(MazeGenerator.commandPrefix + "Muuttujat:");
        if (pVar.getPos1() == null){
            player.sendMessage("§9| §7pos1 = §5null");
        }else {
            player.sendMessage("§9| §7pos1 = §9" + pVar.getPos1().getBlockX() + "x, " + pVar.getPos1().getBlockY() + "y, " + pVar.getPos1().getBlockZ() + "z");
        }
        if (pVar.getPos2() == null){
            player.sendMessage("§9| §7pos2 = §5null");
        }else {
            player.sendMessage("§9| §7pos2 = §9" + pVar.getPos2().getBlockX() + "x, " + pVar.getPos2().getBlockY() + "y, " + pVar.getPos2().getBlockZ() + "z");
        }
        player.sendMessage("§9| §7Palikoita sekunissa: §9" + pVar.getBps());

        String roofMat = "§9| [Katon materiaalit]";
        String roofTitle = "§9Katon materiaalit:§7";
        TextComponent roofComponent = new TextComponent(roofMat);
        roofComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMaterialString(pVar.getRoofMaterials(), roofTitle)).create()));
        player.spigot().sendMessage(roofComponent);

        String wallMat = "§9| [Seinien materiaalit]";
        String wallTitle = "§9Seinien materiaalit:§7";
        TextComponent wallComponent = new TextComponent(wallMat);
        wallComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMaterialString(pVar.getWallMaterials(), wallTitle)).create()));
        player.spigot().sendMessage(wallComponent);

        String floorMat = "§9| [Lattian materiaalit]";
        String floorTitle = "§9Lattian materiaalit:§7";
        TextComponent floorComponent = new TextComponent(floorMat);
        floorComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMaterialString(pVar.getFloorMaterials(), floorTitle)).create()));
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
