package br.com.texthelper.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.com.texthelper.models.Request;
import br.com.texthelper.models.RequestSimple;

public class ToObjectImplTest {
	
	@InjectMocks
	private ToObjectImpl toObjectImpl;
	
	private static final String MESSAGE_REQUEST = "0000000001Notebook Dell                 00000000000000001000290520221249220000000000000000550000000000000000010234";
	private static final String MESSAGE_REQUEST_SIMPLE = "0000123156007800000000001200030-01-2023 10:20:59000000000006000Casas Bahia         ";
	
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRequest() {
		Request req = new Request();
		req = (Request) toObjectImpl.toObject(MESSAGE_REQUEST, req);
		Assertions.assertEquals(1, req.getProductCode().intValue());
	}

	@Test
	void testRequestSimple() {
		RequestSimple req = new RequestSimple();
		req = (RequestSimple) toObjectImpl.toObject(MESSAGE_REQUEST_SIMPLE, req);
		Assertions.assertEquals(123156, req.getId().intValue());
	}
	
}
