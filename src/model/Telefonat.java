package model;
import java.io.Serializable; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@SuppressWarnings("serial")
public abstract class Telefonat implements Serializable
{
	private String eigeneNr;
	private String fremdeNr;
	private LocalDateTime datum;
	private boolean aktiv;
	
	public Telefonat(String eigeneNr, String fremdeNr, LocalDateTime datum, boolean aktiv) throws TelefonException
	{
		setEigeneNr(eigeneNr);
		setFremdeNr(fremdeNr);
		setDatum(datum);
		setAktiv(aktiv);
	}
	public Telefonat()// für GUI nötig
	{
	}
	//-------------------------------- getter --------------------
	public boolean isAktiv()
	{
		return aktiv;
	}
	public LocalDateTime getDatum()
	{
		return datum;
	}
	public String getEigeneNr()
	{
		return eigeneNr;
	}
	public String getFremdeNr()
	{
		return fremdeNr;
	}
	//-------------------------------- setter --------------------
	public void setAktiv(boolean aktiv)
	{
		this.aktiv = aktiv;
	}
	public void setDatum(LocalDateTime datum) throws TelefonException
	{
		if (datum != null)
			this.datum = datum;
		else
			throw new TelefonException("null-Referenz für Telefonat.setDatum !!");
	}
	public void setEigeneNr(String eigeneNr) throws TelefonException
	{
		checkTelNr(eigeneNr, "Eigen");
		this.eigeneNr = eigeneNr;
	}
	public void setFremdeNr(String fremdeNr) throws TelefonException
	{
		checkTelNr(fremdeNr, "Fremd");
		this.fremdeNr = fremdeNr;
	}
	private void checkTelNr(String telNr, String eigeneOderFremde) throws TelefonException
	{
		if (telNr != null)
			if (telNr.length() >= 10  && telNr.length() <= 15)              // äußerst simpel Prüfungen!!
				if (telNr.charAt(0) == '0' || telNr.charAt(0) == '+')
					try
					{
						Long.parseLong(telNr.substring(1, telNr.length()));  // Exception, wenn nicht nur Ziffern o. '+'  !!
					}
					catch(NumberFormatException e)
					{
						throw new TelefonException(eigeneOderFremde + "Tel.Nr. für set"+eigeneOderFremde+"Nr("+telNr+") enthält nicht nur Ziffern!!");
					}
				else
					throw new TelefonException(eigeneOderFremde + "Tel.Nr. für set"+eigeneOderFremde+"Nr("+telNr+") beginnt nicht mit 0 oder '+' !!");
			else
				throw new TelefonException(eigeneOderFremde + "Tel.Nr. für set"+eigeneOderFremde+"Nr("+telNr+") hat falsche Länge!!");
		else
			throw new TelefonException(eigeneOderFremde + "Tel.Nr. für set"+eigeneOderFremde+"Nr(...) ist null !!");

	}
	//-------------------------------- sonstige --------------------
	public abstract float berechneBetrag();
	public abstract int berechneLaenge(); 
	
	public String toString()
	{
		String zeitString = datum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm"));
		return String.format("eigeneNr: %-12s fremdeNr: %-12s  Zeit: %s", eigeneNr, fremdeNr, zeitString);
	}
	//-------------------------------- Comparatoren --------------------------
	public static class ZeitComparator implements Comparator<Telefonat>
	{
		public int compare(Telefonat t1, Telefonat t2)
		{
			return t1.getDatum().compareTo(t2.getDatum());
		}		
	}
	public static class TelNrComparator implements Comparator<Telefonat>
	{
		boolean eigeneNr;
		public TelNrComparator(boolean eigeneNr)
		{
			this.eigeneNr = eigeneNr;
		}
		public int compare(Telefonat t1, Telefonat t2)
		{
			if (eigeneNr)
				return t1.getEigeneNr().compareTo(t2.getEigeneNr());
			else
				return t1.getFremdeNr().compareTo(t2.getFremdeNr());
		}
	}
	public static class LaengeComparator implements Comparator<Telefonat> // nachträglich für erste GUI-PLÜP dazu-implementiert
	{
		public int compare(Telefonat t1, Telefonat t2)
		{
			return t1.berechneLaenge() - t2.berechneLaenge();  		
		}
	}
	//-------------------------------- für GUI --------------------------
	
	
	
	
	
	
	
}
