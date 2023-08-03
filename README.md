# Mx Login Plugin

MxLogin is a Bukkit plugin that provides a simple login and registration system for players on a Minecraft server. With MxLogin, players are required to register an account and log in before they can fully interact with the server. This adds an extra layer of security and control over player actions.

## Features

- Player registration and login system.
- Adventure mode set for unregistered players.
- Creative mode set for registered and logged-in players.
- Player movement restriction for unlogged players.
- Data persistence through JSON files.

## How to Install

1. Build the main class with your JDK.
2. Place the `MxLogin.jar` file in the `plugins` folder of your Bukkit/Spigot server.
3. Start or restart your server to enable the MxLogin plugin.

## Commands

- `/register <senha> <confirmar_senha>`: Registers a new account with the given password.
- `/login <senha>`: Logs into the registered account.
- (Note: Replace `<senha>` with the desired password.)

## Usage

1. When a player joins the server, they will be placed in Adventure mode and prompted to register using `/register <senha> <confirmar_senha>`.
2. After registration, the player can log in using `/login <senha>` to switch to Creative mode and gain full access to the server.

## Contributing

Contributions to the MxLogin plugin are welcome! If you encounter any issues or have suggestions for improvements, please open an issue or submit a pull request on the GitHub repository.

## License

This plugin is licensed under the MIT License. You are free to use, modify, and distribute the code as per the terms of the license. See the [LICENSE](./LICENSE) file for more details.

<hr>

Could you follow me? ❤