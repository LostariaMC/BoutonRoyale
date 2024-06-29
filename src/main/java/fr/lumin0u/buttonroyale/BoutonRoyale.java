package fr.lumin0u.buttonroyale;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import fr.lumin0u.buttonroyale.events.CosmoxListener;
import fr.lumin0u.buttonroyale.util.Achievements;
import fr.lumin0u.buttonroyale.util.I18n;
import fr.worsewarn.cosmox.API;
import fr.worsewarn.cosmox.api.players.WrappedPlayer;
import fr.worsewarn.cosmox.api.statistics.Statistic;
import fr.worsewarn.cosmox.game.Game;
import fr.worsewarn.cosmox.game.GameVariables;
import fr.worsewarn.cosmox.tools.map.MapLocation;
import fr.worsewarn.cosmox.tools.map.MapLocationType;
import fr.worsewarn.cosmox.tools.map.MapTemplate;
import fr.worsewarn.cosmox.tools.map.MapType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.List;

public final class BoutonRoyale extends JavaPlugin {
    public static final String GAME_IDENTIFIER = "boutonroyale";
    
    private GameManager gameManager;
    private API api;
    private Game game;
    private static BoutonRoyale instance;
    private ProtocolManager protocolManager;
    
    private static long currentTick;
    
    @Override
    public void onEnable() {
        instance = this;
        
        Bukkit.getScheduler().runTaskTimer(API.instance(), () -> currentTick++, 1, 1);
        
        protocolManager = ProtocolLibrary.getProtocolManager();
        
        getServer().getPluginManager().registerEvents(new CosmoxListener(), this);
        
        api = API.instance();
        
        WrappedPlayer.registerType(new WrappedPlayer.PlayerWrapper<BRPlayer>(BRPlayer.class)
        {
            @Override
            public BRPlayer unWrap(java.util.UUID uuid) {
                return gameManager.getPlayer(uuid);
            }
            
            @Override
            public java.util.UUID wrap(BRPlayer player) {
                return player.getUniqueId();
            }
        });
        
        game = new Game(GAME_IDENTIFIER, "BoutonRoyale", ChatColor.of("#DEB40E"), Material.STONE_BUTTON, null, 3, false, true,
                List.of(
                        new Statistic(I18n.interpretable("main", "statistics_time_played"), GameVariables.TIME_PLAYED, true),
                        new Statistic(I18n.interpretable("main", "statistics_games_played"), GameVariables.GAMES_PLAYED),
                        new Statistic(I18n.interpretable("main", "statistics_win"), GameVariables.WIN)
                        //new Statistic(I18n.interpretable("statistics_kills"), PLOUF_ITEMS_CRAFTED, true, true)
                ),
                List.of(Achievements.BUTTONROYALE,
                        Achievements.SUICIDE),
                List.of("", I18n.interpretable("game_description")),
                List.of(new MapTemplate(MapType.NONE, List.of(
                        new MapLocation("name", MapLocationType.STRING),
                        new MapLocation("authors", MapLocationType.STRING),
                        new MapLocation("spawnpoint", MapLocationType.LOCATION)
                )))
        );
        
        game.setRestrictedGame();
        game.setGameAuthor("lumin0u");
        game.setPreparationTime(5);
        /*game.addParameter(new Parameter(PLOUF_ITEM_DELAY, "", 5, 0.25f, 20,
                new ItemBuilder(Material.CLOCK)
                        .setDisplayName(I18n.interpretable("host_parameter_item_delay"))
                        .addLore(List.of(
                                " ",
                                "§7" + I18n.interpretable("host_parameter_item_delay_description"),
                                " ",
                                I18n.interpretable("host_parameter_item_delay_value")))
                        .build(),
                List.of(1f, 0.5f, 0.25f), false, false));*/
        
        API.instance().registerNewGame(game);
        
        reset();
    }
    
    public void reset()
    {
        gameManager = new GameManager(this);
    }
    
    public API getAPI() {
        return api;
    }
    
    public static Game getGame() {
        return instance.game;
    }
    
    public static BoutonRoyale getInstance() {
        return instance;
    }
    
    public static String getPrefix() {
        return getGame().getPrefix();
    }
    
    public GameManager getGameManager() {
        return gameManager;
    }
}
