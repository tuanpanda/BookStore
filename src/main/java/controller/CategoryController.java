package controller;

import Models.CategoryModel;
import Dao.CategoryDao;
import factory.DAOFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Quản lý Loại sách", description = "Các API dùng để thêm, sửa, xóa sách trong hệ thống")
public class CategoryController {

	private final CategoryDao categoryDao;
	// Constructor này sẽ giúp khởi tạo Dao đúng cách
	public CategoryController(DataSource dataSource) {
		// Khởi tạo factory cụ thể từ dataSource mà Spring Boot tự nạp vào
		DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL, dataSource);
		this.categoryDao = factory.getCategoryDao();
	}

	// 1. LẤY TẤT CẢ THỂ LOẠI (GET)
	@GetMapping
	public ResponseEntity<List<CategoryModel>> getAllCategories() {
		List<CategoryModel> list = categoryDao.getAllCategories();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// 2. LẤY CHI TIẾT 1 THỂ LOẠI THEO ID (GET)
	@GetMapping("/{id}")
	public ResponseEntity<CategoryModel> getCategoryById(@PathVariable int id) {
		CategoryModel category = categoryDao.getCategoryById(id);
		if (category != null) {
			return new ResponseEntity<>(category, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// 3. THÊM MỚI THỂ LOẠI (POST)
	@PostMapping
	public ResponseEntity<String> createCategory(@RequestBody CategoryModel category) {
		// Lưu ý: JSON gửi lên chỉ cần { "categoryName": "Tên thể loại" }
		int newId = categoryDao.insertCategory(category);
		if (newId > 0) {
			return new ResponseEntity<>("Thêm thành công! ID mới: " + newId, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Thêm thất bại!", HttpStatus.BAD_REQUEST);
		}
	}

	// 4. CẬP NHẬT THỂ LOẠI (PUT)
	@PutMapping("/{id}")
	public ResponseEntity<String> updateCategory(@PathVariable int id, @RequestBody CategoryModel category) {
		// Đảm bảo ID trong đường dẫn và ID trong đối tượng khớp nhau
		category.categoryId = id;
		boolean updated = categoryDao.updateCategory(category);
		if (updated) {
			return new ResponseEntity<>("Cập nhật thành công!", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Không tìm thấy thể loại để cập nhật!", HttpStatus.NOT_FOUND);
		}
	}

	// 5. XÓA THỂ LOẠI (DELETE)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable int id) {
		try {
			boolean deleted = categoryDao.deleteCategory(id);
			if (deleted) {
				return new ResponseEntity<>("Xóa thành công!", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Không tìm thấy ID để xóa!", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// Trường hợp lỗi khóa ngoại (Foreign Key) như bạn gặp lúc nãy
			return new ResponseEntity<>("Lỗi: Không thể xóa thể loại này vì đang có sách thuộc thể loại này!",
					HttpStatus.CONFLICT);
		}
	}
}
