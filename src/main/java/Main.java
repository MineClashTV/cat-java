import cli.Arguments;
import concat.Concat;
import concat.ConcatOptions;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class Main {

	public static final String version = "1.1.0";

	public static void main(String[] args) {
		var arguments = new Arguments(args, version);

		try {
			arguments.parse();
		} catch(UnrecognizedOptionException e) {
			System.err.println("Unknown argument option: '" + e.getOption() + "'");

			System.exit(1);
		} catch(ParseException e) {
			e.printStackTrace();
		}

		var concat = new Concat(
				(List<String>) arguments.getValue("files"),
				new ConcatOptions(
						(boolean) arguments.getValue("number"),
						(boolean) arguments.getValue("number-nonblank"),
						(boolean) arguments.getValue("squeeze-blank"),
						(boolean) arguments.getValue("show-ends")
				)
		);

		try {
			System.out.println(concat.concatenate());
		} catch(NoSuchFileException e) {
			System.err.println("File does not exist: '" + e.getFile() + "' (" + e.getReason() + ")");

			System.exit(1);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
