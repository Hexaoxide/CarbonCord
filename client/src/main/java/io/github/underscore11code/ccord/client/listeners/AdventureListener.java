package io.github.underscore11code.ccord.client.listeners;

import io.github.underscore11code.ccord.client.CarbonClient;
import io.github.underscore11code.ccord.messaging.MessageService;
import io.github.underscore11code.ccord.messaging.packets.AdventureChatPacket;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public class AdventureListener {
  private final CarbonClient carbonClient;

  @SuppressWarnings("methodref.receiver.bound.invalid")
  public AdventureListener(final CarbonClient carbonClient) {
    this.carbonClient = carbonClient;

    this.carbonClient.messageService().bus().register(AdventureChatPacket.class, this::onPacket);
  }

  private void onPacket(final AdventureChatPacket p) {
    final Audience audience = this.audienceFor(p);
    if (audience == null) return;

    final UUID identity = p.identity();
    if (identity != null) {
      audience.sendMessage(Identity.identity(identity), p.deserializedComponent());
    } else {
      audience.sendMessage(p.deserializedComponent());
    }
  }

  private @Nullable Audience audienceFor(final AdventureChatPacket p) {
    if (p.targetedClient() == null && p.targetedPlayer() == null) return null;

    final UUID targetedClient = p.targetedClient();
    final UUID targetedPlayer = p.targetedPlayer();

    // If given both client and player & they're both valid, use that
    if (targetedClient != null && targetedPlayer != null) {
      if (targetedClient.equals(MessageService.SERVER_ID) && this.carbonClient.adapter().isOnline(targetedPlayer)) {
        return this.carbonClient.adapter().player(targetedPlayer);
      }
      return null;
    }

    if (targetedClient != null && targetedClient.equals(MessageService.SERVER_ID)) {
      return this.carbonClient.adapter().global();
    }

    if (targetedPlayer != null && this.carbonClient.adapter().isOnline(targetedPlayer)) {
      return this.carbonClient.adapter().player(targetedPlayer);
    }

    return null;
  }
}
