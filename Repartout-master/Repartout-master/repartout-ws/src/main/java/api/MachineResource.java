package api;

import dao.MachineDAO;
import models.Machine;
import models.Client;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/machines")
@Produces(MediaType.APPLICATION_JSON)
public class MachineResource {

    private MachineDAO dao = new MachineDAO();

    // Récupérer toutes les machines
    @GET
    public Response getAllMachines() {
        try {
            return Response.ok(dao.findAll()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération des machines: " + e.getMessage())
                    .build();
        }
    }

    // Récupérer une machine par son numéro de série
    @GET
    @Path("/{serialNumber}")
    public Response getMachineBySerial(@PathParam("serialNumber") String serialNumber) {
        try {
            Machine machine = dao.findBySerialNumber(serialNumber);
            if (machine == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Machine non trouvée")
                        .build();
            }
            return Response.ok(machine).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération de la machine: " + e.getMessage())
                    .build();
        }
    }

    // Récupérer le client associé à une machine
    @GET
    @Path("/{serialNumber}/client")
    public Response getClientByMachine(@PathParam("serialNumber") String serialNumber) {
        try {
            Client client = dao.findClientByMachineSerial(serialNumber);
            if (client == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Client non trouvé pour cette machine")
                        .build();
            }
            return Response.ok(client).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération du client: " + e.getMessage())
                    .build();
        }
    }

    // Récupérer les machines d'un client
    @GET
    @Path("/client/{clientId}")
    public Response getMachinesByClient(@PathParam("clientId") int clientId) {
        try {
            return Response.ok(dao.findByClientId(clientId)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération des machines: " + e.getMessage())
                    .build();
        }
    }
}