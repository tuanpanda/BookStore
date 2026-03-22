package Dao;

import Models.BookModel;
import Models.PublisherModel;

import java.util.List;

public interface BookDao {
	List<BookModel> getAllBooks();
	BookModel getBookById(int id);
	int insertBook(BookModel book);      // Thêm dòng này
	boolean updateBook(BookModel book);  // Thêm dòng này
	boolean deleteBook(int id);
}
