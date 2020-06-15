package model;
import java.io.*;
import java.util.*;
 
public class Telefonprovider
{
	private LinkedList<Telefonat> telefonate;
	
	public Telefonprovider()
	{
		telefonate = new LinkedList<Telefonat>();
	}
	public Telefonat getTelefonat(int pos) throws TelefonException
	{
		if (pos >= 0 && pos < telefonate.size())
			return telefonate.get(pos);
		else
			throw new TelefonException("Falsche Psition ("+pos+") für getTelefonat(int pos)!");
	}
	public List<Telefonat> getTelefonate()
	{
		return telefonate;
	}
	
	public void add(Telefonat telefonat) throws TelefonException
	{
		if (telefonat != null)
			if ( !telefonate.contains(telefonat))
				telefonate.add(telefonat);
			else
				throw new TelefonException("Telefonat \n   "+telefonat+"\nist schon in der Collection!!");
		else
			throw new TelefonException("Null-Referenz für Telefonprovider.add(telefonat!!");
	}
	public String toString()
	{
		StringBuilder sb = new StringBuilder(1000);
		sb.append("Telefonprovider TurboTel\n-------------------------\n");
		Iterator<Telefonat> it = telefonate.iterator();
		while (it.hasNext())
		{
			sb.append(it.next()).append('\n');
		}
		return sb.toString();
	}
	public String kontaktListe(String telNr)
	{
		StringBuilder sb = new StringBuilder(500);
		sb.append("Rufnummern-Liste für ").append(telNr).
		   append("\n-------------------------------------\n");
		for (Telefonat t : telefonate)
		{
			if (t.getEigeneNr().equals(telNr))
			{
				sb.append(t.getFremdeNr()).append(t.isAktiv()?"   -> aktiv":"   <- passiv").append('\n');
			}
		}
		return sb.toString();
	}
	public void remove(List<Telefonat> toBeDeleted)
	{
		telefonate.removeAll(toBeDeleted);
	}
	public int remove(String telNr) throws TelefonException        // Achtung! nur aktive!!!
	{
		if (telNr != null)
		{
			int anz = 0;
			Iterator<Telefonat> iter = telefonate.iterator();
			Telefonat tel;
			while (iter.hasNext())
			{
				tel = iter.next();
				if (tel.getEigeneNr().equals(telNr) && tel.isAktiv())
				{
					iter.remove();
					anz++;
				}
			}
			return anz;
		}
		else
			throw new TelefonException("Null-Referenz für Telefonprovider.remove(telNr) !!");
	}
	public void remove(int pos) throws TelefonException
	{
		if (pos >= 0 && pos < telefonate.size()) 
			telefonate.remove(pos);
		else
			throw new TelefonException("Falsche Position ("+pos+") für remove(pos) !!");
	}
	public float berechneGesamtertrag()
	{
		float ertrag = 0f;
		for (Telefonat tel : telefonate)
		{
			ertrag += tel.berechneBetrag();
		}
		return ertrag;
	}
	public int berechneAverageDauer() throws TelefonException
	{
		int summe = 0, anz = 0;
		for (Telefonat tel : telefonate)
		{
			if (tel instanceof Gespraech)
			{
				
				summe += ((Gespraech)tel).getDauer();
				anz++;
			}
		}
		if (anz > 0)
			return summe/anz;
		else
			throw new TelefonException("Keine Gespräche für Telefonprovider.berechneAverageDauer gespeichert!!");
	}
	public int berechneAverageLaenge() throws TelefonException
	{
		int summe = 0, anz = 0;
		for (Telefonat tel : telefonate)
		{
			if (tel instanceof SMS)
			{
				
				summe += ((SMS)tel).getText().length();
				anz++;
			}
		}
		if (anz > 0)
			return summe/anz;
		else
			throw new TelefonException("Keine SMS für Telefonprovider.berechneAverageLaenge gespeichert!!");
	}
	public void sortiereZeit()
	{
		Collections.sort(telefonate, new Telefonat.ZeitComparator());
	}
	public void sortiereTelNr(boolean eigene)
	{
		Collections.sort(telefonate, new Telefonat.TelNrComparator(eigene));
	}
	public void sortiereLaenge()
	{
		Collections.sort(telefonate, new Telefonat.LaengeComparator());
	}
	public String rechnung(String eigeneNr) throws TelefonException
	{
		if (eigeneNr != null)
		{
			float summe = 0f;
			StringBuilder sb = new StringBuilder(1000);
			sb.append("Telefon-Abrechnung für Rufnummer  ").append(eigeneNr)
			  .append("\n---------------------------------------------\n");
			for (Telefonat tel : telefonate)
			{
				if (tel.getEigeneNr().equals(eigeneNr) && tel.isAktiv())
				{
					summe += tel.berechneBetrag();
					sb.append(tel).append('\n');
				}
			}
			sb.append("---------------------------------------------\n")
			  .append("Gesamt-Betrag: ").append(summe);
			return sb.toString();
		}
		else
			throw new TelefonException("Null-Referenz für Telefonprovider.rechnung(eigeneNr) !!");
	}
	//------------------------------------ Files ------------------------------------
	public void saveTelefonate(String filename) throws TelefonException
	{
		if (filename != null)
		{
			try
			{
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
				oos.writeObject(telefonate);
				oos.close();
			}
			catch (FileNotFoundException e)
			{
				throw new TelefonException("FileNotFoundException bei Telefonprovider.saveTelefonate("+filename+") !!");
			}
			catch (IOException e)
			{
				throw new TelefonException("IOException bei Telefonprovider.saveTelefonate("+filename+") !!");
			}
		}
		else
			throw new TelefonException("null-Referenz für filename bei Telefonprovider.saveTelefonate(String filename)  !!");
	}
	@SuppressWarnings("unchecked")
	public void loadTelefonate(String filename) throws TelefonException
	{
		if (filename != null)
		{
			try
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
				telefonate.addAll((LinkedList<Telefonat>)ois.readObject());
				ois.close();
			}
			catch (FileNotFoundException e)
			{
				throw new TelefonException("FileNotFoundException bei Telefonprovider.loadTelefonate("+filename+") !!");
			}
			catch (IOException e)
			{
				throw new TelefonException("IOException bei Telefonprovider.loadTelefonate("+filename+") !!");
			}
			catch (ClassNotFoundException e)
			{
				throw new TelefonException("ClassNotFoundException bei Telefonprovider.loadTelefonate("+filename+") !!");
			}
		}
		else
			throw new TelefonException("null-Referenz für filename bei Telefonprovider.loadTelefonate(String filename)  !!");
	}
	// --------------------------------------- für GUI ----------------------------------------------

	
	
	
	
	
	
	
}
