package com.github.ricksbrown.cowsay;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Java port of the original cowsay commandline utility.
 * There are three entry points to this class:
 * <ul>
 *	<li>{@code Cowsay.main(args);} will call either cowsay or cowthink depending on the --cowthink flag</li>
 *	<li>{@code Cowsay.say(args);} is equivalent to cowsay</li>
 *	<li>{@code Cowsay.think(args);} is equivalent to cowthink</li>
 * </ul>
 *
 * @author Rick Brown
 */
public final class Cowsay {

	/**
	 * No instances needed, all static.
	 */
	private Cowsay() {

	}

	/**
	 * Equivalent to cowsay.
	 * @param args the command line arguments
	 * @return What the cow said.
	 */
	public static String say(final String[] args) {
		return sayOrThink(args, false);
	}

	/**
	 * Equivalent to cowthink.
	 * @param args the command line arguments
	 * @return What the cow thought.
	 */
	public static String think(final String[] args) {
		return sayOrThink(args, true);
	}

	/**
	 * Do some cowsaying or cowthinking.
	 * @param args the command line arguments
	 * @param think if true will think instead of say (the --cowthink flag can also invoke thinking)
	 * @return A talking or thinking cow.
	 */
	private static String sayOrThink(final String[] args, final boolean think) {
		CommandLine commandLine = CowsayCli.parseCmdArgs(args);
		if (commandLine != null) {
			if (commandLine.hasOption(CowsayCli.Opt.HELP.toString())) {
				CowsayCli.showCmdLineHelp();
				return "";
			} else if (commandLine.hasOption(CowsayCli.Opt.LIST_COWS.toString())) {
				return listCows(commandLine);
			}
			return getOutput(commandLine, think || commandLine.hasOption(CowsayCli.Opt.THINK.toString()));
		}
		return "";
	}

	/**
	 * Gets the message fully formatted ready for output.
	 * @param commandLine The parsed command line.
	 * @param isThought true if the message should be thought rather than said.
	 * @return The fully formatted message.
	 */
	private static String getOutput(final CommandLine commandLine, final boolean isThought) {
		try {
			String cowTemplate;
			CowFace cowFace;
			cowFace = getCowFaceByMode(commandLine);
			if (cowFace == null) {
				// if we are in here no modes were set
				cowTemplate = Cowloader.load(commandLine);
				cowFace = getCowFace(commandLine);
			} else {
				cowTemplate = Cowloader.load(Cowloader.DEFAULT_COW);
			}
			if (cowTemplate != null) {
				String[] moosages = commandLine.getArgs();
				if (moosages.length == 0) {
					moosages = CowsayCli.getPipedInput();
				}
				String moosage = StringUtils.join(moosages, " ");
				if (moosage != null && moosage.length() > 0) {
					moosage = normalizeSpace(moosage);
					Message message = new Message(moosage, isThought);
					message.setWordwrap(commandLine);
					String cow = CowFormatter.formatCow(cowTemplate, cowFace, message);
					cow = formatHtml(commandLine, cow, moosage, isThought);
					return cow;
				}
			}
		} catch (CowParseException ex) {
			Logger.getLogger(Cowsay.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}

	/**
	 * Handles -list cli arg.
	 * @param commandLine The parsed args.
	 * @return The list of cows.
	 */
	private static String listCows(final CommandLine commandLine) {
		String[] files = Cowloader.listAllCowfiles();
		if (files != null) {
			if (commandLine.hasOption(CowsayCli.Opt.LIST_COWS.toString())) {
				return StringUtils.join(files, " ");
			}
			return StringUtils.join(files, System.lineSeparator());
		}
		return "";
	}

	/**
	 * Normalize space in the message in accordance with behavior of the original cowsay.
	 * @param s A string to normalize.
	 * @return The string with normalized whitespace.
	 */
	private static String normalizeSpace(final String s) {
		String moosage = StringUtils.replaceChars(s, '\t', ' ');  // Tabs are converted to spaces
		moosage = moosage.replaceAll("[ ]+", " ");  // All consecutive spaces are reduced to a single space
		return moosage;
	}

	/**
	 * May apply HTML markup to the cow, if requested in the command line.
	 * @param commandLine The command line with user options.
	 * @param plainCow The cow formatted in plain text.
	 * @param moosage The message the cow is saying.
	 * @param isThought true if this is cowthink instead of cowsay.
	 * @return Either the plaintext cow or an HTML marked up version, depending on command line.
	 */
	private static String formatHtml(final CommandLine commandLine, final String plainCow, final String moosage,
									 final boolean isThought) {
		String cow = plainCow;
		if (commandLine.hasOption(CowsayCli.Opt.HTML.toString())) {
			cow = StringEscapeUtils.escapeHtml4(cow);
			cow = "<figure><pre>" + cow + "</pre><figcaption style=\"left:-999px; position:absolute\">";
			String alt;
			if (commandLine.hasOption(CowsayCli.Opt.ALT.toString())) {
				alt = commandLine.getOptionValue(CowsayCli.Opt.ALT.toString());
			} else {
				alt = isThought ? I18n.getMessage("altthink") : I18n.getMessage("altsay");

			}
			String escaped = StringEscapeUtils.escapeHtml4(moosage);
			cow += String.format(alt, escaped);
			cow += "</figcaption></figure>";
		}
		return cow;
	}

	/**
	 * If a pre-defined cow mode has been set on the command line then use that face.
	 * @param commandLine The command line with user options.
	 * @return The cowface for the mode selected on the command line or null if no mode set.
	 */
	private static CowFace getCowFaceByMode(final CommandLine commandLine) {
		CowFace cowFace = null;
		Set<String> modes = CowFace.COW_MODES.keySet();
		for (String mode : modes) {
			if (commandLine.hasOption(mode)) {
				cowFace = CowFace.getByMode(mode);
				break;
			}
		}
		return cowFace;
	}

	/**
	 * Get a regular cow face optionally formatted with custom eyes and tongue from the command line.
	 * @param commandLine The command line with user options.
	 * @return A regular cowface, possibly formatted with custom tongue and/or eyes.
	 */
	private static CowFace getCowFace(final CommandLine commandLine) {
		CowFace cowFace;
		cowFace = new CowFace();
		if (commandLine.hasOption(CowsayCli.Opt.EYES.toString())) {
			cowFace.setEyes(commandLine.getOptionValue(CowsayCli.Opt.EYES.toString()));
		}
		if (commandLine.hasOption(CowsayCli.Opt.TONGUE.toString())) {
			cowFace.setTongue(commandLine.getOptionValue(CowsayCli.Opt.TONGUE.toString()));
		}
		return cowFace;
	}

	/**
	 * Will cowsay (or cowthink if the --cowthink flag is set).
	 * @param args the command line arguments
	 */
	public static void main(final String[] args) {
		CowsayCli.addCowthinkOption();
		String cowsay = say(args);
		if (cowsay != null && cowsay.length() > 0) {
			System.out.println(cowsay);
		}
	}
}
