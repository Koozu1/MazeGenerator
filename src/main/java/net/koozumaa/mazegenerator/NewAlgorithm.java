package net.koozumaa.mazegenerator;

import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazegenerator.Utils.Mode;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NewAlgorithm {
    public MazeGenerator plugin;

    public NewAlgorithm(MazeGenerator plugin){
        this.plugin = plugin;
    }

    /**
     *
     * Async maze generating algorithm
     * OUT  OF ORDER
     */
    /*
    Random rand = new Random();
    ArrayList<Location> locList = new ArrayList<>();
    ArrayList<KoozuPair<Chunk, ArrayList<Location>>> chunkLocList = new ArrayList<>();
    boolean sendMsgs = false;
    public void calculateMaze(Player player, final Location pos1, final Location pos2, World world, int blocksPerSecond, Mode genMode) {
        Bukkit.getScheduler().runTaskAsynchronously(MazeGenerator.instance, () -> {

            final KoozuPair<Location, Location> invertedLoc = plugin.utils.getInvertedLocations(pos1, pos2, world);
            final Location start = invertedLoc.getKey();

            final Location finish;

            if (genMode.equals(Mode.SLIM3x3)){
                finish = plugin.utils.splitToThird(plugin.utils.devideLocation(start.clone(), invertedLoc.getValue()), start);
            }else {
                finish = plugin.utils.devideLocation(start.clone(), invertedLoc.getValue());
            }


            Location iAmHere = start.clone();
            ArrayList<Location> whereWasI = new ArrayList<>();
            ArrayList<Location> visitedLocs = new ArrayList<>();
            ArrayList<KoozuPair<Location, Location>> points = new ArrayList<>();
            locList.clear();
            int surfaceArea = (Math.abs(start.getBlockX() - finish.getBlockX()) * Math.abs(start.getBlockZ() - finish.getBlockZ()));
            int ranNum = 0;

            while (true) {
                ranNum++;
                ArrayList<Location> possibleLocs = plugin.utils.getPossibleBlocksAround(iAmHere, start, finish, visitedLocs);
                //rand.setSeed((world.getSeed() + iAmHere.getBlockX() + iAmHere.getBlockZ()) /iAmHere.getBlockX() % 10);
                rand.setSeed(plugin.utils.randSeed(iAmHere.getBlockX(), iAmHere.getBlockZ()));
                rand.setSeed(plugin.utils.getRandomSeed(rand.nextDouble()));

                if (possibleLocs.isEmpty()){
                    if (whereWasI.size() <= 1){
                        break;
                    }
                    whereWasI.remove(iAmHere);
                    if (rand.nextInt(2) == 1){
                        iAmHere = whereWasI.get(whereWasI.size() - 1);
                    }else {
                        iAmHere = whereWasI.get(rand.nextInt(whereWasI.size()));
                    }
                    continue;
                }
                final Location selectedLoc = possibleLocs.get(rand.nextInt(possibleLocs.size()));
                points.add(new KoozuPair<>(iAmHere, selectedLoc));
                visitedLocs.add(selectedLoc);

                iAmHere = selectedLoc.clone();
                whereWasI.add(iAmHere);

                if (surfaceArea == visitedLocs.size()){

                    points.forEach(p -> locList.addAll(plugin.utils.multiplyLocations(p.getKey(), p.getValue(), start)));
                    if (genMode.equals(Mode.SLIM3x3)){
                        ArrayList<Location> newLocList = plugin.utils.stretchTo3Times(locList, start);
                        locList.clear();
                        locList.addAll(newLocList);
                    }
                    if (sendMsgs) {
                        player.sendMessage(MazeGenerator.commandPrefix + "Labyrintin laskenta valmis. Lasketaan palikat!");
                    }

                    ArrayList<KoozuPair<Location, Material>> blocks = plugin.utils.countMazeBlocks(world, start, invertedLoc.getValue(), locList);
                    if (sendMsgs) {
                        player.sendMessage(MazeGenerator.commandPrefix + "Palikat laskettu, asetetaan palikat!");
                    }
                    if (genMode.equals(Mode.WORLDGEN)){
                        plugin.genBlocks.clear();
                        plugin.genBlocks.addAll(blocks);
                        return;
                    }

                    plugin.utils.placeBlocks(blocks, blocksPerSecond,  world);
                    break;
                }
            }

        });
    }

     */
}
