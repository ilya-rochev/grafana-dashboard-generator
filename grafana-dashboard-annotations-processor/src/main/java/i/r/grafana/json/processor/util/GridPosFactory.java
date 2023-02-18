package i.r.grafana.json.processor.util;

import i.r.grafana.json.processor.model.GridPos;

/**
 * Определение позиции панели на дашюорде
 * Механизм реализован на максимально простом уровне -
 * расчет исходит из того, что панели располагаются последовательно слева направно, в строке 2 панели
 */
public class GridPosFactory {

    public static GridPos build(int h, int w, int panelIndex) {
        int xPosition = (panelIndex - 1) % 2;
        int yPosition = (panelIndex - 1) / 2;

        return new GridPos(h, w, xPosition*w, yPosition * h - h );
    }

}
