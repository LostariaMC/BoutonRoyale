package fr.lumin0u.buttonroyale.events;

import fr.lumin0u.buttonroyale.BoutonRoyale;
import fr.worsewarn.cosmox.API;
import fr.worsewarn.cosmox.api.players.WrappedPlayer;
import fr.worsewarn.cosmox.game.events.GameStartEvent;
import fr.worsewarn.cosmox.game.events.GameStopEvent;
import fr.worsewarn.cosmox.game.events.PlayerJoinGameEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class CosmoxListener implements Listener
{
	private boolean gameStarted;

	@EventHandler
	public void onPlayerJoin(PlayerJoinGameEvent event) {

		if(API.instance().getManager().getPhase().getState() == 0) return;

		Player player = event.getPlayer();
		player.setGameMode(GameMode.SPECTATOR);
		if(Bukkit.getOnlinePlayers().size()>1) player.teleport(Bukkit.getOnlinePlayers().stream().filter(all -> all != player).toList().get(0));

		BoutonRoyale.getInstance().getGameManager().resetScoreboard(WrappedPlayer.of(player));
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent event)
	{
		if(event.getGame().equals(BoutonRoyale.getGame()))
		{
			gameStarted = true;
			
			BoutonRoyale.getInstance().getGameManager().onCosmoxStart(event.getMap());
			
			BoutonRoyale.getInstance().getServer().getPluginManager().registerEvents(new InteractListener(), BoutonRoyale.getInstance());
		}
	}
	
	@EventHandler
	public void onGameStop(GameStopEvent event) {
		
		if(gameStarted)
		{
			gameStarted = false;
			
			HandlerList.unregisterAll(BoutonRoyale.getInstance()); //Tous les évènements ne sont plus écoutés
			
			Bukkit.getScheduler().cancelTasks(BoutonRoyale.getInstance()); // arret de toutes les taches programmées du plugin
			
			BoutonRoyale.getInstance().reset();
			
			Bukkit.getPluginManager().registerEvents(this, BoutonRoyale.getInstance());
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(!BoutonRoyale.getInstance().getGameManager().isStarted() && event.hasChangedPosition()) {
			event.setCancelled(true);
		}
	}
}
