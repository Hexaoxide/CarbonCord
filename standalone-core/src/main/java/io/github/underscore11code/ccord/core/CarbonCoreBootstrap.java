package io.github.underscore11code.ccord.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;

public final class CarbonCoreBootstrap {
  private static final Logger logger = LoggerFactory.getLogger(CarbonCoreBootstrap.class);

  private CarbonCoreBootstrap() {
  }

  @SuppressWarnings({"dereference.of.nullable", "argument.type.incompatible"})
  public static void main(final String[] args) {
    logger.info("Bootstrapping CarbonCord Core");

    // https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
    try {
      new CarbonCore(new File(CarbonCoreBootstrap.class.getProtectionDomain().getCodeSource().getLocation()
        .toURI()).getParentFile());
    } catch (final URISyntaxException e) {
      logger.error("FATAL: Error while starting up, should be impossible?", e);
    }
  }
}
