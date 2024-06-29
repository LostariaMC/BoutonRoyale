package fr.lumin0u.buttonroyale;

import fr.lumin0u.buttonroyale.util.Sounds;
import fr.worsewarn.cosmox.api.players.WrappedPlayer;
import fr.worsewarn.cosmox.game.teams.Team;
import fr.worsewarn.cosmox.tools.chat.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BRPlayer extends WrappedPlayer {
	private static final int BUTTON_KILL_TIME = 63;
	private ButtonState button;
	
	public BRPlayer(UUID uuid) {
		super(uuid);
	}
	
	public boolean isSpectator() {
		return toCosmox().getTeam().equals(Team.SPEC);
	}
	
	public static BRPlayer of(Object player) {
		return WrappedPlayer.of(player).to(BRPlayer.class);
	}
	
	public void setButton(ButtonState button) {
		this.button = button;
	}
	
	public ButtonState getButton() {
		return button;
	}
	
	public void activateButtonOf(BRPlayer other) {
		other.buttonActivatedBy(this);
		
		Sounds.ACTIVATE_BUTTON.playTo(this);
		
		other.toBukkit().showTitle(Title.title(Component.text("§eActivation du bouton"), Component.text(""),
				Title.Times.times(Ticks.duration(2), Ticks.duration(20), Ticks.duration(6))));
	}
	
	private void buttonActivatedBy(BRPlayer other) {
		
		toBukkit().showTitle(Title.title(Component.text("§cBouton trouvé"), Component.text("§6par %s".formatted(other.getName())),
				Title.Times.times(Ticks.duration(2), Ticks.duration(20), Ticks.duration(10))));
		
		toBukkit().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false));
		
		Sounds.MY_BUTTON_FOUND.playTo(this);
		
		new BukkitRunnable() {
			int i = 0;
			
			@Override
			public void run() {
				i++;
				if(i == BUTTON_KILL_TIME) {
					explode(other);
					cancel();
				}
			}
		}.runTaskTimer(BoutonRoyale.getInstance(), 1, 1);
	}
	
	public void activateFakeButton(Block block) {
		Sounds.ACTIVATE_FAKE_BUTTON.playTo(this);
		block.setType(Material.AIR, false);
	}
	
	public void explode(BRPlayer killer) {
		toBukkit().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, toBukkit().getLocation(), 1);
		toBukkit().getWorld().spawnParticle(Particle.LAVA, toBukkit().getLocation(), 1);
		
		kill();
		
		Bukkit.getOnlinePlayers().forEach(pl ->
				WrappedPlayer.of(pl).sendMessage(BoutonRoyale.getPrefix() + Messages.BROADCAST_KILL_LANG, getName(), killer.getName()));
	}
	
	public void kill() {
		toBukkit().setGameMode(GameMode.SPECTATOR);
	}
	
	public record ButtonState(Block block, BlockFace against, Material type) {}
}
