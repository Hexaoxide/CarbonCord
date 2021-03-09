package io.github.underscore11code.ccord.core;

import io.github.underscore11code.ccord.messaging.RedisMessagingService;
import io.github.underscore11code.ccord.messaging.packets.PingPacket;
import io.lettuce.core.RedisURI;

import java.util.UUID;

public final class CarbonCore {
  public static void main(final String[] args) {
    System.out.println("hello");
    final RedisMessagingService localhost = new RedisMessagingService(RedisURI.builder().withHost("localhost").build());
    try {
      localhost.connect();
    } catch (final Exception e) {
      e.printStackTrace();
    }

    localhost.send(new PingPacket(UUID.randomUUID(), UUID.randomUUID()));
  }

  private CarbonCore() {
  }
}
