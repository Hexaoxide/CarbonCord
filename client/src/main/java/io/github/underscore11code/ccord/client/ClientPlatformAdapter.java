package io.github.underscore11code.ccord.client;

import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.UUID;

public interface ClientPlatformAdapter {
  Audience global();

  @Nullable Audience player(final UUID uuid);

  List<UUID> onlineList();

  boolean isOnline(final UUID uuid);
}
