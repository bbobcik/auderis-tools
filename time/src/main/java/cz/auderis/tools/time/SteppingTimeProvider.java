package cz.auderis.tools.time;


public class SteppingTimeProvider extends ManualTimeProvider {
	private static final long serialVersionUID = -1804476424526305765L;
	private static final String ERR_ILLEGAL_STEP = "invalid time step value";

	protected long step;

	public SteppingTimeProvider(long startTime) {
		this(startTime, 1L);
	}

	public SteppingTimeProvider(long startTime, long stepMillis) {
		super(startTime);
		if (stepMillis < 0L) {
			throw new IllegalArgumentException(ERR_ILLEGAL_STEP);
		}
		this.step = stepMillis;
	}

	@Override
	public long getTimeInMillis() {
		return time.getAndAdd(step);
	}

	public long getTimeStep() {
		return step;
	}

	public void setTimeStep(long newStep) {
		if (newStep < 0L) {
			throw new IllegalArgumentException(ERR_ILLEGAL_STEP);
		}
		this.step = newStep;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder("SteppingTimeProvider[t=");
		str.append(time.get());
		str.append(", delta=").append(step);
		str.append("]");
		return str.toString();
	}

}
