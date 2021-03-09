package io.github.underscore11code.ccord.core;

import io.github.underscore11code.ccord.messaging.Packet;
import io.github.underscore11code.ccord.messaging.RedisMessagingService;
import io.lettuce.core.RedisURI;

public final class CarbonCore {
  public static void main(final String[] args) {
    System.out.println("hello");
    final RedisMessagingService localhost = new RedisMessagingService(RedisURI.builder().withHost("localhost").build());
    try {
      localhost.connect();
    } catch (final Exception e) {
      e.printStackTrace();
    }
    localhost.bus().register(Packet.class, event -> {
      System.out.println(event.packetID());
    });

    localhost.send(new PacketTest());
  }

  private CarbonCore() {
  }

  public static class PacketTest extends Packet {
  }
}
