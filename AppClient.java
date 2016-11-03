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
      org.omg.CORBA.Object objRef;
      ORB orb = ORB.init(args, null);
      // create and initialize the ORB
      // get the naming service
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      // resolve the object reference from the naming service
      objRef = ncRef.resolve_str("app.bank1");
      // convert the CORBA object reference into Echo reference
      Bank bank1 = BankHelper.narrow(objRef);
      // remote method invocation
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
