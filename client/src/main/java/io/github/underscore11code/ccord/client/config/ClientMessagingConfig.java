package io.github.underscore11code.ccord.client.config;

import io.github.underscore11code.ccord.messaging.RedisConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class ClientMessagingConfig {
  @Setting
  @Comment("Type of message service to use. Currently only redis supported")
  private String serviceType = "redis";

  @Setting
  @Comment("Configuration for the \"redis\" message service")
  private RedisConfig redis = new RedisConfig();

  public String serviceType() {
    return this.serviceType;
  }

  public void serviceType(final String serviceType) {
    this.serviceType = serviceType;
  }

  public RedisConfig redis() {
    return this.redis;
  }

  public void redis(final RedisConfig redis) {
    this.redis = redis;
  }
}
