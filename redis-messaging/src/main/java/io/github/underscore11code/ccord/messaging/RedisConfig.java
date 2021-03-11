package io.github.underscore11code.ccord.messaging;

import io.lettuce.core.RedisURI;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class RedisConfig {
  @Setting
  @Comment("Host for the Redis Database.")
  private String host = "localhost";

  @Setting
  @Comment("Port for the Redis Database.")
  private String port = "6379";

  @Setting
  @Comment("Password to the Redis Database. Leave empty for no password.")
  private String password = "";

  public RedisURI toRedisUri() {
    RedisURI.Builder builder = RedisURI.builder().withHost(this.host);

    if (!this.port.equals("")) builder = builder.withPort(Integer.parseInt(this.port));

    // Why does this take both Strings (deprecated) and CharSequences...
    if (!this.password.equals("")) builder = builder.withPassword((CharSequence) this.password);

    return builder.build();
  }

  public String host() {
    return this.host;
  }

  public void host(final String host) {
    this.host = host;
  }

  public String port() {
    return this.port;
  }

  public void port(final String port) {
    this.port = port;
  }

  public String password() {
    return this.password;
  }

  public void password(final String password) {
    this.password = password;
  }
}
