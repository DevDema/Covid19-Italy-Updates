import net.ddns.andrewnetwork.MainEntry;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static net.ddns.andrewnetwork.helpers.util.StringConfig.*;

public class ArgumentTest {

    @Test
    public void setOneRegionArguments() {
        try {
            String[] args = new String[]{"ITALY","-l","ITA","-r","Puglia"};

            MainEntry.main(args);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void setMultipleRegionArguments() {
        try {
            String[] args = new String[]{"ITALY","-l","ITA","-r","Puglia", "Lombardia"};

            MainEntry.main(args);
            assert true;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }


    @Test
    public void setMissingRequiredArguments() {
        try {
            String[] args = new String[]{"-l","ITA","-r","Puglia"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_INVALID_COUNTRY);
        }
    }

    @Test
    public void setUnsupportedRequiredArguments() {
        try {
            String[] args = new String[]{"UK","-l","ITA","-r","Puglia"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_INVALID_COUNTRY);
        }
    }

    @Test
    public void setMissingArguments() {
        try {
            String[] args = new String[]{};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_MISSING_ARGUMENTS);
        }
    }

    @Test
    public void setMultipleLanguageArguments() {
        try {
            String[] args = new String[]{"ITALY","-l","ITA","EN","-r","Puglia"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_MULTIPLE_LANGUAGES);
        }
    }

    @Test
    public void setUnsupportedLanguageArguments() {
        try {
            String[] args = new String[]{"ITALY","-l","EN","-r","Puglia"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_UNSUPPORTED_LANGUAGE);
        }
    }

    @Test
    public void setMissingLanguageArguments() {
        try {
            String[] args = new String[]{"ITALY","-l","-r","Puglia"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_MISSING_ARGUMENTS_OPTION + 'l');
        }
    }

    @Test
    public void setMissingRegionArguments() {
        try {
            String[] args = new String[]{"ITALY","-l","ITA","-r"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_MISSING_ARGUMENTS_OPTION + 'r');
        }
    }
}
