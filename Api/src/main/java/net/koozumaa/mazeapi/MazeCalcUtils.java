package net.koozumaa.mazeapi;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MazeCalcUtils {
    public static final int blockspersecond = 2000;
    //Get the placeble blocks in the maze. Roof, Floor, Walls
    //TODO: Outer walls
    private static final Random random = new Random();

    //Pos1 to lower coord, pos2 to higher.
    public static KoozuPair<Location, Location> getInvertedLocations(final Location loc1, final Location loc2, World world) {
        int startx = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int endx = Math.max(loc1.getBlockX(), loc2.getBlockX());

        int starty = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int endy = Math.max(loc1.getBlockY(), loc2.getBlockY());

        int startz = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int endz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        Location start = new Location(world, startx, starty, startz);
        Location finish = new Location(world, endx, endy, endz);
        return new KoozuPair<>(start, finish);
    }

    //Check if loc is in the 2d square of corner 1 & 2
    public static boolean isInRegion(Location location, Location corner1, Location corner2) {
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
        return location.getBlockX() >= minX && location.getBlockX() <= maxX && location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ;
    }


    //Devides pos2 to 1/4 of original surface area
    public static Location devideLocation(final Location pos1, final Location pos2) {
        return pos1.clone().add((pos2.getBlockX() - pos1.getBlockX()) / 2, 0, (pos2.getBlockZ() - pos1.getBlockZ()) / 2);
    }

    //Checks if location is visited
    public static boolean isVisited(Location loc, ArrayList<Location> list) {
        return list.contains(loc);
    }

    //Get 4 locations around a location
    public static ArrayList<Location> getBlocksAround(Location loc) {
        Location aroundLoc1 = loc.clone().add(1, 0, 0);
        Location aroundLoc2 = loc.clone().subtract(1, 0, 0);

        Location aroundLoc3 = loc.clone().add(0, 0, 1);
        Location aroundLoc4 = loc.clone().subtract(0, 0, 1);

        return new ArrayList<>(Arrays.asList(aroundLoc1, aroundLoc2, aroundLoc3, aroundLoc4));
    }

    //Get 4 locations around & return each if not visited and in region
    public static ArrayList<Location> getPossibleBlocksAround(Location loc, Location pos1, Location pos2, ArrayList<Location> visitedLocs) {
        ArrayList<Location> locs = getBlocksAround(loc);
        ArrayList<Location> pLocs = new ArrayList<>();
        locs.forEach(location -> {
            if (isInRegion(location, pos1, pos2) && !isVisited(location, visitedLocs)) {
                pLocs.add(location);
            }
        });
        return pLocs;
    }

    //Place blocks with defined amount of blocks per second
    //TODO: Optimize with placing all block in a chunk at a time
    public static void placeBlocks(ArrayList<KoozuPair<Location, Material>> pairs, int blocksPerSecond, World world, Plugin plugin) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            final int bpTick = blocksPerSecond / 20;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (pairs.size() < bpTick) {
                        for (KoozuPair<Location, Material> pair : pairs) {
                            //world.getBlockAt(pair.getKey()).setType(pair.getValue());
                            pair.getKey().getBlock().setType(pair.getValue(), false);
                        }
                        cancel();
                        return;
                    }
                    final List<KoozuPair<Location, Material>> pairSubList = pairs.subList(0, bpTick - 1);

                    for (KoozuPair<Location, Material> pair : pairSubList) {
                        //world.getBlockAt(pair.getKey()).setType(pair.getValue());
                        pair.getKey().getBlock().setType(pair.getValue(), false);
                    }
                    pairs.removeAll(pairSubList);
                }
            }.runTaskTimer(plugin, 0L, 1L);
        });
    }

    //When stretching, multiply pairs to corects size and add wall inbetween
    public static ArrayList<Location> multiplyLocations(final Location pos1, final Location pos2, final Location mazeStartLocation) {
        Location mPos1 = pos1.clone();
        Location mPos2 = pos2.clone();

        mPos1.add(pos1.getX() - mazeStartLocation.getX(), 0, pos1.getZ() - mazeStartLocation.getZ());
        mPos2.add(pos2.getX() - mazeStartLocation.getX(), 0, pos2.getZ() - mazeStartLocation.getZ());
        Location between = mPos1.clone();
        if (mPos1.getX() != mPos2.getX()) {
            between.add(mPos1.getX() < mPos2.getX() ? 1 : -1, 0, 0);
        }
        if (mPos1.getZ() != mPos2.getZ()) {
            between.add(0, 0, mPos1.getZ() < mPos2.getZ() ? 1 : -1);
        }
        return new ArrayList<>(Arrays.asList(mPos1, mPos2, between));
    }

    //center block
    public static Location toBlockLocation(Location loc) {
        return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static ArrayList<KoozuPair<Location, Material>> countMazeBlocks(Maze pVar, ArrayList<Location> locations) {
        ArrayList<KoozuPair<Location, Material>> blocks = new ArrayList<>();
        World world;
        if (pVar.getUUID() == null) {
            world = pVar.getWorld();
        } else {
            world = Bukkit.getPlayer(pVar.getUUID()).getWorld();
        }
        Location pos1 = pVar.getPos1();
        Location pos2 = pVar.getPos2();

        for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
            for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                Location blockLoc = new Location(world, x, pos1.getY(), z);
                if (!locations.contains(blockLoc)) {
                    for (int y = pos1.getBlockY() + 1; y <= pos2.getBlockY() - 1; y++) {
                        Location blockLockWY = new Location(world, x, y, z);
                        Material wallMaterial = pVar.getMaterials().getWall().get(random.nextInt(pVar.getMaterials().getWall().size()));
                        blocks.add(new KoozuPair<>(blockLockWY, wallMaterial));
                    }
                }
                Location floorLoc = new Location(world, x, pos1.getY(), z);

                Material floorMaterial = pVar.getMaterials().getFloor().get(random.nextInt(pVar.getMaterials().getFloor().size()));
                blocks.add(new KoozuPair<>(floorLoc, floorMaterial));

                Location roofLoc = new Location(world, x, pos2.getY(), z);
                Material roofMaterial = pVar.getMaterials().getRoof().get(random.nextInt(pVar.getMaterials().getRoof().size()));
                blocks.add(new KoozuPair<>(roofLoc, roofMaterial));
            }
        }
        return blocks;
    }

    public static ArrayList<Location> stretchTo3Times(final ArrayList<Location> locList, final Location startLoc) {
        ArrayList<Location> stretchedLocs = new ArrayList<>();
        locList.forEach(loc -> {
            Location locMid = startLoc.clone().add((loc.clone().getBlockX() - startLoc.clone().getBlockX()) * 3, 0, (loc.clone().getBlockZ() - startLoc.clone().getBlockZ()) * 3);
            final Location arLoc1 = locMid.clone().add(1, 0, 1);
            final Location arLoc2 = locMid.clone().add(1, 0, 0);
            final Location arLoc3 = locMid.clone().add(1, 0, -1);
            final Location arLoc4 = locMid.clone().add(0, 0, 1);
            final Location arLoc5 = locMid.clone().add(0, 0, -1);
            final Location arLoc6 = locMid.clone().add(-1, 0, 1);
            final Location arLoc7 = locMid.clone().add(-1, 0, 0);
            final Location arLoc8 = locMid.clone().add(-1, 0, -1);
            stretchedLocs.addAll(Arrays.asList(arLoc1, arLoc2, arLoc3, arLoc4, arLoc5, arLoc6, arLoc7, arLoc8, locMid));
        });
        return stretchedLocs;
    }

    public static Location splitToThird(Location pos1, Location pos2) {
        return pos1.clone().add((pos2.getBlockX() - pos1.getBlockX()) / 3, 0, (pos2.getBlockZ() - pos1.getBlockZ()) / 3);
    }

    public static Location split(Location pos1, Location pos2, int i) {
        double xDiff = pos2.getBlockX() - pos1.getBlockX();
        double zDiff = pos2.getBlockZ() - pos1.getBlockZ();
        return pos1.clone().add(xDiff / i, 0, zDiff / i);
    }

    public static ArrayList<Chunk> getChunksBetween(Location min, Location max, World world) {
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (int x = min.getBlockX(); x <= max.getBlockX(); x += 16) {
            for (int z = min.getBlockZ(); z <= max.getBlockX(); z += 16) {
                Chunk chunk = world.getChunkAt(x, z);
                if (chunks.contains(chunk)) {
                    continue;
                }
                chunks.add(chunk);
            }
        }
        Chunk chunk = world.getChunkAt(max);
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
        }
        return chunks;
    }
}
