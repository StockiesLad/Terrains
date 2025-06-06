package net.stockieslad.magical_utilities;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MagicalUtilities {
    public static final String NAMESPACE = "magical_utilities";

    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    public static Identifier getIdentifier(String name) {
        return new Identifier(NAMESPACE, name);
    }
    public static String interpolateNameSpace(String string) {
        return string.replace("${name}", NAMESPACE);
    }
}
