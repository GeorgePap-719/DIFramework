package diframework;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

// class Main {
//     public static void main() {
//         final DI di = new DI(); // this is the injector
//
//         final MyService myService = di.singletonOf(MyService.class); // construct a singleton (but not static) instance of MyService
//         final OtherService otherService = di.oneOf(OtherService.class); // contstruct each time a new instance of OtherService
//         final List<CarInsuranceProvider> myShapres = di.listOf(CarInsuranceProvider.class); // construct a list of objects implementing the given interface
//
//         // use the objects
//     }
// }
public class DI {

  public <T> T singletonOf(Class<T> clazz) {
    //TODO
    throw new IllegalStateException();
  }

  public <T> T oneOf(Class<T> clazz) {
    //TODO
    throw new IllegalStateException();
  }

  public <T> T listOf(Class<T> clazz) {
    //TODO
    throw new IllegalStateException();
  }

  public <T> T loadClass(Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors()).findFirst().get(); //TODO
    Class<?>[] parameterTypes = constructor.getParameterTypes();
    if (parameterTypes.length == 0) {
      //noinspection unchecked
      return (T) constructor.newInstance();
    }
    // Create Parameters.
    final var parameters = new Object[parameterTypes.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      Class<?> parameterType = parameterTypes[i];
      final Class<?> retrievedClass = Class.forName(parameterType.getCanonicalName());
      parameters[i] = loadClass(retrievedClass);
    }
    //noinspection unchecked
    return (T) constructor.newInstance(parameters);
  }

}

