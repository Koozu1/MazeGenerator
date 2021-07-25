package net.koozumaa.mazegenerator.WorldGenerator;

import net.koozumaa.mazegenerator.MazeGenerator;
import net.koozumaa.mazegenerator.NewAlgorithm;
import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazegenerator.Utils.Mode;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.generator.ChunkGenerator;
import org.checkerframework.checker.units.qual.A;
import sun.security.mscapi.CPublicKey;

import java.util.*;

public class WorldGen extends ChunkGenerator {
    public MazeGenerator plugin;

    public WorldGen(MazeGenerator plugin){
        this.plugin = plugin;
    }
    NewAlgorithm algorithm = new NewAlgorithm(MazeGenerator.instance);
    KoozuPair<ArrayList<Chunk>, ArrayList<KoozuPair<Location, Material>>> chunksNblocks = new KoozuPair<>(null, null);
    ArrayList<KoozuPair<ArrayList<Chunk>, ArrayList<KoozuPair<Location, Material>>>> arChunksBlocks = new ArrayList<>();
    ArrayList<Chunk> generateChunks = new ArrayList<>();
    ArrayList<Material> matList = new ArrayList<>(Arrays.asList(Material.TERRACOTTA, Material.BLUE_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.ORANGE_TERRACOTTA));
    boolean isGenerated = false;
    int x;
    int z;
    boolean containsChunk;
    Map<Chunk, Set<Location>> adada = new HashMap<>();

    ArrayList<KoozuPair<Chunk, ArrayList<Block>>> chunkBlocks = new ArrayList<>();
    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        if (!(chunkX > 0 && chunkZ > 0)){
            return createChunkData(world);
        }
        Location pos1 = new Location(world, chunkX, 0, chunkZ);
        Location pos2 = pos1.clone().add(16 * 3, 10,  16 * 3);

        KoozuPair<Chunk, ArrayList<Block>> chunkB = new KoozuPair<>(null, null);
        Material mat = matList.get(random.nextInt(matList.size() - 1));
        for (x = chunkX; x < chunkX + 32; x++){
            for (z = chunkZ; z < chunkZ + 32; z++){

                chunkBlocks.forEach(chunk ->{

                    if (chunk.getKey() == world.getChunkAt(x, z)){
                        chunkB.getValue().forEach(block -> {
                            if (block.getLocation().getChunk().equals(chunk.getKey())){
                                world.getBlockAt(block.getLocation()).setType(block.getType());
                            }
                        });
                        isGenerated = true;
                    }
                });
                if (!isGenerated){

                }
            }
        }

        if (!adada.containsKey(world.getChunkAt(chunkX, chunkZ))){
            //ArrayList<KoozuPair<Location, Material>> locMatPairs = plugin.manager.genMazee(pos1, pos2, world, 5000, Mode.WORLDGEN);
            //locMatPairs.forEach(pair ->{

                //adada.forEach(chunk);
            //});
        }

        /*
        ChunkData chunk = createChunkData(world);
        isGenerated = false;
        x = 0;
        z = 0;
        Location pos1 = new Location(world, chunkX, 0, chunkZ);
        Location pos2 = pos1.clone().add(16 * 4, 10,  16 * 4);
        if (!arChunksBlocks.isEmpty() && !generateChunks.contains(world.getChunkAt(chunkX, chunkZ))) {
            arChunksBlocks.forEach(arList -> {
                arList.getKey().forEach(arChunk -> {
                    if (arChunk.equals(world.getChunkAt(chunkX, chunkZ))) {
                        isGenerated = true;
                    }
                });
                if (isGenerated) {
                    arList.getValue().forEach(blockLoc -> {
                        if (plugin.utils.isInRegion(blockLoc.getKey(), new Location(world, chunkX, 0, chunkZ), new Location(world, chunkX + 16, 20, chunkZ + 16))) {
                            chunk.setBlock(blockLoc.getKey().getBlockX(), blockLoc.getKey().getBlockY(), blockLoc.getKey().getBlockZ(), blockLoc.getValue());

                        }
                    });
                }
            });
            if (isGenerated) {
                return chunk;
            }
        }


        ArrayList<KoozuPair<Location, Material>> locMatPairs = plugin.manager.genMaze(pos1, pos2, world, 2000, Mode.WORLDGEN);
        ArrayList<Chunk> chunkArray = new ArrayList<>();
        for (x = chunkX; x < chunkX + (16 *4); x += 16){
            for (z = chunkZ; z < chunkZ + (16 * 4); z += 16){
                containsChunk = false;
                arChunksBlocks.forEach(chunkBlock ->{
                    if(chunkBlock.getKey().contains(world.getChunkAt(x, z))){
                       containsChunk = true;
                    }
                });
                if (!containsChunk){
                    chunkArray.add(world.getChunkAt(x, z));
                }
            }
        }
        arChunksBlocks.add(new KoozuPair<>(chunkArray, locMatPairs));

         */




        /*
        for (int x = 0; x < 16; x++){
            for (int z = 0; z < 16; z++){
                chunk.setBlock(x, 1, z, Material.GRASS);
            }
        }

         */
        /*
        locMatPairs.forEach(pair -> {
            if (plugin.utils.isInRegion(pair.getKey(), pos1, pos2)) {
                chunk.setBlock(pair.getKey().getBlockX(), pair.getKey().getBlockY(), pair.getKey().getBlockZ(), pair.getValue());
            }
        });

         */
        //algorithm.calculateMaze(null, pos1, pos2, world, 2000, Mode.NORMAL);
        /*
        plugin.genBlocks.forEach(locMatPair -> chunk.setBlock(
                locMatPair.getKey().getBlockX() + chunkZ,
                locMatPair.getKey().getBlockY(),
                locMatPair.getKey().getBlockZ() + chunkZ,
                locMatPair.getValue()));

         */


        return createChunkData(world);
    }
}
