package net.koozumaa.mazegenerator.Tab;

import net.koozumaa.mazegenerator.MazeGenerator;
import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazegenerator.Utils.Mode;
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

    List<String> commands = new ArrayList<>();
    KoozuPair<List<String>, List<String>> materialPair = new KoozuPair<>(null, null);
    KoozuPair<String, List<String>> modePair = new KoozuPair<>(null, null);


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Player player = (Player) sender;

        //return result1;
        List<String> results = new ArrayList<>();
        if (args.length == 1){
            for (String a : commands){
                if (a.toLowerCase().startsWith(args[0].toLowerCase())){
                    results.add(a);
                }
            }
            return results;
        }else if (args.length == 2){
            /*
            if (!args[0].equalsIgnoreCase("addmaterial") && !args[0].equalsIgnoreCase("removematerial")){
                return null;
            }
            for (String a : arg2Results){
                if (a.toLowerCase().startsWith(args[1].toLowerCase())){
                    results.add(a);
                }
            }

             */
            materialPair.getKey().forEach(arg0 ->{
                if (arg0.toLowerCase().startsWith(args[0].toLowerCase())){
                    materialPair.getValue().forEach(arg1 ->{
                        if (arg1.toLowerCase().startsWith(args[1])){
                            results.add(arg1);
                        }
                    });
                    return;
                }
            });
            if (args[0].toLowerCase().startsWith(modePair.getKey().toLowerCase())){
               modePair.getValue().forEach(mode -> {
                   if (mode.toLowerCase().startsWith(args[1].toLowerCase())){
                       results.add(mode);
                   }
               });
            }

            return results;
        }
        return null;


    }

    public void addTabCompleteStrings(){
        List<String> materialArg0 = new ArrayList<>();
        List<String> materialArg1 = new ArrayList<>();

        materialArg0.add("AddMaterial");

        materialArg1.add("Roof");
        materialArg1.add("Wall");
        materialArg1.add("Floor");

        materialPair = new KoozuPair<>(materialArg0, materialArg1);

        commands.add("Pos1");
        commands.add("Pos2");
        commands.add("Solve");
        commands.add("Generate");
        commands.add("Variables");
        commands.add("BlocksPerSecond");
        commands.add("AddMaterial");
        commands.add("RemoveMaterial");
        commands.add("ClearRoofMaterials");
        commands.add("ClearWallMaterials");
        commands.add("ClearFloorMaterials");
        commands.add("Mode");

        String mode = "Mode";
        List<String> modes = new ArrayList<>();

        Arrays.stream(Mode.values())
                .forEach(genMode -> modes.add(genMode.toString()));

        modePair = new KoozuPair<>(mode, modes);

    }
}
