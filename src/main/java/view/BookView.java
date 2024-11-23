package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import org.w3c.dom.Text;
import view.model.BookDTO;

import java.util.List;

public class BookView {
    private TableView bookTableView;
    //private TableView orderTableView;
    private final ObservableList<BookDTO> booksObservableList;
    //private final ObservableList<Order> ordersObservableList;
    private TextField authorTextField;
    private TextField titleTextField;
    private TextField bookPriceTextField;
    private TextField bookStockTextField;
    private TextField quantityTextField;
    private Label authorLabel;
    private Label titleLabel;
    private Label bookPriceLabel;
    private Label bookStockLabel;
    private Label quantityLabel;
    private Button saveButton;
    private Button deleteButton;
    private Button orderButton;
    private Scene scene;

    public BookView(Stage primaryStage, List<BookDTO> books){
        primaryStage.setTitle("Library");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane); // vom adauga un tabel in gridPane

        Scene scene = new Scene(gridPane, 900, 900);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(books); //daca schimbam referinta la booksObservableList pierdem legatura cu tabelul
        initTableView(gridPane);
        initSaveBookOptions(gridPane);
        initOrderBooksOptions(gridPane);
        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane){
        bookTableView = new TableView<BookDTO>();
        bookTableView.setPlaceholder(new Label("No books to be displayed"));
        TableColumn<BookDTO, String> titleColumn = new TableColumn<BookDTO, String>("Title"); // numele coloanei efective
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<BookDTO, String> authorColumn = new TableColumn<BookDTO, String>("Author"); // numele coloanei efective
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<BookDTO, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<BookDTO, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));


        bookTableView.getColumns().addAll(titleColumn, authorColumn, priceColumn, stockColumn);
        bookTableView.setItems(booksObservableList);
        gridPane.add(bookTableView, 0, 0, 5, 1);
    }

    private void initSaveBookOptions(GridPane gridPane){

        titleLabel = new Label("Title");
        gridPane.add(titleLabel, 1, 1);
        titleTextField = new TextField();
        gridPane.add(titleTextField, 2, 1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel, 3, 1);
        authorTextField = new TextField();
        gridPane.add(authorTextField, 4, 1);

        bookPriceLabel = new Label("Price");
        gridPane.add(bookPriceLabel, 1, 2);
        bookPriceTextField = new TextField();
        gridPane.add(bookPriceTextField, 2, 2);

        bookStockLabel = new Label("Stock");
        gridPane.add(bookStockLabel, 3, 2);
        bookStockTextField = new TextField();
        gridPane.add(bookStockTextField, 4, 2);

        saveButton = new Button("Save");
        gridPane.add(saveButton, 5, 1);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 6, 1);
    }

    private void initOrderBooksOptions(GridPane gridPane){
        quantityLabel = new Label("Quantity ");
        gridPane.add(quantityLabel, 5, 0);

        quantityTextField = new TextField();
        quantityTextField.setPromptText("Ex. 2");
        gridPane.add(quantityTextField, 6, 0);

        orderButton = new Button("Order");
        gridPane.add(orderButton, 7, 0);
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener){
        saveButton.setOnAction(saveButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addOrderButtonListener(EventHandler<ActionEvent> orderButtonListener){
        orderButton.setOnAction(orderButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public String getTitle(){
        return titleTextField.getText();
    }

    public String getAuthor(){
        return authorTextField.getText();
    }

    public String getPrice(){
        return bookPriceTextField.getText();
    }

    public String getStock(){
        return bookStockTextField.getText();
    }

    public String getQuantity(){
        return quantityTextField.getText();
    }

    public void addBookObservableList(BookDTO bookDTO){
        this.booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO){
        this.booksObservableList.remove(bookDTO);
    }

    public TableView getBookTableView(){
        return bookTableView;
    }

}
