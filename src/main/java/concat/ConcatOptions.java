package concat;

public class ConcatOptions {

	public final boolean number;
	public final boolean numberNonBlank;
	public final boolean squeezeBlank;
	public final boolean showEnds;

	public ConcatOptions(boolean number, boolean numberNonBlank, boolean squeezeBlank, boolean showEnds) {
		this.number = number;
		this.numberNonBlank = numberNonBlank;
		this.squeezeBlank = squeezeBlank;
		this.showEnds = showEnds;
	}
}
