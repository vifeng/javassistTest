package javassist_annotation;



import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ClassFileWriter;
import javassist.bytecode.FieldInfo;

/**
 * Tests sur les annotations et javassist
 * 
 * @author Virg
 *
 */
public class Test {
	public static void main(String[] args) throws Exception {
		monTest1();
		monTest2();
		monTest3();
		

	}

	/**
	 * Récupère les variables d'une annotation et les imprime
	 * input=@Book(bName="Dynamic Programming", price=20) outpout=book name: Dynamic
	 * Programming, price: 20
	 * 
	 * @throws Exception
	 */
	public static void monTest1() throws Exception {
		CtClass cc = ClassPool.getDefault().get("javassist_annotation.LibraryComputer");
		Object[] all = cc.getAnnotations();
		Book b = (Book) all[0];
		String name = b.bName();
		int price = b.price();
		System.out.println("book name: " + name + ", price: " + price);
	}

	/**
	 * Récupère les variables d'une annotation et les imprime
	 * input=@Book(bName="Peur dans la nuit", price=8) output=book name: Peur dans
	 * la nuit, price: 8
	 * 
	 * @throws Exception
	 */
	public static void monTest2() throws Exception {
		CtClass cc = ClassPool.getDefault().get("javassist_annotation.LibraryThriller");
		Object[] all = cc.getAnnotations();
		Book b = (Book) all[0];
		String name = b.bName();
		int price = b.price();
		System.out.println("book name: " + name + ", price: " + price);
	}

	/**
	 * Créer une nouvelle classe selon la variable contenu dans l'annotation 
	 * Example:
	 * input=book name: Peur dans la nuit, price: 8
	 * outut= nouvelle class Thriller 
	 * input =@Book(bName="Dynamic Programming", price=20)
	 * output= nouvelle class Computer
	 * @throws Exception
	 */
	public static void monTest3() throws Exception {
		CtClass cc = ClassPool.getDefault().get("javassist_annotation.LibraryThriller");
		//CtClass cc = ClassPool.getDefault().get("javassist_annotation.LibraryComputer");
		
		Object[] all = cc.getAnnotations();
		Book b = (Book) all[0];
		String name = b.bName();
		int price = b.price();		
		if (name.equals("Peur dans la nuit")){
			  ClassPool pool = ClassPool.getDefault();
			  CtClass cthriller = pool.makeClass("Thriller");
//			  Simple way
			  CtField f = CtField.make("public int myfield = 42;", cthriller);
			  cthriller.addField(f);
//			original way			  
//			  CtField f = new CtField(CtClass.intType, "myfield",cthriller);
//			  f.setModifiers(AccessFlag.PUBLIC);
//			  cthriller.addField(f, "42"); // default value 42
			  
			  CtMethod m = CtNewMethod.make(
		                 "public void myprint() {System.out.println(\"bravo!\");}",
		                 cthriller);
			  cthriller.addMethod(m);
			  cthriller.writeFile();
			 

			  //other way
//			  CtMethod m = new CtMethod(CtClass.voidType, "myprint",
//                      new CtClass[] { CtClass.intType }, cthriller);
//				cthriller.addMethod(m);
//				m.setBody("{System.out.println(\"bravo!\");}");
//				cc.setModifiers(cc.getModifiers() & ~Modifier.ABSTRACT);

			  //VERIF / OK
//			  ClassPool classPool = ClassPool.getDefault();
//				Field[] fields = classPool.toClass(cthriller).getFields();
//				assertEquals(fields[0].getName(), "myfield");
//					  
			 
		}else if (name.equals("Dynamic Programming")){
			System.out.println("c'est un Dynamic Programming");
		}
		
	}
}
