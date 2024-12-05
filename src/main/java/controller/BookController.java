package controller;

import mapper.BookMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Book;
import model.Order;
import model.builder.OrderBuilder;
import service.book.BookService;
import service.order.OrderService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.time.LocalDate;


public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    private final OrderService orderService;
    private Long userId;
    public BookController(BookView bookView, BookService bookService, OrderService orderService, Long userId){
        this.bookView = bookView;
        this.bookService = bookService;
        this.orderService = orderService;
        this.userId = userId;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addOrderButtonListener(new OrderButtonListener());
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            String price = bookView.getPrice();
            String stock = bookView.getStock();

            if(title.isEmpty() || author.isEmpty() || price.isEmpty() || stock.isEmpty()){
                bookView.addDisplayAlertMessage("Save Error", "Problem at Author, Title, Price or Stock fields", "Can not have any empty field");
            }
            else{
                int priceInt = 0, stockInt = 0;
                try{
                    priceInt = Integer.parseInt(price);
                    stockInt = Integer.parseInt(stock);

                    BookDTO bookDTO = new BookDTOBuilder().setTitle(title).setAuthor(author).setPrice(priceInt).setStock(stockInt).build();
                    boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                    if(savedBook){
                        bookView.addDisplayAlertMessage("Save Successful", "Book added", "Book was successfully added");
                        bookView.addBookObservableList(bookDTO);
                    }
                    else{
                        bookView.addDisplayAlertMessage("Save Error", "Problem at adding book to the database", "There was a problem at adding the book to the database. Please try again.");
                    }
                } catch(NumberFormatException e){
                    bookView.addDisplayAlertMessage("Save Error", "Problem at data introduced in Price or Stock fields", "Please introduce numbers for Price and Stock");
                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if(bookDTO != null){
                boolean deletionSuccessful = bookService.delete((BookMapper.convertBookDTOToBook(bookDTO)));
                if(deletionSuccessful){
                    bookView.addDisplayAlertMessage("Delete Successful", "Book deleted", "Book was successfully deleted");
                    bookView.removeBookFromObservableList(bookDTO);
                }
                else {
                    bookView.addDisplayAlertMessage("Delete Error", "Problem at deleting book", "There was a problem with the delete. Please try again");
                }
            }
            else {
                bookView.addDisplayAlertMessage("Delete Error", "Problem at deleting book", "You must select a book before pressing the delete button");
            }
        }
    }


    private class OrderButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            String quantity = bookView.getQuantity();
            int quantityInt = 0;
            try{
                if(!quantity.isEmpty())
                    quantityInt = Integer.parseInt(quantity);
                else
                    quantityInt = 1;

                if (bookDTO != null) {
                    Book book = getBook(bookDTO);
                    if(checkStock(quantityInt, book)) {
                        addOrder(book, quantityInt, bookDTO);
                    }else{
                        bookView.addDisplayAlertMessage("Order Error", "Problem at ordering book", "Please enter a smaller quantity.");
                    }
                } else {
                    bookView.addDisplayAlertMessage("Order Error", "Problem at ordering book", "You must select a book before pressing the order button");
                }

            } catch(NumberFormatException e){
                bookView.addDisplayAlertMessage("Order Error", "Problem at ordering book", "The quantity must be a number");
            }
        }

        private void addOrder(Book book, Integer quantityInt, BookDTO bookDTO){
            Order order = new OrderBuilder().setBookTitle(book.getTitle())
                    .setBookAuthor(book.getAuthor())
                    .setQuantity(quantityInt)
                    .setUserId(userId)
                    .setTotalPrice(book.getPrice() * quantityInt)
                    .setTimestamp(LocalDate.now())
                    .build();

            boolean savedOrder = orderService.save(order);
            if (savedOrder) {
                bookView.addDisplayAlertMessage("Order Successful", "Book ordered", "Book was successfully ordered");
                book.setStock(book.getStock()-quantityInt);

                if(bookService.update(book)){
                    bookView.removeBookFromObservableList(bookDTO);
                    bookView.addBookObservableList(BookMapper.convertBooktoBookDTO(book));
                }
                else {
                    bookView.addDisplayAlertMessage("Order Error", "Problem at ordering book", "There was a problem with the updating book table.");
                }
            } else {
                bookView.addDisplayAlertMessage("Order Error", "Problem at ordering book", "There was a problem with the order. Please try again");
            }
        }

        private Book getBook(BookDTO bookDTO){
            return bookService.findByTitleAndAuthor(BookMapper.convertBookDTOToBook(bookDTO));
        }

        private boolean checkStock(Integer quantity, Book book){
            return (book.getStock()-quantity >= 0);
        }


    }

}
