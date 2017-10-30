package com.piyush.pani.spring.service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.journaldev.spring.model.Person;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Service("personService")
@Transactional
public class PersonServiceImpl implements PersonService {
	
	@Autowired(required=true)
	private MongoTemplate mongoTemplate;
	protected static Logger logger = Logger.getLogger("service");
	
	@Override
	
	public void addPerson(Person person) {
		logger.debug("Adding a new person");
		   
		 try {
			 if(person.getId() != null && person.getId()!="" ){
				 // Find an entry where id matches the id
				 DBObject query1 = new BasicDBObject();
	        	 query1.put("id", person.getId());
	        	 DBObject query = new BasicDBObject();
		   		 query.put("id", person.getId());
		   		 query.put("name", person.getName());
		   		 query.put("country", person.getCountry());
	   		     mongoTemplate.getDb().getCollection("person").update(query1, query);
	        }else{
	        	 // Set a new value to the empid property first since it's blank
				 person.setId(UUID.randomUUID().toString());
				 // Insert to db
				 mongoTemplate.save(person);
	        }
			
	 	  
		 } 
		 catch (Exception e) {
			 logger.error("An error has occurred while trying to add new person", e);
		
		 }
	}

	@Override
	
	public void updatePerson(Person person) {
		 logger.debug("Editing existing person");
		   
		 try {
	    
			 // Find an entry where empid matches the id
	         Query query = new Query(where("id").is(person.getId()));
	          
	         // Declare an Update object. 
	         // This matches the update modifiers available in MongoDB
	         Update update = new Update();
	          
	         update.set("name", person.getName());
	        mongoTemplate.updateMulti(query, update, "collectionNam");
	          
	         update.set("country", person.getCountry());
	         //mongoTemplate.updateMulti(query, update, "collectionNam");
	          
	    
		 } catch (Exception e) {
			 logger.error("An error has occurred while trying to edit existing person", e);
		 }
	   
	 }
	

	@Override
	
	public List<Person> listPersons() {
		 logger.debug("Retrieving all persons");
		 Query query = new Query(where("empId").exists(true));
	        //List<person> persons = mongoTemplate.find(query, person.class);
	         
		  // Execute the query and find all matching entries
		List<Person> persons = mongoTemplate.findAll(Person.class);
	 
		 return persons;
	}

	@Override
	
	public Person getPersonById(int id) {
		 logger.debug("Retrieving an existing person");
		 Person person = new Person();
		 // Find an entry where empid matches the id
		 DBObject query = new BasicDBObject();
		 query.put("id", id);
		 DBObject cursor = mongoTemplate.getDb().getCollection("person").findOne(query);
		 person.setId(cursor.get("id").toString());
		 person.setName(cursor.get("name").toString());
		 person.setCountry(cursor.get("xcountry").toString());
	      
		 return person;
	}

	@Override
	
	public void removePerson(int id) {
		logger.debug("Deleting existing person");
		   
		 try {
			// Find an entry where pid matches the id
	         Query query = new Query(where("id").is(id));
	         // Run the query and delete the entry
	         mongoTemplate.remove(query);
//	         
//			 // Find an entry where empid matches the id
//			 DBObject query = new BasicDBObject();
//			 query.put("empId", empid);
//	         // Run the query and delete the entry
//	         mongoTemplate.getDb().getCollection("person").findAndRemove(query);
	          
		 } catch (Exception e) {
			 logger.error("An error has occurred while trying to delete new person", e);
		 }
	}

}
