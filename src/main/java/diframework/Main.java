package diframework;

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

  }
}