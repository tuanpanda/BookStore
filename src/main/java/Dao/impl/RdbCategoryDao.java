package Dao.impl;

import Dao.CategoryDao;
import Models.CategoryModel;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RdbCategoryDao implements CategoryDao {
	// Khai báo DataSource
	private DataSource dataSource;

	// Truyền DataSource vào qua Constructor
	public RdbCategoryDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<CategoryModel> getAllCategories() {
		List<CategoryModel> categories = new ArrayList<>();
		String sql = "SELECT * FROM Category";

		// Sử dụng try-with-resources để tự động đóng Connection và ResultSet
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				CategoryModel category = new CategoryModel();
				// CHÚ Ý: Sửa "id" và "name" cho đúng với tên cột trong database của bạn
				category.setCategoryId(rs.getInt("id"));
				category.setCategoryName(rs.getString("name"));
				categories.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}

	@Override
	public CategoryModel getCategoryById(int id) {
		String sql = "SELECT * FROM Category WHERE id = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					CategoryModel category = new CategoryModel();
					category.setCategoryId(rs.getInt("id"));
					category.setCategoryName(rs.getString("name"));
					return category;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Không tìm thấy
	}

	@Override
	public int insertCategory(CategoryModel category) {
		String sql = "INSERT INTO Category (name) VALUES (?)";
		int generatedId = -1;

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, category.getCategoryName());
			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						generatedId = rs.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return generatedId; // Trả về ID vừa được tạo mới
	}

	@Override
	public boolean updateCategory(CategoryModel category) {
		String sql = "UPDATE Category SET name = ? WHERE id = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, category.getCategoryName());
			stmt.setInt(2, category.getCategoryId());

			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteCategory(int id) {
		String sql = "DELETE FROM Category WHERE id = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
