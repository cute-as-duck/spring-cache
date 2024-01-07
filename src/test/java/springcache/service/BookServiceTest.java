package springcache.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import springcache.model.Book;
import springcache.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BookService bookService;

    @SpyBean
    @Autowired
    private BookRepository bookRepository;

    @Test
    void findBookById() {
        final long bookId = 1L;

        Book book = bookService.findBookById(bookId);
        assertNotNull(book, "Book is not found");
        LOG.info("Book: " + book.getTitle());

        Book cachedBook = bookService.findBookById(bookId);
        assertNotNull(cachedBook, "Book is not found");
        LOG.info("Book: " + book.getTitle());

        verify(bookRepository, times(1)).findById(bookId);

        applicationContext.getBean("cacheManager");
    }

    @Test
    void findBookByIdNullResult() {
        final long bookId = 10L;

        Book book = bookService.findBookById(bookId);
        assertNull(book, "Book is not found");

        Book cachedBook = bookService.findBookById(bookId);
        assertNull(cachedBook, "Book is not found");

        verify(bookRepository, times(2)).findById(bookId);
    }

    @Test
    void testFindBookByTitleAndAuthor() {
        Book book = bookService.findBookByTitleAndAuthor("Bridget Jones", "Helen Fielding");
        assertNotNull(book, "Book is not found");
        LOG.info("Book: " + book.getTitle());

        Book cachedBook = bookService.findBookByTitleAndAuthor("Bridget Jones", "Helen Fielding");
        assertNotNull(cachedBook, "Book is not found");
        LOG.info("Book: " + book.getTitle());

        verify(bookRepository, times(1)).findBookByTitleAndAuthor("Bridget Jones", "Helen Fielding");
    }

    @Test
    void testSaveBookAndPutCache() {
        final long bookId = 4L;

        Book book = new Book(bookId, "And there were none", "Agatha Christie");

        Book savedBook = bookService.saveBook(book);

        Book foundedBook = bookService.findBookById(savedBook.getId());
        assertNotNull(foundedBook, "Book not found");
        LOG.info("Book: " + foundedBook.getTitle());

        verify(bookRepository, never()).findById(bookId);
    }

    @Test
    void testDeleteBookAndCacheEvict() {
        final long bookId = 2L;

        Book foundedBook = bookService.findBookById(bookId);

        bookService.deleteBook(foundedBook);

        foundedBook = bookService.findBookById(bookId);

        //repository and cache must be empty
        assertNull(foundedBook, "Book is found");
    }

    @Test
    void testDeleteBookWithoutCacheEvict() {
        final long bookId = 3L;

        Book foundedBook = bookService.findBookById(bookId);

        bookService.deleteBookWithoutCacheEvict(foundedBook);

        // will find in cache
        foundedBook = bookService.findBookById(bookId);
        assertNotNull(foundedBook, "Book is not found");

        verify(bookRepository, times(1)).findById(bookId);
    }
}