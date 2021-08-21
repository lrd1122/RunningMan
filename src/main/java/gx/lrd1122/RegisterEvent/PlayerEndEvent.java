package gx.lrd1122.RegisterEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerEndEvent extends Event implements Cancellable {

    private final static HandlerList handler = new HandlerList();
    private Player player;
    private boolean cancelled;

    public PlayerEndEvent(Player player)
    {
        this.player = player;
    }
    @Override
    public HandlerList getHandlers() {
        return handler;
    }
    public static HandlerList getHandler() {
        return handler;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
