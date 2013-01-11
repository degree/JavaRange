package jr.experiments.core.gc;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Cycle linking test (garbage collecting)
 *
 * @author Joke Roger <jokeroger@gmail.com>
 */
public class GCCyclingLinksMain {

	private static final Logger LOGGER = Logger.getLogger(GCCyclingLinksMain.class);
	public static final int DELAY = 5000;

	public static void main(String[] args) {
		LOGGER.log(Level.TRACE, "Cycle linking test started");

		LOGGER.log(Level.TRACE, "Getting runtime");
		Runtime runtime = Runtime.getRuntime();

		logUsedMemory(runtime);

		LOGGER.log(Level.TRACE, "Creating cycle linking");
		LinkObject link = new LinkObject(new LinkObject(new LinkObject()));
		link.getLink().getLink().setLink(link);
		logUsedMemory(runtime);

		LOGGER.log(Level.TRACE, "Making linking objects able for GC");
		link = null;
		logUsedMemory(runtime);

		LOGGER.log(Level.TRACE, "Cycle linking test finished");
	}

	protected static void logUsedMemory(Runtime runtime) {
		try {
			LOGGER.log(Level.TRACE, "Call GC");
			System.gc();
			LOGGER.log(Level.TRACE, String.format("Sleep for %3.1f sec.", DELAY / 1000.));
			Thread.sleep(DELAY);
			LOGGER.log(Level.DEBUG, "Memory: " + (runtime.totalMemory() + runtime.freeMemory()));
		} catch (InterruptedException e) {
			LOGGER.log(Level.WARN, "Error occurred while thread sleeping", e);
		}
	}

	static class LinkObject {
		private LinkObject link;

		LinkObject() {
			LOGGER.log(Level.INFO, "Linked object created");
		}

		LinkObject(LinkObject link) {
			this();
			this.link = link;
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			LOGGER.log(Level.INFO, "Linked object destroyed");
		}

		public LinkObject getLink() {
			return link;
		}

		public void setLink(LinkObject link) {
			this.link = link;
		}
	}

}
