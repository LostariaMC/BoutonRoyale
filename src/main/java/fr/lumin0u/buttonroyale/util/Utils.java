package fr.lumin0u.buttonroyale.util;

import org.bukkit.Material;

import java.util.*;
import java.util.function.Predicate;

public final class Utils {
	public static final Set<Material> BUTTONS = Set.of(
			Material.ACACIA_BUTTON,
			Material.BIRCH_BUTTON,
			Material.BAMBOO_BUTTON,
			Material.CHERRY_BUTTON,
			Material.JUNGLE_BUTTON,
			Material.MANGROVE_BUTTON,
			Material.CRIMSON_BUTTON,
			Material.OAK_BUTTON,
			Material.POLISHED_BLACKSTONE_BUTTON,
			Material.STONE_BUTTON,
			Material.WARPED_BUTTON,
			Material.DARK_OAK_BUTTON,
			Material.SPRUCE_BUTTON
	);
	
	private final static List<Material> BUTTONS_LIST = new ArrayList<>(BUTTONS);
	
	public static Collection<Material> getNButtons(Random rand, int n) {
		synchronized(BUTTONS_LIST) {
			Set<Material> buttons = new HashSet<>(n);
			for(int i = 0; i < n; i++) {
				buttons.add(BUTTONS_LIST.remove(rand.nextInt(BUTTONS_LIST.size())));
			}
			BUTTONS_LIST.addAll(buttons);
			return buttons;
		}
	}
}
