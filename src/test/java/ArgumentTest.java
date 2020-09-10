import net.ddns.andrewnetwork.MainEntry;
import net.ddns.andrewnetwork.helpers.util.builder.SavedDataBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static net.ddns.andrewnetwork.helpers.util.StringConfig.*;

public class ArgumentTest {

    @BeforeAll
    public static void setup() {
        SavedDataBuilder.setSavedDataPath("config-test.json");
    }

    @Test
    public void setOneRegionArguments() {
        try {
            String[] args = new String[]{"ITALY", "-l", "ITA", "-r", "Puglia"};

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
            String[] args = new String[]{"ITALY", "-l", "ITA", "-r", "Puglia", "Lombardia"};

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
            String[] args = new String[]{"-l", "ITA", "-r", "Puglia"};
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
            String[] args = new String[]{"UK", "-l", "ITA", "-r", "Puglia"};
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
            String[] args = new String[]{"ITALY", "-l", "ITA", "EN", "-r", "Puglia"};
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
            String[] args = new String[]{"ITALY", "-l", "EN", "-r", "Puglia"};
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
            String[] args = new String[]{"ITALY", "-l", "-r", "Puglia"};
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
            String[] args = new String[]{"ITALY", "-l", "ITA", "-r"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_MISSING_ARGUMENTS_OPTION + 'r');
        }
    }

    @Test
    public void setUnrecognizedArguments() {
        try {
            String[] args = new String[]{"ITALY", "-o"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_UNRECOGNIZED + 'o');
        }
    }

    @Test
    public void setUncorrectDelayArguments1() {
        try {
            String[] args = new String[]{"ITALY", "-d", "60*30"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_OPTION_NUMBER_FORMAT + 'd');
        }
    }

    @Test
    public void setMissingDelayArguments() {
        try {
            String[] args = new String[]{"ITALY", "-d"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_MISSING_ARGUMENTS_OPTION + 'd');
        }
    }

    @Test
    public void setUncorrectDelayArguments2() {
        try {
            String[] args = new String[]{"ITALY", "-d", "test"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_OPTION_NUMBER_FORMAT + 'd');
        }
    }

    @Test
    public void setManyDelayArguments() {
        try {
            String[] args = new String[]{"ITALY", "-d", "60", "30"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(String.format(EXCEPTION_MANY_ARGS_OPTION, 'd', 1));
        }
    }


    @Test
    public void setMissingConfigArguments() {
        try {
            String[] args = new String[]{"ITALY", "-c"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(EXCEPTION_MISSING_ARGUMENTS_OPTION + 'c');
        }
    }

    @Test
    public void setManyDaemonArguments() {
        try {
            String[] args = new String[]{"ITALY", "-s", "test"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(String.format(EXCEPTION_MANY_ARGS_OPTION, 's', 0));
        }
    }

    @Test
    public void setManyConfigArguments() {
        try {
            String[] args = new String[]{"ITALY", "-c", "test", "test"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(String.format(EXCEPTION_MANY_ARGS_OPTION, 'c', 1));
        }
    }

    @Test
    public void setManyDebugArguments() {
        try {
            String[] args = new String[]{"ITALY", "-D", "test"};
            MainEntry.main(args);
            assert false;
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getMessage());
            assert e.getMessage().equalsIgnoreCase(String.format(EXCEPTION_MANY_ARGS_OPTION, 'D', 0));
        }
    }
}
