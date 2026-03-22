package Dao.impl;

import Dao.BookDao;
import Models.BookModel;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RdbBookDao implements BookDao {
	private final DataSource dataSource;
	public RdbBookDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<BookModel> getAllBooks() {
		List<BookModel> books = new ArrayList<>();
		// Truy vấn đúng tên bảng 'Books' từ SQL của bạn
		String sql = "SELECT * FROM Books";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				BookModel book = new BookModel();
				book.bookId = rs.getInt("BookID");
				book.title = rs.getString("Title");
				book.categoryId = rs.getInt("CategoryID");
				book.publisherId = rs.getInt("PublisherID");
				book.publishYear = rs.getInt("PublishYear");
				book.isbn = rs.getString("ISBN");
				book.importPrice = rs.getBigDecimal("ImportPrice");
				book.salePrice = rs.getBigDecimal("SalePrice");
				book.stockQuantity = rs.getInt("StockQuantity");
				book.imageUrl = rs.getString("ImageURL");
				book.status = rs.getInt("Status");
				books.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}

	@Override
	public BookModel getBookById(int id) {
		String sql = "SELECT * FROM Books WHERE BookID = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					BookModel book = new BookModel();
					book.bookId = rs.getInt("BookID");
					book.title = rs.getString("Title");
					book.categoryId = rs.getInt("CategoryID");
					book.publisherId = rs.getInt("PublisherID");
					book.publishYear = rs.getInt("PublishYear");
					book.isbn = rs.getString("ISBN");
					book.importPrice = rs.getBigDecimal("ImportPrice");
					book.salePrice = rs.getBigDecimal("SalePrice");
					book.stockQuantity = rs.getInt("StockQuantity");
					book.imageUrl = rs.getString("ImageURL");
					book.status = rs.getInt("Status");
					return book;
				}
			}
		} catch (SQLException e) {
			System.err.println("Lỗi getBookById: " + e.getMessage());
		}
		return null; // Trả về null nếu không tìm thấy sách
	}

	@Override
	public int insertBook(BookModel book) {
		// Câu lệnh SQL khớp hoàn toàn với các cột bạn đã tạo trong TABLE Books
		String sql = "INSERT INTO Books (Title, CategoryID, PublisherID, PublishYear, ISBN, " +
				"ImportPrice, SalePrice, StockQuantity, ImageURL, Status) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, book.title);
			stmt.setInt(2, book.categoryId);
			stmt.setInt(3, book.publisherId);
			stmt.setInt(4, book.publishYear);
			stmt.setString(5, book.isbn);
			stmt.setBigDecimal(6, book.importPrice);
			stmt.setBigDecimal(7, book.salePrice);
			stmt.setInt(8, book.stockQuantity);
			stmt.setString(9, book.imageUrl);
			stmt.setInt(10, book.status); // 1 là đang bán, 0 là ngừng bán

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						return rs.getInt(1); // Trả về BookID vừa tạo
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Lỗi Insert Book: " + e.getMessage());
		}
		return -1;
	}

	@Override
	public boolean updateBook(BookModel book) {
		String sql = "UPDATE Books SET Title = ?, CategoryID = ?, PublisherID = ?, PublishYear = ?, " +
				"ISBN = ?, ImportPrice = ?, SalePrice = ?, StockQuantity = ?, ImageURL = ?, Status = ? " +
				"WHERE BookID = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, book.title);
			stmt.setInt(2, book.categoryId);
			stmt.setInt(3, book.publisherId);
			stmt.setInt(4, book.publishYear);
			stmt.setString(5, book.isbn);
			stmt.setBigDecimal(6, book.importPrice);
			stmt.setBigDecimal(7, book.salePrice);
			stmt.setInt(8, book.stockQuantity);
			stmt.setString(9, book.imageUrl);
			stmt.setInt(10, book.status);
			stmt.setInt(11, book.bookId); // Điều kiện WHERE

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Lỗi Update Book: " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean deleteBook(int id) {
		// Tên bảng phải là Books (có s) theo SQL của bạn
		String sql = "DELETE FROM Books WHERE BookID = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);

			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0; // Trả về true nếu xóa thành công ít nhất 1 dòng

		} catch (SQLException e) {
			System.err.println("Lỗi khi xóa sách: " + e.getMessage());
			// Nếu có lỗi ràng buộc khóa ngoại khác (không phải BookAuthors), nó sẽ nhảy vào đây
			return false;
		}
	}
}
