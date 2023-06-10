package br.com.texthelper.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.com.texthelper.models.Request;

public class ToTextImplTest {
	
	@InjectMocks
	private ToTextImpl toTextImpl;
	
	private static final String MESSAGE_REQUEST = "0000000001Notebook Dell                 00000000000000001000300120231020590000000000000000550000000000000000010234";
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testToText() {
		String text = toTextImpl.toText(buildRequest());
		Assertions.assertNotNull(text);
		Assertions.assertEquals(MESSAGE_REQUEST, text);
	}
	
	public static Request buildRequest() {
		return Request.build(BigDecimal.valueOf(10d), 1, 
				"Notebook Dell", LocalDateTime.of(2023,1, 30, 10, 20, 59), 55d, 10.234f);
	}

}
