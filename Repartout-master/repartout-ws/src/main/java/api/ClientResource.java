package api;

import dao.ClientDAO;
import models.Client;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {

    private ClientDAO dao = new ClientDAO();

    // Récupérer tous les clients
    @GET
    public Response getAllClients() {
        try {
            return Response.ok(dao.findAll()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération des clients: " + e.getMessage())
                    .build();
        }
    }

    // Récupérer un client par son ID
    @GET
    @Path("/{id}")
    public Response getClientById(@PathParam("id") int id) {
        try {
            Client client = dao.findById(id);
            if (client == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Client non trouvé")
                        .build();
            }
            return Response.ok(client).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération du client: " + e.getMessage())
                    .build();
        }
    }
}