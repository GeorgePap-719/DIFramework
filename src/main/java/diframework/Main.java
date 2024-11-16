package diframework;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");

    final var di = new DI();
    final var myService = di.singletonOf(MyService.class);
    final var otherService = di.oneOf(OtherService.class);
    final var myShapres = di.listOf(CarInsuranceProvider.class);
    for (CarInsuranceProvider myShapre : myShapres) {
      System.out.println(myShapre.toString());
    }

  }
}

@Component
class MyService {
  private Dependency dependency;

  MyService(Dependency dependency) {
    this.dependency = dependency;
  }
}

@Component
class OtherService {
  private Dependency dependency;

  OtherService(Dependency dependency) {
    this.dependency = dependency;
  }
}

@Component
class Dependency {
}

interface CarInsuranceProvider {
}

@Component
class CarInsuranceProviderImpl1 implements CarInsuranceProvider {
  private Dependency dependency;

  CarInsuranceProviderImpl1(Dependency dependency) {
    this.dependency = dependency;
  }
}

@Component
class CarInsuranceProviderImpl2 implements CarInsuranceProvider {
  private Dependency dependency;

  CarInsuranceProviderImpl2(Dependency dependency) {
    this.dependency = dependency;
  }
}

@Component
class CarInsuranceProviderImpl3 implements CarInsuranceProvider {
  private Dependency dependency;

  CarInsuranceProviderImpl3(Dependency dependency) {
    this.dependency = dependency;
  }
}