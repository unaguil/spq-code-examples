package com.example;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("users")
public class UsersResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
    	PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
		PersistenceManager pm = pmf.getPersistenceManager();
		
		Query<User> q = pm.newQuery(User.class);
		q.setOrdering("surname desc");
		
		List<User> users = q.executeList();

		pm.close();
		
		return users;
    }
}

