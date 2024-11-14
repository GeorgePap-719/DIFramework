package diframework;

import java.lang.reflect.InvocationTargetException;

public class Main {
  public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
    System.out.println("Hello world!");


    // ClassLoader systemClassLoader = ClassLoader.getPlatformClassLoader();
    // Class<Waiter> waiterClass = Waiter.class;
    // Waiter waiter = waiterClass.getDeclaredConstructor().newInstance();
    // System.out.println(waiter.whoIAm());
    //
    // Class<MyService> myServiceClass = MyService.class;
    // for (Constructor<?> constructor : myServiceClass.getDeclaredConstructors()) {
    //   System.out.println(Arrays.toString(constructor.getParameterTypes()));
    // }

    DI di = new DI();
    Waiter waiter = di.loadClass(Waiter.class);
    System.out.println(waiter.whoIAm());
    MyService myService = di.loadClass(MyService.class);
    System.out.println(myService.getDependency().whatIAm());

  }
}