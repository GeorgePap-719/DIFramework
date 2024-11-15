package diframework;

@Component
public class MyService {
  private final Dependency dependency;

  MyService(Dependency dependency) {
    this.dependency = dependency;
  }

  public Dependency getDependency() {
    return dependency;
  }
}
