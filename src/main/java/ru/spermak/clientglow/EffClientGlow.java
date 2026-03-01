package ru.spermak.clientglow;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.util.Kleenean;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

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

            byte flags = 0x40; // glow
            if (remove) flags = 0;

            WrappedDataWatcher.Serializer serializer =
                    WrappedDataWatcher.Registry.get(Byte.class);

            WrappedDataValue value = new WrappedDataValue(
                    0, // index флагов
                    serializer,
                    flags
            );

            packet.getDataValueCollectionModifier().write(0, List.of(value));

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
