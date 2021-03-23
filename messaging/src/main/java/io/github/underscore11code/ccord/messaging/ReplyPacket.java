package io.github.underscore11code.ccord.messaging;

import java.util.UUID;

public abstract class ReplyPacket extends Packet {
  private final UUID respondingToServer;
  private final UUID respondingToPacketId;

  public ReplyPacket(final UUID origin, final UUID packetID, final UUID respondingToServer, final UUID respondingToPacketId) {
    super(origin, packetID);
    this.respondingToServer = respondingToServer;
    this.respondingToPacketId = respondingToPacketId;
  }

  public ReplyPacket(final UUID respondingToServer, final UUID respondingToPacketId) {
    this.respondingToServer = respondingToServer;
    this.respondingToPacketId = respondingToPacketId;
  }

  public ReplyPacket(final Packet respondingTo) {
    this.respondingToServer = respondingTo.serverId();
    this.respondingToPacketId = respondingTo.packetID();
  }

  public UUID respondingToServer() {
    return this.respondingToServer;
  }

  public UUID respondingToPacket() {
    return this.respondingToPacketId;
  }
}
