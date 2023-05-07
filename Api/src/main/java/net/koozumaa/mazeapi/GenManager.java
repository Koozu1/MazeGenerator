package net.koozumaa.mazeapi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class GenManager{

    public static void genMaze(Maze pVar, Plugin plugin) {
        Player player = Bukkit.getPlayer(pVar.getUUID());
        World world;
        if(pVar.getUUID() == null){
            world = pVar.getWorld();
        }else {
            world = player.getWorld();
        }

        Mode genMode = pVar.getGenMode();
        boolean sendMsg = pVar.isSendMessages();
        int bps = pVar.getBps();
        if (genMode == Mode.CUSTOM){
            MainGenerator.calculateMazeLocsNew(pVar, plugin ,locations -> {
                ArrayList<KoozuPair<Location, Material>> blocks;
                ArrayList<Location> newLocList = MazeCalcUtils.stretch(new ArrayList<>(locations), pVar.getPos1().clone(), pVar.size);
                newLocList.forEach(loc -> loc.add((pVar.size - 1) / 2, 0 , (pVar.size - 1) / 2));
                locations.clear();
                locations.addAll(newLocList);
                sendCountDoneMessage(player, sendMsg);
                blocks = MazeCalcUtils.countMazeBlocksNew(pVar, locations);
                sendStartBPMessage(player, sendMsg);
                MazeCalcUtils.placeByChunk(blocks, plugin);
            });
        }else {

            MainGenerator.calculateMazeLocs(pVar, plugin, locations -> {
                ArrayList<KoozuPair<Location, Material>> blocks;
                switch (genMode) {
                    case NORMAL:
                        sendCountDoneMessage(player, sendMsg);
                        blocks = MazeCalcUtils.countMazeBlocks(pVar, locations);
                        sendStartBPMessage(player, sendMsg);
                        MazeCalcUtils.placeBlocks(blocks, pVar.getBps(), world, plugin);
                        break;
                    case SLIM3x3:
                        ArrayList<Location> newLocList = MazeCalcUtils.stretch(locations, pVar.getPos1(), pVar.size);
                        newLocList.forEach(loc -> loc.add((pVar.size - 1) / 2, 0 , (pVar.size - 1) / 2));
                        locations.clear();
                        locations.addAll(newLocList);
                        sendCountDoneMessage(player, sendMsg);
                        blocks = MazeCalcUtils.countMazeBlocks(pVar, locations);
                        sendStartBPMessage(player, sendMsg);
                        MazeCalcUtils.placeBlocks(blocks, pVar.getBps(), world, plugin);
                        break;
                    case WORLDGEN:
                        sendCountDoneMessage(player, sendMsg);
                        ArrayList<KoozuPair<Location, Material>> blocksWG = MazeCalcUtils.countMazeBlocks(pVar, locations);
                        sendStartBPMessage(player, sendMsg);
                        MazeCalcUtils.placeBlocks(blocksWG, bps, world, plugin);
                        break;

                }
            });
        }
    }

    //Messages
    public static void sendCountDoneMessage(Player player, boolean sendmsg) {
        if (sendmsg) {
            player.sendMessage("Labyrintin laskenta valmis. Lasketaan palikat!");
        }
    }

    public static void sendStartBPMessage(Player player, boolean sendmsg) {
        if (sendmsg) {
            player.sendMessage("Palikat laskettu, asetetaan palikat!");
        }
    }
}
