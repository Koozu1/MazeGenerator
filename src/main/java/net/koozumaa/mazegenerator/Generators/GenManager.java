package net.koozumaa.mazegenerator.Generators;

import net.koozumaa.mazegenerator.MazeGenerator;
import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazegenerator.Utils.PlayerVar;
import net.koozumaa.mazegenerator.Utils.Mode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.function.Consumer;

public class GenManager {
    public MazeGenerator plugin;

    public GenManager(MazeGenerator plugin) {
        this.plugin = plugin;
    }


    public void genMaze(final PlayerVar pVar) {
        final Location nPos1 = pVar.getPos1();
        final Location nPos2 = pVar.getPos2();
        Player player = pVar.getPlayer();
        World world = player.getWorld();
        Mode genMode = pVar.getGenMode();
        boolean sendMsg = pVar.isSendMessages();
        int bps = pVar.getBps();


        plugin.mainGenerator.calculateMazeLocs(pVar, locations -> {
            Bukkit.broadcastMessage("Donecalc");
            ArrayList<KoozuPair<Location, Material>> blocks;
            /*
            Bukkit.getScheduler().runTask(MazeGenerator.instance, () ->{
                locations.forEach(loc -> {
                    world.getBlockAt(loc).setType(Material.BLUE_TERRACOTTA);
                });
            });

             */
            switch (genMode) {
                case NORMAL:
                    //ArrayList<Location> normLocs =  plugin.utils.stretchTo3Times(mazeBlocks, pos1);
                    sendCountDoneMessage(player, sendMsg);
                    blocks = plugin.utils.countMazeBlocks(pVar, locations);
                    sendStartBPMessage(player, sendMsg);
                    plugin.utils.placeBlocks(blocks, pVar.getBps(), world);
                    break;
                case SLIM3x3:

                    ArrayList<Location> newLocList = plugin.utils.stretchTo3Times(locations, pVar.getPos1());
                    locations.clear();
                    locations.addAll(newLocList);
                    sendCountDoneMessage(player, sendMsg);
                    blocks = plugin.utils.countMazeBlocks(pVar, locations);
                    sendStartBPMessage(player, sendMsg);
                    plugin.utils.placeBlocks(blocks, pVar.getBps(), world);
                    break;
                case WORLDGEN:
                    sendCountDoneMessage(player, sendMsg);
                    ArrayList<KoozuPair<Location, Material>> blocksWG = plugin.utils.countMazeBlocks(pVar, locations);
                    sendStartBPMessage(player, sendMsg);
                    plugin.utils.placeBlocks(blocksWG, bps, world);
                    break;
            }

        });
        /*
        player.sendMessage(mazeBlocks.size() + " koko");
        mazeBlocks.forEach(loc ->{
            loc.add(0, 20, 0).getBlock().setType(Material.BLUE_TERRACOTTA);
            Bukkit.getConsoleSender().sendMessage(loc.getBlockX() + "x, z:" + loc.getBlockZ());
        });

        sendCountDoneMessage(player, sendMsg);

        ArrayList<KoozuPair<Location, Material>> blocks = plugin.utils.countMazeBlocks(world, nPos1, nPos2, mazeBlocks);
        sendStartBPMessage(player, sendMsg);

        plugin.utils.placeBlocks(blocks, pVar.getBps(), world);

         */


        /*
        switch (genMode){
            case NORMAL:
                //ArrayList<Location> normLocs =  plugin.utils.stretchTo3Times(mazeBlocks, pos1);
                sendCountDoneMessage(player, sendMsg);
                ArrayList<KoozuPair<Location, Material>> blocks = plugin.utils.countMazeBlocks(world, nPos1, nPos2, mazeBlocks);
                sendStartBPMessage(player, sendMsg);
                plugin.utils.placeBlocks(blocks, pVar.getBps(), world);
                break;
            case SLIM3x3:
                break;
            case WORLDGEN:
                sendCountDoneMessage(player, sendMsg);
                ArrayList<KoozuPair<Location, Material>> blocksWG = plugin.utils.countMazeBlocks(world, nPos1, nPos2, mazeBlocks);
                sendStartBPMessage(player, sendMsg);
                plugin.utils.placeBlocks(blocksWG, bps, world);
                break;
        }

         */
    }
    /*
    public ArrayList<KoozuPair<Location, Material>> genMazee(final Location pos1, final Location pos2, final World world, final int blocksPerSecond, final Mode genMode){
        KoozuPair<Location, Location> invertedLocations = plugin.utils.getInvertedLocations(pos1, pos2, world);
        Location nPos1 = invertedLocations.getKey();
        Location nPos2 = invertedLocations.getValue();
        ArrayList<Location> mazeBlocks = plugin.mainGenerator.calculateMazeLocs(nPos1, nPos2, world, blocksPerSecond, genMode);
        ArrayList<KoozuPair<Location, Material>> finalMazeBlocks = null;
        switch (genMode){
            case NORMAL:
                ArrayList<Location> normLocs =  plugin.utils.stretchTo3Times(mazeBlocks, pos1);
                ArrayList<KoozuPair<Location, Material>> blocks = plugin.utils.countMazeBlocks(world, nPos1, nPos2, normLocs);
                finalMazeBlocks = null;
                break;
            case SLIM3x3:
                ArrayList<Location> normLocs2 =  plugin.utils.stretchTo3Times(mazeBlocks, pos1);
                finalMazeBlocks = plugin.utils.countMazeBlocks(world, nPos1, nPos2, normLocs2);;
                break;
            case WORLDGEN:
                finalMazeBlocks = plugin.utils.countMazeBlocks(world, nPos1, nPos2, mazeBlocks);
                break;
        }
        return finalMazeBlocks;
    }

     */

    //Messages
    public void sendCountDoneMessage(Player player, boolean sendmsg) {
        if (sendmsg) {
            player.sendMessage("Labyrintin laskenta valmis. Lasketaan palikat!");
        }
    }

    public void sendStartBPMessage(Player player, boolean sendmsg) {
        if (sendmsg) {
            player.sendMessage("Palikat laskettu, asetetaan palikat!");
        }
    }
}
