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

public class GenManager {
    public MazeGenerator plugin;

    public GenManager(MazeGenerator plugin) {
        this.plugin = plugin;
    }


    public void genMaze(final PlayerVar pVar) {
        Player player = Bukkit.getPlayer(pVar.getUUID());
        World world = player.getWorld();
        Mode genMode = pVar.getGenMode();
        boolean sendMsg = pVar.isSendMessages();
        int bps = pVar.getBps();

        boolean useApi = true;


        plugin.mainGenerator.calculateMazeLocs(pVar, locations -> {
            Bukkit.broadcastMessage("Donecalc");
            ArrayList<KoozuPair<Location, Material>> blocks;
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
    }


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
