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
  private ArrayList<BankTransaction> list_bank_transaction = new ArrayList<BankTransaction>();
  private ArrayList<Bank> list_bank_active = new ArrayList<Bank>();
  private ArrayList<Bank> list_bank_stopped = new ArrayList<Bank>();
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
        if (is_activated(t.bank_dest)){
          BankTransaction bank = this.find_bank(t.bank_dest);
          InterBankSender ibs = new InterBankSender(t, bank, t.transaction_confirme);
          ibs.start();
          //bank.recevoirTransaction(t);
          System.out.println("------------------------------------------");
          System.out.println("A live transaction of "+t.amount+"$ was successffuly sent to Bank "+t.bank_dest+" by Bank "+t.bank_source);
          System.out.println("------------------------------------------");
        }
        else
        {
          if(is_stopped(t.bank_dest)){
            System.out.println("------------------------------------------");
            System.out.println("Banque "+t.bank_dest+" is not available right now!");
            System.out.println("------------------------------------------");
            this.list_transaction_pending.add(t);
          }
          else
          {
            System.out.println("------------------------------------------");
            System.out.println("Banque "+t.bank_dest+" doesn't exist right now!");
            System.out.println("------------------------------------------");
            this.list_transaction_pending.add(t);
          }

        }

      }
      catch (Exception e)
      {
        System.out.println("ERROR");
        this.list_transaction_pending.add(t);
      }
    }
    else
    {
      System.out.println("------------------------------------------");
      System.out.println("Invalid Operation: Received a Confirmation on a Transaction() Call");
      System.out.println("------------------------------------------");
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
        if (is_activated(t.bank_dest)){
          BankTransaction confirmation_bank = this.find_bank(t.bank_source);
          InterBankSender ibs = new InterBankSender(t, confirmation_bank, t.transaction_confirme);
          ibs.start();
          //confirmation_bank.confirmerTransaction(t);
          System.out.println("------------------------------------------");
          System.out.println("A live confirmation of "+t.amount+"$ was successffuly sent back to Bank "+t.bank_source+" by Bank "+t.bank_dest);
          System.out.println("------------------------------------------");
        }
        else
        {
          if(is_stopped(t.bank_dest)){
            System.out.println("------------------------------------------");
            System.out.println("Banque "+t.bank_dest+" is not available right now!");
            System.out.println("------------------------------------------");
            this.list_transaction_pending.add(t);
          }
          else
          {
            System.out.println("------------------------------------------");
            System.out.println("Banque "+t.bank_dest+" doesn't exist right now!");
            System.out.println("------------------------------------------");
            this.list_transaction_pending.add(t);
          }

      }
    }
      catch (Exception e)
      {
        System.out.println("------------------------------------------");
        System.out.println("ERROR");
        System.out.println("------------------------------------------");
        this.list_transaction_pending.add(t);
      }
    }
    else
    {
      System.out.println("------------------------------------------");
      System.out.println("Invalid Operation: Received a Confirmation on a Transaction() Call");
      System.out.println("------------------------------------------");
    }
    return true;
  }
  private void send_pending_transactions(BankTransaction b)
  {
    System.out.println("------------------------------------------");
    System.out.println("Looking for pending Transaction and Confirmation for Bank"+b.get_num());
    boolean response;
    for (Iterator<Transaction> iter = this.list_transaction_pending.listIterator(); iter.hasNext(); )
    {
      response = false;
      Transaction t = iter.next();
      if (t.bank_dest == b.get_num() && t.num == 1 && !(t.transaction_confirme) && is_activated(t.bank_dest))
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
      else if(t.bank_source == b.get_num() && t.num == 1 && t.transaction_confirme && is_activated(t.bank_dest))
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
    System.out.println("------------------------------------------");
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

  private boolean is_activated(int num){
    for (Iterator<Bank> iter = this.list_bank_active.listIterator(); iter.hasNext(); )
    {
      Bank b = iter.next();
      if (b.get_num() == num)
      {
        return true;
      }
    }
    return false;
  }

  private boolean is_stopped(int num){
    for (Iterator<Bank> iter = this.list_bank_stopped.listIterator(); iter.hasNext(); )
    {
      Bank b = iter.next();
      if (b.get_num() == num)
      {
        return true;
      }
    }
    return false;
  }

  private Bank activate_bank(int num)
  {
    for (Iterator<Bank> iter = this.list_bank_stopped.listIterator(); iter.hasNext(); )
    {
      Bank b = iter.next();
      if (b.get_num() == num)
      {
        list_bank_active.add(b);
        list_bank_stopped.remove(b);
        System.out.println("------------------------------------------");
        System.out.println("Bank number "+b.get_num()+" OPEN");
        System.out.println("------------------------------------------");
        return b;
      }
    }
    return null;
  }


  public boolean desactivate_bank(int num)
  {
    for (Iterator<Bank> iter = this.list_bank_active.listIterator(); iter.hasNext(); )
    {
      Bank b = iter.next();
      if (b.get_num() == num)
      {
        list_bank_active.remove(b);
        list_bank_stopped.add(b);
        BankTransaction bt = this.find_bank(num);
        list_bank_transaction.remove(bt);
        System.out.println("------------------------------------------");
        System.out.println("Bank number "+b.get_num()+" CLOSE");
        System.out.println("------------------------------------------");
        // b.close();
        print_banks();
        return true;
      }
    }
    return false;
  }

  public boolean join(int num)
  {
    //Client of Bank
    boolean toReturn = false;

    Bank bankA = null;
    Bank b = this.activate_bank(num);
    if (b != null){
      bankA = b;
      // bankA.open();
    }else{

      /* CLIENT of InterBanK */
      try
      {
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
     InterBank interbank = InterBankHelper.narrow(objRef);
      bankA = new Bank(num, interbank);
      list_bank_active.add(bankA);
    }
    catch (Exception e)
    {
      System.out.println("InterBank Exception while join: " + e.getMessage());
      e.printStackTrace();
    }
    }
    try
    {
      //Server for Clients

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
      NameComponent path1[ ] = ncRef.to_name("app.bankTransaction"+num);
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
      NameComponent path2[ ] = ncRef.to_name("app.bankCustomer"+num);
      ncRef.rebind(path2, bankCustomerRef);
      ServerThread st1 = new ServerThread(orb);
      st1.start();

      list_bank_transaction.add(bankTransactionRef);
      System.out.println("------------------------------------------");
      System.out.println("Bank number "+num+" Added to InterBank");
      System.out.println("------------------------------------------");
      print_banks();
      this.send_pending_transactions(bankTransactionRef);
      toReturn = true;
    }
    catch (Exception e)
    {
      System.out.println("HelloBank Exception: " + e.getMessage());
      e.printStackTrace();
    }

    return toReturn;
  }
  private BankTransaction find_bank(int num)
  {
    BankTransaction toReturn = null;
    for (Iterator<BankTransaction> iter = this.list_bank_transaction.listIterator(); iter.hasNext(); )
    {
      BankTransaction bank = iter.next();
      if (bank.get_num() == num)
      {
          toReturn = bank;
      }
    }
    return toReturn;
  }

  public void print_banks()
  {
    System.out.println("------------ List of Banks ---------------");
    for (Iterator<Bank> iter = this.list_bank_active.listIterator(); iter.hasNext(); )
    {
      Bank b = iter.next();
      System.out.println("** BANK "+b.get_num()+" [OPENED]");
      b.print_accounts();
    }
    for (Iterator<Bank> iter = this.list_bank_stopped.listIterator(); iter.hasNext(); )
    {
      Bank b = iter.next();
      System.out.println("** BANK "+b.get_num()+" [CLOSED]");
      b.print_accounts();
    }
  }

}
