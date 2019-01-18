package pe.com.sysFacturacion.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import pe.com.sysFacturacion.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{
	
}
