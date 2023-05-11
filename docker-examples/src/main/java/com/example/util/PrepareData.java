package com.example.util;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import com.example.User;

public class PrepareData {

	public static void main(String[] args) {
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			User userA = new User("John", "Smith");
			pm.makePersistent(userA);
			User userB = new User("Albert", "Einstein");
			pm.makePersistent(userB);
			User userC = new User("Isaac", "Newton");
			pm.makePersistent(userC);

			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
}
