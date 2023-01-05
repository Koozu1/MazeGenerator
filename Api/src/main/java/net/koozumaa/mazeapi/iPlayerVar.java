package net.koozumaa.mazeapi;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.UUID;

public interface iPlayerVar {

    UUID getUUID();

    void setUUID(UUID uuid);

    Location getPos1();

    void setPos1(Location pos1);

    Location getPos2();

    void setPos2(Location pos2);

    int getBps();

    void setBps(int bps);

    Mode getGenMode();

    void setGenMode(Mode genMode);

    boolean isSendMessages();

    void setSendMessages(boolean sendMessages);

    ArrayList<Material> getRoofMaterials();

    void setRoofMaterials(ArrayList<Material> roofMaterials);

    void addRoofMaterial(Material material);

    ArrayList<Material> getWallMaterials();

    void setWallMaterials(ArrayList<Material> wallMaterials);

    void addWallMaterial(Material material);

    ArrayList<Material> getFloorMaterials();

    void setFloorMaterials(ArrayList<Material> floorMaterials);

    void addFloorMaterial(Material material);

    World getWorld();

    void setWorld(World world);

}
