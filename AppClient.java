import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import org.omg.CosNaming.*;



public class AppClient
{
  public static void main(String args[])
  {
    try
    {
      //Client of Bank
      org.omg.CORBA.Object objRef;
      ORB orb = ORB.init(args, null);
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      objRef = ncRef.resolve_str("app.bank1");
      Bank bank1 = BankHelper.narrow(objRef);
      boolean response = bank1.create(1);
      System.out.println(response);
      response = bank1.deposit(1000, 1);
      System.out.println(response);
      int balance = bank1.balance(1);
      System.out.println(balance);
    }
    catch (Exception e)
    {
      System.out.println("BankClient Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
