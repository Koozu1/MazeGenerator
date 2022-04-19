package net.koozumaa.mazegenerator;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.koozumaa.mazegenerator.Generators.GenManager;
import net.koozumaa.mazegenerator.Generators.MainGenerator;
import net.koozumaa.mazegenerator.Tab.TabCompleter;
import net.koozumaa.mazegenerator.Utils.MazeCalcUtils;
import net.koozumaa.mazegenerator.Utils.PlayerVar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public final class MazeGenerator extends JavaPlugin {
    public static MazeGenerator instance;
    public static String commandPrefix = "§f§lMoti§a§lMaze §8§l» §7";
    public static String commandFormat = "command: /Maze [pos1, pos2, generate, variables]";
    public HashMap<UUID, PlayerVar> players = new HashMap<>();

    public int blocksPerSecondStock = 2000;
    public MazeCalcUtils utils;
    public TabCompleter tabCompleter;
    public GenManager manager;
    public MainGenerator mainGenerator;
    public WorldEditPlugin worldEditPlugin;

    @Override
    public void onEnable() {
        instance = this;
        utils = new MazeCalcUtils(this);
        tabCompleter = new TabCompleter(this);
        manager = new GenManager(this);
        mainGenerator = new MainGenerator(this);
        getCommand("maze").setExecutor(new MazeCommand(this));
        getCommand("maze").setTabCompleter(tabCompleter);
        tabCompleter.addTabCompleteStrings();
        Bukkit.getPluginManager().registerEvents(new Clicklistener(), this);
        worldEditPlugin = ( WorldEditPlugin ) Bukkit.getPluginManager().getPlugin( "WorldEdit" );


    }

}
