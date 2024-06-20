
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.peer.BufferMap;
import org.peer.MessageGenerator;
import java.util.ArrayList;

public class MessageGeneratorTest {


    @Test
    public void testGenerateHaveMessage() {
        MessageGenerator generator = new MessageGenerator();
        BufferMap buffermap = new BufferMap(5, "key1");
        buffermap.setPiecePresent(0);
        buffermap.setPiecePresent(2);
        buffermap.setPiecePresent(4);
        String message = generator.generateHaveMessage("key1", buffermap);
        assertEquals("have [key1] %10101%", message);
    }

    @Test
    public void testGenerateUpdateSeedMessage() {
        MessageGenerator generator = new MessageGenerator();
        String[] keyFilesSeed = {"key1", "key2"};
        String[] keyFilesLeech = {"key3", "key4"};
        String message = generator.generateUpdateSeedMessage(keyFilesSeed, keyFilesLeech);
        assertEquals("update seed [key1 key2] leech [key3 key4]", message);
    }

    @Test
    public void testGenerateDataMessage() {
        MessageGenerator generator = new MessageGenerator();
        String keyFile = "8905e92afeb80fc7722ec89eb0bf0966";
        ArrayList<byte[]> pieces = new ArrayList<>();
        ArrayList<Integer> idx = new ArrayList<>();
        idx.add(3);
        idx.add(5);
        idx.add(7);
        idx.add(8);
        idx.add(9);
        pieces.add(new byte[]{1, 0, 0, 1});
        pieces.add(new byte[]{0, 1, 0, 1});
        pieces.add(new byte[]{0, 0, 0, 0});
        pieces.add(new byte[]{1, 1, 1, 0});
        pieces.add(new byte[]{1, 1, 0, 0});
        
        String message = generator.generateDataMessage(keyFile, idx, pieces);
        assertEquals("data 8905e92afeb80fc7722ec89eb0bf0966 [3:%1001% 5:%0101% 7:%0000% 8:%1110% 9:%1100%]", message);
    }
}
