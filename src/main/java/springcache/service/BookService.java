package springcache.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import springcache.model.Book;
import springcache.repository.BookRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    @Cacheable(value = "book", unless = "#result == null")
    public Book findBookById(long id) {
        LOG.info("Calling findBookById ...");
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.orElse(null);
    }

    @Cacheable(value = "book", key = "#title", unless = "#result == null")
    public Book findBookByTitleAndAuthor(String title, String author) {
        LOG.info("Calling findBookByTitleAndAuthor ...");
        Optional<Book> bookOptional = bookRepository.findBookByTitleAndAuthor(title, author);
        return bookOptional.orElse(null);
    }

    @CachePut(value = "book", key = "#book.id")
    public Book saveBook(Book book) {
        LOG.info("Calling saveBook...");
        return bookRepository.save(book);
    }

    @CacheEvict(value = "book", key = "#book.id")
    public void deleteBook(Book book) {
        LOG.info("Calling deleteBook...");
        bookRepository.delete(book);
    }

    public void deleteBookWithoutCacheEvict(Book book) {
        LOG.info("Calling deleteBook...");
        bookRepository.delete(book);
    }
}
