package Dao.impl;

import Dao.AuthorDao;
import Models.AuthorModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RdbAuthorDao implements AuthorDao {
	private final DataSource dataSource;

	public RdbAuthorDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<AuthorModel> getAllAuthors() {
		List<AuthorModel> authors = new ArrayList<>();
		String sql = "SELECT * FROM Author";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				AuthorModel author = new AuthorModel();
				author.authorId = rs.getInt("AuthorID"); // Sửa tên cột cho đúng DB
				author.authorName = rs.getString("AuthorName");
				authors.add(author);
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return authors;
	}

	@Override
	public int insertAuthor(AuthorModel author) {
		String sql = "INSERT INTO Author (AuthorName) VALUES (?)";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, author.authorName);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) return rs.getInt(1);
		} catch (SQLException e) { e.printStackTrace(); }
		return -1;
	}

	@Override
	public AuthorModel getAuthorById(int id) {
		String sql = "SELECT * FROM Authors WHERE AuthorID = ?";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					AuthorModel author = new AuthorModel();
					author.authorId = rs.getInt("AuthorID");
					author.authorName = rs.getString("AuthorName");
					author.biography = rs.getString("Biography"); // Khớp với cột Biography trong SQL
					return author;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateAuthor(AuthorModel author) {
		String sql = "UPDATE Authors SET AuthorName = ?, Biography = ? WHERE AuthorID = ?";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, author.authorName);
			stmt.setString(2, author.biography);
			stmt.setInt(3, author.authorId);

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteAuthor(int id) {
		// Lưu ý: SQL của bạn có ON DELETE CASCADE ở bảng BookAuthors
		// nên khi xóa Author, các liên kết sách-tác giả sẽ tự mất theo.
		String sql = "DELETE FROM Authors WHERE AuthorID = ?";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Lỗi xóa tác giả: " + e.getMessage());
			return false;
		}
	}
}
