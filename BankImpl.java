import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;


class BankImpl extends BankPOA
{
  private ArrayList<Account> list_account = new ArrayList<Account>();
  private int numeroBank;
  private InterBank interBank;
  public BankImpl(int num, InterBank interBank)
  {
    this.numeroBank = num;
    this.interBank = interBank;
  }
  public int get_num()
  {
    return this.numeroBank;
  }
  public boolean create(int num)
  {
    Account new_account = new Account(num);
    this.list_account.add(new_account);
    return true;
  }
  public boolean destroy(int num)
  {
    this.list_account.remove(this.find_account(num));
    return true;
  }
  private Account find_account(int num)
  {
    Account toReturn = null;
    for (Iterator<Account> iter = this.list_account.listIterator(); iter.hasNext(); )
    {
      Account a = iter.next();
      if (a.get_account_num() == num)
      {
          toReturn = a;
      }
    }
    return toReturn;
  }
  public boolean deposit(int amount, int num)
  {
    Account a = this.find_account(num);
    a.deposit(amount);
    return true;
  }
  public boolean withdraw(int amount, int num)
  {
    Account a = this.find_account(num);
    a.withdraw(amount);
    return true;
  }
  public boolean transfert(int accountNum, int amount, int bankNum, int dest_accountNum)
  {
    boolean toReturn = false;
    if (this.withdraw(amount, accountNum))
    {
      if(bankNum == this.numeroBank)
      {
        this.deposit(amount, dest_accountNum);
      }
      else
      {
        Transaction t = new Transaction(1, amount, accountNum, dest_accountNum, false, this.numeroBank, bankNum);
        this.interBank.envoyerTransaction(t);
      }
      toReturn = true;
    }
    return toReturn;
  }
  public int balance(int num)
  {
    Account a = this.find_account(num);
    return a.balance();
  }
  public boolean recevoirTransaction(Transaction t)
  {
    boolean toReturn = false;
    if(t.bank_dest == this.numeroBank && this.find_account(t.account_destination) != null)
    {
      Account a = this.find_account(t.account_destination);
      a.deposit(t.amount);
      toReturn = true;
      System.out.println("Transfer Recu, Envoie de confirmation...");
    }
    return toReturn;
  }
  public boolean confirmerTransaction(Transaction t)
  {
    System.out.println("Reception de confirmation sur la transaction :)");
    return true;
  }
}
