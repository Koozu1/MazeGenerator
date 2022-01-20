package net.koozumaa.mazegenerator.Generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.koozumaa.mazegenerator.MazeGenerator;
import net.koozumaa.mazegenerator.Utils.KoozuPair;
import net.koozumaa.mazegenerator.Utils.Mode;
import net.koozumaa.mazegenerator.Utils.PlayerVar;
import org.bukkit.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        boolean useApi = true;
        ArrayList<Location> locList = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(MazeGenerator.instance, () -> {
            if (useApi){
                try {
                    callback.accept(requestApi(pVar));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }


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
            int surfaceArea = (Math.abs(start.getBlockX() - finish.getBlockX()) * Math.abs(start.getBlockZ() - finish.getBlockZ()));

            while (true) {
                ArrayList<Location> possibleLocs = plugin.utils.getPossibleBlocksAround(iAmHere, start, finish, visitedLocs);
                if (possibleLocs.isEmpty()) {
                    if (whereWasI.size() <= 1) {
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

                if (surfaceArea == visitedLocs.size()) {
                    points.forEach(p -> {
                        locList.addAll(plugin.utils.multiplyLocations(p.getKey(), p.getValue(), start));
                    });
                    callback.accept(locList);
                    break;
                }
            }

        });

    }

    public ArrayList<Location> requestApi(PlayerVar pVar) throws JsonProcessingException {
        try {
            var pair = new KoozuPair<>(pVar.getPos1(), pVar.getPos2());
            var objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writeValueAsString(pair);

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://86.60.197.61:8080/posttest"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            ArrayList<Location> locs = objectMapper.readValue(response.body(), new TypeReference<>() {});
            locs.forEach(loc ->{
                loc.setWorld(pVar.getPos1().getWorld());
                loc.setY(pVar.getPos1().getBlockY());
            });
            return locs;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}

