package io.github.underscore11code.ccord.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

// Sorry, the one time when I can't use a static block or constructor.
@SuppressWarnings("initialization.static.fields.uninitialized")
public final class CarbonCoreBootstrap {
  private static final Logger logger = LoggerFactory.getLogger(CarbonCoreBootstrap.class);
  private static CarbonCore carbonCore;

  private CarbonCoreBootstrap() {
  }

  @SuppressWarnings({"dereference.of.nullable", "argument.type.incompatible"})
  public static void main(final String[] args) {
    logger.info("Bootstrapping CarbonCord Core");

    // https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
    try {
      carbonCore = new CarbonCore(new File(CarbonCoreBootstrap.class.getProtectionDomain().getCodeSource().getLocation()
        .toURI()).getParentFile());
    } catch (final Exception e) {
      logger.error("FATAL: Error while starting up - {}: {}", e.getClass().getSimpleName(), e.getMessage());
      logger.error("", e);
      try {
        carbonCore.shutdown();
      } catch (final Exception shutdownException) {
        logger.error("Second error while shutting down, killing process...", shutdownException);
        System.exit(1);
      }
    }

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        carbonCore.shutdown();
      } catch (final Exception e) {
        logger.error("Error while shutting down - {}: {}", e.getClass().getSimpleName(), e.getMessage());
        logger.error("", e);
      }
    }));
  }
}
