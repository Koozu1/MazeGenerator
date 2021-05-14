package net.koozumaa.mazegenerator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private final MazeGenerator plugin;

    public TabCompleter(MazeGenerator plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Player player = (Player) sender;
        List<String> arg1Results = new ArrayList<>(plugin.TabCommands);
        List<String> arg2Results = new ArrayList<>(plugin.TabMaterial);

        //return result1;
        List<String> results = new ArrayList<>();
        if (args.length == 1){
            for (String a : arg1Results){
                if (a.toLowerCase().startsWith(args[0].toLowerCase())){
                    results.add(a);
                }
            }
            return results;
        }else if (args.length == 2){
            if (!args[0].equalsIgnoreCase("addmaterial") && !args[0].equalsIgnoreCase("removematerial")){
                return null;
            }
            for (String a : arg2Results){
                if (a.toLowerCase().startsWith(args[1].toLowerCase())){
                    results.add(a);
                }
            }
            return results;
        }
        return null;


    }
}
