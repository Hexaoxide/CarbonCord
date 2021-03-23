package io.github.underscore11code.ccord.messaging.packets;

import io.github.underscore11code.ccord.messaging.Packet;

import java.util.UUID;

public class RawChatPacket extends Packet {
  private final UUID originPLayer;
  private final String originChannel;
  private final String rawMessage;

  public RawChatPacket(final UUID origin, final UUID packetID, final UUID originPLayer, final String originChannel, final String rawMessage) {
    super(origin, packetID);
    this.originPLayer = originPLayer;
    this.originChannel = originChannel;
    this.rawMessage = rawMessage;
  }

  public RawChatPacket(final UUID originPLayer, final String originChannel, final String rawMessage) {
    this.originPLayer = originPLayer;
    this.originChannel = originChannel;
    this.rawMessage = rawMessage;
  }

  public UUID originPLayer() {
    return this.originPLayer;
  }

  public String originChannel() {
    return this.originChannel;
  }

  public String rawMessage() {
    return this.rawMessage;
  }
}
