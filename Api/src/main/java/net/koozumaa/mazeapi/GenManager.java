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

        MainGenerator.calculateMazeLocs(pVar, plugin ,locations -> {
            ArrayList<KoozuPair<Location, Material>> blocks;
            switch (genMode) {
                case NORMAL:
                    //ArrayList<Location> normLocs =  plugin.utils.stretchTo3Times(mazeBlocks, pos1);
                    sendCountDoneMessage(player, sendMsg);
                    blocks = MazeCalcUtils.countMazeBlocks(pVar, locations);
                    sendStartBPMessage(player, sendMsg);
                    MazeCalcUtils.placeBlocks(blocks, pVar.getBps(), world, plugin);
                    break;
                case SLIM3x3:

                    ArrayList<Location> newLocList = MazeCalcUtils.stretchTo3Times(locations, pVar.getPos1());
                    newLocList.forEach(location -> location.add(1, 0, 1));
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
