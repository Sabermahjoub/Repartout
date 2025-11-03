package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Client;
import models.Machine;
import dao.MachineDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/machines")
public class MachineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachineDAO machineDAO;

    @Override
    public void init() throws ServletException {
    	machineDAO = new MachineDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                action = "list";
            }
            
            switch (action) {
                case "list":
                    listMachines(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteMachine(request, response);
                    break;
                case "deleteMultiple":
                    deleteMultipleClients(request, response);
                    break;
				case "search": 
					searchMachines(request, response); 
					break;
				 
                default:
                    listMachines(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur de base de données", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            if ("add".equals(action)) {
                addMachine(request, response);
            } else if ("update".equals(action)) {
                updateMachine(request, response);
            } else {
                listMachines(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur de base de données", e);
        }
    }

    private void listMachines(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        List<Machine> machines = machineDAO.getAllMachines();
        //int totalClients = machineDAO.getTotalClients();
        System.out.println("Nombre de machines : " + machines.size());

        request.setAttribute("machines", machines);
        request.setAttribute("totalClients", 4);
        request.getRequestDispatcher("/Machines.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        String id = request.getParameter("id");
        Machine machine = machineDAO.getMachineById(id);
        
        request.setAttribute("machine", machine);
        request.getRequestDispatcher("/Machines.jsp").forward(request, response);
    }

    private void addMachine(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        String idMachine = request.getParameter("idMachine");
        String marque = request.getParameter("marque");
        String modele = request.getParameter("modele");
        String description = request.getParameter("description");
        int client = Integer.parseInt(request.getParameter("client"));
        String dateFabrication = request.getParameter("dateFabrication");
        String dateFinFabrication = request.getParameter("dateFinFabrication");
        String notes = request.getParameter("notes");
        
        Machine machine = new Machine(idMachine, marque, modele, description, client, dateFabrication, dateFinFabrication, notes);
        
        boolean success = machineDAO.addMachine(machine);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/machines?success=add");
        } else {
            response.sendRedirect(request.getContextPath() + "/machines?error=add");
        }
    }

    private void updateMachine(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        String idMachine = request.getParameter("idMachine");
        String marque = request.getParameter("marque");
        String modele = request.getParameter("modele");
        String description = request.getParameter("description");
        int client = Integer.parseInt(request.getParameter("client"));
        String dateFabrication = request.getParameter("dateFabrication");
        String dateFinFabrication = request.getParameter("dateFinFabrication");
        String notes = request.getParameter("notes");
        
        Machine machine = new Machine(idMachine, marque, modele, description, client, dateFabrication, dateFinFabrication, notes);
        
        boolean success = machineDAO.updateMachine(machine);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/machines?success=update");
        } else {
            response.sendRedirect(request.getContextPath() + "/machines?error=update");
        }
    }

    private void deleteMachine(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        String idMachine = request.getParameter("idMachine");
        boolean success = machineDAO.deleteMachine(idMachine);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/machines?success=delete");
        } else {
            response.sendRedirect(request.getContextPath() + "/machines?error=delete");
        }
    }

    private void deleteMultipleClients(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        String[] idsStr = request.getParameterValues("ids");
        
        if (idsStr != null && idsStr.length > 0) {
            String[] ids = new String[idsStr.length];
            for (int i = 0; i < idsStr.length; i++) {
                ids[i] = idsStr[i];
            }
            
            boolean success = machineDAO.deleteMultipleMachines(ids);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/machines?success=deleteMultiple");
            } else {
                response.sendRedirect(request.getContextPath() + "/machines?error=deleteMultiple");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/clients");
        }
    }

	
	  private void searchMachines(HttpServletRequest request, HttpServletResponse response) 
			  throws SQLException, ServletException, IOException {
	  
		  String searchTerm = request.getParameter("searchTerm");
		  
		  List<Machine> machines; 
		  if (searchTerm != null && !searchTerm.trim().isEmpty()){ 
			  machines = machineDAO.searchMachines(searchTerm); 
		  } 
		  else { 
			  machines = machineDAO.getAllMachines(); 
		  }
	  	  int totalMachines = machineDAO.getTotalMachines();
	  
		  request.setAttribute("machines", machines);
		  request.setAttribute("totalMachines", totalMachines);
		  request.setAttribute("searchTerm", searchTerm);
		  request.getRequestDispatcher("/Machines.jsp").forward(request, response); 
	  	}
	 
}