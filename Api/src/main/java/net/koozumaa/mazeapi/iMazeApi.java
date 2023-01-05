package net.koozumaa.mazeapi;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;

public interface iMazeApi {
    void genMaze(iPlayerVar playerVar);
    iPlayerVar createPlayerVar(Location pos1, Location pos2, int bps, Mode genMode, boolean sendMessages, ArrayList<Material> roofMaterials, ArrayList<Material> wallMaterials, ArrayList<Material> floorMaterials, World world);
}
