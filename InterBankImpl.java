import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import org.omg.CosNaming.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;


class InterBankImpl extends InterBankPOA
{
  private ArrayList<Bank> list_bank = new ArrayList<Bank>();
  private ArrayList<Transaction> list_transaction = new ArrayList<Transaction>();
  private int numeroInterBank = 1;
  public int get_num()
  {
    return this.numeroInterBank;
  }
  public boolean envoyerTransaction(Transaction t)
  {
    list_transaction.add(t);
    Bank bank = this.find_bank(t.bank_dest);
    bank.recevoirTransaction(t);
    this.confirmerTransaction(t);
    return true;
  }
  public boolean confirmerTransaction(Transaction t)
  {
    t.transaction_confirme = true;
    Bank confirmation_bank = this.find_bank(t.bank_source);
    confirmation_bank.recevoirTransaction(t);
    return true;
  }
  public boolean join(int num)
  {
    //Client of Bank
    boolean toReturn = false;
    try
    {
      org.omg.CORBA.Object objRef;
      String test[] = new String[2];
      test[0] = "-ORBInitRef";
      test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
      ORB orb = ORB.init(test, null);
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      objRef = ncRef.resolve_str("app.bank"+num);
      Bank bank1 = BankHelper.narrow(objRef);
      list_bank.add(bank1);
      System.out.println("Bank number "+bank1.get_num()+" Added to InterBank");
      toReturn = true;
    }
    catch (Exception e)
    {
      System.out.println("InterBank Exception while join: " + e.getMessage());
      e.printStackTrace();
    }
    return toReturn;
  }
  private Bank find_bank(int num)
  {
    Bank toReturn = null;
    for (Iterator<Bank> iter = this.list_bank.listIterator(); iter.hasNext(); )
    {
      Bank bank = iter.next();
      if (bank.get_num() == num)
      {
          toReturn = bank;
      }
    }
    return toReturn;
  }
}
