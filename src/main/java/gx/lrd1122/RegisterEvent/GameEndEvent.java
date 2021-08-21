package gx.lrd1122.RegisterEvent;

import gx.lrd1122.GameStates.Game;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public final class GameEndEvent extends Event implements Cancellable {
    private static final HandlerList handler = new HandlerList();
    private Game game;
    private boolean cancelled;

    public GameEndEvent(Game game)
    {
        this.game = game;
    }
    public Game getGame() {
        return game;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public HandlerList getHandlers() {
        return handler;
    }

    public static HandlerList getHandler() {
        return handler;
    }
}
