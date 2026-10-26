package nl.tudelft.jpacman.sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/** Draws a short identifying label over an existing sprite. */
public class LabelSprite implements Sprite {
    private final Sprite background;
    private final String label;
    private final Color color;

    /**
     * Creates a labeled sprite.
     *
     * @param backgroundSprite sprite drawn below the label
     * @param text label to draw
     * @param labelColor label color
     */
    public LabelSprite(Sprite backgroundSprite, String text, Color labelColor) {
        this.background = backgroundSprite;
        this.label = text;
        this.color = labelColor;
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int width, int height) {
        background.draw(graphics, x, y, width, height);
        Font oldFont = graphics.getFont();
        Color oldColor = graphics.getColor();
        graphics.setFont(oldFont.deriveFont(Font.BOLD, Math.max(9.0f, height * 0.65f)));
        graphics.setColor(color);
        FontMetrics metrics = graphics.getFontMetrics();
        int labelX = x + 1;
        int labelY = y + metrics.getAscent();
        graphics.drawString(label, labelX, labelY);
        graphics.setFont(oldFont);
        graphics.setColor(oldColor);
    }

    @Override
    public Sprite split(int x, int y, int width, int height) {
        return new LabelSprite(background.split(x, y, width, height), label, color);
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }
}
