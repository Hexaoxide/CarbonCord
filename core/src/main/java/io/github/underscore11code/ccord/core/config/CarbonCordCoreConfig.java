package io.github.underscore11code.ccord.core.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Set;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class CarbonCordCoreConfig {
  @Setting
  @Comment("Your Bot Token | Not sure what this is? See our installation instructions at <link>\n" +
    "!!!         TREAT THIS LIKE A PASSWORD        !!!\n" +
    "!!! WITH THIS, ANYONE CAN SIGN IN AS YOUR BOT !!!\n" +
    "!!!  DO NOT SHARE IT UNDER ANY CIRCUMSTANCES  !!!")
  private String botToken = "BOTTOKEN";

  @Setting
  @Comment("")
  private CoreMessagingConfig messaging = new CoreMessagingConfig();

  @Setting
  @Comment("Chat Channels | Not sure what this is? See our installation instructions at <link>")
  private Set<CoreChatChannelConfig> chatChannels = Set.of(new CoreChatChannelConfig());

  public String botToken() {
    return this.botToken;
  }

  public void botToken(final String botToken) {
    this.botToken = botToken;
  }

  public CoreMessagingConfig messaging() {
    return this.messaging;
  }

  public void messaging(final CoreMessagingConfig messaging) {
    this.messaging = messaging;
  }

  public Set<CoreChatChannelConfig> chatChannels() {
    return this.chatChannels;
  }

  public void chatChannels(final Set<CoreChatChannelConfig> chatChannels) {
    this.chatChannels = chatChannels;
  }
}
