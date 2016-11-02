package App;


/**
* App/BankOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from App.idl
* mercredi 2 novembre 2016 11 h 50 CET
*/

public interface BankOperations 
{
  boolean create (int num);
  boolean destroy (int num);
  boolean deposit (int amount, int num);
  boolean withdraw (int amount, int num);
  boolean transfert (int amount, int numBank, int num);
  int balance (int num);
  boolean recevoirTransaction (App.Transaction t);
  boolean confirmerTransaction (App.Transaction t);
} // interface BankOperations
