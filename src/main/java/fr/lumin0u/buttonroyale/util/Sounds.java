package fr.lumin0u.buttonroyale.util;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

import java.util.Map;
import fr.lumin0u.buttonroyale.util.TFSound.*;

public final class Sounds {
	
	public static final TFSound SILENCE = new SoundCombination();
	
	public static final TFSound ALREADY_A_BUTTON = new InstrumentNote(Instrument.BANJO, Note.natural(1, Note.Tone.A));
	
	public static final TFSound MY_BUTTON_FOUND = new SoundCombination(Map.of(
			new InstrumentNote(Instrument.PIANO, Note.flat(2, Note.Tone.E)), 0,
			new SimpleSound(Sound.ENTITY_SKELETON_DEATH, 1, 0.6f, SoundCategory.MASTER), 0,
			new SimpleSound(Sound.ENTITY_CREEPER_PRIMED, 1, 0.8f, SoundCategory.MASTER), 20
	));
	public static final TFSound ACTIVATE_BUTTON = new SoundCombination(Map.ofEntries(
			Map.entry(new SimpleSound(Sound.BLOCK_IRON_DOOR_OPEN, 1, 0.5f, SoundCategory.MASTER), 0),
			Map.entry(new SimpleSound(Sound.BLOCK_LEVER_CLICK, 1, 0.5f, 1, SoundCategory.MASTER), 7),
			Map.entry(new SimpleSound(Sound.BLOCK_LEVER_CLICK, 1, 0.5f, 1, SoundCategory.MASTER), 14),
			Map.entry(new SimpleSound(Sound.BLOCK_LEVER_CLICK, 1, 0.5f, 1, SoundCategory.MASTER), 23),
			Map.entry(new SimpleSound(Sound.BLOCK_LEVER_CLICK, 1, 0.5f, 1, SoundCategory.MASTER), 33),
			Map.entry(new SimpleSound(Sound.BLOCK_BARREL_CLOSE, 1, 1.2f, 2f, SoundCategory.MASTER), 3),
			Map.entry(new SimpleSound(Sound.BLOCK_BARREL_CLOSE, 1, 1.2f, 2f, SoundCategory.MASTER), 14),
			Map.entry(new SimpleSound(Sound.BLOCK_BARREL_CLOSE, 1, 1.2f, 2f, SoundCategory.MASTER), 26),
			Map.entry(new SimpleSound(Sound.BLOCK_BARREL_CLOSE, 1, 1.2f, 2f, SoundCategory.MASTER), 39),
			Map.entry(new SimpleSound(Sound.BLOCK_NOTE_BLOCK_HAT, 1, 0.5f, SoundCategory.MASTER), 50),
			Map.entry(new SimpleSound(Sound.ENTITY_GUARDIAN_HURT, 1, 1f, SoundCategory.MASTER), 60)
	));
	public static final TFSound ACTIVATE_FAKE_BUTTON = new SoundCombination(Map.ofEntries(
			Map.entry(new SimpleSound(Sound.BLOCK_LEVER_CLICK, 1, 0.5f, 0.8f, SoundCategory.MASTER), 7),
			Map.entry(new SimpleSound(Sound.BLOCK_LEVER_CLICK, 1, 0.5f, 0.8f, SoundCategory.MASTER), 15),
			Map.entry(new SimpleSound(Sound.BLOCK_LEVER_CLICK, 1, 0.5f, 0.8f, SoundCategory.MASTER), 25),
			Map.entry(new SimpleSound(Sound.BLOCK_LEVER_CLICK, 1, 0.5f, 0.8f, SoundCategory.MASTER), 38),
			Map.entry(new SimpleSound(Sound.BLOCK_BARREL_CLOSE, 1, 1.2f, 1.8f, SoundCategory.MASTER), 3),
			Map.entry(new SimpleSound(Sound.BLOCK_BARREL_CLOSE, 1, 1.2f, 1.8f, SoundCategory.MASTER), 15),
			Map.entry(new SimpleSound(Sound.BLOCK_BARREL_CLOSE, 1, 1.2f, 1.8f, SoundCategory.MASTER), 28),
			Map.entry(new SimpleSound(Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 2f, SoundCategory.MASTER), 40)
	));
}
