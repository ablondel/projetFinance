import App.*;

class Account
{
  private int balance_total = 0;
  private int numAccount;
  public Account(int num)
  {
    this.numAccount = num;
  }
  public void deposit(int amount)
  {
      this.balance_total += amount;
  }
  public void withdraw(int amount)
  {
      this.balance_total -= amount;
  }
  public int balance()
  {
      return this.balance_total;
  }
  public int get_account_num()
  {
    return this.numAccount;
  }
}
