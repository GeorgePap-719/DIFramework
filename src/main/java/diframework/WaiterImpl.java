package diframework;

@Component
public class WaiterImpl implements WaiterI {
  @Override
  public String toString() {
    return "WaiterImpl";
  }
}

@Component
class WaiterImpl2 implements WaiterI {
  private final Person person;

  public WaiterImpl2(Person person) {
    this.person = person;
  }

  @Override
  public String toString() {
    return "WaiterImpl2 " + getPerson().toString();
  }

  public Person getPerson() {
    return person;
  }
}

@Component
class WaiterImpl3 implements WaiterI {
  private final OtherComponent otherComponent;

  public WaiterImpl3(OtherComponent otherComponent) {
    this.otherComponent = otherComponent;
  }

  @Override
  public String toString() {
    return "WaiterImpl3 " + getOtherComponent().toString();
  }

  public OtherComponent getOtherComponent() {
    return otherComponent;
  }
}


@Component
class Person {
  @Override
  public String toString() {
    return "Person";
  }
}

@Component
class OtherComponent {
  @Override
  public String toString() {
    return "OtherComponent";
  }
}