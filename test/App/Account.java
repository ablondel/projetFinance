package App;

class Account
{
  private long balance_total = 0;
  private long numAccount;
  public Account(long num)
  {
    this.numAccount = num;
  }
  public void deposit(long amount)
  {
      this.balance_total += amount;
  }
  public void withdraw(long amount)
  {
      this.balance_total -= amount;
  }
  public long balance()
  {
      return this.balance_total;
  }
  public long get_account_num()
  {
    return this.numAccount;
  }
}
