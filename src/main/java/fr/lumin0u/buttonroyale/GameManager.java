package fr.lumin0u.buttonroyale;

import fr.lumin0u.buttonroyale.util.I18n;
import fr.lumin0u.buttonroyale.util.ItemBuilder;
import fr.lumin0u.buttonroyale.util.Utils;
import fr.worsewarn.cosmox.API;
import fr.worsewarn.cosmox.api.players.WrappedPlayer;
import fr.worsewarn.cosmox.api.scoreboard.CosmoxScoreboard;
import fr.worsewarn.cosmox.game.Phase;
import fr.worsewarn.cosmox.tools.chat.MessageBuilder;
import fr.worsewarn.cosmox.tools.map.GameMap;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static java.util.function.Predicate.not;

public class GameManager {
	
	private static final RoundType[] ROUND_TYPES = new RoundType[] {RoundType.CLASSIC, RoundType.CLASSIC, RoundType.PVP, RoundType.PVP};
	private static final int ROUND_MAX = ROUND_TYPES.length;
	
	private final BoutonRoyale plugin;
	private Map<UUID, BRPlayer> players = new HashMap<>();
	private boolean started;
	private int time;
	private GameMap map;
	private Location spawnpoint;
	private final BossBar bossBar;
	
	private int round;
	private GamePhase phase;
	
	final int gameDuration = 2 * 60 * 20;
	
