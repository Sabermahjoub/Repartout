package servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Machine;
import dao.InterventionDAO;
import dao.MachineDAO;
import models.Intervention;

@WebServlet("/interventions")
public class InterventionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private InterventionDAO interventionDAO;
    private MachineDAO machineDAO;
    
    public void init() {
        interventionDAO = new InterventionDAO();
        machineDAO = new MachineDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                listInterventions(request, response);
            } else {
                switch (action) {
                    case "new":
                        showNewForm(request, response);
                        break;
                    case "edit":
                        showEditForm(request, response);
                        break;
                    case "delete":
                        deleteIntervention(request, response);
                        break;
                    case "annuler":
                        annulerIntervention(request, response);
                        break;
                    default:
                        listInterventions(request, response);
                        break;
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if ("insert".equals(action)) {
                insertIntervention(request, response);
            } else if ("update".equals(action)) {
                updateIntervention(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    private void listInterventions(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Intervention> interventions = interventionDAO.getAllInterventions();
        request.setAttribute("interventions", interventions);
        request.getRequestDispatcher("intervention-list.jsp").forward(request, response);
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // DEBUG 1: Vérifiez si le DAO est initialisé
            System.out.println("=== DEBUG InterventionServlet ===");
            System.out.println("machineDAO est null ? " + (machineDAO == null));
            
            // DEBUG 2: Récupérez les machines
            List<Machine> machines = machineDAO.getAllMachines();
            System.out.println("Nombre de machines récupérées: " + (machines != null ? machines.size() : "null"));
            
            if (machines != null) {
                for (Machine machine : machines) {
                    System.out.println("Machine: " + machine.getIdMachine() + " - " + machine.getMarque());
                }
            }
            
            // DEBUG 3: Génération du numéro
            String numeroIntervention = interventionDAO.generateNumeroIntervention();
            System.out.println("Numéro généré: " + numeroIntervention);
            
            // Set attributes
            request.setAttribute("machines", machines);
            request.setAttribute("numeroIntervention", numeroIntervention);
            
            System.out.println("=== FIN DEBUG ===");
            
        } catch (Exception e) {
            System.err.println("ERREUR dans showNewForm: " + e.getMessage());
            e.printStackTrace();
            throw e; // Important: relancer l'exception
        }
        
        request.getRequestDispatcher("intervention-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String numero = request.getParameter("numero");
        Intervention intervention = interventionDAO.getInterventionById(numero);
        request.setAttribute("intervention", intervention);
        request.setAttribute("machines", machineDAO.getAllMachines());
        request.getRequestDispatcher("intervention-form.jsp").forward(request, response);
    }
    
    private void insertIntervention(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String numero = request.getParameter("numeroIntervention");
        String machineId = request.getParameter("machineId");
        String description = request.getParameter("descriptionProbleme");
        String technicien = request.getParameter("technicien");
        String etat = "en attente";
        String notes = request.getParameter("notes");
        
        String dateInterventionStr = request.getParameter("dateIntervention");
        System.out.println("Date reçue du formulaire: " + dateInterventionStr);
        
        // CORRECTION : Forcer le timezone et s'assurer qu'on garde le bon jour
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Utiliser UTC pour être consistent
        
        Date dateIntervention = dateFormat.parse(dateInterventionStr);
        Date dateDemande = new Date();
        
        // DEBUG
        System.out.println("Date après correction timezone: " + dateIntervention);
        System.out.println("Formatée (dd/MM/yyyy): " + new SimpleDateFormat("dd/MM/yyyy").format(dateIntervention));
        
        Intervention intervention = new Intervention(numero, dateDemande, machineId, description, 
                                                    dateIntervention, technicien, etat, notes);
        
        if (interventionDAO.addIntervention(intervention)) {
            response.sendRedirect("interventions?success=created");
        } else {
            response.sendRedirect("interventions?error=create_failed");
        }
    }
    private void updateIntervention(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String numero = request.getParameter("numeroIntervention");
        String machineId = request.getParameter("machineId");
        String description = request.getParameter("descriptionProbleme");
        String technicien = request.getParameter("technicien");
        String etat = request.getParameter("etat");
        String notes = request.getParameter("notes");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateDemande = dateFormat.parse(request.getParameter("dateDemande"));
        Date dateIntervention = dateFormat.parse(request.getParameter("dateIntervention"));
        
        Intervention intervention = new Intervention(numero, dateDemande, machineId, description, dateIntervention, technicien, etat, notes);
        
        if (interventionDAO.updateIntervention(intervention)) {
            response.sendRedirect("interventions?success=updated");
        } else {
            response.sendRedirect("interventions?error=update_failed");
        }
    }
    
    private void deleteIntervention(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String numero = request.getParameter("numero");
        if (interventionDAO.deleteIntervention(numero)) {
            response.sendRedirect("interventions?success=deleted");
        } else {
            response.sendRedirect("interventions?error=delete_failed");
        }
    }
    
    private void annulerIntervention(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String numero = request.getParameter("numero");
        if (interventionDAO.annulerIntervention(numero)) {
            response.sendRedirect("interventions?success=cancelled");
        } else {
            response.sendRedirect("interventions?error=cancel_failed");
        }
    }
}