package ru.spermak.clientglow;

import ch.njol.skript.Skript;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClientGlowAddon extends JavaPlugin {

    @Override
    public void onEnable() {
        Skript.registerEffect(EffClientGlow.class,
                "client glow %entity% for %player%",
                "remove client glow of %entity% for %player%");
        getLogger().info("ClientGlow enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ClientGlow disabled");
    }
}
