package net.koozumaa.mazegenerator.Utils;

import net.koozumaa.mazegenerator.MazeGenerator;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MazeCalcUtils {
    private final MazeGenerator plugin;

    public MazeCalcUtils(MazeGenerator plugin) {
        this.plugin = plugin;
    }

    //MOST METHODS REQUIRE LOCATION pos1 < pos2

    //Pos1 to lower coord, pos2 to higher.
    public KoozuPair<Location, Location> getInvertedLocations(final Location loc1, final Location loc2, World world) {
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

    //Devides pos2 to 1/4 of original surface area
    public Location devideLocation(final Location pos1, final Location pos2) {
        return pos1.clone().add((pos2.getBlockX() - pos1.getBlockX()) / 2, 0, (pos2.getBlockZ() - pos1.getBlockZ()) / 2);
    }

    //Checks if location is visited
    public boolean isVisited(Location loc, ArrayList<Location> list) {
        return list.contains(loc);
    }

    //Get 4 locations around a location
    public ArrayList<Location> getBlocksAround(Location loc) {
        Location aroundLoc1 = loc.clone().add(1, 0, 0);
        Location aroundLoc2 = loc.clone().subtract(1, 0, 0);

        Location aroundLoc3 = loc.clone().add(0, 0, 1);
        Location aroundLoc4 = loc.clone().subtract(0, 0, 1);

        return new ArrayList<>(Arrays.asList(aroundLoc1, aroundLoc2, aroundLoc3, aroundLoc4));
    }

    //Get 4 locations around & return each if not visited and in region
    public ArrayList<Location> getPossibleBlocksAround(Location loc, Location pos1, Location pos2, ArrayList<Location> visitedLocs) {
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
    public void placeBlocks(ArrayList<KoozuPair<Location, Material>> pairs, int blocksPerSecond, World world) {
        Bukkit.getScheduler().runTask(MazeGenerator.instance, () -> {
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
            }.runTaskTimer(MazeGenerator.instance, 0L, 1L);
        });
    }

    //When stretching, multiply pairs to corects size and add wall inbetween
    public ArrayList<Location> multiplyLocations(final Location pos1, final Location pos2, final Location mazeStartLocation) {
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
    public Location toBlockLocation(Location loc) {
        return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    //Get the placeble blocks in the maze. Roof, Floor, Walls
    //TODO: Outer walls
    Random random = new Random();
    public ArrayList<KoozuPair<Location, Material>> countMazeBlocks(PlayerVar pVar,  ArrayList<Location> locations) {
        ArrayList<KoozuPair<Location, Material>> blocks = new ArrayList<>();
        World world = pVar.getPlayer().getWorld();
        Location pos1 = pVar.getPos1();
        Location pos2 = pVar.getPos2();

        for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
            for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                Location blockLoc = new Location(world, x, pos1.getY(), z);
                if (!locations.contains(blockLoc)) {
                    for (int y = pos1.getBlockY() + 1; y <= pos2.getBlockY() - 1; y++) {
                        Location blockLockWY = new Location(world, x, y, z);
                        /*
                        if (locations.contains(blockLoc)) {
                            continue;
                        }

                         */
                        Material material = (pVar.getWallMaterials().isEmpty() ? Material.STONE : pVar.getWallMaterials().get(random.nextInt(pVar.getWallMaterials().size())));
                        //Pair<Location, Material> pair = new Pair<>(blockLockWY, material);
                        blocks.add(new KoozuPair<>(blockLockWY, material));
                        //Bukkit.getWorld(world.getName()).getBlockAt(blockLockWY).setType(material);
                    }
                }
                Location floorLoc = new Location(world, x, pos1.getY(), z);
                Material floorMaterial = (pVar.getFloorMaterials().isEmpty() ? Material.STONE_BRICKS : pVar.getFloorMaterials().get(random.nextInt(pVar.getFloorMaterials().size())));
                blocks.add(new KoozuPair<>(floorLoc, floorMaterial));

                Location roofLoc = new Location(world, x, pos2.getY(), z);
                Material roofMaterial = (pVar.getRoofMaterials().isEmpty() ? Material.GLASS : pVar.getRoofMaterials().get(random.nextInt(pVar.getRoofMaterials().size())));
                blocks.add(new KoozuPair<>(roofLoc, roofMaterial));

            }
        }
        return blocks;
    }

    public ArrayList<Location> stretchTo3Times(final ArrayList<Location> locList, final Location startLoc){
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
    public Location splitToThird(Location pos1, Location pos2){
        return pos1.clone().add((pos2.getBlockX() - pos1.getBlockX()) / 3, 0, (pos2.getBlockZ() - pos1.getBlockZ()) / 3);
    }
    public ArrayList<Chunk> getChunksBetween(Location min, Location max, World world){
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (int x = min.getBlockX(); x <= max.getBlockX(); x += 16){
            for (int z = min.getBlockZ(); z <= max.getBlockX(); z += 16){
                Chunk chunk = world.getChunkAt(x, z);
                if (chunks.contains(chunk)){
                    continue;
                }
                chunks.add(chunk);
            }
        }
        Chunk chunk = world.getChunkAt(max);
        if (!chunks.contains(chunk)){
            chunks.add(chunk);
        }
        return chunks;
    }

    public int randSeed(final int x,final int z){
        return ((x % 10 + 1) * (z));
    }


    public int getRandomSeed(double randInt){
        String a = String.valueOf(randInt);
        String newa = "";
        for (int i = 0; i < 8; i++){
            newa += a.substring(a.length() - 1);
            a = a.substring(0, a.length() -1);
        }
        return Integer.parseInt(newa);

    }

}
