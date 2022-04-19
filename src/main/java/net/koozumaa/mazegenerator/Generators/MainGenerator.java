package net.koozumaa.mazegenerator.Generators;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import net.koozumaa.mazegenerator.MazeGenerator;
import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazegenerator.Utils.Mode;
import net.koozumaa.mazegenerator.Utils.PlayerVar;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class MainGenerator {
    public MazeGenerator plugin;

    public MainGenerator(MazeGenerator plugin){
        this.plugin = plugin;
    }
    /**
     *
     * Async maze generating algorithm
     *
     */
    Random rand = new Random();

    boolean sendMsgs = false;
    public void calculateMazeLocs(PlayerVar pVar, Consumer<ArrayList<Location>> callback) {
        ArrayList<Location> locList = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(MazeGenerator.instance, () -> {

            final Location start = pVar.getPos1();
            final Location finish;

            if (pVar.getGenMode().equals(Mode.SLIM3x3)) {
                finish = plugin.utils.splitToThird(start.clone(), plugin.utils.devideLocation(start.clone(), pVar.getPos2()));
            } else {
                finish = plugin.utils.devideLocation(start.clone(), pVar.getPos2());
            }


            Location iAmHere = start.clone();
            ArrayList<Location> whereWasI = new ArrayList<>();
            ArrayList<Location> visitedLocs = new ArrayList<>();
            ArrayList<KoozuPair<Location, Location>> points = new ArrayList<>();

            while (true) {
                ArrayList<Location> possibleLocs = plugin.utils.getPossibleBlocksAround(iAmHere, start, finish, visitedLocs);

                if (possibleLocs.isEmpty()) {
                    ArrayList<Location> abba = plugin.utils.getPossibleBlocksAroundWorldEditRegion(iAmHere, pVar, visitedLocs);
                    if (!abba.isEmpty()){
                        Bukkit.broadcast(Component.text( iAmHere + " first." + abba.size() + ": " + abba.get(0)));
                    }
                    if (whereWasI.size() <= 1) {
                        Bukkit.broadcast(Component.text("KIKI"));
                        points.forEach(p -> {
                            locList.addAll(plugin.utils.multiplyLocations(p.getKey(), p.getValue(), start));
                        });
                        callback.accept(locList);
                        break;
                    }
                    whereWasI.remove(iAmHere);
                    if (rand.nextInt(2) == 1) {
                        iAmHere = whereWasI.get(whereWasI.size() - 1);
                    } else {
                        iAmHere = whereWasI.get(rand.nextInt(whereWasI.size()));
                    }
                    continue;
                }

                if (plugin.utils.getPossibleBlocksAroundWorldEditRegion(iAmHere, pVar, visitedLocs).isEmpty()){
                    Bukkit.broadcast(Component.text("not empty !=  notempty"));
                }
                final Location selectedLoc = possibleLocs.get(rand.nextInt(possibleLocs.size()));
                points.add(new KoozuPair<>(iAmHere, selectedLoc));

                if(!visitedLocs.contains(selectedLoc)){
                    visitedLocs.add(selectedLoc);
                }

                iAmHere = selectedLoc.clone();
                whereWasI.add(iAmHere);
            }

        });
    }
    public void calculateMazeLocs(PlayerVar pVar, WorldEditPlugin we, Consumer<ArrayList<Location>> callback) {
        ArrayList<Location> locList = new ArrayList<>();

        Bukkit.getScheduler().runTaskAsynchronously(MazeGenerator.instance, () -> {
            Player player = Bukkit.getPlayer(pVar.getUUID());
            Location start = null;
            try {
                BlockVector3 bvec = we.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld())).getBoundingBox().getPos1();
                start = new Location(player.getWorld(), bvec.getX(), bvec.getY(), bvec.getZ());
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }
            //final Location finish = plugin.utils.devideLocation(start.clone(), pVar.getPos2());

            Location iAmHere = start.clone();
            ArrayList<Location> whereWasI = new ArrayList<>();
            ArrayList<Location> visitedLocs = new ArrayList<>();
            ArrayList<KoozuPair<Location, Location>> points = new ArrayList<>();

            while (true) {
                ArrayList<Location> possibleLocs = plugin.utils.getPossibleBlocksAroundWorldEditRegion(iAmHere, pVar, visitedLocs);
                Bukkit.broadcast(Component.text(iAmHere + "!!!!! size: " + possibleLocs.size()));
                if (possibleLocs != null && !possibleLocs.isEmpty()) {
                    ArrayList<Location> posLoclist = new ArrayList<>();
                    Location finalIAmHere = iAmHere;
                    possibleLocs.forEach(posLoc -> {
                        //REUNAT???
                        /*
                        if (plugin.utils.getPossibleBlocksAroundWorldEditRegion(posLoc.clone(), pVar, visitedLocs).size() == 3) {
                            posLoclist.add(posLoc);
                        }

                         */
                        if (plugin.utils.checkIfPossibleLoc(posLoc, finalIAmHere, visitedLocs)){
                            posLoclist.add(posLoc);
                        }
                    });
                    possibleLocs = posLoclist;
                }



                if (possibleLocs.isEmpty()) {
                    if (whereWasI.size() <= 1) {
                        /*
                        points.forEach(p -> {
                            locList.addAll(plugin.utils.multiplyLocations(p.getKey(), p.getValue(), start));
                        });

                         */
                        points.forEach(p -> {
                            locList.add(p.getValue());
                        });

                        callback.accept(locList);
                        break;
                    }
                    whereWasI.remove(iAmHere);
                    if (rand.nextInt(2) == 1) {
                        iAmHere = whereWasI.get(whereWasI.size() - 1);
                    } else {
                        iAmHere = whereWasI.get(rand.nextInt(whereWasI.size()));
                    }
                    continue;
                }
                final Location selectedLoc = possibleLocs.get(rand.nextInt(possibleLocs.size()));
                points.add(new KoozuPair<>(iAmHere, selectedLoc));
                visitedLocs.add(selectedLoc);

                iAmHere = selectedLoc.clone();
                whereWasI.add(iAmHere);
            }

        });

    }


}

