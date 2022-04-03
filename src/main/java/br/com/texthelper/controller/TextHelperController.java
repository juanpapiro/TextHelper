package br.com.texthelper.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.texthelper.models.Request;
import br.com.texthelper.utils.TextHelperFromObject;
import br.com.texthelper.utils.TextHelperFromText;

@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("/texthelper")
public class TextHelperController {
	
	@PostMapping(value = "/toobject")
	public Request getRequest(@RequestBody String message) {
		Request request = (Request) TextHelperFromText.toObject(new Request(), message);
		String messageText = TextHelperFromObject.toText(request);
		return request;
	}

}
