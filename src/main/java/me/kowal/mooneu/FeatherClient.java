package me.kowal.mooneu;

import net.digitalingot.feather.serverapi.api.FeatherAPI;
import net.digitalingot.feather.serverapi.api.event.player.PlayerHelloEvent;
import net.digitalingot.feather.serverapi.api.model.FeatherMod;
import net.digitalingot.feather.serverapi.api.player.FeatherPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;
import java.util.stream.Collectors;

public final class FeatherClient extends JavaPlugin {

    private List<FeatherMod> multipleModsToBlock;

    @Override
    public void onEnable() {
        getLogger().info("Loading config...");
        saveDefaultConfig();
        getLogger().info("Loading mods...");
        loadBlockedMods();

        FeatherAPI.getEventService().subscribe(
                PlayerHelloEvent.class,
                event -> {
                    FeatherPlayer player = event.getPlayer();
                    player.blockMods(multipleModsToBlock);
                }
        );
    }

    private void loadBlockedMods() {
        FileConfiguration config = getConfig();
        List<String> modNames = config.getStringList("blocked-mods");
        multipleModsToBlock = modNames.stream().map(FeatherMod::new).collect(Collectors.toList());
        getLogger().info("Loaded blocked mods: " + modNames);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled blocker...");
    }
}
