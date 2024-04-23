package com.sample.product;

import java.io.File;
import java.net.URISyntaxException;

import com.sample.product.util.ProductCatalog;

public class Product {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length == 0)
		{
			System.out.println("enter catalog location in this form x:\\path\\Product");
			return;
		}
		
		try {
			System.out.println("Process waiting. Press enter to continue...");
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}		

		try {
			Product catalog = new Product();
			catalog.readCatalogFromFolder(args[0]);
	
			System.in.read();
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readCatalogFromFolder(String name) throws URISyntaxException {
		if(name == null)
		{
			System.out.println("invalid folder name");
			return;
		}
		
		File file = new File(Product.class.getResource(name).toURI());
		if(!file.exists() || !file.isDirectory())
		{
			System.out.format("invalid folder name '%s'\n", name);
			return;
		}
		
		ProductCatalog info = new ProductCatalog();
		info.readData(name);
		
		System.out.println(info.getContent());
	}

}
