package io.github.underscore11code.ccord.common;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;

/**
 * Util class for handling loading, saving, and getting config objects using Configurate
 * @param <C> Type of the config that this will attempt to load. Must be annotated with {@link org.spongepowered.configurate.objectmapping.ConfigSerializable}
 */
public class Config<C> {
  private static final Logger logger = LoggerFactory.getLogger(Config.class);

  private final Class<C> type;
  private final File file;
  private final HoconConfigurationLoader loader;

  private @Nullable CommentedConfigurationNode configNode;
  private @Nullable C config;

  public Config(final Class<C> type, final File file) {
    this.type = type;
    this.file = file;

    this.loader = HoconConfigurationLoader.builder()
      .defaultOptions(opts -> opts.shouldCopyDefaults(true))
      .file(file)
      .build();
  }

  /**
   * Attempt to load + serialize the config file
   * @throws ConfigurateException If there's an exception while loading or serializing
   */
  public void load() throws ConfigurateException {
    logger.info("Loading config {}", this.file.getAbsolutePath());
    this.configNode = this.loader.load(); // Load from file
    this.config = this.configNode.get(this.type);
  }

  /**
   * @return The config object
   * @throws IllegalStateException If called before the config is loaded
   */
  public C get() {
    if (this.config != null)
      return this.config;
    throw new IllegalStateException("Call to get config, but config isn't loaded!");
  }

  /**
   * @return The config object, loading it from file if needed
   * @throws ConfigurateException If there's an exception while loading or serializing
   */
  public C loadAndGet() throws ConfigurateException {
    if (this.config != null) return this.config;
    this.load();
    if (this.config != null) return this.config;
    throw new IllegalStateException("Config null despite after loading it!");
  }

  /**
   * @throws ConfigurateException
   */
  public void save() throws ConfigurateException {
    logger.info("Saving config {}", this.file.getAbsolutePath());
    if (this.configNode == null)
      throw new IllegalStateException("Call to save config, but config isn't loaded!");

    this.configNode.set(this.config);
    this.loader.save(this.configNode);
  }

  public File file() {
    return this.file;
  }
}
