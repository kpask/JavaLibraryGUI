package biblioteka.core;

/**
 * Implements the Prototype design pattern for creating {@link Knyga} (Book) objects.
 * This class provides pre-configured book prototypes (e.g., "Liaudies," "Klasika," "Vaikiska")
 * that can be cloned to create new {@code Knyga} instances with default values.
 *
 * <p>Available templates:</p>
 * <ul>
 *   <li>"liaudies" - A typical Lithuanian folk tale book.</li>
 *   <li>"klasika" - A classic literature book.</li>
 *   <li>"vaikiska" - A children's book.</li>
 *   <li>Any other string will result in a default "empty" book.</li>
 * </ul>
 *
 * @author kapa1135
 * @version 1.0
 * @see Knyga
 * @see Cloneable
 * @see CloneNotSupportedException
 */

public class PrototipineKnyga implements Cloneable {

    private static final Knyga LIETUVIU_LIAUDES;
    private static final Knyga KLASIKINE_LITERATURA;
    private static final Knyga VAIKU_KNYGA;
    private static final Knyga TUSCIA_KNYGA;

    static {
        try {
            TUSCIA_KNYGA = new Knyga("Nameless", 0, 0, "", "", "", "", "9782802100683");

            LIETUVIU_LIAUDES = new Knyga("Liaudies kurinys", 1000, 5,
                    "Nėra", "Liaudies", "Lietuvių", "Liaudies leidykla", "9782802100683");

            KLASIKINE_LITERATURA = new Knyga("Klasikinis kūrinys", 1900, 3,
                    "Žinomas autorius", "Klasika", "Lietuvių", "Valstybinė leidykla", "9782802100683");

            VAIKU_KNYGA = new Knyga("Pasaka", 2000, 10,
                    "Vaikų autorius", "Vaikams", "Lietuvių", "Šviesa", "9782802100683");

        } catch (InvalidISBNException e) {
            throw new RuntimeException("Klaida inicijuojant prototipus", e);
        }
    }

    public static Knyga createTemplateBook(String template) throws CloneNotSupportedException {
        switch (template.toLowerCase()){
            case "liaudies":
                return LIETUVIU_LIAUDES.clone();
            case "klasika":
                return KLASIKINE_LITERATURA.clone();
            case "vaikiska":
                return VAIKU_KNYGA.clone();
            default:
                return TUSCIA_KNYGA.clone();

        }
    }
}
