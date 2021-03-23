package io.github.underscore11code.ccord.core.entities;

import io.github.underscore11code.ccord.core.CarbonCore;
import io.github.underscore11code.ccord.messaging.packets.AdventureChatPacket;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class RemoteClient implements Audience {
  private final UUID uuid;
  private long lastResponse = System.currentTimeMillis();
  private String name;

  public RemoteClient(final UUID uuid, final String name) {
    this.uuid = uuid;
    this.name = name;
  }

  public UUID uuid() {
    return this.uuid;
  }

  public long lastResponse() {
    return this.lastResponse;
  }

  public void lastResponse(final long lastResponse) {
    this.lastResponse = lastResponse;
  }

  public String name() {
    return this.name;
  }

  public void name(final String name) {
    this.name = name;
  }

  @Override
  public void sendMessage(final @NonNull ComponentLike message) {
    this.sendMessage(message.asComponent());
  }

  @Override
  public void sendMessage(final @NonNull Identified source, final @NonNull ComponentLike message) {
    this.sendMessage(source.identity(), message.asComponent());
  }

  @Override
  public void sendMessage(final @NonNull Identity source, final @NonNull ComponentLike message) {
    this.sendMessage(source, message.asComponent());
  }

  @Override
  public void sendMessage(final @NonNull Component message) {
    CarbonCore.instance().messageService().send(new AdventureChatPacket(message, this.uuid, null, null));
  }

  @Override
  public void sendMessage(final @NonNull Identified source, final @NonNull Component message) {
    this.sendMessage(source.identity(), message);
  }

  @Override
  public void sendMessage(final @NonNull Identity source, final @NonNull Component message) {
    CarbonCore.instance().messageService().send(new AdventureChatPacket(message, this.uuid, null, source.uuid()));
  }
}
