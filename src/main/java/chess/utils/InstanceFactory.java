package chess.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class InstanceFactory {

    /**
     * Creates an instance of a class through reflection
     *
     * @param className      class path string
     * @param parameterTypes class constructor's types
     * @param parameters     class constructor's parameters
     * @return instance of a class
     * @throws IOException when className is invalid or parameterTypes or parameters are wrong
     */
    public static Object createInstance(String className, Class<?>[] parameterTypes, Object[] parameters) throws IOException {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor(parameterTypes);
            return ctor.newInstance(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
