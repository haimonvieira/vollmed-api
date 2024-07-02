package med.voll.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//anotacao para API Rest
@RestController
//o mapeamento desta calsse vai ser dado por "/hello"
@RequestMapping("/hello")
public class HelloController {

    //Retornará uma String quando for feita requisição para o servidor
    @GetMapping
    public String olaMundo(){
        return  "Hello World!";
    }

}
