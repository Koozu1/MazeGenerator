package net.koozumaa.mazegenerator.Generators;

import net.koozumaa.mazeapi.iPlayerVar;
import net.koozumaa.mazegenerator.MazeGenerator;
import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazeapi.Mode;
import net.koozumaa.mazegenerator.Utils.PlayerVar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import net.koozumaa.mazeapi.iMazeApi;

import java.util.ArrayList;

public class GenManager implements iMazeApi {
    public MazeGenerator plugin;

    public GenManager(MazeGenerator plugin) {
        this.plugin = plugin;
    }

    @Override
    public void genMaze(iPlayerVar pVar) {
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

        plugin.mainGenerator.calculateMazeLocs(pVar, locations -> {
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

    @Override
    public iPlayerVar createPlayerVar(Location pos1, Location pos2, int bps, Mode genMode, boolean sendMessages, ArrayList<Material> roofMaterials, ArrayList<Material> wallMaterials, ArrayList<Material> floorMaterials, World world) {
        return new PlayerVar(pos1, pos2, bps, genMode, sendMessages, roofMaterials, wallMaterials, floorMaterials, world);
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
