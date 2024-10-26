package rip.hardcore.basic.listeners;

import com.destroystokyo.paper.event.block.AnvilDamagedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class General implements Listener {

    @EventHandler
    public void onAnvilDamage(AnvilDamagedEvent e){
        e.setCancelled(true);
    }
    
    
}
