package de.lmu.ifi.sosylab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class MyObjectTest {

  private MyObject myObject;

  @BeforeAll
  void setUp() {
    myObject = new MyObject();
  }

  @AfterAll
  void tearDown() {
    myObject = null;
  }

  @Test
  void someValue_hasCorrectValue() {
    assertNotNull(myObject, "Expected myObject to be non-null");
    assertEquals(myObject.getSomeValue(), 10);
  }

  @Disabled
  void expectedError() {
    // Fail this method in order to test gitlab CI
    fail("Expected to test gitlab CI");
  }
}
