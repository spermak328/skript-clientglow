package ru.spermak.clientglow;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.util.Kleenean;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class EffClientGlow extends Effect {

    private Expression<Entity> entityExpr;
    private Expression<Player> playerExpr;
    private boolean remove;

    @Override
    protected void execute(Event e) {
        Entity entity = entityExpr.getSingle(e);
        Player viewer = playerExpr.getSingle(e);
        if (entity == null || viewer == null) return;

        try {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();

            PacketContainer packet = manager.createPacket(
                    com.comphenix.protocol.PacketType.Play.Server.ENTITY_METADATA
            );

            packet.getIntegers().write(0, entity.getEntityId());

            byte flags = entity.getEntityId() != 0 ? (byte) 0x40 : 0;
            if (remove) flags = 0;

            packet.getWatchableCollectionModifier().write(0, java.util.List.of(
                    new com.comphenix.protocol.wrappers.WrappedWatchableObject(
                            0,
                            flags
                    )
            ));

            manager.sendServerPacket(viewer, packet);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed,
                        ch.njol.skript.lang.SkriptParser.ParseResult parseResult) {
        entityExpr = (Expression<Entity>) expr[0];
        playerExpr = (Expression<Player>) expr[1];
        remove = matchedPattern == 1;
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "client glow";
    }
}
