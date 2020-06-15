package application;

import java.io.File;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import model.SMS;
import model.TelefonException;
import model.Telefonat;
import model.Telefonprovider;

public class RootBorderPane extends BorderPane
{
	private MenuBar menuBar;
	private Menu mDatei, mTelefonate, mHinzufuegen;
	private MenuItem miLaden, miSpeichern, miImportieren, miExportieren, miBeenden,
					 miHinzuGespraech, miHinzuSMS, miLoeschen, miAendern;

	private FlowPane buttonFlowPane;
	private Button btSortLaenge, btSortTelNr, btBeenden;
	private RadioButton rbEigene, rbFremde;
	private ToggleGroup toggleGroup;
	private Telefonprovider telefonprovider;

	private TelefonateUebersicht uebersichtsFenster;
	private SmsDialog smsDialog;
	
	public RootBorderPane()
	{
		initComponents();
		addComponents();
		addHandlers();
		disableComponents(true);
	}
	private void initComponents()
	{
		menuBar = new MenuBar();
		mDatei = new Menu("Datei");
		mTelefonate = new Menu("Telefonate");
		mHinzufuegen = new Menu("Hinzufügen");
		miLaden = new MenuItem("Laden");
		miSpeichern = new MenuItem("Speichern");
		miImportieren = new MenuItem("Importieren");
		miExportieren = new MenuItem("Exportieren");
		miBeenden = new MenuItem("Beenden");
		miHinzuGespraech = new MenuItem("Gespräch");
		miHinzuSMS = new MenuItem("SMS");
		miLoeschen = new MenuItem("Löschen");
		miAendern = new MenuItem("Ändern");
		
		buttonFlowPane = new FlowPane();
			buttonFlowPane.setAlignment(Pos.CENTER);
			buttonFlowPane.setHgap(10);
			buttonFlowPane.setVisible(false);
		btSortLaenge = new Button("Sort nach Länge");
		btSortTelNr = new Button("Sort nach Tel.Nr. ->");
		btBeenden = new Button("Beenden");
		rbEigene = new RadioButton("eigene");
			rbEigene.setSelected(true);
		rbFremde = new RadioButton("fremde");
		toggleGroup = new ToggleGroup();
		
		telefonprovider = new Telefonprovider();
		uebersichtsFenster = new TelefonateUebersicht(this);
			uebersichtsFenster.setVisible(false);
		smsDialog = new SmsDialog();
	}
	private void addComponents()
	{
		mDatei.getItems().addAll(miLaden, miSpeichern, new SeparatorMenuItem(), miImportieren, miExportieren, new SeparatorMenuItem(), miBeenden);
		mHinzufuegen.getItems().addAll(miHinzuGespraech, miHinzuSMS);
		mTelefonate.getItems().addAll(miAendern, mHinzufuegen, miLoeschen);
		menuBar.getMenus().addAll(mDatei, mTelefonate);
		buttonFlowPane.getChildren().addAll(btSortTelNr, rbEigene, rbFremde, btSortLaenge, btBeenden);
		toggleGroup.getToggles().addAll(rbEigene, rbFremde);
		
		setTop(menuBar);
		setCenter(uebersichtsFenster);
		setBottom(buttonFlowPane);
	}
	private void addHandlers()
	{
		miLaden.setOnAction(event -> laden() );
		miSpeichern.setOnAction(event -> speichern() );
		btSortLaenge.setOnAction(event -> sortLaenge() );
		btSortTelNr.setOnAction(event -> sortTelNr() );
		miBeenden.setOnAction(event -> beenden() );
		btBeenden.setOnAction(event -> beenden() );
		miLoeschen.setOnAction(event -> loeschen());
		miHinzuSMS.setOnAction(event -> smsHinzufügen());
		miAendern.setOnAction(event -> aendern());
	}
	private void laden()
	{
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("c:\\scratch"));
		File selected = fc.showOpenDialog(null);
		if (selected != null)
		{
			try
			{
				telefonprovider.loadTelefonate(selected.getAbsolutePath());
				uebersichtsFenster.update(telefonprovider.getTelefonate());
				uebersichtsFenster.setVisible(true);
				buttonFlowPane.setVisible(true);
				disableComponents(false);
			}
			catch (TelefonException e)
			{
				Main.showAlert(AlertType.ERROR, e.getMessage());
			}
		}
		else
			Main.showAlert(AlertType.WARNING, "Keine Datei ausgewählt!");
	}
	private void speichern()
	{
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("c:\\scratch"));
		File selected = fc.showSaveDialog(null);
		if (selected != null)
		{
			try
			{
				telefonprovider.saveTelefonate(selected.getAbsolutePath());
				Main.showAlert(AlertType.INFORMATION, "Datei gespeichert!");
			}
			catch (TelefonException e)
			{
				Main.showAlert(AlertType.ERROR, e.getMessage());
			}
		}
		else
			Main.showAlert(AlertType.WARNING, "Keine Datei gewählt!!");
	}
	private void sortLaenge()
	{
		telefonprovider.sortiereLaenge();
		uebersichtsFenster.update(telefonprovider.getTelefonate());
	}
	private void sortTelNr()
	{
		
		telefonprovider.sortiereTelNr(rbEigene.isSelected());
		uebersichtsFenster.update(telefonprovider.getTelefonate());
	}
	private void disableComponents(boolean disabled)
	{
		miExportieren.setDisable(disabled);
		miSpeichern.setDisable(disabled);
		miLoeschen.setDisable(disabled);
		miAendern.setDisable(disabled);
	}
	public void loeschen()
	{
		List<Telefonat> selected = uebersichtsFenster.getSelectionModel().getSelectedItems();
		if(selected.size()>0)
		{
			telefonprovider.remove(selected);
			uebersichtsFenster.update(telefonprovider.getTelefonate());
		}
		else
			Main.showAlert(AlertType.INFORMATION,"kein Telefonat markiert");
	}
	public void aendern()
	{
		List<Telefonat> sms = uebersichtsFenster.getSelectionModel().getSelectedItems();
		if(sms.size()==1)
			if(sms.get(0) instanceof SMS)
			{
				smsDialog.updateAndShow((SMS)sms.get(0));
				if (smsDialog.isUebernehmen())
					uebersichtsFenster.update(telefonprovider.getTelefonate());
				else
					Main.showAlert(AlertType.INFORMATION, "Bearbeitung durch Nutzer abgebrochen!");
			}
			else
				Main.showAlert(AlertType.INFORMATION, "Es können derzeit nur SMS bearbeitet werden");
		else
			Main.showAlert(AlertType.INFORMATION, "Bitte genau eine SMS auswählen zum bearbeiten!");
	}
	
	private void smsHinzufügen()
	{
		SMS sms;
		try
		{
			sms = new SMS();
			smsDialog.updateAndShow(sms);
			if (smsDialog.isUebernehmen())
			{
				telefonprovider.add(sms);
				uebersichtsFenster.update(telefonprovider.getTelefonate());
			}
			else
				Main.showAlert(AlertType.INFORMATION, "Abbruch durch nutzer!");
		} 
		catch (TelefonException e)
		{
			Main.showAlert(AlertType.ERROR, e.getMessage());
		}



	}
	private void beenden()
	{
		Platform.exit();
	}
}
