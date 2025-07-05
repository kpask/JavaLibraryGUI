package biblioteka.testing;

import biblioteka.core.InvalidISBNException;
import biblioteka.core.Knyga;
import biblioteka.core.PrototipineKnyga;
import biblioteka.factory.KnygaCreator;
import biblioteka.factory.LeidinysFactory;
import biblioteka.factory.ZurnalasCreator;

public class KnyguTest {
    public static void main(String[] args) throws InvalidISBNException, CloneNotSupportedException {

        ZurnalasCreator zurnalasCreator = new ZurnalasCreator();
        KnygaCreator knygaCreator = new KnygaCreator();

        Knyga original = PrototipineKnyga.createTemplateBook("liaudies");
        original.addComment("Great book!");

        Knyga copy = original.clone();
        copy.addComment("Overrated...");

        System.out.println("Original comments: " + original.getComments());
        System.out.println("Shallow comments: " + copy.getComments());

        // Creating book with factory
        Knyga factoryKnyga = knygaCreator.createLeidinys(
                "Mazasis princas", 2000, 100,
                "", "", "", "", "9782802100683",
                "", 0, "", "", ""
        );

        System.out.println(factoryKnyga.toString());
    }
}
