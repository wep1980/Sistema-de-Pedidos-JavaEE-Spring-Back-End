package br.com.waldirep.springionicmc.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.waldirep.springionicmc.domain.Categoria;
import br.com.waldirep.springionicmc.domain.Pedido;
import br.com.waldirep.springionicmc.dto.CategoriaDTO;
import br.com.waldirep.springionicmc.services.PedidoService;



@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET) // Este END POINT recebe /pedidos/id ( Recebe o id digitado )
	public ResponseEntity<Pedido> find (@PathVariable Integer id) {     // O END POINT recebe o id da URL atraves da anotação @PathVariable
		
		Pedido pedido = service.find(id);
		
		return ResponseEntity.ok().body(pedido);
	}
	
	
	/**
	 * RequestMethod.POST -> Insere
	 * O método save retorna um objeto
	 * Para o objeto ser construído a partir dos dados JSON enviado é necessario colocar a anotação @RequestBody
	 * @param obj
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj){
		
		
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); // Captura a URi do novo recurso inserido
		return ResponseEntity.created(uri).build();
	}

}