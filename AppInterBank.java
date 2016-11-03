import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import org.omg.CosNaming.*;



public class AppInterBank
{
  public static void main(String args[])
  {
    try
    {
      //SERVER for Banks
      ORB orb = ORB.init(args, null);
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("RootPOA");
      POA rootpoa = POAHelper.narrow(objRef);
      rootpoa.the_POAManager().activate();
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      InterBankImpl interBankImpl = new InterBankImpl();
      objRef = rootpoa.servant_to_reference(interBankImpl);
      InterBank interBankRef = InterBankHelper.narrow(objRef);
      NameComponent path1[ ] = ncRef.to_name("app.interbank1");
      ncRef.rebind(path1, interBankRef);
      orb.run();
    }
    catch (Exception e)
    {
      System.out.println("HelloInterBank Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
