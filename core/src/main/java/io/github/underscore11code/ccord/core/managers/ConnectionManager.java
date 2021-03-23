package io.github.underscore11code.ccord.core.managers;

import io.github.underscore11code.ccord.core.CarbonCore;
import io.github.underscore11code.ccord.core.entities.RemoteClient;
import io.github.underscore11code.ccord.messaging.Packet;
import io.github.underscore11code.ccord.messaging.packets.ClientConnectPacket;
import io.github.underscore11code.ccord.messaging.packets.PingPacket;
import io.github.underscore11code.ccord.messaging.packets.PingResponsePacket;
import net.kyori.event.EventSubscriber;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ConnectionManager {
  private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
  private static final int HEARTBEAT_RATE = 5;
  private static final int TIMEOUT_RATE = 15000;
  private final CarbonCore carbonCore = CarbonCore.instance();
  private final Map<UUID, RemoteClient> clients = new HashMap<>();
  private final Map<String, UUID> clientNameCache = new HashMap<>();

  @SuppressWarnings("method.invocation.invalid")
  public ConnectionManager() {
    this.register(PingPacket.class, p -> {
      if (!p.isFromThis()) this.send(new PingResponsePacket(p));
    });

    // Connection Listener
    this.register(ClientConnectPacket.class, p -> {
      this.clients.put(p.serverId(), new RemoteClient(p.serverId(), p.serverName()));
      this.clientNameCache.put(p.serverName(), p.serverId());
      logger.info("Client Connected: {}", p.serverName());
    });

    // Heartbeat Listener
    this.register(Packet.class, p -> {
      if (p.isFromThis() || p instanceof ClientConnectPacket) return;
      final RemoteClient remoteClient = this.forId(p.serverId());
      if (remoteClient == null) {
        logger.warn("Got packet from unknown client id {}", p.serverId());
        return;
      }
      remoteClient.lastResponse(System.currentTimeMillis());
    });

    // Heartbeat / Timeout worker
    this.carbonCore.scheduledExecutorService().scheduleAtFixedRate(() -> {
      this.send(new PingPacket());

      for (final Map.Entry<UUID, RemoteClient> entry : this.clients.entrySet()) {
        if (entry.getValue().lastResponse() + TIMEOUT_RATE <= System.currentTimeMillis()) {
          logger.debug("server : {}", entry.getValue().name());
          logger.debug("current: {}", System.currentTimeMillis());
          logger.debug("last   : {}", entry.getValue().lastResponse());
          logger.debug("timeout: {}", entry.getValue().lastResponse() + TIMEOUT_RATE);
          logger.info("Client Disconnected: {} re: Stopped Responding", entry.getValue().name());
          this.clients.remove(entry.getKey());
        }
      }
    }, HEARTBEAT_RATE, HEARTBEAT_RATE, TimeUnit.SECONDS);

    // Ping Responder
    this.register(PingPacket.class, p -> {
      if (p.isFromThis()) return;
      this.send(new PingResponsePacket(p));
    });
  }

  public @Nullable RemoteClient forId(final UUID uuid) {
    return this.clients.get(uuid);
  }

  public @Nullable RemoteClient forName(final String name) {
    final UUID uuid = this.clientNameCache.get(name);
    if (uuid == null) return null;
    return this.forId(uuid);
  }

  private void send(final Packet packet) {
    this.carbonCore.messageService().send(packet);
  }

  private <P extends Packet> void register(final Class<P> packetClass, final EventSubscriber<P> eventSubscriber) {
    this.carbonCore.messageService().bus().register(packetClass, eventSubscriber);
  }

  private <P extends Packet> void register(final Class<P> packetClass,
                                           final Consumer<P> eventSubscriber,
                                           final int postOrder,
                                           final boolean consumeCanceled) {
    this.carbonCore.messageService().bus().register(packetClass, new EventSubscriber<P>() {
      @Override
      public void invoke(final @NonNull P p) {
        eventSubscriber.accept(p);
      }

      @Override
      public int postOrder() {
        return postOrder;
      }

      @Override
      public boolean consumeCancelledEvents() {
        return consumeCanceled;
      }
    });
  }
}
