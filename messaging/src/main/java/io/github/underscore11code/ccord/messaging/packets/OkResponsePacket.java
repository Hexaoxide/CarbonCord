package io.github.underscore11code.ccord.messaging.packets;

import io.github.underscore11code.ccord.messaging.Packet;
import io.github.underscore11code.ccord.messaging.ReplyPacket;

import java.util.UUID;

public class OkResponsePacket extends ReplyPacket {
  public OkResponsePacket(final UUID origin, final UUID packetID, final UUID respondingToServer, final UUID respondingToPacketId) {
    super(origin, packetID, respondingToServer, respondingToPacketId);
  }

  public OkResponsePacket(final UUID respondingToServer, final UUID respondingToPacketId) {
    super(respondingToServer, respondingToPacketId);
  }

  public OkResponsePacket(final Packet respondingTo) {
    super(respondingTo);
  }
}
