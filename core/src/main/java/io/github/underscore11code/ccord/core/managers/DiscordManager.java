package io.github.underscore11code.ccord.core.managers;

import io.github.underscore11code.ccord.core.CarbonCore;
import io.github.underscore11code.ccord.core.config.CoreChatChannelConfig;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.kyori.event.EventBus;
import net.kyori.event.SimpleEventBus;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordManager implements EventListener {
  private static final Logger logger = LoggerFactory.getLogger(DiscordManager.class);
  private final CarbonCore carbonCore;
  private final EventBus<GenericEvent> bus = new SimpleEventBus<>(GenericEvent.class);

  @SuppressWarnings({"methodref.receiver.bound.invalid", "argument.type.incompatible"})
  public DiscordManager(final CarbonCore carbonCore) {
    this.carbonCore = carbonCore;
    this.carbonCore.jda().addEventListener(this);
    this.bus.register(GuildMessageReceivedEvent.class, this::messageHandler);
  }

  private void messageHandler(final GuildMessageReceivedEvent e) {
    if (e.getAuthor().isBot() || e.isWebhookMessage()) return;
    final CoreChatChannelConfig chatChannel = this.chatChannelFromDiscordId(e.getChannel().getId());
    if (chatChannel == null) return;
  }

  @Override
  public void onEvent(final @NonNull GenericEvent event) { // IJ the @NonNull is inferred when none present...
    this.bus.post(event);
  }

  public EventBus<GenericEvent> bus() {
    return this.bus;
  }

  private @Nullable CoreChatChannelConfig chatChannelFromDiscordId(final String id) {
    for (final CoreChatChannelConfig chatChannel : this.carbonCore.config().get().chatChannels()) {
      if (id.equals(chatChannel.discordChannelId())) return chatChannel;
    }
    return null;
  }
}
