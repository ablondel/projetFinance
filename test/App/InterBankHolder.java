package App;

/**
* App/InterBankHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from App.idl
* mercredi 2 novembre 2016 11 h 50 CET
*/

public final class InterBankHolder implements org.omg.CORBA.portable.Streamable
{
  public App.InterBank value = null;

  public InterBankHolder ()
  {
  }

  public InterBankHolder (App.InterBank initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = App.InterBankHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    App.InterBankHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return App.InterBankHelper.type ();
  }

}
