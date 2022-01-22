package net.koozumaa.mazegenerator.Utils;

import net.koozumaa.mazegenerator.MazeGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerVar {

    UUID uuid = null;
    public UUID getUUID() { return uuid; }
    public void setUUID(UUID uuid) {this.uuid = uuid;}

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

    public boolean isApi() {
        return api;
    }

    public void setApi(boolean api) {
        this.api = api;
    }

    boolean api = true;



}
