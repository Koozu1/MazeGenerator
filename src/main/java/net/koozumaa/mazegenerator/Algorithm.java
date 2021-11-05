package net.koozumaa.mazegenerator;

import net.koozumaa.mazegenerator.Utils.KoozuPair;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Algorithm {
    private MazeGenerator plugin;

    public Algorithm(MazeGenerator plugin) {
        this.plugin = plugin;
    }

    ArrayList<Location> visited = new ArrayList<>();
    Random random = new Random();

    public void generate2(Player player, final Location startLocation, final Location finishLocation) {
        KoozuPair<Location, Location> invertedLocations = getInvertedLocations(startLocation, finishLocation);
        final Location start = invertedLocations.getKey().clone();
        final Location realFinish = invertedLocations.getValue();
        Location finish = devideLocation(start, realFinish);

        visited.clear();
        plugin.pointList.clear();
        Location iAmHere = start.clone();
        ArrayList<Location> whereWasI = new ArrayList<>();
        int surfaceArea = (Math.abs(start.getBlockX() - finish.getBlockX()) * Math.abs(start.getBlockZ() - finish.getBlockZ()));
        while (true) {
            ArrayList<Location> aroundLocs = getBlocksAround(iAmHere);
            ArrayList<Location> possibleLocsAround = new ArrayList<>(aroundLocs);
            for (int i = 0; i < aroundLocs.size(); i++) {
                final Location aloc = aroundLocs.get(i);
                if (!isInRegion(aloc, start, finish)) {
                    possibleLocsAround.remove(aloc);
                    continue;
                }


                if (isVisited(aloc)) {
                    possibleLocsAround.remove(aloc);

                }
            }

            if (possibleLocsAround.isEmpty()) {
                if (whereWasI.size() <= 1) {
                    break;
                }
                whereWasI.remove(iAmHere);
                if (random.nextInt(2) == 1) {
                    iAmHere = whereWasI.get(whereWasI.size() - 1);
                } else {
                    iAmHere = whereWasI.get(random.nextInt(whereWasI.size()));
                }

                continue;

            }
            final Location selectedLocation = possibleLocsAround.get(random.nextInt(possibleLocsAround.size()));
            KoozuPair<Location, Location> point = new KoozuPair<>(iAmHere, selectedLocation);
            plugin.pointList.add(point);
            visited.add(selectedLocation);


            iAmHere = selectedLocation.clone();
            whereWasI.add(iAmHere);
            if (surfaceArea == visited.size()) {
                //setBlocks(iAmHere.getWorld());

                ArrayList<Location> locationList = new ArrayList<>();
                for (int q = 0; q < plugin.pointList.size(); q++) {
                    Location startloc = plugin.pointList.get(q).getKey();
                    Location finishLoc = plugin.pointList.get(q).getValue();
                    locationList.addAll(getMultipliedLocations(start, startloc, finishLoc));
                }
                player.sendMessage("Lasketaan palikoiden paikat. (lattia, seinät, katto. :) vie RAM:ia paljon");


                countMazeParts(start.getWorld(), start, realFinish, locationList, player);
                break;
            }
        }
    }

    //Kääntää sijainnit pienemmästä suurempaan.
    public KoozuPair<Location, Location> getInvertedLocations(final Location loc1, final Location loc2) {
        int startx = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int endx = Math.max(loc1.getBlockX(), loc2.getBlockX());

        int starty = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int endy = Math.max(loc1.getBlockY(), loc2.getBlockY());

        int startz = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int endz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        Location start = new Location(loc1.getWorld(), startx, starty, startz);
        Location finish = new Location(loc1.getWorld(), endx, endy, endz);
        return new KoozuPair<>(start, finish);
    }

    //Laskee lattian, seinien ja katon palikat
    public void countMazeParts(World world, Location pos1, Location pos2, ArrayList<Location> locations, Player player) {
        player.sendMessage(MazeGenerator.commandPrefix + "Laitetaan palikat! §cHuom! Lagaa mikäli palikat/sekuntti on liian suuri.");
        if (pos1.getBlockY() + 3 > pos2.getBlockY()) {
            return;
        }
        //ArrayList<Pair<Location, Material>> pairs = new ArrayList<>();
        ArrayList<KoozuPair<Location, Material>> koozuPairs = new ArrayList<>();

        for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
            for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                for (int y = pos1.getBlockY() + 1; y <= pos2.getBlockY() - 1; y++) {
                    Location blockLoc = new Location(world, x, pos1.getY(), z);
                    Location blockLockWY = new Location(world, x, y, z);
                    if (locations.contains(blockLoc)) {
                        continue;
                    }
                    Material material = Material.STONE;
                    if (!plugin.wallMaterials.isEmpty()) {
                        material = plugin.wallMaterials.get(random.nextInt(plugin.wallMaterials.size()));
                    }
                    //Pair<Location, Material> pair = new Pair<>(blockLockWY, material);
                    KoozuPair<Location, Material> koozuPair = new KoozuPair<>(blockLockWY, material);
                    koozuPairs.add(koozuPair);
                    //Bukkit.getWorld(world.getName()).getBlockAt(blockLockWY).setType(material);
                }
                Location floorLoc = new Location(world, x, pos1.getY(), z);
                Material floorMaterial = Material.STONE;
                if (!plugin.floorMaterial.isEmpty()) {
                    floorMaterial = plugin.floorMaterial.get(random.nextInt(plugin.floorMaterial.size()));
                }
                KoozuPair<Location, Material> floorPair = new KoozuPair<>(floorLoc, floorMaterial);
                koozuPairs.add(floorPair);

                Location roofLoc = new Location(world, x, pos2.getY(), z);
                Material roofMaterial = Material.STONE;
                if (!plugin.roofMaterial.isEmpty()) {
                    roofMaterial = plugin.roofMaterial.get(random.nextInt(plugin.roofMaterial.size()));
                }
                KoozuPair<Location, Material> roofPair = new KoozuPair<>(roofLoc, roofMaterial);
                koozuPairs.add(roofPair);


            }
        }
        placeBlocks(koozuPairs, plugin.blocksPerSecond);
    }

    //Asettaa palikat
    public void placeBlocks(ArrayList<KoozuPair<Location, Material>> pairs, int blocksPerSecond) {
        int bps = blocksPerSecond / 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (pairs.size() < bps) {
                    for (KoozuPair<Location, Material> pair : pairs) {
                        Bukkit.getWorld(pair.getKey().getWorld().getName()).getBlockAt(pair.getKey()).setType(pair.getValue());
                    }
                    cancel();
                    return;
                }
                final List<KoozuPair<Location, Material>> pairSubList = pairs.subList(0, bps - 1);

                for (KoozuPair<Location, Material> pair : pairSubList) {
                    Bukkit.getWorld(pair.getKey().getWorld().getName()).getBlockAt(pair.getKey()).setType(pair.getValue());
                }
                pairs.removeAll(pairSubList);

            }
        }.runTaskTimer(MazeGenerator.instance, 0L, 1L);
    }

    //Onko sijainti labyrintin sisällä?
    public boolean isInRegion(Location location, Location corner1, Location corner2) {
        int doubleCheck = 0;

        if (location.getBlockX() >= corner1.getBlockX() && location.getBlockX() <= corner2.getBlockX()) {
            doubleCheck += 1;
        }
        if (location.getBlockZ() >= corner1.getBlockZ() && location.getBlockZ() <= corner2.getBlockZ()) {
            doubleCheck += 1;
        }

        if (doubleCheck == 2) {
            return true;
        }
        return false;
    }

    //Taikatemppu joka venyttää labyrintin kokoonsa
    public ArrayList<Location> getMultipliedLocations(Location origo, final Location start, final Location finish) {
        ArrayList<Location> multipliedDistance = new ArrayList<>();
        Location finish2 = finish.clone();
        Location start2 = start.clone();
        Location middle;
        if (origo.getX() < start.getX()) {
            start2.add((start.getX() - origo.getX()), 0, 0);
        } else {
            start2.add((origo.getX() - start.getX()), 0, 0);
        }
        if (origo.getZ() < start.getZ()) {
            start2.add(0, 0, (start.getZ() - origo.getZ()));
        } else {
            start2.add(0, 0, (origo.getZ() - start.getZ()));
        }
        multipliedDistance.add(start2);

        if (origo.getX() < finish.getX()) {
            finish2.add((finish.getX() - origo.getX()), 0, 0);
        } else {
            finish2.add((origo.getX() - finish.getX()), 0, 0);
        }
        if (origo.getZ() < finish.getZ()) {
            finish2.add(0, 0, (finish.getZ() - origo.getZ()));
        } else {
            finish2.add(0, 0, (origo.getZ() - finish.getZ()));
        }
        multipliedDistance.add(finish2);

        middle = start2.clone();
        if (start2.getX() < finish2.getX()) {
            middle.add(1, 0, 0);
        } else if (finish2.getX() < start2.getX()) {
            middle.add(-1, 0, 0);
        }
        if (start2.getZ() < finish2.getZ()) {
            middle.add(0, 0, 1);
        } else if (finish2.getZ() < start2.getZ()) {
            middle.add(0, 0, -1);
        }
        multipliedDistance.add(middle);

        return multipliedDistance;
    }

    public Location devideLocation(final Location pos1, final Location pos2) {
        Location location = pos1.clone();
        if (pos1.getBlockX() < pos2.getBlockX()) {
            location.add((pos2.getBlockX() - pos1.getBlockX()) / 2, 0, 0);
        } else {
            location.add((pos1.getBlockX() - pos2.getBlockX()) / 2, 0, 0);
        }
        if (pos1.getBlockZ() < pos2.getBlockZ()) {
            location.add(0, 0, (pos2.getBlockZ() - pos1.getBlockZ()) / 2);
        } else {
            location.add(0, 0, (pos1.getBlockZ() - pos2.getBlockZ()) / 2);
        }
        return location;

    }

    public boolean isVisited(Location location) {
        return visited.contains(location);
    }

    public ArrayList<Location> getBlocksAround(Location loc) {
        Location aroundLoc1 = loc.clone().add(1, 0, 0);
        Location aroundLoc2 = loc.clone().subtract(1, 0, 0);

        Location aroundLoc3 = loc.clone().add(0, 0, 1);
        Location aroundLoc4 = loc.clone().subtract(0, 0, 1);

        ArrayList<Location> locations = new ArrayList<>();
        locations.add(aroundLoc1);
        locations.add(aroundLoc2);
        locations.add(aroundLoc3);
        locations.add(aroundLoc4);
        return locations;
    }

    Location loca;
    Location lastVisited;

    public void solveMaze(final Location pos1, final Location pos2) {

        KoozuPair<Location, Location> invertedLocations = getInvertedLocations(pos1, pos2);
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
                ArrayList<Location> aroundLocs = getBlocksAround(loca);
                ArrayList<Location> aroundLocClone = getBlocksAround(loca);
                for (int i = 0; i < aroundLocs.size(); i++) {
                    if (!isInRegion(aroundLocs.get(i), startLoc, finishLoc) || aroundLocs.get(i).getBlock().equals(lastVisited.getBlock()) || notRightPath.contains(aroundLocs.get(i))) {
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
