package concat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Concat {

	private final List<String> files;
	private final ConcatOptions options;

	public Concat(List<String> files, ConcatOptions options) {
		this.files = files;
		this.options = options;
	}

	public String concatenate() throws IOException {
		var builder = new StringBuilder();
		var combined = new ArrayList<String>();

		// doing the actual concatenation
		for(var path : files)
			Files.lines(Path.of(path)).forEach(line -> combined.add(line + "\n"));

		// applying commandline options, appending to the builder
		for(var i = 0; i < combined.size(); i++) {
			var current = combined.get(i);

			// suppressing repeated blank lines
			if(options.squeezeBlank && (current.isBlank() && combined.get(i - 1).isBlank()))
				continue;

			// numbering all lines
			if(options.number && !options.numberNonBlank)
				builder.append(i + 1).append(" | ");

			// TODO: don't take blank lines into consideration at all when getting the line number
			// numbering nonempty output lines
			if(options.numberNonBlank && !current.isBlank())
				builder.append(i + 1).append(" | ");

			builder.append(current);
		}

		var result = builder.toString();

		// displaying '$' at end of each line, would've preferred to implement this in the loop above but couldn't get
		// it to work properly
		if(options.showEnds)
			result = result.replace("\n", "$\n");

		// removing the last newline character, suboptimal solution but who cares anyways
		return result.substring(0, result.length() - 1);
	}
}
