package App;

/**
* App/TransactionHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from App.idl
* Thursday, November 3, 2016 5:52:37 PM CET
*/

public final class TransactionHolder implements org.omg.CORBA.portable.Streamable
{
  public App.Transaction value = null;

  public TransactionHolder ()
  {
  }

  public TransactionHolder (App.Transaction initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = App.TransactionHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    App.TransactionHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return App.TransactionHelper.type ();
  }

}
