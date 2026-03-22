package controller;

import Dao.BookDao;
import Models.AuthorModel;
import Models.BookModel;
import Models.CategoryModel;
import Models.PublisherModel;
import dto.BookRequestDTO;
import factory.DAOFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;


@RestController
@RequestMapping("/api/books")
@Tag(name = "Quản lý Sách", description = "Các API dùng để thêm, sửa, xóa sách trong hệ thống")
public class BookController {
	private final BookDao bookDao;

	public BookController(DataSource dataSource) {
		DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL, dataSource);
		this.bookDao = factory.getBookDao();
	}

	@GetMapping
	public List<BookModel> getAll() {
		return bookDao.getAllBooks();
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookModel> getById(@PathVariable int id) {
		BookModel book = bookDao.getBookById(id);
		if (book != null) {
			return ResponseEntity.ok(book);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public String add(@RequestBody BookModel book) {
		int id = bookDao.insertBook(book);
		if (id > 0) return "Thêm sách thành công! ID: " + id;
		return "Thêm sách thất bại (Kiểm tra xem CategoryID và PublisherID có tồn tại không?)";
	}

	// Cập nhật sách (PUT)
	@PutMapping("/{id}")
	public String updateBook(@PathVariable int id, @RequestBody BookModel book) {
		book.bookId = id; // Đảm bảo dùng ID từ đường dẫn
		boolean updated = bookDao.updateBook(book);
		return updated ? "Cập nhật sách thành công!" : "Cập nhật thất bại (Kiểm tra ID)" + id;
	}

	// Xóa sách (DELETE)
	@DeleteMapping("/{id}")
	public String deleteBook(@PathVariable int id) {
		boolean deleted = bookDao.deleteBook(id); // Giả sử bạn đã viết hàm delete trong DAO
		return deleted ? "Xóa sách thành công!" : "Xóa thất bại";
	}
}
