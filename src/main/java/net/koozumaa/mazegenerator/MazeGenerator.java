package net.koozumaa.mazegenerator;

import net.koozumaa.mazegenerator.Generators.GenManager;
import net.koozumaa.mazegenerator.Generators.MainGenerator;
import net.koozumaa.mazegenerator.Tab.TabCompleter;
import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazegenerator.Utils.MazeCalcUtils;
import net.koozumaa.mazegenerator.Utils.PlayerVar;
import net.koozumaa.mazegenerator.Utils.Mode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class MazeGenerator extends JavaPlugin {
    public static MazeGenerator instance;
    ArrayList<KoozuPair<Location, Location>> pointList = new ArrayList<>();
    public static String commandPrefix = "§f§lMoti§a§lMaze §8§l» §7";
    public ArrayList<Material> wallMaterials = new ArrayList<>();
    public ArrayList<Material> floorMaterial = new ArrayList<>();
    public ArrayList<Material> roofMaterial = new ArrayList<>();
    public static String commandFormat = "command: /Maze [pos1, pos2, generate, variables]";
    public ArrayList<PlayerVar> playerVars = new ArrayList<>();

    public int blocksPerSecond = 2000;
    public int blocksPerSecondStock = 2000;
    public MazeCalcUtils utils;
    public Mode genMode = Mode.NORMAL;
    public TabCompleter tabCompleter;

    //Väliaikainen
    public ArrayList<KoozuPair<Location, Material>> genBlocks = new ArrayList<>();


    public Algorithm algorithm;
    public NewAlgorithm newAlgorithm;
    public GenManager manager;
    public MainGenerator mainGenerator;

    @Override
    public void onEnable() {
        instance = this;
        utils = new MazeCalcUtils(this);
        tabCompleter = new TabCompleter(this);
        newAlgorithm = new NewAlgorithm(this);
        manager = new GenManager(this);
        mainGenerator = new MainGenerator(this);
        getCommand("maze").setExecutor(new MazeCommand(this));
        getCommand("maze").setTabCompleter(tabCompleter);
        tabCompleter.addTabCompleteStrings();
        //genSomeMaze();

    }
    /*
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id){
        return new WorldGen(this);
    }
    public void genSomeMaze(){
        Location pos1 = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        Location pos2 = pos1.clone().add(16, 10, 16);
        newAlgorithm.calculateMaze(null, pos1, pos2, Bukkit.getWorld("world"), 2000, Mode.WORLDGEN);
    }

     */
}
