package ptl.ajneb97.configs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import ptl.ajneb97.PlayerTimeLimit;
import ptl.ajneb97.model.TimeLimitPlayer;

public class PlayerConfigsManager {

	private final PlayerTimeLimit plugin;
	private final List<PlayerConfig> configPlayers;
	
	public PlayerConfigsManager(PlayerTimeLimit plugin) {
		this.plugin = plugin;
		this.configPlayers = new ArrayList<>();
	}
	
	public void configurar() {
		createPlayersFolder();
		registerPlayers();
	}
	
	public void createPlayersFolder(){
		File folder;
        try {
            folder = new File(plugin.getDataFolder() + File.separator + "players");
            if(!folder.exists()){
                folder.mkdirs();
            }
        } catch(SecurityException ignored) {
		}
	}
	
	public void savePlayers() {
		for (PlayerConfig configPlayer : configPlayers) {
			configPlayer.savePlayerConfig();
		}
	}
	
	public void registerPlayers(){
		String path = plugin.getDataFolder() + File.separator + "players";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (File listOfFile : listOfFiles) {
			if (listOfFile.isFile()) {
				String pathName = listOfFile.getName();
				PlayerConfig config = new PlayerConfig(pathName, plugin);
				config.registerPlayerConfig();
				configPlayers.add(config);
			}
		}
	}
	
	public List<PlayerConfig> getConfigPlayers(){
		return this.configPlayers;
	}
	
	public boolean archivoYaRegistrado(String pathName) {
		for (PlayerConfig configPlayer : configPlayers) {
			if (configPlayer.getPath().equals(pathName)) {
				return true;
			}
		}
		return false;
	}
	
	public PlayerConfig getPlayerConfig(String pathName) {
		for (PlayerConfig configPlayer : configPlayers) {
			if (configPlayer.getPath().equals(pathName)) {
				return configPlayer;
			}
		}
		return null;
	}
	
	public List<PlayerConfig> getPlayerConfigs() {
		return this.configPlayers;
	}
	
	public boolean registerPlayer(String pathName) {
		if(!archivoYaRegistrado(pathName)) {
			PlayerConfig config = new PlayerConfig(pathName,plugin);
	        config.registerPlayerConfig();
	        configPlayers.add(config);
	        return true;
		}else {
			return false;
		}
	}
	
	public void removerConfigPlayer(String path) {
		for(int i=0;i<configPlayers.size();i++) {
			if(configPlayers.get(i).getPath().equals(path)) {
				configPlayers.remove(i);
			}
		}
	}
	
	public void cargarJugadores() {
		List<TimeLimitPlayer> jugadores = new ArrayList<>();
		
		for(PlayerConfig playerConfig : configPlayers) {
			FileConfiguration players = playerConfig.getConfig();
			String name = players.getString("name");
			String uuid = playerConfig.getPath().replace(".yml", "");
			
			TimeLimitPlayer p = new TimeLimitPlayer(uuid,name);
			
			p.setCurrentTime(players.getInt("current_time"));
			p.setTotalTime(players.getInt("total_time"));
			p.setMessageEnabled(players.getBoolean("messages"));
			
			jugadores.add(p);
		}
		plugin.getPlayerManager().setPlayers(jugadores);
	}
	
	public void guardarJugadores() {
		for(TimeLimitPlayer player : plugin.getPlayerManager().getPlayers()) {
			String jugador = player.getName();
			PlayerConfig playerConfig = getPlayerConfig(player.getUuid()+".yml");
			if(playerConfig == null) {
				registerPlayer(player.getUuid()+".yml");
				playerConfig = getPlayerConfig(player.getUuid()+".yml");
			}
			FileConfiguration players = playerConfig.getConfig();
			
			players.set("name", jugador);
			players.set("current_time", player.getCurrentTime());
			players.set("total_time", player.getTotalTime());
			players.set("messages", player.isMessageEnabled());
		}
		savePlayers();
	}
}
