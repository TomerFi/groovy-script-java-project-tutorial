package info.tomfi.tutorials;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public final class VariousApproachTest {
  @TestFactory
  Collection<DynamicTest> testApproaches() {
    return List.of(
      dynamicTest("test method approach", () -> assertApproach(MethodApproach.class)),
      dynamicTest("test bi-function approach", () -> assertApproach(BiFunctionApproach.class)),
      dynamicTest("test function currying approach", () -> assertApproach(FunctionCurryingApproach.class)),
      dynamicTest("test groovy script approach", () -> assertApproach(GroovyScriptApproach.class))
      );
  }

  private void assertApproach(final Class<? extends ApproachInterface> approach)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    assertThat(
      approach.getConstructor().newInstance().getMessage("tomer", 6))
    .startsWith("Hello tomer, you're number 6");
  }
}
