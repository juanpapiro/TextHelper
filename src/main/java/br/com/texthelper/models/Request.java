package br.com.texthelper.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.google.gson.Gson;

import br.com.texthelper.annotations.MakeText;

public class Request {
	
	@MakeText(length = 20, order = 3, align = "R", trelling = "0", 
			  decimalPrecision = 2, decimalMovePoint = 2)
	private BigDecimal value;
	@MakeText(length = 10, order = 1, align = "R", trelling = "0")
	private Integer productCode;
	@MakeText(length = 30, order = 2)
	private String productName;
	@MakeText(length = 14, order = 4, pattern = "ddMMyyyyHHmmss")
	private LocalDateTime date;
	@MakeText(length = 20, order = 5, align = "R", trelling = "0", 
			decimalPrecision = 2, decimalMovePoint = 2)
	private Double doubleValue;
	@MakeText(length = 20, order = 6, align = "R", trelling = "0", 
			decimalPrecision = 3, decimalMovePoint = 3)
	private float floatValue;
	
	
	public Request() {}
	
	
	private Request(BigDecimal value, Integer productCode, String productName,
			LocalDateTime date, Double doubleValue, float floatValue) {
		this.value = value;
		this.productCode = productCode;
		this.productName = productName;
		this.date = date;
		this.doubleValue = doubleValue;
		this.floatValue = floatValue;
	}
	
	public static Request build(BigDecimal value, Integer productCode, String productName,
			LocalDateTime date, Double doubleValue, float floatValue) {
		return new Request(value, productCode, productName, date, doubleValue, floatValue);
	}


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
	public Double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}
	
	public String toJson() {
		Gson g = new Gson();
		return g.toJson(this);
	}
	

}
