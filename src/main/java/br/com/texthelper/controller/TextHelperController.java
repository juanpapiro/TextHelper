package br.com.texthelper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.texthelper.models.RequestSimple;
import br.com.texthelper.service.ParseService;
import br.com.texthelper.service.ToObject;
import br.com.texthelper.utils.TextHelperFromText;

@RestController
public class TextHelperController {

	
	@GetMapping("/parse")
	public Object parse(@RequestParam String text) {
		ToObject parserFromText = new TextHelperFromText();
		try {
			return parserFromText.toObject(text, new RequestSimple());
		} catch (Exception e) {
			return null;
		}
	}

}
