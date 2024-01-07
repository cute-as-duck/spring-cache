package springcache.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springcache.model.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByTitleAndAuthor(String title, String author);
}
