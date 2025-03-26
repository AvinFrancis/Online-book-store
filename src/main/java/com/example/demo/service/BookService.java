package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        logger.debug("Saving book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        logger.debug("Fetching all books from the database");
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        logger.debug("Looking for book with ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", id);
                    return new ResourceNotFoundException("Book not found with id: " + id);
                });
    }

    public Book updateBook(Long id, Book updatedBook) {
        logger.debug("Updating book with ID: {}", id);
        Book existingBook = getBookById(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setPublishedDate(updatedBook.getPublishedDate());
        logger.debug("Book updated successfully: {}", existingBook.getId());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        logger.warn("Deleting book with ID: {}", id);
        Book existingBook = getBookById(id);
        bookRepository.delete(existingBook);
        logger.warn("Book with ID {} deleted", id);
    }
}
