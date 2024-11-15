package diframework;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
//
// TODO:
// 1 - store all valid constructors with their parameters, if any.
// 2 - store all singletons
// 3 - impl listOf.
public class DI {

  private final HashMap<String, Instance> singletons = new HashMap<>();
  private final HashMap<String, Class<?>> components = new HashMap<>();

  private final List<String> packages = new ArrayList<>();

  DI() {
    for (Package p : Package.getPackages()) {
      packages.add(p.getName());
    }
    scanForAnnotations();
  }

  DI(String packageName) {
    packages.add(packageName);
    scanForAnnotations();
  }

  private void scanForAnnotations() {
    for (String p : packages) {
      final var annotatedClasses = AnnotationScanner.findAnnotatedClasses(p, Component.class);
      for (Class<?> annotatedClass : annotatedClasses) {
        components.put(annotatedClass.getCanonicalName(), annotatedClass);
      }
    }
  }

  // Constructs a singleton instance.
  public <T> T singletonOf(Class<T> clazz) {
    String target = clazz.getCanonicalName();
    final var instance = singletons.computeIfAbsent(target, key -> new Instance(oneOf(clazz)));
    //noinspection unchecked
    return (T) instance.value;
  }

  // Constructs each time a new instance.
  public <T> T oneOf(Class<T> clazz) {
    try {
      return loadClass(clazz);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  // Constructs a list of objects implementing the given interface.
  public <T> List<T> listOf(Class<T> interfaceName) {
    final var implementations = new ArrayList<T>();
    for (Map.Entry<String, Class<?>> entry : components.entrySet()) {
      if (!interfaceName.isAssignableFrom(entry.getValue())) {
        continue;
      }
      Object impl = oneOf(entry.getValue());
      //noinspection unchecked
      implementations.add((T) impl);
    }
    return implementations;
  }

  private <T> T loadClass(Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    if (components.get(clazz.getCanonicalName()) == null) {
      throw new NoSuchElementException("Class: " + clazz.getCanonicalName() + " does not have the '@Component' annotation");
    }
    final var constructor = Arrays.stream(clazz.getDeclaredConstructors())
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("unexpected"));
    final var parameterTypes = constructor.getParameterTypes();
    if (parameterTypes.length == 0) {
      //noinspection unchecked
      return (T) constructor.newInstance();
    }
    // Create Parameters to inject them into constructor.
    final var parameters = new Object[parameterTypes.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      final var parameterType = parameterTypes[i];
      final var retrievedClass = Class.forName(parameterType.getCanonicalName());
      parameters[i] = loadClass(retrievedClass);
    }
    //noinspection unchecked
    return (T) constructor.newInstance(parameters);
  }

  private static class Node {
    private final Constructor<?> constructor;
    private final Object[] parameters;


    public Node(Constructor<?> constructor, Object[] parameters) {
      this.constructor = constructor;
      this.parameters = parameters;
    }

    public Node(Constructor<?> constructor) {
      this.constructor = constructor;
      this.parameters = null;
    }

    public Constructor<?> getConstructor() {
      return constructor;
    }

    public Object[] getParameters() {
      return parameters;
    }

    @SuppressWarnings("unchecked")
    public <T> T newInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException {
      if (parameters == null) {
        return (T) constructor.newInstance();
      } else {
        return (T) constructor.newInstance(parameters);
      }
    }
  }

  private record Instance(Object value) {
  }
}

