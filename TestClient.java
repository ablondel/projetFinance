import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import org.omg.CosNaming.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class TestClient
{
  BankCustomer bank1 = null;
  BankCustomer bank2 = null;
  int num1;
  int num2;

  public TestClient(int num1, int num2)
  {
    try{
    //Client of Bank
    org.omg.CORBA.Object objRef;
    org.omg.CORBA.Object objRef2;
    String test[] = new String[2];
    test[0] = "-ORBInitRef";
    test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
    ORB orb = ORB.init(test, null);
    objRef = orb.resolve_initial_references("NameService");
    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
    objRef = ncRef.resolve_str("app.bankCustomer"+num1);
    bank1 = BankCustomerHelper.narrow(objRef);
    this.num1 = num1;
    objRef2 = ncRef.resolve_str("app.bankCustomer"+num2);
    bank2 = BankCustomerHelper.narrow(objRef2);
    this.num2 = num2;
  }
  catch (Exception e)
  {
    System.out.println("HelloInterBank Exception: " + e.getMessage());
    e.printStackTrace();
  }
  }

  public void testCreate(){
    boolean response = bank1.create(num1);
    assert response == true : "testCreate : error";
    response = bank2.create(num2);
    assert response == true : "testCreate : error";
  }

  public void testDeposit(){
    int balance = bank1.balance(num1);
    boolean response = bank1.deposit(20, num1);
    assert bank1.balance(num1) == balance + 20 : "testDeposit : error";
    response = bank1.deposit(-20, num1);
    assert bank1.balance(num1) == balance + 20 : "testDeposit : deposit negative ";
  }

  public void testWithdraw(){
    int balance = bank1.balance(num1);
    boolean response = bank1.withdraw(10, num1);

    assert bank1.balance(num1) == balance - 10 : "testWithdraw : error";
    response = bank1.withdraw(100, num1);
    assert bank1.balance(num1) == balance - 10 : "testWithdraw : error";
  }

  public void testTransfert(){
    int balance1 = bank1.balance(num1);
    int balance2 = bank2.balance(num2);
    boolean response = bank1.transfert(num1, 10, num2, num2);
    // assert bank1.balance(num1) == balance1 - 10 : "testTransfert : error";
    // assert bank2.balance(num2) == balance2 + 10 : "testTransfert : error";
  }

  public void lancer() {
        int nbTest = 0;

        System.out.print('.'); nbTest++;
        testCreate();
        System.out.print('.'); nbTest++;
        testDeposit();
        System.out.print('.'); nbTest++;
        testWithdraw();
        System.out.print('.'); nbTest++;
        testTransfert();

        bank1.closeAccount(num1);
        bank2.closeAccount(num2);

        System.out.println("(" + nbTest + "):OK: " + getClass().getName());
    }

}
