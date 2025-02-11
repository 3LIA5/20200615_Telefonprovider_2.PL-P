package model;
import java.time.LocalDateTime;
 
@SuppressWarnings("serial")
public class SMS extends Telefonat
{
	private String text;
	private int kostenProSMS;
	
	public SMS(String eigeneNr, String fremdeNr, LocalDateTime datum,
			boolean aktiv, String text, int kostenProSMS) throws TelefonException
	{
		super(eigeneNr, fremdeNr, datum, aktiv);
		setText(text);
		setKostenProSMS(kostenProSMS);
	}
	public SMS() throws TelefonException  // f�r GUI n�tig
	{
		setDatum(LocalDateTime.now());
	}
	//----------------------------------------- getter ------------------------
	public int getKostenProSMS()
	{
		return kostenProSMS;
	}
	public String getText()
	{
		return text;
	}
	public int berechneLaenge()				// nachtr�glich f�r erste GUI-PL�P dazu-implementiert
	{
		return text.length();
	}
	//----------------------------------------- setter ------------------------
	public void setKostenProSMS(int kostenProSMS) throws TelefonException
	{
		if (kostenProSMS >= 0 && kostenProSMS <= 20)
			this.kostenProSMS = kostenProSMS;
		else
			throw new TelefonException("Falscher Parameterwert f�r SMS.setKostenProSMS("+kostenProSMS+") !!");
	}
	public void setText(String text) throws TelefonException
	{
		if (text != null)
		{
			int laenge = text.length();
			if (laenge <= 740)
				this.text = text;
			else
				throw new TelefonException("SMS-Text ist zu lang in SMS.setText("+laenge+") !!");
		}
		else
			throw new TelefonException("null-Referenz f�r SMS.setText(...) !!");
	}
	//----------------------------------------- sonstige ------------------------
	public float berechneBetrag()
	{
		if (isAktiv())
		{
			int laenge = text.length();
			if (laenge >= 0 && laenge <= 160)
				return kostenProSMS;
			// ab hier L�nge mindestens 161 -> daher Zerlegung auf 153er-Portionen
			int anz = laenge/153 + (laenge%153>0?1:0);
			return anz*kostenProSMS;
		}
		else
			return 0;
	}
	public String toString()
	{
		return String.format("%s, %d Zeichen -> %.1f Cent", super.toString(),text.length(),berechneBetrag());
	}
	//-------------------------------- f�r GUI --------------------------
	
	
	
	
	
	
	
}
