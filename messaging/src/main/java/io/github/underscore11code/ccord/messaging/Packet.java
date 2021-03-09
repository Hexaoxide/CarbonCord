package io.github.underscore11code.ccord.messaging;

import java.util.UUID;

public abstract class Packet {
  private final UUID origin;
  private final UUID packetID;

  public Packet(final UUID origin, final UUID packetID) {
    this.origin = origin;
    this.packetID = packetID;
  }

  public Packet() {
    this.origin = MessageService.SERVER_ID;
    this.packetID = UUID.randomUUID();
  }

  public UUID origin() {
    return this.origin;
  }

  public UUID packetID() {
    return this.packetID;
  }

  /**
   * @return True if this packet is from this server, false if not
   */
  public boolean isFromThis() {
    return this.origin.equals(MessageService.SERVER_ID);
  }
}
