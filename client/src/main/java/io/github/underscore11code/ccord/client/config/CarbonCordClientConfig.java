package io.github.underscore11code.ccord.client.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class CarbonCordClientConfig {
  private String serverName = "server";

  private ClientMessagingConfig messaging = new ClientMessagingConfig();

  public String serverName() {
    return this.serverName;
  }

  public void serverName(final String serverName) {
    this.serverName = serverName;
  }

  public ClientMessagingConfig messaging() {
    return this.messaging;
  }

  public void messaging(final ClientMessagingConfig messaging) {
    this.messaging = messaging;
  }
}
