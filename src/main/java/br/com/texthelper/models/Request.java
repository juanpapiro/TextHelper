package br.com.texthelper.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.texthelper.annotations.MakeText;

public class Request {
	
	@MakeText(length = 20, order = 3, align = "R", trelling = "0")
	private BigDecimal value;
	@MakeText(length = 10, order = 1, align = "R", trelling = "0")
	private Integer productCode;
	@MakeText(length = 30, order = 2)
	private String productName;
	@MakeText(length = 14, order = 4, pattern = "ddMMyyyyHHmmss")
	private LocalDateTime date;
	
	
	public Integer getProductCode() {
		return productCode;
	}
	public void setProductCode(Integer productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	
	

}
