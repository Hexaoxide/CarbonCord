package io.github.underscore11code.ccord.client;

import io.github.underscore11code.ccord.client.config.CarbonCordClientConfig;
import io.github.underscore11code.ccord.client.listeners.AdventureListener;
import io.github.underscore11code.ccord.common.Config;
import io.github.underscore11code.ccord.common.VersionChecker;
import io.github.underscore11code.ccord.messaging.MessageService;
import io.github.underscore11code.ccord.messaging.RedisMessagingService;
import io.github.underscore11code.ccord.messaging.packets.ClientConnectPacket;
import io.github.underscore11code.ccord.messaging.packets.PingPacket;
import io.github.underscore11code.ccord.messaging.packets.PingResponsePacket;
import io.github.underscore11code.ccord.messaging.packets.RawChatPacket;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

public class CarbonClient {
  private static @Nullable CarbonClient instance = null;
  private static final Logger logger = LoggerFactory.getLogger(CarbonClient.class);

  private final File configDir;
  private final Config<CarbonCordClientConfig> config;
  private final MessageService messageService;
  private final ClientPlatformAdapter adapter;

  @SuppressWarnings({"assignment.type.incompatible", "argument.type.incompatible"})
  public CarbonClient(final File configDir, final ClientPlatformAdapter adapter) throws Exception {
    if (instance != null) {
      throw new IllegalStateException("Attempted to load CarbonClient when it's already running!");
    }
    instance = this;

    if (!VersionChecker.checkJvmVersion()) {
      throw new Exception();
    }

    this.adapter = adapter;

    this.configDir = configDir;
    this.config = new Config<>(CarbonCordClientConfig.class, new File(configDir, "client.conf"));
    this.config.load();
    this.config.save();

    // TODO Remove when a second messaging service is added
    //noinspection SwitchStatementWithTooFewBranches
    switch (this.config.get().messaging().serviceType()) {
      case "redis":
        this.messageService = new RedisMessagingService(this.config.get().messaging().redis().toRedisUri());
        break;

      default:
        logger.error("Invalid messaging service type \"{}\"!", this.config.get().messaging().serviceType());
        throw new Exception();
    }

    this.messageService.connect();

    this.messageService.bus().register(PingPacket.class, p -> {
      if (!p.isFromThis()) this.messageService.send(new PingResponsePacket(p));
    });
    new AdventureListener(this);
    this.messageService.send(new ClientConnectPacket(this.config.get().serverName()));
  }

  public void shutdown() {
    this.messageService.disconnect();
    instance = null;
  }

  public void handleMessage(final Identified player, final String channel, final String message) {
    this.messageService.send(new RawChatPacket(player.identity().uuid(), channel, message));
  }

  public void handleMessage(final Identity identity, final String channel, final String message) {
    this.messageService.send(new RawChatPacket(identity.uuid(), channel, message));
  }

  public void handleMessage(final UUID uuid, final String channel, final String message) {
    this.messageService.send(new RawChatPacket(uuid, channel, message));
  }

  public File configDir() {
    return this.configDir;
  }

  public Config<CarbonCordClientConfig> config() {
    return this.config;
  }

  public MessageService messageService() {
    return this.messageService;
  }

  public ClientPlatformAdapter adapter() {
    return this.adapter;
  }

  public static @Nullable CarbonClient instance() {
    return instance;
  }
}
