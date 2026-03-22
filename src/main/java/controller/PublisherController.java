package controller;

import Models.PublisherModel;
import Dao.PublisherDao;
import factory.DAOFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@Tag(name = "Quản lý nhà xuất bản", description = "Các API dùng để thêm, sửa, xóa sách trong hệ thống")
public class PublisherController {

	private final PublisherDao publisherDao;

	public PublisherController(DataSource dataSource) {
		DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL, dataSource);
		this.publisherDao = factory.getPublisherDao();
	}

	// 1. Lấy danh sách tất cả nhà xuất bản
	@GetMapping
	public List<PublisherModel> getAll() {
		return publisherDao.getAllPublishers();
	}

	// 2. Lấy chi tiết 1 nhà xuất bản theo ID
	@GetMapping("/{id}")
	public ResponseEntity<PublisherModel> getById(@PathVariable int id) {
		PublisherModel p = publisherDao.getPublisherById(id);
		if (p != null) {
			return ResponseEntity.ok(p);
		}
		return ResponseEntity.notFound().build();
	}

	// 3. Thêm mới một nhà xuất bản
	@PostMapping
	public ResponseEntity<String> create(@RequestBody PublisherModel publisher) {
		int id = publisherDao.insertPublisher(publisher);
		if (id > 0) {
			return new ResponseEntity<>("Thêm NXB thành công! ID: " + id, HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Thêm NXB thất bại!", HttpStatus.BAD_REQUEST);
	}

	// 4. Cập nhật thông tin nhà xuất bản
	@PutMapping("/{id}")
	public ResponseEntity<String> update(@PathVariable int id, @RequestBody PublisherModel publisher) {
		publisher.publisherId = id; // Gán ID từ đường dẫn vào đối tượng
		boolean updated = publisherDao.updatePublisher(publisher);
		if (updated) {
			return ResponseEntity.ok("Cập nhật NXB thành công!");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy NXB để cập nhật!");
	}

	// 5. Xóa nhà xuất bản
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable int id) {
		try {
			boolean deleted = publisherDao.deletePublisher(id);
			if (deleted) {
				return ResponseEntity.ok("Xóa NXB thành công!");
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy NXB để xóa!");
		} catch (Exception e) {
			// Lỗi này thường xảy ra nếu đang có Sách tham chiếu đến NXB này (Khóa ngoại)
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Lỗi: Không thể xóa vì đang có sách thuộc nhà xuất bản này!");
		}
	}
}