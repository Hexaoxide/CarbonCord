package io.github.underscore11code.ccord.client;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaperClientAdapter implements ClientPlatformAdapter {
  private final CarbonClientPaper client;

  public PaperClientAdapter(final CarbonClientPaper client) {
    this.client = client;
  }

  @Override
  public Audience global() {
    return this.client.getServer();
  }

  @Override
  public @Nullable Audience player(final UUID uuid) {
    return this.client.getServer().getPlayer(uuid);
  }

  @Override
  public List<UUID> onlineList() {
    final List<UUID> online = new ArrayList<>();
    for (final Player onlinePlayer : this.client.getServer().getOnlinePlayers()) {
      online.add(onlinePlayer.getUniqueId());
    }
    return online;
  }

  @Override
  public boolean isOnline(final UUID uuid) {
    return this.onlineList().contains(uuid);
  }
}
