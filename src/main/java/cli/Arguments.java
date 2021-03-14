package cli;

import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Arguments {

	private final String version;
	private final String[] args;
	private final Option[] optionArray;
	private final Options options;
	private final Map<String, Object> parsedArgs;

	public Arguments(String[] args, String version) {
		this.version = version;
		this.args = args;
		this.options = new Options();
		this.parsedArgs = new HashMap<>();
		this.optionArray = new Option[] {
				// opt, longOpt, hasArg, desc
				new Option("n", "number", false, "number all output lines"),
				new Option("b", "number-nonblank", false, "number nonempty output lines, overrides -n"),
				new Option("s", "squeeze-blank", false, "suppress repeated empty output lines"),
				new Option("E", "show-ends", false, "display $ at end of each line"),
				new Option("h", "help", false, "display this help and exit"),
				new Option("v", "version", false, "display version information and exit"),
		};
	}

	public void init() {
		Arrays.stream(optionArray).forEach(options::addOption);

		parsedArgs.put("number", false);
		parsedArgs.put("number-nonblank", false);
		parsedArgs.put("squeeze-blank", false);
		parsedArgs.put("show-ends", false);
	}

	/**
	 * Parses the arguments, requires options to be initialized before.
	 * Will set the corresponding entries in the parsedArgs map to later use them elsewhere
	 *
	 * @throws ParseException if there are any problems encountered while parsing the command line tokens
	 */
	public void parse() throws ParseException {
		var parser = new DefaultParser();
		var commandLine = parser.parse(options, args);

		if(commandLine.hasOption("number"))
			parsedArgs.replace("number", true);

		if(commandLine.hasOption("number-nonblank"))
			parsedArgs.replace("number-nonblank", true);

		if(commandLine.hasOption("squeeze-blank"))
			parsedArgs.replace("squeeze-blank", true);

		if(commandLine.hasOption("show-ends"))
			parsedArgs.replace("show-ends", true);

		if(commandLine.hasOption("help") ||
				(commandLine.getOptions().length == 0 && commandLine.getArgs().length == 0)) {
			// default HelpFormatter configuration is quite ugly
			var help = new HelpFormatter();
			help.setLeftPadding(4);
			help.setDescPadding(4);
			help.setSyntaxPrefix("usage: ");

			help.printHelp(
					"cat.jar [option] [file(s)]",
					"concatenate files and print on the standard output\n\n",
					options,
					"\nPlease report issues at <git url goes here>"
			);

			// by the way Commons CLI team, I'd like to also be able to configure a padding for the short and long
			// option separator (the comma), by default it looks bad.

			System.exit(0);
		}

		if(commandLine.hasOption("version")) {
			System.out.println("cat version " + version);

			System.exit(0);
		}

		parsedArgs.put("files", commandLine.getArgList());
	}

	public Object getValue(String key) {
		return parsedArgs.get(key);
	}
}
