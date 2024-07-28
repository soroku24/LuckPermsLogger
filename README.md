# LuckPermsLogger Plugin

LuckPermsLogger is a Minecraft Velocity plugin designed to log LuckPerms events and send these logs to a Discord channel using a bot. This plugin depends on the DiscordVelocityPlus plugin for its Discord integration.

## Features

- Sends LuckPerms logs to a specified Discord channel using a Discord bot.
- Configurable via in-game commands.
- Toggle logging on or off as needed.

## Permission

- `luckpermslogger.all`
  Gives all access to all luckpermslogger command.

## Commands

- `/lpl reload`  
  Reloads the plugin configuration.

- `/lpl set channel <channelId>`  
  Sets the Discord channel ID where logs will be sent.

- `/lpl set bot <botName>`  
  Sets the name of the Discord bot used for logging.

- `/lpl toggle`  
  Toggles whether the bot will send logs to Discord or not.

## Dependencies

 Ensure you have this plugin installed and configured correctly

- [SyncSecurePlus](https://github.com/soroku24/SyncSecurePlus) for Discord communication.
- [LuckPerms](https://luckperms.net/download) for Listening luckperms events (use velocity version)

## Installation

1. Download the LuckPermsLogger plugin jar file.
2. Place the jar file in the `plugins` directory of your Minecraft server.
3. Ensure you have DiscordVelocityPlus installed and configured.
4. Start your server to generate the default configuration files for LuckPermsLogger.
5. Configure the plugin using the generated configuration files and the provided commands.

## Configuration

Below is an example configuration for LuckPermsLogger:

```yaml
enabled: false # Set to true to enable the feature

discord:
  bot_name: "bot_name" # The name of the bot as set in the Sync Secure Plus configuration
  channel_id: 123 # The ID of the Discord channel

```


## This plugin is powered by Yohhan Development.

### For inquiries and support, feel free to join our Discord. We also offer illustration and graphic design services. Just open a ticket! [https://discord.gg/A9jgs4SvkD](https://discord.gg/A9jgs4SvkD)


#

Unauthorized copying, modification, distribution, in whole or in part, is strictly prohibited.

Any alteration, adaptation, or derivation of the plugin’s code is forbidden. Users must not reverse engineer or attempt to decompile the plugin in any form.

We reserve the right to modify these terms and conditions at any time. By continuing to use this plugin, you agree to comply with the most current version of these terms.


