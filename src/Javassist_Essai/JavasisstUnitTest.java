package Javassist_Essai;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Mnemonic;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Différent tests de Javaassist
 * @author Virg
 *
 */
public class JavasisstUnitTest {
	/*
	 * Generating a Java Class. Let’s say that we want to generate a
	 * JavassistGeneratedClass class that implements a java.lang.Cloneable
	 * interface. We want that class to have an id field of int type. The ClassFile
	 * is used to create a new class file and FieldInfo is used to add a new field
	 * to a class:
	 */
	@Test
	public void givenJavasisstAPI_whenConstructClass_thenGenerateAClassFile() throws CannotCompileException,
			IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

		// Creation of a new class with an interface
		String classNameWithPackage = "Javassist_Essai.JavassistGeneratedClass";
		ClassFile cf = new ClassFile(false, classNameWithPackage, null);
		cf.setInterfaces(new String[] { "java.lang.Cloneable" });
		// New field added
		FieldInfo f = new FieldInfo(cf.getConstPool(), "id", "I");
		f.setAccessFlags(AccessFlag.PUBLIC);
		cf.addField(f);

		String className = "JavassistGeneratedClass.class";
		cf.write(new DataOutputStream(new FileOutputStream(className)));

		ClassPool classPool = ClassPool.getDefault();
		Field[] fields = classPool.makeClass(cf).toClass().getFields();
		assertEquals(fields[0].getName(), "id");

		String classContent = new String(Files.readAllBytes(Paths.get(className)));
		assertTrue(classContent.contains("java/lang/Cloneable"));
	}

	/*
	 * Loading Bytecode Instructions of Class. If we want to load bytecode
	 * instructions of an already existing class method, we can get a CodeAttribute
	 * of a specific method of the class. Then we can get a CodeIterator to iterate
	 * over all bytecode instructions of that method.
	 */
	@Test
	public void givenJavaClass_whenLoadAtByJavassist_thenTraversWholeClass()
			throws NotFoundException, CannotCompileException, BadBytecode {
		// given
		ClassPool cp = ClassPool.getDefault();
		ClassFile cf = cp.get("Javassist_Essai.Point").getClassFile();
		MethodInfo minfo = cf.getMethod("move");
		CodeAttribute ca = minfo.getCodeAttribute();
		CodeIterator ci = ca.iterator();

		// when
		List<String> operations = new LinkedList<>();
		while (ci.hasNext()) {
			int index = ci.next();
			int op = ci.byteAt(index);
			operations.add(Mnemonic.OPCODE[op]);
		}

		// then
		assertEquals(operations,
				Arrays.asList("aload_0", "iload_1", "putfield", "aload_0", "iload_2", "putfield", "return"));

	}

	/*
	 * Adding Fields to Existing Class Bytecode Let’s say that we want to add a
	 * field of int type to the bytecode of the existing class. We can load that
	 * class using ClassPoll and add a field into it:
	 */
	@Test
	public void givenTableOfInstructions_whenAddNewInstruction_thenShouldConstructProperSequence()
			throws NotFoundException, BadBytecode, CannotCompileException, IllegalAccessException,
			InstantiationException {
		// given - Récupère Point
		ClassFile cf = ClassPool.getDefault().get("Javassist_Essai.Point").getClassFile();

		// when - ajout d'un champs
		FieldInfo f = new FieldInfo(cf.getConstPool(), "id", "I");
		f.setAccessFlags(AccessFlag.PUBLIC);
		cf.addField(f);

		// Then - vérification de l'ajout
		ClassPool classPool = ClassPool.getDefault();
		Field[] fields = classPool.makeClass(cf).toClass().getFields();
		List<String> fieldsList = Stream.of(fields).map(Field::getName).collect(Collectors.toList());
		for (String s : fieldsList)
			System.out.print(s);
		assertTrue(fieldsList.contains("id"));

	}

	/**
	 * Adding Constructor to Class Bytecode. We can add a constructor to the
	 * existing class Point by using an addInvokespecial() method.
	 * And we can add a parameterless constructor by invoking a <init> method from
	 * java.lang.Object class:
	 * 
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 * @throws BadBytecode
	 */
	@Test
	public void givenLoadedClass_whenAddConstructorToClass_shouldCreateClassWithConstructor()
			throws NotFoundException, CannotCompileException, BadBytecode {
		// given
		ClassFile cf = ClassPool.getDefault().get("Javassist_Essai.Point").getClassFile();
		Bytecode code = new Bytecode(cf.getConstPool());
		code.addAload(0);
		code.addInvokespecial("java/lang/Object", MethodInfo.nameInit, "()");
		code.addReturn(null);

		// when
		MethodInfo minfo = new MethodInfo(cf.getConstPool(), MethodInfo.nameInit, "()V");
		minfo.setCodeAttribute(code.toCodeAttribute());
		cf.addMethod(minfo);

		// then
		CodeIterator ci = code.toCodeAttribute().iterator();
		List<String> operations = new LinkedList<>();
		while (ci.hasNext()) {
			int index = ci.next();
			int op = ci.byteAt(index);
			operations.add(Mnemonic.OPCODE[op]);
		}

		assertEquals(operations, Arrays.asList("aload_0", "invokespecial", "return"));

	}
	
}
