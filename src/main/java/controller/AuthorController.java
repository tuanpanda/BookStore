package controller;

import Dao.AuthorDao;
import Models.AuthorModel;
import factory.DAOFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Quản lý tác giả", description = "Các API dùng để thêm, sửa, xóa sách trong hệ thống")
public class AuthorController {
	private final AuthorDao authorDao;

	public AuthorController(DataSource dataSource) {
		DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL, dataSource);
		this.authorDao = factory.getAuthorDao();
	}

	// 1. Lấy danh sách tất cả tác giả
	@GetMapping
	public List<AuthorModel> getAll() {
		return authorDao.getAllAuthors();
	}

	// 2. Lấy chi tiết 1 tác giả theo ID
	@GetMapping("/{id}")
	public ResponseEntity<AuthorModel> getById(@PathVariable int id) {
		AuthorModel author = authorDao.getAuthorById(id);
		if (author != null) {
			return ResponseEntity.ok(author);
		}
		return ResponseEntity.notFound().build();
	}

	// 3. Thêm mới một tác giả
	@PostMapping
	public ResponseEntity<String> create(@RequestBody AuthorModel author) {
		int id = authorDao.insertAuthor(author);
		if (id > 0) {
			return new ResponseEntity<>("Thêm tác giả thành công! ID: " + id, HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Thêm thất bại!", HttpStatus.BAD_REQUEST);
	}

	// 4. Cập nhật thông tin tác giả
	@PutMapping("/{id}")
	public ResponseEntity<String> update(@PathVariable int id, @RequestBody AuthorModel author) {
		author.authorId = id;
		boolean updated = authorDao.updateAuthor(author);
		if (updated) {
			return ResponseEntity.ok("Cập nhật thông tin tác giả thành công!");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tác giả!");
	}

	// 5. Xóa tác giả
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable int id) {
		boolean deleted = authorDao.deleteAuthor(id);
		if (deleted) {
			return ResponseEntity.ok("Xóa tác giả thành công!");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tác giả để xóa!");
	}
}
