package ru.spermak.clientglow;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.util.Kleenean;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public class EffClientGlow extends Effect {

    private Expression<org.bukkit.entity.Entity> entityExpr;
    private Expression<Player> playerExpr;
    private boolean remove;

    @Override
    protected void execute(Event e) {
        org.bukkit.entity.Entity bukkitEntity = entityExpr.getSingle(e);
        Player viewer = playerExpr.getSingle(e);
        if (bukkitEntity == null || viewer == null) return;

        Entity nms = ((CraftEntity) bukkitEntity).getHandle();
        SynchedEntityData data = nms.getEntityData();

        byte flags = data.get(Entity.DATA_SHARED_FLAGS_ID);

        if (remove) {
            flags &= ~0x40;
        } else {
            flags |= 0x40;
        }

        data.set(Entity.DATA_SHARED_FLAGS_ID, flags);

        ClientboundSetEntityDataPacket packet =
                new ClientboundSetEntityDataPacket(
                        nms.getId(),
                        List.of(data.packDirty().get(0))
                );

        ((CraftPlayer) viewer).getHandle().connection.send(packet);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "client glow";
    }

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed,
                        ch.njol.skript.lang.SkriptParser.ParseResult parseResult) {
        entityExpr = (Expression<org.bukkit.entity.Entity>) expr[0];
        playerExpr = (Expression<Player>) expr[1];
        remove = matchedPattern == 1;
        return true;
    }
}
