package net.koozumaa.mazegenerator;

import net.koozumaa.mazeapi.Maze;
import net.koozumaa.mazegenerator.Tab.TabCompleter;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public final class MazeGenerator extends JavaPlugin {
    public static MazeGenerator instance;
    public static String commandPrefix = "§f§lMoti§a§lMaze §8§l» §7";
    public static String commandFormat = "command: /Maze [pos1, pos2, generate, variables]";
    public HashMap<UUID, Maze> players = new HashMap<>();

    public TabCompleter tabCompleter;

    @Override
    public void onEnable() {
        instance = this;
        tabCompleter = new TabCompleter(this);
        getCommand("maze").setExecutor(new MazeCommand(this));
        getCommand("maze").setTabCompleter(tabCompleter);
        tabCompleter.addTabCompleteStrings();
    }

}
