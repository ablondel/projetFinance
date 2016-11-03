import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import org.omg.CosNaming.*;



public class AppBank
{
  public static void main(String args[])
  {
    try
    {
      //CLIENT of InterBanK
      org.omg.CORBA.Object objRef;
      ORB orb = ORB.init(args, null);
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      objRef = ncRef.resolve_str("app.interbank1");
      InterBank interbank1 = InterBankHelper.narrow(objRef);
      Transaction t = new Transaction();
      boolean response = interbank1.envoyerTransaction(t);
      System.out.println(response);
    }
    catch (Exception e)
    {
      System.out.println("HelloInterBank Exception: " + e.getMessage());
      e.printStackTrace();
    }
    try
    {
      //Server for Clients
      ORB orb = ORB.init(args, null);
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("RootPOA");
      POA rootpoa = POAHelper.narrow(objRef);
      rootpoa.the_POAManager().activate();
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      BankImpl bankImpl = new BankImpl(1);
      objRef = rootpoa.servant_to_reference(bankImpl);
      Bank bankRef = BankHelper.narrow(objRef);
      NameComponent path1[ ] = ncRef.to_name("app.bank1");
      ncRef.rebind(path1, bankRef);
      orb.run();
    }
    catch (Exception e)
    {
      System.out.println("HelloBank Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
