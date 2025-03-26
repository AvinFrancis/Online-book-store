package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        // Manually initialize Mockito
        MockitoAnnotations.openMocks(this);
        
        // Create a sample book
        book = new Book("Test Book", "Author Name", BigDecimal.valueOf(29.99), LocalDate.now());
        book.setId(1L);
    }

    @Test
    void addBook_ShouldReturnSavedBook() {
        when(bookRepository.save(book)).thenReturn(book);
        Book savedBook = bookService.addBook(book);
        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
    }

    @Test
    void getAllBooks_ShouldReturnBookList() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> result = bookService.getAllBooks();
        assertEquals(1, result.size());
    }

    @Test
    void getBookById_ShouldReturnBook_WhenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Book foundBook = bookService.getBookById(1L);
        assertNotNull(foundBook);
        assertEquals(1L, foundBook.getId());
    }

    @Test
    void getBookById_ShouldThrowException_WhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() {
        Book updatedBook = new Book("Updated Title", "Updated Author", BigDecimal.valueOf(39.99), LocalDate.now());
        updatedBook.setId(1L);
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.updateBook(1L, updatedBook);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void deleteBook_ShouldDeleteBook_WhenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository, times(1)).delete(book);
    }
}
