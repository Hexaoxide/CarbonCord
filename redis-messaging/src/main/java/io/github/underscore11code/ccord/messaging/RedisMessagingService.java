package io.github.underscore11code.ccord.messaging;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.github.underscore11code.ccord.messaging.packets.PingPacket;
import io.github.underscore11code.ccord.messaging.packets.PingResponsePacket;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public final class RedisMessagingService extends AbstractMessagingService {
  private static final Logger logger = LoggerFactory.getLogger(RedisMessagingService.class);
  private static final Gson gson = new Gson();
  private static final String CHANNEL_NAME = "carboncord";
  // Despite JDocs saying handler is nullable, it's not annotated *flips table*
  @SuppressWarnings("argument.type.incompatible")
  private final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), pool1 -> {
    final ForkJoinWorkerThread newThread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool1);
    newThread.setName("CCord-RedisPool-" + newThread.getPoolIndex());
    return newThread;
  }, null, false);

  private final RedisClient client;
  private StatefulRedisConnection<String, String> send;
  private RedisPubSubCommands<String, String> receive;

  // Understandably not happy about not initializing the connections, but we don't do that in the constructor.
  @SuppressWarnings("initialization.fields.uninitialized")
  public RedisMessagingService(final RedisURI uri) {
    this.client = RedisClient.create(uri);
  }

  @Override
  public void connect() throws Exception {
    logger.info("Connecting to Redis...");
    final StatefulRedisPubSubConnection<String, String> pubSub = this.client.connectPubSub();
    this.receive = pubSub.sync();
    this.receive.subscribe(CHANNEL_NAME);
    this.send = this.client.connect();
    logger.info("Connected!");

    pubSub.addListener((Listener) (channel, message) -> this.pool.submit(() -> {
      final String[] split = message.split(" ", 2);
      if (!(split.length == 2))
        throw new PacketDecodeException("Illegal split length for message \"" + message + "\" in channel " + channel + ":" + split.length);

      final Class<?> clazz;
      try {
        clazz = Class.forName(split[0]);
        final Object o = gson.fromJson(split[1], clazz);
        if (o == null) {
          throw new PacketDecodeException("Null packet from " + message);
        }
        bus().post((Packet) o);
      } catch (final ClassNotFoundException e) {
        throw new PacketDecodeException("Could not find Packet Class " + split[0], e);
      } catch (final JsonSyntaxException | ClassCastException e) {
        throw new PacketDecodeException(e);
      }
    }));

    this.bus().register(PingPacket.class, packet -> {
      if (!packet.isFromThis()) this.send(new PingResponsePacket(packet));
    });

    this.bus().register(Packet.class, packet -> {
      logger.debug("Packet in: {} (type {})", packet, packet.getClass().getName());
    });
  }

  @Override
  public void disconnect() {
    this.send.close();
    this.receive.getStatefulConnection().close();
    this.client.shutdown();
    this.pool.shutdown();
  }

  @Override
  public void send(final Packet packet) {
    this.pool.submit(() -> {
      logger.debug("Packet out: {} (type {})", packet, packet.getClass().getName());
      final String encoded = packet.getClass().getName() + " " + gson.toJson(packet);
      this.send.sync().publish(CHANNEL_NAME, encoded);
    });
  }

  @FunctionalInterface
  private interface Listener extends RedisPubSubListener<String, String> {
    @Override
    void message(String channel, String message);

    @Override
    default void message(String pattern, String channel, String message) {
    }

    @Override
    default void subscribed(String channel, long count) {
    }

    @Override
    default void psubscribed(String pattern, long count) {
    }

    @Override
    default void unsubscribed(String channel, long count) {
    }

    @Override
    default void punsubscribed(String pattern, long count) {
    }
  }

  public static class PacketDecodeException extends RuntimeException {
    public PacketDecodeException() {
      super();
    }

    public PacketDecodeException(final String message) {
      super(message);
    }

    public PacketDecodeException(final String message, final Throwable cause) {
      super(message, cause);
    }

    public PacketDecodeException(final Throwable cause) {
      super(cause);
    }
  }
}
