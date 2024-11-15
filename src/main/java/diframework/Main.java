package diframework;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");

    DI di = new DI();
    Waiter waiter = di.oneOf(Waiter.class);
    System.out.println(waiter.whoIAm());
    MyService myService = di.oneOf(MyService.class);
    System.out.println(myService.getDependency().whatIAm());

    // Test package-level annotation
    for (WaiterI waiterI : di.listOf(WaiterI.class)) {
      System.out.println(waiterI.toString());
    }


    // for (Package p : Package.getPackages()) {
    //   // System.out.println(p.getName());
    //   List<Class<?>> annotatedClasses = AnnotationScanner.findAnnotatedClasses(p.getName(), Component.class);
    //   for (Class<?> annotatedClass : annotatedClasses) {
    //     // System.out.println("annotatedClass:" + annotatedClass);
    //
    //     // if (annotatedClass.getSuperclass()) {
    //     //   System.out.println("YES");
    //     // }
    //   }
    // }

  }
}


// class InterfaceImplementationsFinder {
//
//   public static <T> List<Class<? extends T>> findImplementations(Class<T> interfaceClass, String packageName) {
//     List<Class<? extends T>> implementations = new ArrayList<>();
//
//     try {
//       // Convert package name to directory path
//       String packagePath = packageName.replace('.', '/');
//       Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
//
//       // Scan each directory in the package path
//       while (resources.hasMoreElements()) {
//         URL resource = resources.nextElement();
//         File directory = new File(resource.getFile());
//
//         if (directory.exists()) {
//           // Scan each file in the directory
//           for (String fileName : directory.list()) {
//             if (fileName.endsWith(".class")) {
//               // Remove ".class" to get the class name
//               String className = fileName.substring(0, fileName.length() - 6);
//               className = packageName + '.' + className;
//
//               // Try to load the class
//               Class<?> clazz = Class.forName(className);
//
//               // Check if it is a subclass/implementation of the target interface
//               if (interfaceClass.isAssignableFrom(clazz) && !clazz.isInterface() && !clazz.isAnonymousClass()) {
//                 implementations.add((Class<? extends T>) clazz);
//               }
//             }
//           }
//         }
//       }
//     } catch (IOException | ClassNotFoundException e) {
//       e.printStackTrace();
//     }
//
//     return implementations;
//   }
//
//   // public static void main(String[] args) {
//   //   // Replace MyInterface and "com.example" with your interface and package name
//   //   List<Class<? extends MyInterface>> implementations = findImplementations(MyInterface.class, "com.example");
//   //
//   //   for (Class<?> implClass : implementations) {
//   //     System.out.println("Found implementation: " + implClass.getName());
//   //   }
//   // }
// }

class AnnotationScanner {

  //TODO: refactor it.
  public static List<Class<?>> findAnnotatedClasses(String packageName, Class<? extends Annotation> annotationClass) {
    List<Class<?>> annotatedClasses = new ArrayList<>();

    try {
      // Convert package name to directory path
      String packagePath = packageName.replace('.', '/');
      Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);

      // Scan each directory in the package path
      while (resources.hasMoreElements()) {
        URL resource = resources.nextElement();
        File directory = new File(resource.getFile());
        System.out.println("directory:" + directory);
        if (directory.exists()) {
          // Scan each file in the directory
          for (String fileName : directory.list()) {
            if (fileName.endsWith(".class")) {
              // Remove ".class" to get the class name
              String className = fileName.substring(0, fileName.length() - 6);
              className = packageName + '.' + className;

              // Try to load the class
              Class<?> clazz = Class.forName(className);

              // Check if the class has the specified annotation
              if (clazz.isAnnotationPresent(annotationClass)) {
                annotatedClasses.add(clazz);
              }
            }
          }
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return annotatedClasses;
  }

  // public static void main(String[] args) {
  //   // Find all classes in the package "com.example" annotated with @MyCustomAnnotation
  //   List<Class<?>> classes = findAnnotatedClasses("com.example", MyCustomAnnotation.class);
  //
  //   // Print out the annotated classes
  //   for (Class<?> clazz : classes) {
  //     System.out.println("Found annotated class: " + clazz.getName());
  //   }
  // }
}

