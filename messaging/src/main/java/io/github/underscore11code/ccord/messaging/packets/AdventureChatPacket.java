package io.github.underscore11code.ccord.messaging.packets;

import io.github.underscore11code.ccord.messaging.Packet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public class AdventureChatPacket extends Packet {
  private static final ComponentSerializer<Component, Component, String> serializer = GsonComponentSerializer.gson();
  private final String component;
  private final @Nullable UUID targetedClient;
  private final @Nullable UUID targetedPlayer;
  private final @Nullable UUID identity;

  public AdventureChatPacket(final UUID origin,
                             final UUID packetID,
                             final String component,
                             final @Nullable UUID targetedClient,
                             final @Nullable UUID targetedPlayer,
                             final @Nullable UUID identity) {
    super(origin, packetID);
    this.component = component;
    this.targetedClient = targetedClient;
    this.targetedPlayer = targetedPlayer;
    this.identity = identity;
  }

  public AdventureChatPacket(final String component,
                             final @Nullable UUID targetedClient,
                             final @Nullable UUID targetedPlayer,
                             final @Nullable UUID identity) {
    this.component = component;
    this.targetedClient = targetedClient;
    this.targetedPlayer = targetedPlayer;
    this.identity = identity;
  }

  public AdventureChatPacket(final Component component,
                             final @Nullable UUID targetedClient,
                             final @Nullable UUID targetedPlayer,
                             final @Nullable UUID identity) {
    this.component = serializer.serialize(component);
    this.targetedClient = targetedClient;
    this.targetedPlayer = targetedPlayer;
    this.identity = identity;
  }

  public String component() {
    return this.component;
  }

  public Component deserializedComponent() {
    return serializer.deserialize(this.component);
  }

  public @Nullable UUID targetedClient() {
    return this.targetedClient;
  }

  public @Nullable UUID targetedPlayer() {
    return this.targetedPlayer;
  }

  public @Nullable UUID identity() {
    return this.identity;
  }
}
