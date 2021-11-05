package net.koozumaa.mazegenerator.Generators;

import net.koozumaa.mazegenerator.MazeGenerator;
import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazegenerator.Utils.Mode;
import net.koozumaa.mazegenerator.Utils.PlayerVar;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public class MainGenerator {
    public MazeGenerator plugin;

    public MainGenerator(MazeGenerator plugin){
        this.plugin = plugin;
    }
    /**
     *
     * Async maze generating algorithm
     *
     */
    Random rand = new Random();

    ArrayList<KoozuPair<Chunk, ArrayList<Location>>> chunkLocList = new ArrayList<>();
    boolean sendMsgs = false;
    public void calculateMazeLocs(PlayerVar pVar, Consumer<ArrayList<Location>> callback) {
        ArrayList<Location> locList = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(MazeGenerator.instance, () -> {

            final Location start = pVar.getPos1();
            final Location finish;

            if (pVar.getGenMode().equals(Mode.SLIM3x3)) {
                finish = plugin.utils.splitToThird(start.clone(), plugin.utils.devideLocation(start.clone(), pVar.getPos2()));
            } else {
                finish = plugin.utils.devideLocation(start.clone(), pVar.getPos2());
            }


            Location iAmHere = start.clone();
            ArrayList<Location> whereWasI = new ArrayList<>();
            ArrayList<Location> visitedLocs = new ArrayList<>();
            ArrayList<KoozuPair<Location, Location>> points = new ArrayList<>();
            int surfaceArea = (Math.abs(start.getBlockX() - finish.getBlockX()) * Math.abs(start.getBlockZ() - finish.getBlockZ()));

            while (true) {
                ArrayList<Location> possibleLocs = plugin.utils.getPossibleBlocksAround(iAmHere, start, finish, visitedLocs);
                //rand.setSeed(plugin.utils.randSeed(iAmHere.getBlockX(), iAmHere.getBlockZ()));
                //rand.setSeed(plugin.utils.getRandomSeed(rand.nextDouble()));

                if (possibleLocs.isEmpty()) {
                    if (whereWasI.size() <= 1) {
                        break;
                    }
                    whereWasI.remove(iAmHere);
                    if (rand.nextInt(2) == 1) {
                        iAmHere = whereWasI.get(whereWasI.size() - 1);
                    } else {
                        iAmHere = whereWasI.get(rand.nextInt(whereWasI.size()));
                    }
                    continue;
                }
                final Location selectedLoc = possibleLocs.get(rand.nextInt(possibleLocs.size()));
                points.add(new KoozuPair<>(iAmHere, selectedLoc));
                visitedLocs.add(selectedLoc);

                iAmHere = selectedLoc.clone();
                whereWasI.add(iAmHere);

                if (surfaceArea == visitedLocs.size()) {
                    points.forEach(p -> {
                        locList.addAll(plugin.utils.multiplyLocations(p.getKey(), p.getValue(), start));
                    });
                    callback.accept(locList);
                    break;
                }
            }

        });

    }


}

