package com.example;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Comparator;

import com.example.pojo.User;

@Path("users")
public class UserResource {

    public enum Order {
        ASC,
        DESC;        

        // case insensitive values for order parameter
        public static Order fromString(String value) {
            return Order.valueOf(value.toUpperCase());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(@QueryParam("filter") String str,
        @QueryParam("order") @DefaultValue("ASC") Order order) {
        
            // This data could be retrieved from a database
        List<User> users = new ArrayList<>();
        users.add(new User(0, "John", "Smith"));
        users.add(new User(1, "Isaac", "Newton"));
        users.add(new User(0, "Albert", "Einstein"));

        Stream<User> stream = users.stream();
        // check if the query parameter was passed in the URL
        if (str != null) {
            stream = stream.filter(user -> user.getSurname().contains(str));
        }

        // sort the stream by the passed parameter
        // as the parameter has a default value there is no need to
        // check if the parameter is null
        if (order == Order.DESC) {
            stream = stream.sorted(Comparator.comparing(User::getSurname).reversed());
        } else {
            stream = stream.sorted(Comparator.comparing(User::getSurname));
        }

        // return the resulting stream as a list
        return stream.collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        // here we will process the received user data
        System.out.println("Adding a new user: " + user.getName() + " " + user.getSurname());
        // return a response containing a user with only the code for the new user
        return Response.ok(new User(15)).build();
    }

    @DELETE
    @Path("/{code}")
    public Response deleteUser(@PathParam("code") int code) {
        if (code == 10) {
            System.out.println("Deleting user...");
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}