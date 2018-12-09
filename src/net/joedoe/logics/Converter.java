package net.joedoe.logics;

import java.util.Arrays;
import java.util.List;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.GameData;

class Converter {
    private static GameData gameData = GameData.getInstance();

    static void convertIslesToData(List<Isle> isles) {
        int islesCount = isles.size();
        gameData.setIslesCount(islesCount);
        Object[][] islesData = new Object[islesCount][2];
        for (int i = 0; i < islesCount; i++) {
            Isle isle = isles.get(i);
            islesData[i][0] = isle.getPos();
            islesData[i][1] = isle.getBridges();
        }
        gameData.setIsles(islesData);
    }

    static void convertBridgesToData(List<Bridge> bridges, List<Isle> isles) {
        int bridgesCount = bridges.size();
        Object[][] bridgesData = new Object[bridgesCount][3];
        for (int i = 0; i < bridgesCount; i++) {
            Bridge bridge = bridges.get(i);
            int startIsle = isles.indexOf(bridge.getStartIsle());
            int endIsle = isles.indexOf(bridge.getEndIsle());
            if (startIsle >= endIsle) {
                bridgesData[i][0] = endIsle;
                bridgesData[i][1] = startIsle;
            } else {
                bridgesData[i][0] = startIsle;
                bridgesData[i][1] = endIsle;
            }
            bridgesData[i][2] = bridge.isDoubleBridge();
        }
        Arrays.sort(bridgesData, Converter::compare);
        gameData.setBridges(bridgesData);
    }

    private static int compare(Object[] data, Object[] other) {
        int comp = Integer.compare((int) data[0], (int) other[0]);
        if (comp == 0) return (int) data[1] > (int) other[1] ? 1 : -1;
        return comp;
    }
}
