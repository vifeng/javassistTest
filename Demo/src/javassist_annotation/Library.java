package javassist_annotation;

@Author(aName="Chiba", year=2005)
@Book(bName="Dynamic Programming", price=20)
public class Library {
    int x, y;
    public void myPrint(String aName, int year, String bName, int price) {
    	System.out.println("Library= " + aName + " " + year + " " + bName + " " + price);
    }
}