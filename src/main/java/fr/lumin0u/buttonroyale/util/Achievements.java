package fr.lumin0u.buttonroyale.util;

import fr.worsewarn.cosmox.api.achievements.Achievement;
import org.bukkit.Material;

public class Achievements
{
	public static final Achievement BUTTONROYALE = new Achievement(7300, "Bouton Royale", Material.STONE_BUTTON, "Terminer tous les succès en Bouton Royale", 0);
	public static final Achievement SUICIDE = new Achievement(7301, "Problème de mémoire", Material.AXOLOTL_BUCKET, "Activer son propre bouton", 7300);
}
