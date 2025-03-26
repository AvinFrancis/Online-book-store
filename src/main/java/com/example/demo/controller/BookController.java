package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        logger.info("Request to add a book: {}", book.getTitle());
        Book createdBook = bookService.addBook(book);
        logger.info("Book added successfully: {}", createdBook.getId());
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.info("Fetching all books");
        List<Book> books = bookService.getAllBooks();
        logger.info("Retrieved {} books", books.size());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.info("Fetching book with ID: {}", id);
        Book book = bookService.getBookById(id);
        logger.info("Book found: {}", book.getTitle());
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        logger.info("Updating book with ID: {}", id);
        Book book = bookService.updateBook(id, updatedBook);
        logger.info("Book updated successfully: {}", book.getId());
        return new ResponseEntity<>(book, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        logger.warn("Request to delete book with ID: {}", id);
        bookService.deleteBook(id);
        logger.warn("Book deleted: {}", id);
        return new ResponseEntity<>("Book deleted successfully.", HttpStatus.OK);
    }
}
