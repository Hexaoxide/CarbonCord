package io.github.underscore11code.ccord.core;

import io.github.underscore11.ccord.common.Config;
import io.github.underscore11code.ccord.core.config.CarbonCordCoreConfig;
import io.github.underscore11code.ccord.messaging.MessageService;
import io.github.underscore11code.ccord.messaging.RedisMessagingService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurateException;

import java.io.File;

public final class CarbonCore {
  private static final Logger logger = LoggerFactory.getLogger(CarbonCore.class);
  private static @Nullable CarbonCore instance;
  private final MessageService messageService;
  private final JDA jda;
  private final Config<CarbonCordCoreConfig> config;
  private final File configDir;

  @SuppressWarnings("assignment.type.incompatible")
  public CarbonCore(final File configDir) throws Exception {
    if (instance != null) {
      throw new IllegalStateException("Attempted to load CarbonCore while it's already running!");
    }
    instance = this;

    logger.info("Starting CarbonCord Core.");

    this.configDir = configDir;
    logger.debug("configDir: " + configDir.getAbsolutePath());
    try {
      this.config = new Config<>(CarbonCordCoreConfig.class, new File(this.configDir, "core.conf"));
      this.config.load();
      this.config.save();
    } catch (final ConfigurateException e) {
      logger.error("Error while loading config!", e);
      throw e;
    }

    logger.info("Starting up messaging service...");
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
    logger.info("Messaging service started.");

    logger.info("Starting up Discord connection...");
    this.jda = JDABuilder.createDefault(this.config.get().botToken()).build().awaitReady();
    logger.info("Connected to Discord as {}", this.jda.getSelfUser().getAsTag());
  }

  public void shutdown() throws Exception {
    this.jda.shutdown();
    this.messageService.disconnect();
    this.config.save();

    instance = null;
  }

  public static CarbonCore instance() {
    if (instance == null) throw new IllegalStateException("CarbonCore is not initialized!");
    return instance;
  }

  public Config<CarbonCordCoreConfig> config() {
    return this.config;
  }

  public File configDir() {
    return this.configDir;
  }
}
