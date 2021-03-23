package io.github.underscore11code.ccord.messaging.packets;

import io.github.underscore11code.ccord.messaging.Packet;

import java.util.UUID;

public class ClientConnectPacket extends Packet {
  private final String serverName;

  public ClientConnectPacket(final UUID origin, final UUID packetID, final String serverName) {
    super(origin, packetID);
    this.serverName = serverName;
  }

  public ClientConnectPacket(final String serverName) {
    this.serverName = serverName;
  }

  public String serverName() {
    return this.serverName;
  }
}
