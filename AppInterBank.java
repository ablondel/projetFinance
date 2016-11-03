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
      ORB orb = ORB.init(args, null);
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("RootPOA");
      POA rootpoa = POAHelper.narrow(objRef);

      rootpoa.the_POAManager().activate();

      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // instanciate the servant
      BankImpl bankImpl = new BankImpl(1);

      // get object reference from servant
      objRef = rootpoa.servant_to_reference(bankImpl);
      // convert the generic CORBA object reference into typed Echo reference
      Bank bankRef = BankHelper.narrow(objRef);

      NameComponent path1[ ] = ncRef.to_name("app.interbank1");
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
