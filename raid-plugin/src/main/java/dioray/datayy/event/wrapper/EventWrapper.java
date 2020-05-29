package dioray.datayy.event.wrapper;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventWrapper extends Event implements Cancellable {
    private static final HandlerList list = new HandlerList();
    private boolean cancellable;

    public static HandlerList getHandlerList() {
        return list;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancellable;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancellable = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public EventWrapper() {
        this.cancellable = false;
    }
}
