package mc.xingyan.xycore.Stasis;

public class VowelConsonant {

    public static String VowelConsonant(String string) {

        switch (string.toLowerCase().charAt(0)) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                return "an";
            default:
                return "a";
        }
    }

}
