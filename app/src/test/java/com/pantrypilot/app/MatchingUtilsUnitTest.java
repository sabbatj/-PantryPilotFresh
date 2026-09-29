package com.pantrypilot.app;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
public class MatchingUtilsUnitTest {
  private Object call(String method, Class<?>[] types, Object... args) throws Exception {
    Method m=MatchingUtils.class.getDeclaredMethod(method, types);
    m.setAccessible(true); return m.invoke(null,args);
  }
  @Test public void singularPluralAndAliases() throws Exception {
    assertEquals("tomato",call("normalize",new Class[]{String.class}," Tomatoes "));
    assertEquals("onion",call("normalize",new Class[]{String.class},"ONIONS"));
    assertEquals("garlic",call("normalize",new Class[]{String.class},"garlic cloves"));
  }
  @Test public void unitsShareCompatibleDimensions() throws Exception {
    assertEquals(call("dimension",new Class[]{String.class},"kg"),call("dimension",new Class[]{String.class},"g"));
    assertEquals(call("dimension",new Class[]{String.class},"l"),call("dimension",new Class[]{String.class},"ml"));
    assertEquals(call("dimension",new Class[]{String.class},"pcs"),call("dimension",new Class[]{String.class},"pieces"));
  }
  @Test public void baseUnitConversions() throws Exception {
    assertEquals(500.0,(Double)call("baseQuantity",new Class[]{double.class,String.class},0.5,"kg"),0.0001);
    assertEquals(500.0,(Double)call("baseQuantity",new Class[]{double.class,String.class},0.5,"l"),0.0001);
  }
}
