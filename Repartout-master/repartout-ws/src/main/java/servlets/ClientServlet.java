package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Client;
import dao.ClientDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/clients")
public class ClientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClientDAO clientDAO;

    @Override
    public void init() throws ServletException {
        clientDAO = new ClientDAO();
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
                    listClients(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteClient(request, response);
                    break;
                case "deleteMultiple":
                    deleteMultipleClients(request, response);
                    break;
                case "search":
                    searchClients(request, response);
                    break;
                default:
                    listClients(request, response);
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
                addClient(request, response);
            } else if ("update".equals(action)) {
                updateClient(request, response);
            } else {
                listClients(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur de base de données", e);
        }
    }

    private void listClients(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        List<Client> clients = clientDAO.getAllClients();
        int totalClients = clientDAO.getTotalClients();
        
        request.setAttribute("clients", clients);
        request.setAttribute("totalClients", totalClients);
        request.getRequestDispatcher("/Clients.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Client client = clientDAO.getClientById(id);
        
        request.setAttribute("client", client);
        request.getRequestDispatcher("/Clients.jsp").forward(request, response);
    }

    private void addClient(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        String nom = request.getParameter("nom");
        String nomEntreprise = request.getParameter("nomEntreprise");
        String adressePostale = request.getParameter("adressePostale");
        String adresseEmail = request.getParameter("adresseEmail");
        long numTelephone = Long.parseLong(request.getParameter("numTelephone"));
        String notes = request.getParameter("notes");
        
        Client client = new Client(0, nom, nomEntreprise, adressePostale, adresseEmail, numTelephone, notes);
        
        boolean success = clientDAO.addClient(client);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/clients?success=add");
        } else {
            response.sendRedirect(request.getContextPath() + "/clients?error=add");
        }
    }

    private void updateClient(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String nom = request.getParameter("nom");
        String nomEntreprise = request.getParameter("nomEntreprise");
        String adressePostale = request.getParameter("adressePostale");
        String adresseEmail = request.getParameter("adresseEmail");
        long numTelephone = Long.parseLong(request.getParameter("numTelephone"));
        String notes = request.getParameter("notes");
        
        Client client = new Client(id, nom, nomEntreprise, adressePostale, adresseEmail, numTelephone, notes);
        
        boolean success = clientDAO.updateClient(client);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/clients?success=update");
        } else {
            response.sendRedirect(request.getContextPath() + "/clients?error=update");
        }
    }

    private void deleteClient(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = clientDAO.deleteClient(id);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/clients?success=delete");
        } else {
            response.sendRedirect(request.getContextPath() + "/clients?error=delete");
        }
    }

    private void deleteMultipleClients(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        String[] idsStr = request.getParameterValues("ids");
        
        if (idsStr != null && idsStr.length > 0) {
            int[] ids = new int[idsStr.length];
            for (int i = 0; i < idsStr.length; i++) {
                ids[i] = Integer.parseInt(idsStr[i]);
            }
            
            boolean success = clientDAO.deleteMultipleClients(ids);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/clients?success=deleteMultiple");
            } else {
                response.sendRedirect(request.getContextPath() + "/clients?error=deleteMultiple");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/clients");
        }
    }

    private void searchClients(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        String searchTerm = request.getParameter("searchTerm");
        
        List<Client> clients;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            clients = clientDAO.searchClients(searchTerm);
        } else {
            clients = clientDAO.getAllClients();
        }
        
        int totalClients = clientDAO.getTotalClients();
        
        request.setAttribute("clients", clients);
        request.setAttribute("totalClients", totalClients);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/Clients.jsp").forward(request, response);
    }
}