package App;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import java.util.ArrayList;



class BankImpl extends BankPOA
{
  private ArrayList<Account> list_account = new ArrayList<Account>();

  public BankImpl(long num){
    this.numeroBank = num;
  }
  public boolean create(long num)
  {
    Account new_account = new Account(num);
    this.list_account.add(new_account);
  }
  public boolean destroy(long num)
  {
    this.list_account.removeAll(this.find_account(num));
  }
  private Account find_account(long num)
  {
    for (Iterator<Account> iter = this.list_account.listIterator(); iter.hasNext(); )
    {
      Account a = iter.next();
      if (a.get_account_num() == num)
      {
          return a;
      }
    }
  }
  public boolean deposit(long amount, long num)
  {
    Account a = this.find_account(num);
    a.deposit(amount);
    return True;
  }
  public boolean withdraw(long amount, long num)
  {
    Account a = this.find_account(num);
    a.withdraw(amount);
    return True;
  }
  public boolean transfert(long amount, long bankNum, long accountNum)
  {

  }
  public long balance(long num)
  {
    Account a = this.find_account(num);
    return a.balance();
  }
  public boolean recevoirTransaction(Transaction t)
  {

  }
  public boolean confirmerTransaction(Transaction t)
  {

  }
}
