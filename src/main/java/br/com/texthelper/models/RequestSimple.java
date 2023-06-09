package br.com.texthelper.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.texthelper.annotations.MakeText;

public class RequestSimple {
	
	@MakeText(length = 10, order = 1, align = "R", trelling = "0")
	private Long id;
	
	@MakeText(length = 4, order = 2, align = "R", trelling = "0")
	private Integer channel;
	
	@MakeText(length = 15, order = 3, align = "R", trelling = "0", decimalMovePoint = 2, decimalPrecision = 2)
	private double amount;
	
	@MakeText(length = 19, order = 4, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime date;
	
	@MakeText(length = 15, order = 5, align = "R", trelling = "0", decimalPrecision = 2, decimalMovePoint = 2)
	private BigDecimal refundAmount;
	
	@MakeText(length = 20, order = 6, align = "L", trelling = " ")
	private String fantasyName;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getFantasyName() {
		return fantasyName;
	}
	public void setFantasyName(String fantasyName) {
		this.fantasyName = fantasyName;
	}
	
	

}
