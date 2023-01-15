package net.koozumaa.mazegenerator;

import net.koozumaa.mazeapi.*;
import net.koozumaa.mazeapi.KoozuPair;
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
    public MazeCommand(MazeGenerator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String nullmsg = "Missing variables: ";
        boolean isAnyNull = false;
        Player p = (Player) sender;
        Maze mplayer;

        if(plugin.players.containsKey(p.getUniqueId())){
            mplayer = plugin.players.get(p.getUniqueId());
        }else {
            mplayer = new Maze();
            mplayer.setUUID(p.getUniqueId());
            plugin.players.put(p.getUniqueId(), mplayer);
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "pos1":
                    mplayer.setPos1(MazeCalcUtils.toBlockLocation(p.getLocation().getBlock().getLocation()));
                    p.sendMessage(MazeGenerator.commandPrefix + "Sijainti 1 asetettu");
                    break;
                case "pos2":
                    mplayer.setPos2(MazeCalcUtils.toBlockLocation(p.getLocation().getBlock().getLocation()));
                    p.sendMessage(MazeGenerator.commandPrefix + "Sijainti 2 asetettu");
                    break;
                case "variables":
                    variableMessage(mplayer);
                    break;
                case "solve":
                    Solver solver = new Solver();
                    solver.solveMaze(mplayer.getPos1(), mplayer.getPos2());
                    p.sendMessage(MazeGenerator.commandPrefix + "Ratkaistu");
                    break;
                case "generate":
                    if (mplayer.getPos1() == null) {
                        nullmsg += "pos1,";
                        isAnyNull = true;
                    }
                    if (mplayer.getPos2() == null) {
                        nullmsg += "pos2,";
                        isAnyNull = true;
                    }
                    if (isAnyNull) {
                        p.sendMessage(MazeGenerator.commandPrefix + nullmsg);
                        return true;
                    }

                    if (!isEvenLocation(mplayer.getPos1(), mplayer.getPos2())) {
                        p.sendMessage(MazeGenerator.commandPrefix + "Alueen reunojen on oltavat parilliset");
                        return true;
                    }
                    KoozuPair<Location, Location> koozuPair = MazeCalcUtils.getInvertedLocations(mplayer.getPos1(), mplayer.getPos2(), Bukkit.getPlayer(mplayer.getUUID()).getWorld());

                    mplayer.setPos1(koozuPair.getKey());
                    mplayer.setPos2(koozuPair.getValue());
                    GenManager.genMaze(mplayer, MazeGenerator.instance);
                    break;
                case "clearroofmaterials":
                    if (mplayer.getMaterials().getRoof().isEmpty()){
                        p.sendMessage(MazeGenerator.commandPrefix + "Katon materiaalit olivat jo tyhjät!");
                        break;
                    }
                    p.sendMessage(MazeGenerator.commandPrefix + "Tyhjennetty katon materiaalit!");
                    mplayer.getMaterials().setRoof(new ArrayList<>());
                    break;
                case "clearwallmaterials":
                    if (mplayer.getMaterials().getWall().isEmpty()){
                        p.sendMessage(MazeGenerator.commandPrefix + "Seinien materiaalit olivat jo tyhjät!");
                        break;
                    }
                    p.sendMessage(MazeGenerator.commandPrefix + "Tyhjennetty seinien materiaalit!");
                    mplayer.getMaterials().setWall(new ArrayList<>());
                    break;
                case "clearfloormaterials":
                    if (mplayer.getMaterials().getFloor().isEmpty()){
                        p.sendMessage(MazeGenerator.commandPrefix + "Lattian materiaalit olivat jo tyhjät!");
                        break;
                    }
                    p.sendMessage(MazeGenerator.commandPrefix + "Tyhjennetty lattian materiaalit!");
                    mplayer.getMaterials().setFloor(new ArrayList<>());
                    break;
            }
            return true;
        } else if (args.length == 2) {
            Material mainHandMaterial = p.getInventory().getItemInMainHand().getType();
            switch (args[0].toLowerCase()) {
                case "addmaterial":
                    p.sendMessage(MazeGenerator.commandPrefix + "Lisätty materiaali: §9" + mainHandMaterial);
                    switch (args[1].toLowerCase()) {
                        case "roof":
                            mplayer.getMaterials().addRoofMaterial(mainHandMaterial);
                            break;
                        case "wall":
                            mplayer.getMaterials().addWallMaterial(mainHandMaterial);
                            break;
                        case "floor":
                            mplayer.getMaterials().addFloorMaterial(mainHandMaterial);
                            break;
                    }
                    break;
                case "removeMaterial":
                    p.sendMessage(MazeGenerator.commandPrefix + "Poistettu materiaali: §9" + mainHandMaterial);
                    switch (args[1].toLowerCase()) {
                        case "roof":
                            mplayer.getMaterials().getRoof().remove(mainHandMaterial);
                            break;
                        case "wall":
                            mplayer.getMaterials().getWall().remove(mainHandMaterial);
                            break;
                        case "floor":
                            mplayer.getMaterials().getFloor().remove(mainHandMaterial);
                            break;
                    }
                    break;
                case "blockspersecond":
                    try {
                        mplayer.setBps(Integer.parseInt(args[1]));
                    }catch (NumberFormatException exception){
                        p.sendMessage(MazeGenerator.commandPrefix + args[1] + " ei ole numero! Asetettu vakioksi §9" + 2000);
                        mplayer.setBps(2000);
                        return true;
                    }
                    p.sendMessage(MazeGenerator.commandPrefix + "Kuutiot sekunnissa asetettu arvoon §9" + mplayer.getBps());
                    break;
                case "mode":
                    Optional<Mode> mode = Arrays.stream(Mode.values()).filter(gM -> gM.name().equalsIgnoreCase(args[1])).findFirst();

                    if (!mode.isPresent()){
                        p.sendMessage(MazeGenerator.commandPrefix + "Tätä tilaa ei ole olemassa.");
                        break;
                    }
                    mplayer.setGenMode(mode.get());
                    p.sendMessage(MazeGenerator.commandPrefix + mplayer.getGenMode().name() + "-tila asetettu");
                    break;
            }
        }
        return true;
    }


    public boolean isEvenLocation(Location pos1, Location pos2) {
        return (Math.abs(pos1.getBlockX() - pos2.getBlockX()) % 2 == 0)
                && (Math.abs(pos1.getBlockZ() - pos2.getBlockZ()) % 2 == 0);
    }


    public void variableMessage(Maze pVar) {
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
        roofComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMaterialString(pVar.getMaterials().getRoof(), roofTitle)).create()));
        player.spigot().sendMessage(roofComponent);

        String wallMat = "§9| [Seinien materiaalit]";
        String wallTitle = "§9Seinien materiaalit:§7";
        TextComponent wallComponent = new TextComponent(wallMat);
        wallComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMaterialString(pVar.getMaterials().getWall(), wallTitle)).create()));
        player.spigot().sendMessage(wallComponent);

        String floorMat = "§9| [Lattian materiaalit]";
        String floorTitle = "§9Lattian materiaalit:§7";
        TextComponent floorComponent = new TextComponent(floorMat);
        floorComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMaterialString(pVar.getMaterials().getFloor(), floorTitle)).create()));
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
