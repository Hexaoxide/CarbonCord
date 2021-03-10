package io.github.underscore11code.ccord.messaging;

import net.kyori.event.EventBus;
import net.kyori.event.SimpleEventBus;

public abstract class AbstractMessagingService implements MessageService {
  private final EventBus<Packet> eventBus;

  public AbstractMessagingService(final EventBus<Packet> eventBus) {
    this.eventBus = eventBus;
  }

  public AbstractMessagingService() {
    this.eventBus = new SimpleEventBus<>(Packet.class);
  }

  @Override
  public EventBus<Packet> bus() {
    return this.eventBus;
  }

  public static class Simple extends AbstractMessagingService {

    @Override
    public void connect() {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void send(final Packet packet) {
      this.bus().post(packet);
    }
  }
}
