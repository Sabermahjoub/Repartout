package servlets;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.InterventionDAO;
import dao.MachineDAO;
import dao.ClientDAO;
import models.Intervention;
import models.Machine;
import models.Client;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/techniciens/*")
public class APIServlet extends HttpServlet {
    private InterventionDAO interventionDAO = new InterventionDAO();
    private MachineDAO machineDAO = new MachineDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private ObjectMapper mapper = new ObjectMapper();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        System.out.println("=== API CALLED: " + pathInfo + " ===");
        
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/test")) {
                // Test endpoint
                response.getWriter().write("{\"message\": \"API Techniciens REPARTOUT - SERVLET\"}");
                
            } else if (pathInfo.equals("/interventions")) {
                // 1. Lister les interventions
                List<Intervention> interventions = interventionDAO.getAllInterventions();
                String json = mapper.writeValueAsString(interventions);
                response.getWriter().write(json);
                
            } else if (pathInfo.startsWith("/interventions/")) {
                String[] parts = pathInfo.split("/");
                if (parts.length >= 3) {
                    String numero = parts[2];
                    
                    if (parts.length == 3) {
                        // 2. Détails d'une intervention
                        Intervention intervention = interventionDAO.getInterventionById(numero);
                        if (intervention != null) {
                            response.getWriter().write(mapper.writeValueAsString(intervention));
                        } else {
                            response.setStatus(404);
                            response.getWriter().write("{\"error\": \"Intervention non trouvée\"}");
                        }
                    } else if (parts.length == 4 && parts[3].equals("machine")) {
                        // 3. Machine d'une intervention
                        Intervention intervention = interventionDAO.getInterventionById(numero);
                        if (intervention != null) {
                            Machine machine = machineDAO.getMachineById(intervention.getMachineId());
                            if (machine != null) {
                                response.getWriter().write(mapper.writeValueAsString(machine));
                            } else {
                                response.setStatus(404);
                                response.getWriter().write("{\"error\": \"Machine non trouvée\"}");
                            }
                        } else {
                            response.setStatus(404);
                            response.getWriter().write("{\"error\": \"Intervention non trouvée\"}");
                        }
                    }
                }
                
            } else if (pathInfo.startsWith("/machines/") && pathInfo.endsWith("/client")) {
                // 4. Client d'une machine
                String idMachine = pathInfo.replace("/machines/", "").replace("/client", "");
                Machine machine = machineDAO.getMachineById(idMachine);
                if (machine != null) {
                    Client client = clientDAO.getClientById(machine.getClient());
                    if (client != null) {
                        response.getWriter().write(mapper.writeValueAsString(client));
                    } else {
                        response.setStatus(404);
                        response.getWriter().write("{\"error\": \"Client non trouvé\"}");
                    }
                } else {
                    response.setStatus(404);
                    response.getWriter().write("{\"error\": \"Machine non trouvée\"}");
                }
                
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"error\": \"Endpoint non trouvé: \" + pathInfo}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Erreur serveur: \" + e.getMessage()}");
        }
    }
    
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo != null && pathInfo.startsWith("/interventions/")) {
                String[] parts = pathInfo.split("/");
                if (parts.length >= 3) {
                    String numero = parts[2];
                    
                    // 5. Modifier une intervention
                    Intervention existing = interventionDAO.getInterventionById(numero);
                    if (existing != null) {
                        // Lire le JSON du body
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = request.getReader().readLine()) != null) {
                            sb.append(line);
                        }
                        
                        Intervention updatedIntervention = mapper.readValue(sb.toString(), Intervention.class);
                        updatedIntervention.setNumeroIntervention(numero);
                        
                        boolean success = interventionDAO.updateIntervention(updatedIntervention);
                        if (success) {
                            response.getWriter().write("{\"message\": \"Intervention mise à jour\"}");
                        } else {
                            response.setStatus(500);
                            response.getWriter().write("{\"error\": \"Erreur mise à jour\"}");
                        }
                    } else {
                        response.setStatus(404);
                        response.getWriter().write("{\"error\": \"Intervention non trouvée\"}");
                    }
                }
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"error\": \"Endpoint PUT non trouvé\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Erreur serveur: \" + e.getMessage()}");
        }
    }
}