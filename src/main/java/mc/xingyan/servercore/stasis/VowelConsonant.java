package mc.xingyan.servercore.stasis;

public class VowelConsonant {

    public static String getArticle(String string) {

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

