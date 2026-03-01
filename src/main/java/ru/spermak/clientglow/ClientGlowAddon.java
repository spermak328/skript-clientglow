package ru.spermak.clientglow;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ClientGlowAddon {

    static {
        Skript.registerEffect(EffClientGlow.class,
                "client glow %entity% for %player%",
                "remove client glow of %entity% for %player%");
    }

    public static class EffClientGlow extends Effect {

        private Expression<Entity> entity;
        private Expression<Player> player;
        private boolean remove;

        @Override
        protected void execute(Event e) {
            Entity ent = entity.getSingle(e);
            Player viewer = player.getSingle(e);
            if (ent == null || viewer == null) return;

            // ПОКА ЗАГЛУШКА — позже вставим packet glowing
            if (!remove) {
                viewer.sendMessage("§a[ClientGlow] glow ON for " + ent.getName());
            } else {
                viewer.sendMessage("§c[ClientGlow] glow OFF for " + ent.getName());
            }
        }

        @Override
        public String toString(Event e, boolean debug) {
            return "client glow";
        }

        @Override
        public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed,
                            ch.njol.skript.lang.SkriptParser.ParseResult parseResult) {
            entity = (Expression<Entity>) expr[0];
            player = (Expression<Player>) expr[1];
            remove = matchedPattern == 1;
            return true;
        }
    }
}
