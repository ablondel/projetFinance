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
      int numero_bank = Integer.parseInt(args[0]);
      Bank bankA = new Bank(numero_bank, interbank);
      String test[] = new String[2];
      test[0] = "-ORBInitRef";
      test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
      ORB orb = ORB.init(test, null);
      //Push BankTransaction Object to the NameServer
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("RootPOA");
      POA rootpoa = POAHelper.narrow(objRef);
      rootpoa.the_POAManager().activate();
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      BankImplTransaction bankImplT = new BankImplTransaction(bankA);
      objRef = rootpoa.servant_to_reference(bankImplT);
      BankTransaction bankTransactionRef = BankTransactionHelper.narrow(objRef);
      NameComponent path1[ ] = ncRef.to_name("app.bankTransaction"+numero_bank);
      ncRef.rebind(path1, bankTransactionRef);
      ServerThread st = new ServerThread(orb);
      st.start();

      //Push BankCustomer Object to the NameServer
      org.omg.CORBA.Object objRef2 = orb.resolve_initial_references("RootPOA");
      POA rootpoa2 = POAHelper.narrow(objRef2);
      rootpoa2.the_POAManager().activate();
      BankImplCustomer bankImplC = new BankImplCustomer(bankA);
      objRef2 = rootpoa2.servant_to_reference(bankImplC);
      BankCustomer bankCustomerRef = BankCustomerHelper.narrow(objRef2);
      NameComponent path2[ ] = ncRef.to_name("app.bankCustomer"+numero_bank);
      ncRef.rebind(path2, bankCustomerRef);
      ServerThread st1 = new ServerThread(orb);
      st1.start();

      //Create Call-Back to InterBank
      bankA.declareMyself();

    }
    catch (Exception e)
    {
      System.out.println("HelloBank Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
