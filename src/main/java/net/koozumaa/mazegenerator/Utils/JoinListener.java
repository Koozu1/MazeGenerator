package net.koozumaa.mazegenerator.Utils;

import net.koozumaa.mazegenerator.MazeGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    public MazeGenerator plugin;
    public JoinListener(MazeGenerator plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){

    }
}
