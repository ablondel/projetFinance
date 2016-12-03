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
    Properties properties = System.getProperties();
    properties.put( "org.omg.CORBA.ORBInitialHost", "127.0.0.1" );
    properties.put( "org.omg.CORBA.ORBInitialPort","2810" );
    try
    {
      /* SERVER for Banks */
      String test[] = new String[2];
      test[0] = "-ORBInitRef";
      test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
      ORB orb = ORB.init(test, null);
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("RootPOA");
      POA rootpoa = POAHelper.narrow(objRef);

      //Create the Persistent Policy
      Policy[] persistentPolicy = new Policy[1];
      persistentPolicy[0] = rootpoa.create_lifespan_policy(LifespanPolicyValue.PERSISTENT);
      //Create a POA by passing the Persistent Policy
      POA persistentPOA = rootpoa.create_POA("childPOA", null, persistentPolicy );
      persistentPOA.the_POAManager().activate();

      InterBankImpl interBankImpl = new InterBankImpl();
      persistentPOA.activate_object( interBankImpl );

      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      objRef = persistentPOA.servant_to_reference(interBankImpl);
      InterBank interBankRef = InterBankHelper.narrow(objRef);
      NameComponent path1[ ] = ncRef.to_name("app.interbank");
      ncRef.rebind(path1, interBankRef);
      orb.run();
    }
    catch (Exception e)
    {
      System.err.println("Exception in Persistent Server Startup: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
