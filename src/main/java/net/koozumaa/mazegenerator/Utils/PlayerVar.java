package net.koozumaa.mazegenerator.Utils;

import net.koozumaa.mazegenerator.MazeGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class PlayerVar {

    Player player = null;
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    Location pos1 = null;
    public Location getPos1() { return pos1; }
    public void setPos1(Location pos1) { this.pos1 = pos1; }

    Location pos2 = null;
    public Location getPos2() { return pos2; }
    public void setPos2(Location pos2) { this.pos2 = pos2; }

    int bps = MazeGenerator.instance.blocksPerSecondStock;
    public int getBps() { return bps; }
    public void setBps(int bps) { this.bps = bps; }

    Mode genMode = Mode.NORMAL;
    public Mode getGenMode() { return genMode; }
    public void setGenMode(Mode genMode) { this.genMode = genMode; }

    boolean sendMessages = true;
    public boolean isSendMessages() { return sendMessages; }
    public void setSendMessages(boolean sendMessages) { this.sendMessages = sendMessages; }

    ArrayList<Material> roofMaterials = new ArrayList<>();
    public ArrayList<Material> getRoofMaterials() { return roofMaterials; }
    public void setRoofMaterials(ArrayList<Material> roofMaterials) { this.roofMaterials = roofMaterials; }
    public void addRoofMaterial(Material material){this.roofMaterials.add(material);}

    ArrayList<Material> wallMaterials = new ArrayList<>();
    public ArrayList<Material> getWallMaterials() { return wallMaterials; }
    public void setWallMaterials(ArrayList<Material> wallMaterials) { this.wallMaterials = wallMaterials; }
    public void addWallMaterial(Material material){this.wallMaterials.add(material);}

    ArrayList<Material> floorMaterials = new ArrayList<>();
    public ArrayList<Material> getFloorMaterials() { return floorMaterials; }
    public void setFloorMaterials(ArrayList<Material> floorMaterials) { this.floorMaterials = floorMaterials; }
    public void addFloorMaterial(Material material){this.floorMaterials.add(material);}


}
