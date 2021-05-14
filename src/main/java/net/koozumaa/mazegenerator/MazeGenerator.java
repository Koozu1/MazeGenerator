package net.koozumaa.mazegenerator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MazeGenerator extends JavaPlugin {
    public static MazeGenerator instance;
    //ArrayList<Points> pointList = new KoozuPair<Location, Location>();
    ArrayList<KoozuPair<Location, Location>> pointList = new ArrayList<>();
    public static String commandPrefix = "§f§lMoti§a§lMaze §8§l» §7";
    ArrayList<Material> wallMaterials = new ArrayList<>();
    ArrayList<Material> floorMaterial = new ArrayList<>();
    ArrayList<Material> roofMaterial = new ArrayList<>();
    public static String commandFormat = "command: /Maze [pos1, pos2, generate, variables]";
    List<String> TabCommands = new ArrayList<>();
    List<String> TabMaterial = new ArrayList<>();
    int blocksPerSecond = 2000;
    int blocksPerSecondStock = 2000;

    public Algorithm algorithm;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("maze").setExecutor(new MazeCommand(this));
        getCommand("maze").setTabCompleter(new TabCompleter(this));
        addTabcompleteStrings();

    }

    public void addTabcompleteStrings(){
        TabCommands.add("Pos1");
        TabCommands.add("Pos2");
        TabCommands.add("Solve");
        TabCommands.add("Generate");
        TabCommands.add("Variables");
        TabCommands.add("BlocksPerSecond");
        TabCommands.add("AddMaterial");
        TabCommands.add("RemoveMaterial");
        TabCommands.add("ClearRoofMaterials");
        TabCommands.add("ClearWallMaterials");
        TabCommands.add("ClearFloorMaterials");

        TabMaterial.add("Roof");
        TabMaterial.add("Wall");
        TabMaterial.add("Floor");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
