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
      /* CLIENT of InterBanK */
      System.out.println(args[0]);
      // create and initialize the ORB
      org.omg.CORBA.Object objRef;
      String test[] = new String[2];
      test[0] = "-ORBInitRef";
      test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
      ORB orb = ORB.init(test, null);
       // get the naming service
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      // resolve the object reference from the naming service
      objRef = ncRef.resolve_str("app.interbank");
      // convert the CORBA object reference into Echo reference
      interbank = InterBankHelper.narrow(objRef);
    }
    catch (Exception e)
    {
      System.out.println("HelloInterBank Exception: " + e.getMessage());
      e.printStackTrace();
    }
    try
    {
      /* Server for Clients */
      int numero_bank = Integer.parseInt(args[0]);
      Bank bankA = new Bank(numero_bank, interbank);
      String test[] = new String[2];
      test[0] = "-ORBInitRef";
      test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
      ORB orb = ORB.init(test, null);

      /** Push BankTransaction Object to the NameServer **/
      // get reference to rootpoa & activate the POAManager
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("RootPOA");
      POA rootpoa = POAHelper.narrow(objRef);
      // Create the Persistent Policy
      Policy[] persistentPolicy = new Policy[1];
      persistentPolicy[0] = rootpoa.create_lifespan_policy(LifespanPolicyValue.PERSISTENT);
      // Create a POA by passing the Persistent Policy
      POA persistentPOA = rootpoa.create_POA("childPOAT"+numero_bank, null, persistentPolicy );
      persistentPOA.the_POAManager().activate();
      // instanciate the servant
      BankImplTransaction bankImplT = new BankImplTransaction(bankA);
      persistentPOA.activate_object( bankImplT );
      // get the naming service
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      // convert the generic CORBA object reference into typed BankTransaction reference
      objRef = persistentPOA.servant_to_reference(bankImplT);
      BankTransaction bankTransactionRef = BankTransactionHelper.narrow(objRef);
      // bind the object reference in the naming service
      NameComponent path1[ ] = ncRef.to_name("app.bankTransaction"+numero_bank);
      ncRef.rebind(path1, bankTransactionRef);
      ServerThread st = new ServerThread(orb);
      st.start(); // start server

      /** Push BankCustomer Object to the NameServer **/
      // get reference to rootpoa & activate the POAManager
      org.omg.CORBA.Object objRef2 = orb.resolve_initial_references("RootPOA");
      POA rootpoa2 = POAHelper.narrow(objRef2);


      // Create the Persistent Policy
      Policy[] persistentPolicy2 = new Policy[1];
      persistentPolicy2[0] = rootpoa2.create_lifespan_policy(LifespanPolicyValue.PERSISTENT);
      // Create a POA by passing the Persistent Policy
      POA persistentPOA2 = rootpoa2.create_POA("childPOAC"+numero_bank, null, persistentPolicy );
      persistentPOA2.the_POAManager().activate();
      // instanciate the servant
      BankImplCustomer bankImplC = new BankImplCustomer(bankA);
      persistentPOA2.activate_object( bankImplC );
      objRef2 = persistentPOA2.servant_to_reference(bankImplC);
      // convert the generic CORBA object reference into typed BankCustomer reference
      BankCustomer bankCustomerRef = BankCustomerHelper.narrow(objRef2);
      // bind the object reference in the naming service
      NameComponent path2[ ] = ncRef.to_name("app.bankCustomer"+numero_bank);
      ncRef.rebind(path2, bankCustomerRef);
      ServerThread st1 = new ServerThread(orb);
      st1.start(); // start server

      /* Create Call-Back to InterBank */
      bankA.declareMyself();

    }
    catch (Exception e)
    {
      System.out.println("HelloBank Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
