package io.github.underscore11code.ccord.messaging.packets;

import io.github.underscore11code.ccord.messaging.ReplyPacket;

public class PingResponsePacket extends ReplyPacket {

  public PingResponsePacket(final PingPacket respondingTo) {
    super(respondingTo);
  }
}
