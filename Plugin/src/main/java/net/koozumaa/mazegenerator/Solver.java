package net.koozumaa.mazegenerator;

import net.koozumaa.mazeapi.KoozuPair;
import net.koozumaa.mazeapi.MazeCalcUtils;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.Random;

public class Solver {
    Location loca;
    Location lastVisited;
    Random random = new Random();

    public void solveMaze(final Location pos1, final Location pos2) {

        KoozuPair<Location, Location> invertedLocations = MazeCalcUtils.getInvertedLocations(pos1, pos2, pos1.getWorld());
        final Location startLoc = invertedLocations.getKey();
        final Location finishLoc = invertedLocations.getValue();

        ArrayList<Location> path = new ArrayList<>();
        loca = startLoc.clone();
        lastVisited = loca.clone();
        path.add(loca);


        ArrayList<KoozuPair<Location, ArrayList<Location>>> intersections = new ArrayList<>();
        ArrayList<Location> notRightPath = new ArrayList<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Location> aroundLocs = MazeCalcUtils.getBlocksAround(loca);
                ArrayList<Location> aroundLocClone = MazeCalcUtils.getBlocksAround(loca);
                for (int i = 0; i < aroundLocs.size(); i++) {
                    if (!MazeCalcUtils.isInRegion(aroundLocs.get(i), startLoc, finishLoc) || aroundLocs.get(i).getBlock().equals(lastVisited.getBlock()) || notRightPath.contains(aroundLocs.get(i))) {
                        aroundLocClone.remove(aroundLocs.get(i));
                        continue;
                    }
                    if (!(aroundLocs.get(i).getBlock().getType().equals(Material.AIR) || aroundLocs.get(i).getBlock().getType().equals(Material.BLUE_TERRACOTTA))) {
                        aroundLocClone.remove(aroundLocs.get(i));
                        continue;
                    }

                    if (!intersections.isEmpty()) {
                        for (KoozuPair<Location, ArrayList<Location>> ic : intersections) {
                            if (ic.getKey().getBlock().equals(loca.getBlock())) {
                                if (ic.getValue().contains(aroundLocs.get(i))) {
                                    aroundLocClone.remove(aroundLocs.get(i));
                                }
                                break;
                            }
                        }
                    }

                }
                if (aroundLocClone.isEmpty()) {
                    if (path.isEmpty()) {
                        Bukkit.broadcastMessage("Nyt kävi nolosti");
                        cancel();
                        return;

                    }
                    path.remove(loca);
                    loca.getBlock().setType(Material.AIR);
                    notRightPath.add(loca);
                    loca = path.get(path.size() - 1);
                    lastVisited = path.get(path.size() - 2);
                    return;
                }
                int rand = random.nextInt(aroundLocClone.size());
                if (aroundLocs.size() > 1) {
                    for (int i = 0; i < intersections.size(); i++) {
                        KoozuPair<Location, ArrayList<Location>> ic = intersections.get(i);
                        if (ic.getKey().getBlock().equals(loca.getBlock())) {
                            if (!ic.getValue().contains(aroundLocClone.get(rand))) {
                                ic.getValue().add(aroundLocClone.get(rand));
                                ArrayList<Location> locs = new ArrayList<>(ic.getValue());
                                locs.add(aroundLocClone.get(i));
                                intersections.get(i).setValue(locs);
                            }
                        }
                    }
                }

                lastVisited = loca.clone();
                loca = aroundLocClone.get(rand);
                path.add(loca);
                loca.getBlock().setType(Material.BLUE_TERRACOTTA);
                if (finishLoc.getBlockX() <= loca.getBlockX() && finishLoc.getBlockZ() <= loca.getBlockZ()) {
                    cancel();
                }


            }
        }.runTaskTimer(MazeGenerator.instance, 0L, 1L);
    }
}
