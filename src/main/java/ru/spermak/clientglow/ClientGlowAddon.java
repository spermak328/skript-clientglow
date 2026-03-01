package ru.spermak.clientglow;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class ClientGlowAddon {

    static {
        Skript.registerEffect(EffClientGlow.class,
                "client glow %entity% for %player%",
                "remove client glow of %entity% for %player%");
    }

    public static class EffClientGlow extends Effect {

        private Expression<Entity> entityExpr;
        private Expression<Player> playerExpr;
        private boolean remove;

        @Override
        protected void execute(Event e) {
            Entity target = entityExpr.getSingle(e);
            Player viewer = playerExpr.getSingle(e);
            if (target == null || viewer == null) return;

            Scoreboard board = viewer.getScoreboard();

            String teamName = "cg_" + target.getUniqueId().toString().substring(0, 12);
            Team team = board.getTeam(teamName);

            if (!remove) {
                if (team == null) {
                    team = board.registerNewTeam(teamName);
                    team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
                    team.setCanSeeFriendlyInvisibles(true);
                    team.setColor(org.bukkit.ChatColor.WHITE); // цвет glow
                }

                team.addEntry(target.getUniqueId().toString());
            } else {
                if (team != null) {
                    team.removeEntry(target.getUniqueId().toString());
                }
            }
        }

        @Override
        public String toString(Event e, boolean debug) {
            return "client glow";
        }

        @Override
        public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed,
                            ch.njol.skript.lang.SkriptParser.ParseResult parseResult) {
            entityExpr = (Expression<Entity>) expr[0];
            playerExpr = (Expression<Player>) expr[1];
            remove = matchedPattern == 1;
            return true;
        }
    }
}
