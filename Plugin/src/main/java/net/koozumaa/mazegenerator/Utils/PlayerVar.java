package net.koozumaa.mazegenerator.Utils;

import net.koozumaa.mazeapi.Mode;
import net.koozumaa.mazeapi.iPlayerVar;
import net.koozumaa.mazegenerator.MazeGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerVar implements iPlayerVar {

    UUID uuid = null;
    Location pos1 = null;
    Location pos2 = null;
    int bps = MazeGenerator.instance.blocksPerSecondStock;
    Mode genMode = Mode.NORMAL;
    boolean sendMessages = true;
    ArrayList<Material> roofMaterials = new ArrayList<>();
    ArrayList<Material> wallMaterials = new ArrayList<>();
    ArrayList<Material> floorMaterials = new ArrayList<>();
    World world;

    public PlayerVar(){

    }
    //Api constructor
    public PlayerVar(Location pos1, Location pos2, int bps, Mode genMode, boolean sendMessages, ArrayList<Material> roofMaterials, ArrayList<Material> wallMaterials, ArrayList<Material> floorMaterials, World world) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.bps = bps;
        this.genMode = genMode;
        this.sendMessages = sendMessages;
        this.roofMaterials = roofMaterials;
        this.wallMaterials = wallMaterials;
        this.floorMaterials = floorMaterials;
        this.world = world;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public int getBps() {
        return bps;
    }

    public void setBps(int bps) {
        this.bps = bps;
    }

    public Mode getGenMode() {
        return genMode;
    }

    public void setGenMode(Mode genMode) {
        this.genMode = genMode;
    }

    public boolean isSendMessages() {
        return sendMessages;
    }

    public void setSendMessages(boolean sendMessages) {
        this.sendMessages = sendMessages;
    }

    public ArrayList<Material> getRoofMaterials() {
        return roofMaterials;
    }

    public void setRoofMaterials(ArrayList<Material> roofMaterials) {
        this.roofMaterials = roofMaterials;
    }

    public void addRoofMaterial(Material material) {
        this.roofMaterials.add(material);
    }

    public ArrayList<Material> getWallMaterials() {
        return wallMaterials;
    }

    public void setWallMaterials(ArrayList<Material> wallMaterials) {
        this.wallMaterials = wallMaterials;
    }

    public void addWallMaterial(Material material) {
        this.wallMaterials.add(material);
    }

    public ArrayList<Material> getFloorMaterials() {
        return floorMaterials;
    }

    public void setFloorMaterials(ArrayList<Material> floorMaterials) {
        this.floorMaterials = floorMaterials;
    }

    public void addFloorMaterial(Material material) {
        this.floorMaterials.add(material);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }


}
