import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;


class Bank {
  private ArrayList<Account> list_account_activate = new ArrayList<Account>();
  private int numeroBank;
  private InterBank interBank;
  private boolean isOpened = true;

  public void open(){
    isOpened = true;
  }

  public void close(){
    isOpened = false;
  }


  public Bank(int num, InterBank interBank)
  {
    this.numeroBank = num;
    this.interBank = interBank;
  }

  public int get_num()
  {
    return this.numeroBank;
  }

  public void rmAccount(int num){
    this.list_account_activate.remove(this.find_account_activate(num));
  }

  public Account find_account_activate(int num)
  {
    Account toReturn = null;
    for (Iterator<Account> iter = this.list_account_activate.listIterator(); iter.hasNext(); )
    {
      Account a = iter.next();
      if (a.get_account_num() == num)
      {
        toReturn = a;
      }
    }
    return toReturn;
  }


  public void create(int num)
  {
    if (isOpened){
      Account new_account = this.find_account_activate(num);
      if (new_account == null){
        new_account = new Account(num);
        this.list_account_activate.add(new_account);
      }

      System.out.println("[BANK "+this.numeroBank+"] Account number "+new_account.get_account_num()+" Added");
      this.interBank.get_pending_transaction(this.numeroBank);
    }
  }


  public boolean depositAccount(int amount, int num){
    if (isOpened){
      if (amount <= 0){
        return false;
      }
      Account a = this.find_account_activate(num);
      if (a == null){
        return false;
      }
      a.deposit(amount);
    }else{
      return false;
    }
    return true;
  }

  public boolean withdrawAccount(int amount, int num){
    if (isOpened){
      Account a = null;
      a =  this.find_account_activate(num);
      if (a == null){
        return false;
      }
      if (a.balance() - amount < 0){
        return false;
      }
      a.withdraw(amount);
    }else{
      return false;
    }
    return true;

  }

  public boolean transfert(int accountNum, int amount, int bankNum, int dest_accountNum)
  {
    boolean toReturn = false;
    if (isOpened && !(dest_accountNum == accountNum && bankNum == this.numeroBank)){

      if (this.withdrawAccount(amount, accountNum))
      {
        this.depositAccount(amount, accountNum);
        if(bankNum == this.numeroBank)
        {
          this.depositAccount(amount, dest_accountNum);
          this.withdrawAccount(amount, accountNum);
        }
        else
        {
          Transaction t = new Transaction(1, amount, accountNum, dest_accountNum, false, this.numeroBank, bankNum);
          //DEBUG//System.out.println("amount: "+t.amount+" Acc_to:"+t.account_destination+" Acc_from:"+t.account_source+" Confirmation?:"+t.transaction_confirme+" B_to:"+t.bank_dest+" B_from:"+t.bank_source);
          System.out.println("[BANK "+this.numeroBank+"] Sending new transfer of "+t.amount+"$ to Bank "+t.bank_dest+" and Account "+t.account_destination);
          this.interBank.envoyerTransaction(t);
        }
        toReturn = true;
      }
    }else{
      return false;
    }
    return toReturn;
  }

  public int balance(int num)
  {
    Account a = this.find_account_activate(num);
    if (a == null){
      return -1;
    }
    return a.balance();
  }

  public boolean recevoirTransaction(Transaction t)
  {
    boolean toReturn = false;
    if (isOpened){
      if(t.bank_dest == this.numeroBank && this.find_account_activate(t.account_destination) != null)
      {
        Account a = this.find_account_activate(t.account_destination);
        if (a == null){
          return false;
        }
        a.deposit(t.amount);
        t.transaction_confirme = true;
        System.out.println("[BANK "+this.numeroBank+"] Money transfer of "+t.amount+"$ received from Bank "+t.bank_source+", Sending confirmation...");
        toReturn = true;
        try
        {
          BankSender bs = new BankSender(t, this.interBank, true);
          bs.start();
          //this.interBank.confirmerTransaction(t);
        }
        catch (Exception e)
        {
          System.out.println("InterBank is not reachable right now.... RIP :(");
        }
      }
    }else{
      return false;
    }
    return toReturn;
  }

  public void print_accounts(){
    for (Iterator<Account> iter = this.list_account_activate.listIterator(); iter.hasNext(); )
    {
      Account a = iter.next();
      System.out.println("\t ** ACCOUNT "+a.get_account_num()+" [OPENED]");
    }
  }

}
