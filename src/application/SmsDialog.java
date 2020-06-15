package application;


import javafx.stage.Modality;
import javafx.stage.Stage;
import model.SMS;
import model.TelefonException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;


public class SmsDialog extends Stage
{
	private GridPane gridPane;
	private Scene scene;
	private SMS sms;
	private boolean isUebernehmen;
	
	private Label lblZeichen;
	private TextField tfEigeneNr, tfFremdeNr;
	private CheckBox cbxAktiv;
	private Button btUebernehmen, btAbbruch;
	private TextArea taText;

	public SmsDialog()
	{
		isUebernehmen=false;
		initComponents();
		addComponents();
		addHandler();
		initModality(Modality.APPLICATION_MODAL);
		setTitle("SMS bearbeiten");
	}

	private void initComponents()
	{
		gridPane = new GridPane();
		scene = new Scene(gridPane, 400, 150);
		setScene(scene);
		
		tfEigeneNr = new TextField();
		tfFremdeNr = new TextField();
		taText = new TextArea();
		lblZeichen = new Label(" 0 Zeichen");
		cbxAktiv = new CheckBox("aktiv");
		btUebernehmen = new Button("SMS übernehmen");
			btUebernehmen.setPrefWidth(160);
		btAbbruch = new Button("Abbruch");
			btAbbruch.setPrefWidth(100);
	}

	private void addComponents()
	{
		gridPane.add(new Label("Eigene Tel.Nr.:"), 0, 0);
		gridPane.add(new Label("Fremde Tel.Nr.:"), 0, 1);
		gridPane.add(tfEigeneNr, 3, 0, 4, 1);
		gridPane.add(tfFremdeNr, 3, 1, 4, 1);
		gridPane.add(taText, 0, 3, 11, 5);
		gridPane.add(lblZeichen, 0, 8);
		gridPane.add(cbxAktiv, 2, 8);
		gridPane.add(btUebernehmen, 3, 11, 3, 1);
		gridPane.add(btAbbruch, 6, 11, 3, 1);
			
	}
	public void updateAndShow(SMS sms)
	{
		if(sms!=null)
		{
			this.sms=sms;
			isUebernehmen=false;
			tfEigeneNr.setText(sms.getEigeneNr());
			tfFremdeNr.setText(sms.getFremdeNr());
			taText.setText(sms.getText());
			cbxAktiv.setSelected(sms.isAktiv());
			showAndWait();
		}
		else
			Main.showAlert(AlertType.ERROR, "Null. Ref. für updateAndShow(SMS sms) übergeben!!");
		
	}
	private void addHandler()
	{
		btUebernehmen.setOnAction(event -> uebernehmen());
		btAbbruch.setOnAction(event -> close());
		taText.textProperty().addListener(listener -> lblZeichen.setText(new String(taText.getText().length()+" Zeichen")));
	}
	public boolean isUebernehmen()
	{
		return isUebernehmen;
	}
	private void uebernehmen()
	{
		try
		{
			sms.setEigeneNr(tfEigeneNr.getText());
			sms.setFremdeNr(tfFremdeNr.getText());
			sms.setText(taText.getText());
			sms.setAktiv(cbxAktiv.isSelected());
			isUebernehmen=true;
			close();
		} 
		catch (TelefonException e)
		{
			Main.showAlert(AlertType.ERROR,	e.getMessage());
		}
	}
}
