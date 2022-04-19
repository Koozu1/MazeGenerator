package net.koozumaa.mazegenerator.Utils;

import net.koozumaa.mazegenerator.MazeGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerVar {

    private UUID uuid;
    private Location pos1;
    private Location pos2;
    private int bps;
    private Mode genMode;
    private boolean sendMessages;
    private ArrayList<Material> roofMaterials;
    private ArrayList<Material> wallMaterials;
    private ArrayList<Material> floorMaterials;
    public PlayerVar(UUID uuid){
        this.uuid = uuid;
        bps = MazeGenerator.instance.blocksPerSecondStock;
        genMode = Mode.NORMAL;
        sendMessages = true;
        roofMaterials = new ArrayList<>();
        wallMaterials = new ArrayList<>();
        floorMaterials = new ArrayList<>();
    }

    public UUID getUUID() { return uuid; }
    public void setUUID(UUID uuid) {this.uuid = uuid;}


    public Location getPos1() { return pos1; }
    public void setPos1(Location pos1) { this.pos1 = pos1; }


    public Location getPos2() { return pos2; }
    public void setPos2(Location pos2) { this.pos2 = pos2; }


    public int getBps() { return bps; }
    public void setBps(int bps) { this.bps = bps; }


    public Mode getGenMode() { return genMode; }
    public void setGenMode(Mode genMode) { this.genMode = genMode; }


    public boolean isSendMessages() { return sendMessages; }
    public void setSendMessages(boolean sendMessages) { this.sendMessages = sendMessages; }


    public ArrayList<Material> getRoofMaterials() { return roofMaterials; }
    public void setRoofMaterials(ArrayList<Material> roofMaterials) { this.roofMaterials = roofMaterials; }
    public void addRoofMaterial(Material material){this.roofMaterials.add(material);}


    public ArrayList<Material> getWallMaterials() { return wallMaterials; }
    public void setWallMaterials(ArrayList<Material> wallMaterials) { this.wallMaterials = wallMaterials; }
    public void addWallMaterial(Material material){this.wallMaterials.add(material);}

    public ArrayList<Material> getFloorMaterials() { return floorMaterials; }
    public void setFloorMaterials(ArrayList<Material> floorMaterials) { this.floorMaterials = floorMaterials; }
    public void addFloorMaterial(Material material){this.floorMaterials.add(material);}


}
