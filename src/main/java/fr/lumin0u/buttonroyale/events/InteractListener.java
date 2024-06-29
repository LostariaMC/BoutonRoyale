package fr.lumin0u.buttonroyale.events;

import fr.lumin0u.buttonroyale.BRPlayer;
import fr.lumin0u.buttonroyale.BoutonRoyale;
import fr.lumin0u.buttonroyale.GameManager;
import fr.lumin0u.buttonroyale.util.Sounds;
import fr.lumin0u.buttonroyale.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Switch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class InteractListener implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		BRPlayer player = BRPlayer.of(event.getPlayer());
		
		GameManager gm = BoutonRoyale.getInstance().getGameManager();
		
		if(gm.getPhase() == GameManager.GamePhase.FIGHT) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && Utils.BUTTONS.contains(event.getClickedBlock().getType())) {
				Optional<BRPlayer> optTarget = gm.getNonSpecPlayers().stream()
						.filter(pl -> pl.getButton() != null && pl.getButton().block().equals(event.getClickedBlock()))
						.findFirst();
				
				if(optTarget.isPresent()) {
					BRPlayer target = optTarget.get();
					
					player.activateButtonOf(target);
				}
				else {
					player.activateFakeButton(event.getClickedBlock());
				}
			}
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlaceBlock(BlockPlaceEvent event) {
		BRPlayer player = BRPlayer.of(event.getPlayer());
		
		GameManager gm = BoutonRoyale.getInstance().getGameManager();
		
		if(gm.getPhase() == GameManager.GamePhase.PLACE_BUTTON) {
			
			Optional<BRPlayer> optPlaceHolder = gm.getNonSpecPlayers().stream()
					.filter(pl -> pl.getButton() != null && pl.getButton().block().equals(event.getBlock()))
					.findFirst();
			
			if(optPlaceHolder.isPresent()) {
				// TODO I18N
				player.sendMessage(BoutonRoyale.getPrefix() + "§cQuelqu'un a §ldéjà §cposé son bouton ici...");
				Sounds.ALREADY_A_BUTTON.playTo(player);
			}
			else {
				// TODO I18N
				if(player.getButton() == null)
					player.sendMessage(BoutonRoyale.getPrefix() + "§aBouton posé ! §eVous pouvez toujours le poser §lautre part§e.");
				else
					player.toBukkit().sendBlockChange(player.getButton().block().getLocation(), Material.AIR.createBlockData());
				
				BlockFace face = event.getBlock().getFace(event.getBlockAgainst());
				player.setButton(new BRPlayer.ButtonState(event.getBlock(), face, event.getBlockPlaced().getType()));
				
				Switch buttonData = (Switch) player.getButton().type().createBlockData();
				buttonData.setFacing(face);
				
				Bukkit.getScheduler().runTaskLater(BoutonRoyale.getInstance(), () ->
				{
					player.toBukkit().sendBlockChange(event.getBlock().getLocation(), buttonData);
				}, 1);
			}
			
			event.setCancelled(true);
		}
	}
}
