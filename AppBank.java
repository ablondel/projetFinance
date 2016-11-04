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
    InterBank interbank = null;
    try
    {
      //CLIENT of InterBanK
      System.out.println(args[0]);
      org.omg.CORBA.Object objRef;
      String test[] = new String[2];
      test[0] = "-ORBInitRef";
      test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
      ORB orb = ORB.init(test, null);
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      objRef = ncRef.resolve_str("app.interbank1");
      interbank = InterBankHelper.narrow(objRef);
    }
    catch (Exception e)
    {
      System.out.println("HelloInterBank Exception: " + e.getMessage());
      e.printStackTrace();
    }
    try
    {
      //Server for Clients
      String test[] = new String[2];
      test[0] = "-ORBInitRef";
      test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
      ORB orb = ORB.init(test, null);
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("RootPOA");
      POA rootpoa = POAHelper.narrow(objRef);
      rootpoa.the_POAManager().activate();
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      BankImpl bankImpl = new BankImpl(Integer.parseInt(args[0]), interbank);
      objRef = rootpoa.servant_to_reference(bankImpl);
      Bank bankRef = BankHelper.narrow(objRef);
      NameComponent path1[ ] = ncRef.to_name("app.bank"+args[0]);
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
