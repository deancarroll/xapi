package com.google.gwt.reflect.test;

import static com.google.gwt.reflect.client.GwtReflect.magicClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.reflect.client.GwtReflect;
import com.google.gwt.reflect.client.strategy.ReflectionStrategy;
import com.google.gwt.reflect.test.cases.ReflectionCaseKeepsEverything;
import com.google.gwt.reflect.test.cases.ReflectionCaseKeepsNothing;
import com.google.gwt.reflect.test.cases.ReflectionCaseNoMagic;
import com.google.gwt.reflect.test.cases.ReflectionCaseSubclass;
import com.google.gwt.reflect.test.cases.ReflectionCaseSuperclass;

/**
 * @author James X. Nelson (james@wetheinter.net)
 */
@ReflectionStrategy(keepEverything=true)
public class MethodTests extends AbstractReflectionTest {

  static {magicClass(MethodTests.class);}
  
  private static final Class<ReflectionCaseSuperclass> SUPER_CLASS = magicClass(ReflectionCaseSuperclass.class);
  private static final Class<ReflectionCaseSubclass> SUB_CLASS = magicClass(ReflectionCaseSubclass.class);
  private static final Class<ReflectionCaseKeepsNothing> KEEPS_NONE = magicClass(ReflectionCaseKeepsNothing.class);
  private static final Class<ReflectionCaseKeepsEverything> KEEPS_EVERYTHING = magicClass(ReflectionCaseKeepsEverything.class);
  private static final Class<ReflectionCaseNoMagic> NO_MAGIC = ReflectionCaseNoMagic.class;
  private static final Class<Object> CLASS_OBJECT = Object.class;
  
  
  private static final String PRIVATE_CALL = "privateCall";
  private static final String PUBLIC_CALL = "publicCall";

  public MethodTests() {}
  
  @Test
  public void testDeclaredMethodDirectly() throws Throwable {
    ReflectionCaseNoMagic superClass = new ReflectionCaseNoMagic();
    assertFalse(superClass.wasPrivateCalled());
    Method m = NO_MAGIC.getDeclaredMethod(PRIVATE_CALL);
    m.setAccessible(true);
    assertNotNull(m);
    m.invoke(superClass);
    assertTrue(superClass.wasPrivateCalled());
  }
  
  @Test
  public void testDeclaredMethodInjectly() throws Throwable {
    ReflectionCaseSuperclass superClass = new ReflectionCaseSuperclass();
    assertFalse(superClass.publicCall);
    Method m = GwtReflect.getDeclaredMethod(SUPER_CLASS, PUBLIC_CALL);
    assertNotNull(m);
    m.invoke(superClass);
    assertTrue(superClass.publicCall);
  }

  @Test
  public void testGetPublicMethodDirectly() throws Throwable {
    ReflectionCaseNoMagic noMagic = new ReflectionCaseNoMagic();
    Method m = NO_MAGIC.getMethod(METHOD_EQUALS, Object.class);
    assertNotNull(m);
    assertTrue((Boolean)m.invoke(noMagic, noMagic));
  }
  
  @Test
  public void testGetPublicMethodInjectly() throws Throwable {
    Method m = GwtReflect.getPublicMethod(NO_MAGIC, METHOD_EQUALS, CLASS_OBJECT);
    assertNotNull(m);
    assertFalse((Boolean)m.invoke(new ReflectionCaseNoMagic(), new ReflectionCaseNoMagic()));
  }
  
  @Test(expected=NoSuchMethodException.class)
  public void testCantAccessPrivateMethods() throws Throwable  {
    SUPER_CLASS.getMethod(PRIVATE_CALL);
  }
  

  private static final Class<?> classInt = int.class;
  private static final Class<?> classLit = classList();
  private static final Class<?> classObject(){return Object.class;};
  private static final Class<?> classList(){return List.class;};
  private static final native Class<?> nativeMethod()
  /*-{
   return @java.util.List::class;
   }-*/;
  private static Class<?>[] classArray() {
    final Class<?>[] array = new Class<?>[]{classInt, classObject()};
    return array;
  }

  @Test
  public void testComplexMethodInjection() throws Exception {
    ArrayList<String> list = new ArrayList<String>();
    Method method = ArrayList.class.getMethod("add", Object.class);
    method.invoke(list, "success");
    assertEquals("success", list.get(0));

    final Class<?>[] array = classArray();
    method = classLit.getMethod("add", array);
    method.invoke(list, 0, "Success!");
    assertEquals("Success!", list.get(0));
  }
  
}