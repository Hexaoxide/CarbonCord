package io.github.underscore11code.ccord.messaging.packets;

import io.github.underscore11code.ccord.messaging.Packet;

import java.util.UUID;

public class PingPacket extends Packet {
  public PingPacket(final UUID origin, final UUID packetID) {
    super(origin, packetID);
  }

  public PingPacket() {
  }
}
