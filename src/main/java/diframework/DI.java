package diframework;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class DI {

  private final HashMap<String, Instance> singletons = new HashMap<>();
  private final HashMap<String, Class<?>> components = new HashMap<>();

  private final List<String> packages = new ArrayList<>();

  private void scanForAnnotations() {
    for (String p : packages) {
      final var annotatedClasses = findComponents(p);
      for (Class<?> annotatedClass : annotatedClasses) {
        components.put(annotatedClass.getCanonicalName(), annotatedClass);
      }
    }
  }

  private List<Class<?>> findComponents(String packageName) {
    try {
      final var components = new ArrayList<Class<?>>();
      // Convert package name to directory path.
      final var packagePath = packageName.replace('.', '/');
      final var resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
      // Scan each directory in the package path.
      while (resources.hasMoreElements()) {
        final var resource = resources.nextElement();
        final var directory = new File(resource.getFile());
        if (!directory.exists()) {
          continue;
        }
        final var dirList = directory.list();
        if (dirList == null) {
          continue;
        }
        // Scan each file in the directory.
        for (String fileName : dirList) {
          if (!fileName.endsWith(".class")) {
            continue;
          }
          // Remove ".class" to get the class name.
          final var className = fileName.substring(0, fileName.length() - 6);
          final var canonicalName = packageName + '.' + className;
          // Try to load the class.
          final var clazz = Class.forName(canonicalName);
          // Check if the class has the specified annotation.
          if (clazz.isAnnotationPresent(Component.class)) {
            components.add(clazz);
          }
        }
      }
      return components;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

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

  // Constructs a singleton instance.
  public <T> T singletonOf(Class<T> clazz) {
    String target = clazz.getCanonicalName();
    final var instance = singletons.computeIfAbsent(target, key -> new Instance(oneOf(clazz)));
    //noinspection unchecked
    return (T) instance.value;
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

  // Constructs each time a new instance.
  public <T> T oneOf(Class<T> clazz) {
    try {
      return loadClass(clazz);
    } catch (Throwable e) {
      if (e instanceof NoSuchElementException castedE) {
        throw castedE;
      }
      throw new RuntimeException(e);
    }
  }

  private <T> T loadClass(Class<T> clazz) {
    if (components.get(clazz.getCanonicalName()) == null) {
      throw new NoSuchElementException("Class: " + clazz.getCanonicalName() + " does not have the '@Component' annotation");
    }
    final var constructor = Arrays.stream(clazz.getDeclaredConstructors())
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("unexpected"));
    final var parameterTypes = constructor.getParameterTypes();
    if (parameterTypes.length == 0) {
      return createNewInstance(constructor, null);
    }
    // Create Parameters to inject them into constructor.
    final var parameters = new Object[parameterTypes.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      final var parameterType = parameterTypes[i];
      final var retrievedClass = safeGetClass(parameterType.getCanonicalName());
      parameters[i] = loadClass(retrievedClass);
    }
    return createNewInstance(constructor, parameters);
  }

  private Class<?> safeGetClass(String canonicalName) {
    try {
      return Class.forName(canonicalName);
    } catch (ClassNotFoundException e) {
      // At this point, we should always find a class.
      throw new IllegalStateException(e);
    }
  }

  /**
   * Creates a new instance using the specified constructor and the given parameters.
   * The parameters can be 'null', which implies to use an empty-constructor.
   *
   * @throws RuntimeException which wraps an 'InvocationTargetException', in case the target constructor throws an exception.
   */
  @SuppressWarnings("unchecked")
  private <T> T createNewInstance(Constructor<?> constructor, Object[] parameters) {
    try {
      if (parameters == null) {
        return (T) constructor.newInstance();
      } else {
        return (T) constructor.newInstance(parameters);
      }
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    } catch (InstantiationException e) {
      final var clazz = constructor.getClass();
      throw new IllegalStateException("Illegal target: " + clazz.getCanonicalName(), e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Class to store a single instance for a given object.
   */
  private record Instance(Object value) {
  }
}
