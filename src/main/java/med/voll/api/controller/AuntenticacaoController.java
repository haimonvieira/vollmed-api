package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DadosAutenticacao;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Esta classe sera usada para obter o token
@RestController
@RequestMapping("/login")
public class AuntenticacaoController {

    @Autowired
    private AuthenticationManager  manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping//recebendo informacoes
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados){

        try {

            //Passando os dados do usuario para gerar o token
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
            var authentication = manager.authenticate(authenticationToken);

        /*
        gerarToken retorna um objeto Usuario então é feito um cast para Usuario;
        O authentication representa o objeto que tem a autenticacao e que representa o
        usuario; getPrincipal pega o usuario logado
        */

            var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
            System.out.println(tokenJWT);
            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
