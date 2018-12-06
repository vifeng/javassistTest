package Javassist_Essai;

import javassist.*;

/*
 * Ajout avant une méthode sur .java existant
 */
public class Test {
	public static void main(String[] args) throws Exception {
		ClassPool cp = ClassPool.getDefault();
		CtClass cc = cp.get("Javassist_Essai.Hello");
		CtMethod m = cc.getDeclaredMethod("say");
		m.insertBefore("{ System.out.println(\"Avant on imprime : Hello.say():\"); }");
		m.insertAfter("if(false){ System.out.println(\"Après on imprime: Bravo!\"); }"
				+ "else{System.out.println(\"Après on imprime: Bravo on imprime else!\");}");
		Class c = cc.toClass();
		Hello h = (Hello) c.newInstance();
		h.say();
		/*
		 * will print: Avant on imprime : Hello.say(): On est dans la classe Hello:
		 * Bonjour Après on imprime: Bravo!
		 */
	}
}