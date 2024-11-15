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
  @Override
  public String toString() {
    return "WaiterImpl2";
  }
}

@Component
class WaiterImpl3 implements WaiterI {
  @Override
  public String toString() {
    return "WaiterImpl3";
  }
}
