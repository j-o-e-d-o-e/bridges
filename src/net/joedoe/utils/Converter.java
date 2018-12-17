package net.joedoe.utils;

import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.logics.BridgeController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Konvertiert Spiel-Daten in Daten zum Speichern im .bgs-Format.
 */
public class Converter {
    private static GameData gameData = GameData.getInstance();

    /**
     * Legt Insel-Anzahl und Inseln zum Speichern fest.
     *
     * @param isles Liste mit den aktuellen Inseln
     */
    public static void convertIslesToData(List<IIsle> isles) {
        gameData.setIslesCount(isles.size());
        gameData.setIsles(isles);
    }

    /**
     * Konvertiert Brücken zu Objects, die jeweils aus den Indizes der beiden Inseln
     * bestehen und einem boolean, ob es sich um eine doppelte Brücke handelt.
     *
     * @param controller enthält Listen der aktuellen Brücken und Inseln
     */
    public static void convertBridgesToData(BridgeController controller) {
        List<IBridge> bridges = new ArrayList<>(controller.getBridges());
        List<IIsle> isles = new ArrayList<>(controller.getIsles());
        int bridgesCount = bridges.size();
        Object[][] bridgesData = new Object[bridgesCount][3];
        for (int i = 0; i < bridgesCount; i++) {
            IBridge bridge = bridges.get(i);
            // Indizes der Inseln
            IIsle startIsle = controller.getIsle(bridge.getStart());
            int start = isles.indexOf(startIsle);
            IIsle endIsle = controller.getIsle(bridge.getEnd());
            int end = isles.indexOf(endIsle);
            // kleinerer Insel-Index steht vorne
            if (start >= end) {
                bridgesData[i][0] = end;
                bridgesData[i][1] = start;
            } else {
                bridgesData[i][0] = start;
                bridgesData[i][1] = end;
            }
            bridgesData[i][2] = bridge.isDoubleBridge();
        }
        // Brücken werden nach erstem Index, danach nach zweitem Index sortiert
        Arrays.sort(bridgesData, Converter::compare);
        gameData.setBridgesData(bridgesData);
    }

    private static int compare(Object[] data, Object[] other) {
        int comp = Integer.compare((int) data[0], (int) other[0]);
        if (comp == 0) return (int) data[1] > (int) other[1] ? 1 : -1;
        return comp;
    }
}
