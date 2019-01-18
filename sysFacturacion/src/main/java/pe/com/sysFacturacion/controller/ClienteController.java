package pe.com.sysFacturacion.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.com.sysFacturacion.entity.Cliente;
import pe.com.sysFacturacion.service.IClienteService;
import pe.com.sysFacturacion.service.IUploadFileService;
import pe.com.sysFacturacion.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService; 
	
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		Resource recurso=null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {	
			e.printStackTrace();
		}		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+recurso.getFilename()+"\"").body(recurso);				
	}
	
	

	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id")Long id,Map<String,Object> model,RedirectAttributes flash){
		Cliente cliente=clienteService.findOne(id);
		if(cliente==null){
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		model.put("cliente",cliente);
		model.put("titulo", "Detalle cliente: "+cliente.getNombre());
		return "ver";
	}
	
	
	@RequestMapping(value="/listar",method=RequestMethod.GET)
	private String listar(@RequestParam(name="page",defaultValue="0")int page, Model model){
		
		Pageable pageRequest=PageRequest.of(page,4);
		Page<Cliente> clientes=clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender=new PageRender<>("/listar", clientes);		
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page",pageRender);
		return "listar";
	}
	
	@RequestMapping(value="/form")
	public String crear(Map<String,Object> model){
		Cliente cliente=new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Clientes");
		return "form";
	}
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id")Long id, Map<String,Object> model,RedirectAttributes flash){
		Cliente cliente=null;
		if(id>0){
			cliente=clienteService.findOne(id);
			if(cliente==null){
				flash.addFlashAttribute("error", "Id no existe!");
				return "redirect:/listar";
			}
		}else{
			flash.addFlashAttribute("error", "Id no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Clientes");
		return "form";
	}
	
	
	@RequestMapping(value="/form",method=RequestMethod.POST)
	public String guardar(@Valid Cliente cliente,BindingResult result,Model model,@RequestParam("file") MultipartFile foto,RedirectAttributes flash,SessionStatus status){
		if(result.hasErrors()){
			model.addAttribute("titulo", "Formulario de Clientes");
			return "form";
		}
		
		if(!foto.isEmpty()){
			
			if(cliente.getId() !=null 
					&& cliente.getId()>0
					&& cliente.getFoto() !=null
					&& cliente.getFoto().length()>0){
				uploadFileService.delete(cliente.getFoto());				
			}
			String uniqueFileName=null;
			try {
				uniqueFileName = uploadFileService.copy(foto);
			} catch (IOException e) {			
				e.printStackTrace();
			}
			
			flash.addFlashAttribute("info", "Ha subido correctamente '"+uniqueFileName+"'");
			cliente.setFoto(uniqueFileName);
		}		
		
		String mensaje=(cliente.getId() !=null)?"Cliente editado con exito!":"Cliente creado con exito!";
		
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:listar";
	}
		
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id")Long id,RedirectAttributes flash){
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);

			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito!");

			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminado con exito!");
			}
		}
		return "redirect:/listar";
	}
}
