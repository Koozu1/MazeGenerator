package net.koozumaa.mazeapi;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Maze {

    private UUID uuid = null;
    private Location pos1 = null;
    private Location pos2 = null;
    private int bps = 2000;
    private Mode genMode = Mode.NORMAL;
    private boolean sendMessages = true;
    private Materials materials = new Materials();
    private World world;

    public Maze() {

    }

    public Maze(Location pos1, Location pos2, int bps, Mode genMode, boolean sendMessages, World world, Materials materials) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.bps = bps;
        this.genMode = genMode;
        this.sendMessages = sendMessages;
        this.world = world;
        this.materials = materials;
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

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Materials getMaterials() {
        return materials;
    }

    public void setMaterials(Materials materials) {
        this.materials = materials;
    }

    public static class Materials {
        private ArrayList<Material> roof;
        private ArrayList<Material> wall;
        private ArrayList<Material> floor;

        public Materials() {
            this.roof = new ArrayList<>(Collections.singletonList(Material.WHITE_STAINED_GLASS));
            this.wall = new ArrayList<>(Collections.singletonList(Material.LIGHT_BLUE_CONCRETE_POWDER));
            this.floor = new ArrayList<>(Collections.singletonList(Material.STONE_BRICKS));
        }

        public Materials(ArrayList<Material> roofMaterials, ArrayList<Material> wallMaterials, ArrayList<Material> floorMaterials) {
            this.roof = roofMaterials;
            this.wall = wallMaterials;
            this.floor = floorMaterials;
        }

        public ArrayList<Material> getRoof() {
            return roof;
        }

        public void setRoof(ArrayList<Material> roof) {
            this.roof = roof;
        }

        public void addRoofMaterial(Material material) {
            this.roof.add(material);
        }

        public ArrayList<Material> getWall() {
            return wall;
        }

        public void setWall(ArrayList<Material> wall) {
            this.wall = wall;
        }

        public void addWallMaterial(Material material) {
            this.wall.add(material);
        }

        public ArrayList<Material> getFloor() {
            return floor;
        }

        public void setFloor(ArrayList<Material> floor) {
            this.floor = floor;
        }

        public void addFloorMaterial(Material material) {
            this.floor.add(material);
        }
    }

}
