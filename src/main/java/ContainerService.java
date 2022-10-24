import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ContainerService {
    // Spring -> AsApplicationContext

    public static <T> T getObject(Class<T> classType) {
        T instance = createInstance(classType);
        Arrays.stream(classType.getDeclaredFields()).forEach(f -> {
            if(f.getAnnotation(MyAutowired.class) != null){
                Object fieldInstance = createInstance(f.getType());
                f.setAccessible(true);
                try {
                    f.set(instance,fieldInstance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return instance;
    }

    private static <T> T createInstance(Class<T> classType){
        try {
            return classType.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
