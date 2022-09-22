/**********************************************************************
Copyright (c) 2003 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
    ...
**********************************************************************/
package org.datanucleus.samples.jdo.tutorial;


import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Extent;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

// To support resource bundle
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controlling application for the DataNucleus Tutorial using JDO.
 * Relies on the user defining a file "datanucleus.properties" to be in the CLASSPATH
 * and to include the JDO properties for the DataNucleus PersistenceManager.
 * @author      Diego Lopez-de-Ipina 
 * @version     1.0                                    
 * @since       2012-02-01        
 */
public class Main
{
    private static final String SYSTEM_MESSAGES = "SystemMessages";
    private static final Logger logger = LogManager.getLogger();
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args)
    {
		// ResourceBundle class will use SystemMessages.properties file
		ResourceBundle resourceBundle = ResourceBundle.getBundle(SYSTEM_MESSAGES, Locale.getDefault());
		resourceBundle = ResourceBundle.getBundle(SYSTEM_MESSAGES,	Locale.forLanguageTag("en"));

		logger.info(resourceBundle.getString("starting_msg"));
			
        // Create a PersistenceManagerFactory for this datastore
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");

		logger.info(resourceBundle.getString("app_title"));
        logger.info(resourceBundle.getString("app_underline"));

        // Persistence of a Product and a Book.
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
			logger.info(resourceBundle.getString("persisting_title_msg"));
            Product product = new Product("Sony Discman","A standard discman from Sony",200.00);
            Book book = new Book("Lord of the Rings by Tolkien","The classic story",49.99,"JRR Tolkien", "12345678", "MyBooks Factory");
            pm.makePersistent(product);
            pm.makePersistent(book);
 
            tx.commit();
			logger.info(resourceBundle.getString("persisting_body_msg"));
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        // Basic Extent of all Products
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
		// changing to Spanish now
		resourceBundle = ResourceBundle.getBundle(SYSTEM_MESSAGES,	Locale.forLanguageTag("es"));
        try
        {
            tx.begin();
			logger.info(resourceBundle.getString("retrieving_products_msg"));
            Extent<Product> e = pm.getExtent(Product.class, true);
            Iterator<Product> iter = e.iterator();
            while (iter.hasNext())
            {
                Object obj = iter.next();
                logger.info("> {}", obj);
            }
            tx.commit();
        }
        catch (Exception e)
        {
			logger.info("{} {}", resourceBundle.getString("retrieving_exception_msg"), e.getMessage());
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        // Perform some query operations
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();
			logger.info(resourceBundle.getString("executing_query_msg"));
            Extent<Product> e = pm.getExtent(Product.class,true);
            try (Query<Product> q = pm.newQuery(e, "price < 150.00")) {
                q.setOrdering("price ascending");                        
                for (Product product : (List<Product>)q.execute()) {
                    logger.info("> {}", product);
                    if (product instanceof Book)
                    {
                        Book b = (Book)product;
                        // Give an example of an update
                        b.setDescription(resourceBundle.getString("price_reduced_msg"));
                        logger.info(resourceBundle.getString("price_reduced_msg"));
                    }
        
                }

                tx.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        
		// changing to Basque now
		resourceBundle = ResourceBundle.getBundle(SYSTEM_MESSAGES,	Locale.forLanguageTag("eu"));
		
		
        // Clean out the database
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();
            logger.info(resourceBundle.getString("deleting_products_msg"));
            try (Query<Product> q = pm.newQuery(Product.class)) {
                long numberInstancesDeleted = q.deletePersistentAll();
                logger.info("{} {} {}", resourceBundle.getString("deleted_msg"), numberInstancesDeleted, resourceBundle.getString("products_msg"));

                tx.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        logger.info("");
        logger.info(resourceBundle.getString("end_tutorial_msg"));
		pmf.close();
    }
}
