package io.github.underscore11code.ccord.client;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("initialization.fields.uninitialized") // I know :(
public class CarbonClientPaper extends JavaPlugin implements Listener {
  private CarbonClient carbonClient;

  @Override
  public void onEnable() {
    try {
      this.carbonClient = new CarbonClient(this.getDataFolder(), new PaperClientAdapter(this));
    } catch (final Exception e) {
      e.printStackTrace();
    }

    this.getServer().getPluginManager().registerEvents(this, this);
  }

  @Override
  public void onDisable() {
    this.carbonClient.shutdown();
  }

  @EventHandler
  public void onChat(final AsyncChatEvent chatEvent) {
    this.carbonClient.handleMessage(chatEvent.getPlayer(), "global", PlainComponentSerializer.plain().serialize(chatEvent.message()));
  }
}
