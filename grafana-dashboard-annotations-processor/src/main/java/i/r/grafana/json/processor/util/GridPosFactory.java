package i.r.grafana.json.processor.util;

import i.r.grafana.json.processor.model.GridPos;

/**
 * Detecting panel position on dashboard.
 * The mechanism is implemented at the simplest possible way -
 * Panels are arranged sequentially on the left, there are two panels in the row
 */
public class GridPosFactory {

    public static GridPos build(int h, int w, int panelIndex) {
        int xPosition = (panelIndex - 1) % 2;
        int yPosition = (panelIndex - 1) / 2;

        return new GridPos(h, w, xPosition*w, yPosition * h - h );
    }

}
