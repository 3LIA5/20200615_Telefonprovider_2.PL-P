package application;

import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.*;

public class TelefonateUebersicht extends TableView<Telefonat>
{
	private TableColumn<Telefonat, String>  typCol, eigeneNrCol, fremdeNrCol, textCol, aktCol;
	private TableColumn<Telefonat, Integer> laengeCol;
//	private TableColumn<Telefonat, Boolean> aktCol;
	private MyContextMenu myContextMenu;
	private RootBorderPane rootBorderPane;

	public TelefonateUebersicht(RootBorderPane rootBorderPane)
	{
		this.rootBorderPane=rootBorderPane;
		initAndAddColumns();
		setFactories();
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setOnMouseClicked(event -> {if(event.isPopupTrigger()) myContextMenu.show(this, event.getScreenX(), event.getScreenY());else if(event.getClickCount()>1)rootBorderPane.aendern();});
	}
	
	@SuppressWarnings("unchecked")
	private void initAndAddColumns()
	{
		myContextMenu = new MyContextMenu();
		typCol = new TableColumn<>("Art");
		eigeneNrCol = new TableColumn<>("Eigene Nr.");
		fremdeNrCol = new TableColumn<>("Fremde Nr.");
		laengeCol = new TableColumn<>("Länge in\nSek./Zeich. ");
		textCol = new TableColumn<>("SMS-Text");
			textCol.setPrefWidth(400);
		aktCol = new TableColumn<>("aktiv/passiv");
		
		getColumns().addAll(eigeneNrCol, fremdeNrCol, typCol, aktCol, laengeCol, textCol);
	}	
	private void setFactories()
	{
		eigeneNrCol.setCellValueFactory(new PropertyValueFactory<>("eigeneNr"));
		fremdeNrCol.setCellValueFactory(new PropertyValueFactory<>("fremdeNr"));
		laengeCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().berechneLaenge()));
		typCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getClass().getSimpleName()));		
		aktCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().isAktiv()?"aktiv":"passiv"));
//		aktCol.setCellValueFactory(new PropertyValueFactory<>("aktiv"));
		textCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Telefonat,String>, ObservableValue<String>>()
		{	public ObservableValue<String> call(CellDataFeatures<Telefonat, String> cdf)
			{	return new ObservableValue<String>()
				{	public void removeListener(InvalidationListener listener){}
					public void addListener(InvalidationListener listener){}
					public void removeListener(ChangeListener<? super String> listener){}
					public void addListener(ChangeListener<? super String> listener) {}
					public String getValue()
					{	Telefonat tel = cdf.getValue();
						if (tel instanceof SMS) return ((SMS)tel).getText();
						else return null;
					}
				};
			}
		});
	}

	public void update(List<Telefonat> telefonate)
	{
		getItems().setAll(telefonate);
	}
	
	private class MyContextMenu extends ContextMenu
	{
		private MenuItem aendern,loeschen;
		public MyContextMenu()
		{
			initAndAddComponents();
			addHandler();
		}
		private void initAndAddComponents()
		{
			aendern = new MenuItem("ändern");
			loeschen = new MenuItem("löschen");
			getItems().addAll(aendern,loeschen);			
		}
		private void addHandler()
		{
			aendern.setOnAction(event -> rootBorderPane.aendern());
			loeschen.setOnAction(event -> rootBorderPane.loeschen());			
		}
	}
}
