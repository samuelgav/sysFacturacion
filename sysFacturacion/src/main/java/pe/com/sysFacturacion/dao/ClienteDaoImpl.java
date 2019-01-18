/*package pe.com.SpringBootJPA.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.com.SpringBootJPA.entity.Cliente;

@Repository
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override	
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return em.createQuery("from Cliente", Cliente.class).getResultList();
	}

	@Override	
	public void save(Cliente cliente) {
		if(cliente.getId() !=null && cliente.getId() >0){
			em.merge(cliente);
		}else{
			em.persist(cliente);
		}
	}

	@Override	
	public Cliente findOne(Long id) {
		// TODO Auto-generated method stub
		return em.find(Cliente.class,id);
	}

	@Override	
	public void delete(Long id) {
		// TODO Auto-generated method stub
		em.remove(findOne(id));
	}

}*/
