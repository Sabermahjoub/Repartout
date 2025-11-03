package api;

import dao.InterventionDAO;
import models.Intervention;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

import dao.MachineDAO;
import models.Machine;

@Path("/interventions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InterventionResource {

    private InterventionDAO dao = new InterventionDAO();

    // Lister toutes les interventions
    @GET
    public Response getAllInterventions() {
        try {
            List<Intervention> interventions = dao.findAll();
            return Response.ok(interventions).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération des interventions: " + e.getMessage())
                    .build();
        }
    }

    // Obtenir une intervention par son ID
    @GET
    @Path("/{id}")
    public Response getInterventionById(@PathParam("id") String id) {
        try {
            Intervention intervention = dao.getInterventionById(id);
            if (intervention == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Intervention non trouvée")
                        .build();
            }
            return Response.ok(intervention).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération de l'intervention: " + e.getMessage())
                    .build();
        }
    }

    // Lister les interventions par technicien
    @GET
    @Path("/technicien/{technicien}")
    public Response getInterventionsByTechnicien(@PathParam("technicien") String technicien) {
        try {
            List<Intervention> interventions = dao.findByTechnicien(technicien);
            return Response.ok(interventions).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération des interventions: " + e.getMessage())
                    .build();
        }
    }

    // Lister les interventions par état
    @GET
    @Path("/etat/{etat}")
    public Response getInterventionsByEtat(@PathParam("etat") String etat) {
        try {
            List<Intervention> interventions = dao.findByEtat(etat);
            return Response.ok(interventions).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération des interventions: " + e.getMessage())
                    .build();
        }
    }

    // Mettre à jour une intervention (pour les techniciens)
    @PUT
    @Path("/{id}")
    public Response updateIntervention(@PathParam("id") String id, Intervention updated) {
        try {
            // Récupérer l'intervention existante
            Intervention existing = dao.getInterventionById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Intervention non trouvée")
                        .build();
            }

            // Mettre à jour uniquement les champs fournis (pas les dates)
            if (updated.getEtat() != null) {
                existing.setEtat(updated.getEtat());
            }
            if (updated.getTechnicien() != null) {
                existing.setTechnicien(updated.getTechnicien());
            }
            if (updated.getNotes() != null) {
                existing.setNotes(updated.getNotes());
            }
            // Ne pas toucher aux dates si elles ne sont pas fournies

            boolean success = dao.updateIntervention(existing);
            if (success) {
                return Response.ok(existing).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Erreur lors de la mise à jour")
                        .build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la mise à jour: " + e.getMessage())
                    .build();
        }
    }

    // Changer l'état d'une intervention
    @PATCH
    @Path("/{id}/etat")
    public Response updateEtat(@PathParam("id") String id, 
                              @QueryParam("etat") String etat) {
        try {
            Intervention existing = dao.getInterventionById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Intervention non trouvée")
                        .build();
            }

            existing.setEtat(etat);
            boolean success = dao.updateIntervention(existing);
            
            if (success) {
                return Response.ok(existing).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Erreur lors du changement d'état")
                        .build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors du changement d'état: " + e.getMessage())
                    .build();
        }
    }
    
    
    
    
    
 // Récupérer la machine associée à une intervention
    @GET
    @Path("/{id}/machine")
    public Response getMachineByIntervention(@PathParam("id") String interventionId) {
        try {
            // 1. Récupérer l'intervention
            Intervention intervention = dao.getInterventionById(interventionId);
            if (intervention == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Intervention non trouvée")
                        .build();
            }
            
            // 2. Récupérer la machine associée
            MachineDAO machineDao = new MachineDAO();
            Machine machine = machineDao.findBySerialNumber(intervention.getMachineId());
            
            if (machine == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Machine non trouvée pour cette intervention")
                        .build();
            }
            
            return Response.ok(machine).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération: " + e.getMessage())
                    .build();
        }
    }
}