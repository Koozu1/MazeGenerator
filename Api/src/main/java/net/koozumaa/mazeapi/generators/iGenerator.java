package net.koozumaa.mazeapi.generators;

import net.koozumaa.mazeapi.Maze;
import org.bukkit.plugin.Plugin;

public interface iGenerator {
    void genMaze(Maze pVar, Plugin plugin);

}
