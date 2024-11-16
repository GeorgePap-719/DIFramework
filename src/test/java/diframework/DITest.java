package diframework;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DITest {

  @Test
  void testOneOf() {
    final var di = new DI();
    di.oneOf(MyServiceWithoutDep.class);
  }

  @Test
  void testOneOfWithDependency() {
    final var di = new DI();
    di.oneOf(MyService.class);
  }

  @Test
  void testOneOfWithoutAnnotation() {
    final var di = new DI();
    assertThrows(NoSuchElementException.class, () -> di.oneOf(MyServiceWithoutAnnotation.class));
  }

  @Test
  void testOneOfDependencyWithoutAnnotation() {
    final var di = new DI();
    assertThrows(NoSuchElementException.class, () -> di.oneOf(MyServiceDepWithoutAnnotation.class));
  }

  @Test
  void testSingletonOf() {
    final var di = new DI();
    final var myService1 = di.singletonOf(OtherService.class);
    final var myService2 = di.singletonOf(OtherService.class);
    assertSame(myService1, myService2);
  }

  @Test
  void testListOf() {
    final var di = new DI();
    final var providers = di.listOf(CarInsuranceProviderTest.class);
    assertEquals(3, providers.size());
  }
}

@Component
class MyServiceWithoutDep {
}

class MyServiceWithoutAnnotation {
}

@Component
class MyServiceDepWithoutAnnotation {
  private DependencyWithoutAnnotation dep;

  MyServiceDepWithoutAnnotation(DependencyWithoutAnnotation dep) {
    this.dep = dep;
  }
}

class DependencyWithoutAnnotation {
}

@Component
class MyService {
  private Dependency dependency;

  MyService(Dependency dependency) {
    this.dependency = dependency;
  }
}

@Component
class Dependency {
}

@Component
class OtherService {
  private Dependency dependency;

  OtherService(Dependency dependency) {
    this.dependency = dependency;
  }
}

interface CarInsuranceProviderTest {
}

@Component
class Provider1 implements CarInsuranceProviderTest {
  private Dependency dependency;

  Provider1(Dependency dependency) {
    this.dependency = dependency;
  }
}

@Component
class Provider2 implements CarInsuranceProviderTest {
  private Dependency dependency;

  Provider2(Dependency dependency) {
    this.dependency = dependency;
  }
}

@Component
class Provider3 implements CarInsuranceProviderTest {
  private Dependency dependency;

  Provider3(Dependency dependency) {
    this.dependency = dependency;
  }
}
