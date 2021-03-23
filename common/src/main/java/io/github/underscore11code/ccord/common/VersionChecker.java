package io.github.underscore11code.ccord.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VersionChecker {
  private static final Logger logger = LoggerFactory.getLogger(VersionChecker.class);

  private VersionChecker() {
  }

  private static int jvmVersion() {
    final String javaVersion = System.getProperty("java.version");
    final Matcher matcher = Pattern.compile("(?:1\\.)?(\\d+)").matcher(javaVersion);
    if (!matcher.find()) {
      logger.error("Could not find java version in {}", javaVersion);
      return -1;
    }

    final String version = matcher.group(1);
    if (version == null) {
      throw new IllegalStateException("This should be impossible.");
    }
    try {
      return Integer.parseInt(version);
    } catch (final NumberFormatException e) {
      logger.error("Could not parse java version from {} (From {})", version, javaVersion);
      return -1;
    }
  }

  public static boolean checkJvmVersion() {
    final int ver = jvmVersion();
    if (ver == -1) return false;
    if (ver >= 11) return true;

    logger.error("");
    logger.error("*****************************************");
    logger.error("* YOU ARE USING AN INCOMPATIBLE VERSION *");
    logger.error("* OF JAVA FOR CARBONCORD!               *");
    logger.error("*                                       *");
    logger.error("* CarbonCord requires at least Java 11. *");
    logger.error("* However, you are using Java {}.        *", ver);
    logger.error("*                                       *");
    logger.error("* If you are hosting your server        *");
    logger.error("* yourself, please update Java now.     *");
    logger.error("* If you are using a hosting company,   *");
    logger.error("* please contact them to get Java       *");
    logger.error("* updated.                              *");
    logger.error("*****************************************");
    logger.error("");
    return false;
  }
}
