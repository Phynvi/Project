package com.overload.game.model;

/**
 * Represents a ChatMessage's values, such as their color, effect and text
 * bytes.
 *
 * @author relex lawl
 */
public class ChatMessage {

	/**
	 * The color of the message.
	 */
	private int colour;
	/**
	 * The effects of the message.
	 */
	private int effects;
	/**
	 * The actual text of the message.
	 */
	private String text;

	/**
     * The Message constructor.
     *
     * @param colour  The color the message will have, done through color(#):
     * @param effects The effect the message will have, done through effect(#):
     * @param text    The actual message it will have.
     */
    public ChatMessage(int colour, int effects, String packed) {
        this.colour = colour;
        this.effects = effects;
        this.text = packed;
    }

	/**
	 * Gets the message's chat color.
	 *
	 * @return colour.
	 */
	public int getColour() {
		return colour;
	}

	/**
	 * Gets the message's chat effect.
	 *
	 * @return effects.
	 */
	public int getEffects() {
		return effects;
	}

	/**
	 * Gets the message's actual text in byte form.
	 *
	 * @return text.
	 */
	public String getText() {
		return text;
	}
}