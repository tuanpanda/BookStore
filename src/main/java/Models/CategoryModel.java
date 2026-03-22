package Models;

public class CategoryModel {
	public int categoryId;
	public String categoryName;

	// 1. THÊM CONSTRUCTOR RỖNG (BẮT BUỘC ĐỂ DAO HOẠT ĐỘNG)
	public CategoryModel() {
	}

	// 2. CONSTRUCTOR CÓ THAM SỐ (GIỮ NGUYÊN CỦA BẠN)
	public CategoryModel(String name) {
		this.categoryName = name;
	}

	// Nên bổ sung thêm Getter/Setter nếu sau này bạn dùng thư viện Jackson (để trả về JSON cho Swagger)
	public int getCategoryId() { return categoryId; }
	public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
	public String getCategoryName() { return categoryName; }
	public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
