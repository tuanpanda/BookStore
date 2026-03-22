package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Đối tượng chứa thông tin sách cần thêm mới")
public class BookRequestDTO {
	@Schema(description = "Tên cuốn sách", example = "Clean Code")
	public String title;

	@Schema(description = "Giá nhập", example = "150000")
	public double importPrice;

	@Schema(description = "Giá bán", example = "250000")
	public double salePrice;

	@Schema(description = "Số lượng tồn kho", example = "100")
	public int stockQuantity;

	@Schema(description = "ID của Thể loại", example = "1")
	public int categoryId;

	@Schema(description = "ID của Nhà xuất bản", example = "1")
	public int publisherId;

	@Schema(description = "Danh sách ID các tác giả", example = "[1, 2]")
	public List<Integer> authorIds;
}
