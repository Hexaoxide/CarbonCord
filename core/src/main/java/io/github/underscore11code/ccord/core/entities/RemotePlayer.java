package io.github.underscore11code.ccord.core.entities;

import io.github.underscore11code.ccord.core.CarbonCore;
import io.github.underscore11code.ccord.messaging.packets.AdventureChatPacket;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RemotePlayer implements Audience, Identity {
  private final UUID uuid;
  private String realName;
  private String displayName;

  public RemotePlayer(final UUID uuid, final String realName, final String displayName) {
    this.uuid = uuid;
    this.realName = realName;
    this.displayName = displayName;
  }

  @Override
  public @NotNull UUID uuid() {
    return this.uuid;
  }

  public String realName() {
    return this.realName;
  }

  public void realName(final String realName) {
    this.realName = realName;
  }

  public String displayName() {
    return this.displayName;
  }

  public void displayName(final String displayName) {
    this.displayName = displayName;
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
    CarbonCore.instance().messageService().send(new AdventureChatPacket(message, null, this.uuid, null));
  }

  @Override
  public void sendMessage(final @NonNull Identified source, final @NonNull Component message) {
    this.sendMessage(source.identity(), message);
  }

  @Override
  public void sendMessage(final @NonNull Identity source, final @NonNull Component message) {
    CarbonCore.instance().messageService().send(new AdventureChatPacket(message, null, this.uuid, source.uuid()));
  }
}
