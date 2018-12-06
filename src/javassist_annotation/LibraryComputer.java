package javassist_annotation;

@Book(bName="Dynamic Programming", price=20)
public class LibraryComputer {
	String computer;
	public void myPrint(String bName, int price) {
    	System.out.println("LibraryComputer= "+ bName + " " + price);
    }
}