	public GameManager(BoutonRoyale plugin) {
		this.plugin = plugin;
		this.bossBar = BossBar.bossBar(Component.text(""), 1, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
	}
	
	public BRPlayer getPlayer(UUID uid) {
		players.putIfAbsent(uid, new BRPlayer(uid));
		return players.get(uid);
	}
	
	public Collection<BRPlayer> getNonSpecPlayers() {
		return players.values().stream().filter(not(BRPlayer::isSpectator)).toList();
	}
	
	public Collection<BRPlayer> getOnlinePlayers() {
		return players.values().stream().filter(WrappedPlayer::isOnline).toList();
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void onCosmoxStart(GameMap map) {
		this.map = map;
		
		this.spawnpoint = map.getLocation("spawnpoint");
		this.phase = GamePhase.INTER_PHASE;
		this.round = 0;
		
		for(Player player : Bukkit.getOnlinePlayers())
			getPlayer(player.getUniqueId());
		
		
		for(BRPlayer player : getOnlinePlayers())
		{
			if(!player.isSpectator())
			{
				player.toBukkit().teleport(spawnpoint);
				
				player.toBukkit().getInventory().clear();
				player.toBukkit().setGameMode(GameMode.SURVIVAL);
				
			}
			else {
				player.toBukkit().teleport(spawnpoint);
				
				player.toBukkit().getInventory().clear();
				player.toBukkit().setGameMode(GameMode.SPECTATOR);
			}
		}
		
		
		new BukkitRunnable()
		{
			@Override
			public void run() {
				start();
			}
		}.runTaskLater(plugin, 100);
	}
	
	public void start() {
		
		resetScoreboard();
		
		API.instance().getManager().setPhase(Phase.GAME);
		
		startPhase(GamePhase.PLACE_BUTTON);
		started = true;
	}
	
	public void startPhase(GamePhase phase) {
		this.phase = phase;
		
		switch(phase) {
			case PLACE_BUTTON -> {
				getNonSpecPlayers().forEach(pl -> {
					pl.setButton(null);
					pl.toBukkit().getInventory().clear();
					pl.toBukkit().setGameMode(GameMode.SURVIVAL);
					pl.toBukkit().teleport(spawnpoint);
					
					getNonSpecPlayers().stream()
							.filter(pl::isNot)
							.forEach(other -> other.toBukkit().hidePlayer(plugin, pl.toBukkit()));
					
					// TODO I18N
					pl.toBukkit().sendTitlePart(TitlePart.TITLE, Component.text("§6Cachez votre bouton !"));
					pl.toBukkit().sendTitlePart(TitlePart.SUBTITLE, Component.text("§7Vous avez 1 minute"));
					pl.sendMessage(BoutonRoyale.getPrefix() + "§eVous pouvez §achoisir §ele §abouton §eque vous posez parmi ceux que vous avez à votre disposition.");
					
					Utils.getNButtons(new Random(), 3).forEach(button -> pl.toBukkit().getInventory().addItem(new ItemStack(button)));
				});
				
				bossBar.color(BossBar.Color.GREEN);
				bossBar.name(Component.text("§aReste §en §asecondes"));
				
				new BukkitRunnable() {
					int i = 0;
					final int DURATION = 60 * 20;
					@Override
					public void run() {
						i++;
						if(i == DURATION) {
							startPhase(GamePhase.FIGHT);
							cancel();
							return;
						}
						
						bossBar.progress((DURATION - i) / (float) DURATION);
						bossBar.name(Component.text("§aIl reste §e%d §asecondes".formatted((DURATION - i) / 20)));
					}
				}.runTaskTimer(plugin, 1, 1);
			}
			case FIGHT -> {
				getNonSpecPlayers().forEach(pl -> {
					pl.toBukkit().teleport(spawnpoint);
					pl.toBukkit().getInventory().clear();
					pl.toBukkit().setGameMode(GameMode.ADVENTURE);
					
					getNonSpecPlayers().stream()
							.filter(pl::isNot)
							.forEach(other -> other.toBukkit().showPlayer(plugin, pl.toBukkit()));
					
					if(getRoundType() == RoundType.PVP) {
						pl.toBukkit().getInventory().addItem(new ItemBuilder(Material.STONE_SWORD)
								.addEnchant(Enchantment.DAMAGE_ALL, 0)
								.build());
					}
				});
			}
			case INTER_PHASE -> {
			
			}
		}
	}
	
	public RoundType getRoundType() {
		return ROUND_TYPES[round];
	}
	
	public GamePhase getPhase() {
		return phase;
	}
	
	public int getRound() {
		return round;
	}
	
	public Location getSpawnpoint() {
		return spawnpoint;
	}
	
	/* *****  START OF SCOREBOARD STUFF ***** */
	
	public void updateScoreboardTime() {
		
		for(WrappedPlayer watcher : WrappedPlayer.of(Bukkit.getOnlinePlayers()))
		{
			String s = new MessageBuilder(I18n.interpretable("scoreboard_time_remaining")).formatted((gameDuration - time) / 20 / 60 + ":" + String.format("%02d", ((gameDuration - time) / 20) % 60)).toString(watcher);
			watcher.toCosmox().getScoreboard().updateLine(1, s);
		}
	}
	
	public void updateScoreboardRound() {
		for(WrappedPlayer watcher : WrappedPlayer.of(Bukkit.getOnlinePlayers())) {
			CosmoxScoreboard scoreboard = watcher.toCosmox().getScoreboard();
			// TODO I18N
			scoreboard.updateLine(1, "§7§lManche §f%d".formatted(round + 1));
		}
	}
	
	private void resetScoreboard() {
		for(WrappedPlayer watcher : WrappedPlayer.of(Bukkit.getOnlinePlayers())) {
			resetScoreboard(watcher);
		}
		
		updateScoreboardTime();
		updateScoreboardRound();
	}
	
	public void resetScoreboard(WrappedPlayer watcher) {
		
		CosmoxScoreboard scoreboard = new CosmoxScoreboard(watcher.toBukkit());
		
		scoreboard.updateTitle("§f§lBOUTON ROYALE");
		scoreboard.updateLine(0, "§0");
		scoreboard.updateLine(1, "§7§lManche 1");
		scoreboard.updateLine(2, "§1");
		
		
		watcher.toCosmox().setScoreboard(scoreboard);
		watcher.toBukkit().showBossBar(bossBar);
	}
	
	/* *****  END OF SCOREBOARD STUFF ***** */
	
	public enum RoundType {
		/** All players are invisible, they can't hit each other */
		CLASSIC,
		
		/** All players are visible, starting a fight removes half of your life */
		PVP;
	}
	
	public enum GamePhase {
		PLACE_BUTTON("Placez votre Bouton", ""),
		FIGHT("Trouvez les Boutons !", ""),
		INTER_PHASE("Fin de phase", "");
		
		public final String title;
		public final String desc;
		
		GamePhase(String title, String desc) {
			this.title = title;
			this.desc = desc;
		}
	}
}
