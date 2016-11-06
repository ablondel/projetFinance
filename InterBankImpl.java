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
  private ArrayList<BankTransaction> list_bank = new ArrayList<BankTransaction>();
  private ArrayList<Transaction> list_transaction_done = new ArrayList<Transaction>();
  private ArrayList<Transaction> list_transaction_pending = new ArrayList<Transaction>();
  private int numeroInterBank = 1;
  public int get_num()
  {
    return this.numeroInterBank;
  }
  public boolean envoyerTransaction(Transaction t)
  {
    if(!(t.transaction_confirme))
    {
      try
      {
        BankTransaction bank = this.find_bank(t.bank_dest);
        InterBankSender ibs = new InterBankSender(t, bank, t.transaction_confirme);
        ibs.start();
        //bank.recevoirTransaction(t);
        System.out.println("A live transaction of "+t.amount+"$ was successffuly sent to Bank "+t.bank_dest+" by Bank "+t.bank_source);
      }
      catch (Exception e)
      {
        System.out.println("Banque "+t.bank_dest+"is not available right now!");
        this.list_transaction_pending.add(t);
      }
    }
    else
    {
      System.out.println("Invalid Operation: Received a Confirmation on a Transaction() Call");
    }
    return true;
  }
  public boolean confirmerTransaction(Transaction t)
  {
    if(t.transaction_confirme)
    {
      this.list_transaction_done.add(t);
      try
      {
        BankTransaction confirmation_bank = this.find_bank(t.bank_source);
        InterBankSender ibs = new InterBankSender(t, confirmation_bank, t.transaction_confirme);
        ibs.start();
        //confirmation_bank.confirmerTransaction(t);
        System.out.println("A live confirmation of "+t.amount+"$ was successffuly sent back to Bank "+t.bank_source+" by Bank "+t.bank_dest);
      }
      catch (Exception e)
      {
        System.out.println("Bank "+t.bank_dest+" is not available right now!");
        this.list_transaction_pending.add(t);
      }
    }
    else
    {
      System.out.println("Invalid Operation: Received a Transaction on a Confirmation() Call");
    }
    return true;
  }
  private void send_pending_transactions(BankTransaction b)
  {
    System.out.println("Looking for pending Transaction and Confirmation for Bank"+b.get_num());
    boolean response;
    for (Iterator<Transaction> iter = this.list_transaction_pending.listIterator(); iter.hasNext(); )
    {
      response = false;
      Transaction t = iter.next();
      if (t.bank_dest == b.get_num() && t.num == 1 && !(t.transaction_confirme))
      {
          try
          {
            response = b.recevoirTransaction(t);
          }
          catch (Exception e)
          {
            System.out.println("Bank "+b.get_num()+" is not available");
          }
          if(response)
          {
            System.out.println("A pending transaction of "+t.amount+"$ was successffuly sent to Bank "+t.bank_dest+" from Bank "+t.bank_source);
            t.num = 2;
          }
      }
      else if(t.bank_source == b.get_num() && t.num == 1 && t.transaction_confirme)
      {
        try
        {
          response = b.confirmerTransaction(t);
        }
        catch (Exception e)
        {
          System.out.println("Bank "+b.get_num()+" is not available");
        }
        if(response)
        {
          System.out.println("A pending confirmation of "+t.amount+"$ was successffuly sent back to Bank "+t.bank_source+" from Bank "+t.bank_dest);
          t.num = 2;
        }
      }
    }
    this.clean_pending_transactions();
    System.out.println("Done Looking for Pending stuff");
  }
  private void clean_pending_transactions()
  {
    ArrayList<Transaction> cleaned_list = new ArrayList<Transaction>();
    for (Iterator<Transaction> iter = this.list_transaction_pending.listIterator(); iter.hasNext(); )
    {
      Transaction t = iter.next();
      if (t.num == 1)
      {
        cleaned_list.add(t);
      }
    }
    this.list_transaction_pending = cleaned_list;
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
      objRef = ncRef.resolve_str("app.bankTransaction"+num);
      BankTransaction bank1 = BankTransactionHelper.narrow(objRef);
      list_bank.add(bank1);
      System.out.println("Bank number "+bank1.get_num()+" Added to InterBank");
      toReturn = true;
      this.send_pending_transactions(bank1);
    }
    catch (Exception e)
    {
      System.out.println("InterBank Exception while join: " + e.getMessage());
      e.printStackTrace();
    }
    return toReturn;
  }
  private BankTransaction find_bank(int num)
  {
    BankTransaction toReturn = null;
    for (Iterator<BankTransaction> iter = this.list_bank.listIterator(); iter.hasNext(); )
    {
      BankTransaction bank = iter.next();
      if (bank.get_num() == num)
      {
          toReturn = bank;
      }
    }
    return toReturn;
  }
}
