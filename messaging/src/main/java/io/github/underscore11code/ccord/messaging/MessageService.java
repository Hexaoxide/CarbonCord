package io.github.underscore11code.ccord.messaging;

import net.kyori.event.EventBus;

import java.util.UUID;

public interface MessageService {
  UUID SERVER_ID = UUID.randomUUID();

  void connect() throws Exception;

  void disconnect();

  void send(Packet packet);

  EventBus<Packet> bus();

  static MessageService simple() {
    return new AbstractMessagingService.Simple();
  }
}
