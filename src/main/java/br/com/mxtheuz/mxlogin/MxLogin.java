package br.com.mxtheuz.mxlogin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MxLogin extends JavaPlugin implements Listener {
    private Map<UUID, String> registeredPlayers = new HashMap<>();
    private Map<UUID, Boolean> loggedInPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        loadRegisteredPlayers();
        getLogger().info("[mx-login] Sistema de Login esta funcionando!");
    }

    @Override
    public void onDisable() {
        saveRegisteredPlayers();
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event){
        getLogger().info("Player join;");
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        player.setGameMode(GameMode.ADVENTURE);

        if (!registeredPlayers.containsKey(playerUUID)) {
            player.sendMessage("Voce nao esta registrado. Use /register <senha> <confirmar_senha> para se registrar.");
        } else if (!loggedInPlayers.containsKey(playerUUID)) {
            player.sendMessage("Voce precisa fazer login usando /login <senha>.");
        }
    }

    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playetUUID = player.getUniqueId();
        if(!loggedInPlayers.containsKey(playetUUID)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        loggedInPlayers.remove(playerUUID); // Remover jogador do mapa de jogadores logados ao sair do mundo
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("register")) {
            if (args.length < 2) {
                sender.sendMessage("Uso: /register <senha> <confirmar_senha>");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("Este comando so pode ser usado por jogadores.");
                return true;
            }

            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if (registeredPlayers.containsKey(playerUUID)) {
                sender.sendMessage("Você ja esta registrado.");
                return true;
            }

            String password = args[0];
            String confirmPassword = args[1];

            if (!password.equals(confirmPassword)) {
                sender.sendMessage("As senhas nao coincidem.");
                return true;
            }

            registeredPlayers.put(playerUUID, password);
            ((Player) sender).getPlayer().setGameMode(GameMode.CREATIVE);
            sender.sendMessage("Registro efetuado com sucesso.");
            return true;
        } else if (command.getName().equalsIgnoreCase("login")) {
            if (args.length < 1) {
                sender.sendMessage("Uso: /login <senha>");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("Este comando so pode ser usado por jogadores.");
                return true;
            }

            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if (!registeredPlayers.containsKey(playerUUID)) {
                sender.sendMessage("Voce nao esta registrado. Use /register <senha> <confirmar_senha> para se registrar.");
                return true;
            }

            if (loggedInPlayers.containsKey(playerUUID)) {
                sender.sendMessage("Você já está logado.");
                return true;
            }

            String password = args[0];
            String registeredPassword = registeredPlayers.get(playerUUID);

            if (!password.equals(registeredPassword)) {
                sender.sendMessage("Senha incorreta. Tente novamente.");
                return true;
            }

            loggedInPlayers.put(playerUUID, true);

            ((Player) sender).getPlayer().setGameMode(GameMode.CREATIVE);
            sender.sendMessage("Login efetuado com sucesso. Voce agora esta livre para se mover.");
            return true;
        }

        return false;
    }



    private void loadRegisteredPlayers() {
        File dataFile = new File("./logins.json");

        if (!dataFile.exists()) {
            try {
                // Cria um novo arquivo vazio se ele não existir
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (FileReader reader = new FileReader(dataFile)) {
            Gson gson = new Gson();
            registeredPlayers = gson.fromJson(reader, new TypeToken<Map<UUID, String>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveRegisteredPlayers() {
        File dataFile = new File("./logins.json");

        try (FileWriter writer = new FileWriter(dataFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(registeredPlayers, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
