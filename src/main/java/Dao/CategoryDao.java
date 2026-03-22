package Dao;

import Models.CategoryModel;

import java.util.List;

public interface CategoryDao {
	// Các hàm truy xuất (Read)
	List<CategoryModel> getAllCategories();
	CategoryModel getCategoryById(int id);

	// Các hàm thao tác dữ liệu (Create, Update, Delete)
	int insertCategory(CategoryModel category);
	boolean updateCategory(CategoryModel category);
	boolean deleteCategory(int id);
}
