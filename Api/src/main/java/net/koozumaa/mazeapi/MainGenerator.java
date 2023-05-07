package net.koozumaa.mazeapi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class MainGenerator {
    boolean sendMsgs = false;


    public static void calculateMazeLocs(Maze maze, Plugin plugin, Consumer<ArrayList<Location>> callback) {
        long starttime = System.currentTimeMillis();
        Random rand = new Random();
        ArrayList<Location> locList = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Location start = maze.getPos1().clone();
            final Location finish;

            if (maze.getGenMode().equals(Mode.SLIM3x3)) {
                finish = MazeCalcUtils.split(start.clone(), MazeCalcUtils.devideLocation(start.clone(), maze.getPos2()), maze.size);
            } else {
                finish = MazeCalcUtils.devideLocation(start.clone(), maze.getPos2());
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
                        long finishtime = System.currentTimeMillis();
                        System.out.println("time is" + (finishtime - starttime));
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

    public static void calculateMazeLocsNew(Maze maze, Plugin plugin, Consumer<HashSet<Location>> callback) {
        long starttime = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        HashSet<Location> locList = new HashSet<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Location start = maze.getPos1().clone();
            final Location finish;

            if (maze.getGenMode().equals(Mode.SLIM3x3)) {
                finish = MazeCalcUtils.splitToThird(start.clone(), MazeCalcUtils.devideLocation(start.clone(), maze.getPos2()));
            } else {
                finish = MazeCalcUtils.devideLocation(start.clone(), maze.getPos2());
            }
            Location iAmHere = start.clone();
            LinkedList<Location> whereWasI = new LinkedList<>();
            HashSet<Location> visitedLocs = new HashSet<>();
            ArrayList<KoozuPair<Location, Location>> points = new ArrayList<>();

            while (true) {
                ArrayList<Location> possibleLocs = MazeCalcUtils.getPossibleBlocksAroundNew(iAmHere, start, finish, visitedLocs);
                if (possibleLocs.isEmpty()) {
                    if (whereWasI.size() <= 1) {
                        points.forEach(p -> {
                            locList.addAll(MazeCalcUtils.multiplyLocations(p.getKey(), p.getValue(), start));
                        });
                        long finishtime = System.currentTimeMillis();
                        System.out.println("testNew time is" + (finishtime - starttime));
                        callback.accept(locList);
                        break;
                    }

                    if (rand.nextInt(2) == 1) {
                        iAmHere = whereWasI.getLast();
                        whereWasI.removeLast();
                    } else {
                        iAmHere = whereWasI.getFirst();
                        whereWasI.removeFirst();
                    }
                    continue;
                }
                final Location selectedLoc = possibleLocs.get(rand.nextInt(possibleLocs.size()));
                points.add(new KoozuPair<>(iAmHere, selectedLoc));
                visitedLocs.add(selectedLoc);

                iAmHere = selectedLoc.clone();
                whereWasI.addLast(iAmHere);
                //whereWasI.add(iAmHere);
            }
        });
    }
}
