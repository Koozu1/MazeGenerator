package net.koozumaa.mazeapi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class MainGenerator {
    boolean sendMsgs = false;


    public static void calculateMazeLocs(Maze pVar, Plugin plugin, Consumer<ArrayList<Location>> callback) {
        Random rand = new Random();
        ArrayList<Location> locList = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            final Location start = pVar.getPos1();
            final Location finish;

            if (pVar.getGenMode().equals(Mode.SLIM3x3)) {
                finish = MazeCalcUtils.splitToThird(start.clone(), MazeCalcUtils.devideLocation(start.clone(), pVar.getPos2()));
            } else {
                finish = MazeCalcUtils.devideLocation(start.clone(), pVar.getPos2());
            }


            Location iAmHere = start.clone();
            ArrayList<Location> whereWasI = new ArrayList<>();
            ArrayList<Location> visitedLocs = new ArrayList<>();
            ArrayList<KoozuPair<Location, Location>> points = new ArrayList<>();
            int surfaceArea = (Math.abs(start.getBlockX() - finish.getBlockX()) * Math.abs(start.getBlockZ() - finish.getBlockZ()));

            while (true) {
                ArrayList<Location> possibleLocs = MazeCalcUtils.getPossibleBlocksAround(iAmHere, start, finish, visitedLocs);
                if (possibleLocs.isEmpty()) {
                    if (whereWasI.size() <= 1) {
                        points.forEach(p -> {
                            locList.addAll(MazeCalcUtils.multiplyLocations(p.getKey(), p.getValue(), start));
                        });
                        callback.accept(locList);
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

            }
        });
    }
}

