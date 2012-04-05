package org.shivas.protocol.client.types;

public class GuildEmblem {
	private int backgroundId, backgroundColor,
				foregroundId, foregroundColor;
	
	public GuildEmblem() {
	}

	public GuildEmblem(int backgroundId, int backgroundColor, int foregroundId, int foregroundColor) {
		this.backgroundId = backgroundId;
		this.backgroundColor = backgroundColor;
		this.foregroundId = foregroundId;
		this.foregroundColor = foregroundColor;
	}

	/**
	 * @return the backgroundId
	 */
	public int getBackgroundId() {
		return backgroundId;
	}

	/**
	 * @param backgroundId the backgroundId to set
	 */
	public void setBackgroundId(int backgroundId) {
		this.backgroundId = backgroundId;
	}

	/**
	 * @return the backgroundColor
	 */
	public int getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * @return the foregroundId
	 */
	public int getForegroundId() {
		return foregroundId;
	}

	/**
	 * @param foregroundId the foregroundId to set
	 */
	public void setForegroundId(int foregroundId) {
		this.foregroundId = foregroundId;
	}

	/**
	 * @return the foregroundColor
	 */
	public int getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * @param foregroundColor the foregroundColor to set
	 */
	public void setForegroundColor(int foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
}
