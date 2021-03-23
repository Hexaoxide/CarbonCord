package io.github.underscore11code.ccord.core.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class CoreChatChannelConfig {
  @Setting
  @Comment("Server this chat channel should listen/respond to. Leave blank for all servers.")
  private String serverName = "";

  @Setting
  @Comment("Minecraft channel this chat channel should listen/respond to. Leave blank for all channels.")
  private String minecraftChannelName = "";

  @Setting
  @Comment("Discord channel this chat channel should listen/respond to.")
  private String discordChannelId = "";

  @Setting
  @Comment("Format for messages from Minecraft -> Discord. Leave blank to disable Minecraft -> Discord in this channel.")
  private String minecraftToDiscordFormat = "";

  @Setting
  @Comment("Format for messages from Discord -> Minecraft. Leave blank to disable Discord -> Minecraft in this channel.")
  private String discordToMinecraftFormat = "";

  public String serverName() {
    return this.serverName;
  }

  public void serverName(final String serverName) {
    this.serverName = serverName;
  }

  public String minecraftChannelName() {
    return this.minecraftChannelName;
  }

  public void minecraftChannelName(final String minecraftChannelName) {
    this.minecraftChannelName = minecraftChannelName;
  }

  public String discordChannelId() {
    return this.discordChannelId;
  }

  public void discordChannelId(final String discordChannelId) {
    this.discordChannelId = discordChannelId;
  }

  public String minecraftToDiscordFormat() {
    return this.minecraftToDiscordFormat;
  }

  public void minecraftToDiscordFormat(final String minecraftToDiscordFormat) {
    this.minecraftToDiscordFormat = minecraftToDiscordFormat;
  }

  public String discordToMinecraftFormat() {
    return this.discordToMinecraftFormat;
  }

  public void discordToMinecraftFormat(final String discordToMinecraftFormat) {
    this.discordToMinecraftFormat = discordToMinecraftFormat;
  }
}
