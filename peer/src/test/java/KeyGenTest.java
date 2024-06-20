import javax.xml.bind.JAXBElement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.peer.KeyGen;
public class KeyGenTest {
    
    @Test
    public void testKeyGenTxt(){
        String filename = "/home/mathys/cours/S8/PReseau/free-projet-reseau/peer/src/test/java/bonjour.txt";
        String res = KeyGen.keysGenerator(filename);
        assertEquals("06e58f2e9aea9b9cf171feed26a8e166", res);
    }

    @Test
    public void testKeyGenPom(){
        String filename = "/home/mathys/cours/S8/PReseau/free-projet-reseau/peer/src/test/java/bigFile.pom";
        String res = KeyGen.keysGenerator(filename);
        assertEquals("04452e34f38a5dc915fd4394507d146d", res);
    }

    @Test
    public void testKeyGenDat(){
        String filename = "/home/mathys/cours/S8/PReseau/free-projet-reseau/peer/src/test/java/file_a.dat";
        String res = KeyGen.keysGenerator(filename);
        assertEquals("efeac792aff811e28b2397e942c4c5a9", res); // il faut spécifier tout le chemin, trouver un moyen de ne pas avoir besoin de faire ca
    }

    @Test
    public void testKeyGenVide(){
        String res = KeyGen.keysGenerator("");
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", res); // fichier est vide, si pas de fichier 
    }
}
