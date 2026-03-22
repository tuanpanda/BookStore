package Models;

import java.math.BigDecimal;

public class BookModel {
	public int bookId;
	public String title;
	public int categoryId;
	public int publisherId;
	public int publishYear;
	public String isbn;
	public BigDecimal importPrice;
	public BigDecimal salePrice;
	public int stockQuantity;
	public String imageUrl;
	public int status;

	public BookModel() {} // Bắt buộc
}