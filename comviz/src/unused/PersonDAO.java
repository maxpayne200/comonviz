package unused;



import com.googlecode.genericdao.dao.hibernate.GenericDAO;

import database.model.ontology.OntologyAxiom;
import database.model.ontology.Person;

/**
 * <p>
 * Interface for the Citizen DAO. This is created very simply by extending
 * GenericDAO and specifying the type for the entity class (Citizen) and the
 * type of its identifier (Long).
 * 
 * <p>
 * As a matter of best practice other components reference this interface rather
 * than the implementation of the DAO itself.
 * 
 * @author dwolverton
 * 
 */
public interface PersonDAO extends GenericDAO<Person, Long> {

}
