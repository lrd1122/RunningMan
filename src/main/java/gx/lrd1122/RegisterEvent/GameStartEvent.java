package gx.lrd1122.RegisterEvent;

import gx.lrd1122.GameStates.Game;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameStartEvent extends Event implements Cancellable {
    private static final HandlerList handlerlist = new HandlerList();
    private boolean cancelled;
    private Game game;
    public GameStartEvent(Game game)
    {
        this.game = game;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }
    public static HandlerList getHandlerlist()
    {
        return handlerlist;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
