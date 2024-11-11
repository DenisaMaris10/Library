package controller;

import mapper.BookMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import service.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import javax.swing.*;


public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    public BookController(BookView bookView, BookService bookService){
        this.bookView = bookView;
        this.bookService = bookService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();

            if(title.isEmpty() || author.isEmpty()){
                bookView.addDisplayAlertMessage("Save Error", "Problem at Author or Title fields", "Can not have an empty Title or Author field");
            }
            else{
                BookDTO bookDTO = new BookDTOBuilder().setTitle(title).setAuthor(author).build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if(savedBook){
                    bookView.addDisplayAlertMessage("Save Successful", "Book added", "Book was successfully added");
                    bookView.addBookObservableList(bookDTO);
                }
                else{
                    bookView.addDisplayAlertMessage("Save Error", "Problem at adding book to the database", "There was a problem at adding the book to the database. Please try again.");
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



}
