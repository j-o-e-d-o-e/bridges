//package tests.entities;
//
//import javafx.embed.swing.JFXPanel;
//import net.joedoe.entities.Bridge;
//import net.joedoe.entities.Isle;
//import net.joedoe.entities.Mocks;
//import net.joedoe.utils.Direction;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class BridgeTest {
//    private List<Isle> isles;
//
//    @Before
//    public void setUp() {
//        //workaround for initialization error
//        new JFXPanel(); // todo: separate entities and views more clearly
//        isles = Mocks.ISLES;
//        ;
//    }
//
//    @Test
//    public void getStartRow() {
//        //given
//        Isle startIsle = isles.get(0);
//        Isle endIsle = isles.get(1);
//        Direction direction = Direction.RIGHT;
//        Bridge bridge = new Bridge(startIsle, endIsle, direction);
//        int bridgeLength = Math.abs(bridge.getStartColumn() - bridge.getEndColumn()) + 2;
//        int distance = Math.abs(startIsle.getColumn() - endIsle.getColumn());
//
//        //then
//        assertEquals(bridgeLength, distance);
//    }
//}