<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="models.Client" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Clients - RéparTout</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }

        header {
            background: rgba(255, 255, 255, 0.95);
            padding: 1.5rem 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .header-content {
            max-width: 1400px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .logo-icon {
            font-size: 2rem;
        }

        .logo h1 {
            color: #667eea;
            font-size: 1.5rem;
        }

        nav a {
            color: #333;
            text-decoration: none;
            font-weight: 500;
            margin-left: 2rem;
            transition: color 0.3s;
        }

        nav a:hover {
            color: #667eea;
        }

        .container {
            max-width: 1400px;
            margin: 2rem auto;
            padding: 0 2rem;
        }

        .alert {
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1rem;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .page-header {
            background: white;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
        }

        .page-header h2 {
            color: #333;
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .page-header p {
            color: #666;
        }

        .toolbar {
            background: white;
            padding: 1.5rem;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 1rem;
        }

        .search-box {
            display: flex;
            gap: 1rem;
            flex: 1;
            max-width: 500px;
        }

        .search-box input {
            flex: 1;
            padding: 0.8rem;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1rem;
        }

        .btn {
            padding: 0.8rem 1.5rem;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-danger {
            background: #dc3545;
            color: white;
        }

        .btn-danger:hover {
            background: #c82333;
        }

        .btn-edit {
            background: #28a745;
            color: white;
            padding: 0.5rem 1rem;
            font-size: 0.9rem;
        }

        .btn-delete {
            background: #dc3545;
            color: white;
            padding: 0.5rem 1rem;
            font-size: 0.9rem;
        }

        .table-container {
            background: white;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        th {
            padding: 1rem;
            text-align: left;
            font-weight: 600;
        }

        td {
            padding: 1rem;
            border-bottom: 1px solid #e0e0e0;
        }

        tbody tr:hover {
            background: #f8f9ff;
        }

        .actions {
            display: flex;
            gap: 0.5rem;
        }

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }

        .modal.active {
            display: flex;
        }

        .modal-content {
            background: white;
            padding: 2rem;
            border-radius: 15px;
            max-width: 600px;
            width: 90%;
            max-height: 90vh;
            overflow-y: auto;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .close-btn {
            font-size: 2rem;
            color: #666;
            cursor: pointer;
            background: none;
            border: none;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: #333;
            font-weight: 600;
        }

        .form-group input,
        .form-group textarea {
            width: 100%;
            padding: 0.8rem;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1rem;
            font-family: inherit;
        }

        .form-actions {
            display: flex;
            gap: 1rem;
            justify-content: flex-end;
            margin-top: 2rem;
        }

        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .stat-card {
            background: white;
            padding: 1.5rem;
            border-radius: 10px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
        }

        .stat-card h4 {
            color: #666;
            font-size: 0.9rem;
            margin-bottom: 0.5rem;
        }

        .stat-card p {
            color: #667eea;
            font-size: 2rem;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <header>
        <div class="header-content">
            <div class="logo">
                <span class="logo-icon">🔧</span>
                <h1>RéparTout - Gestion des Clients</h1>
            </div>
            <nav>
                <a href="index.html">Accueil</a>
                <a href="#">Demandes</a>
                <a style="color:blue;" href="clients">Clients</a>
            </nav>
        </div>
    </header>

    <div class="container">
        <!-- Messages de succès/erreur -->
        <c:if test="${param.success == 'add'}">
            <div class="alert alert-success">✓ Client ajouté avec succès !</div>
        </c:if>
        <c:if test="${param.success == 'update'}">
            <div class="alert alert-success">✓ Client modifié avec succès !</div>
        </c:if>
        <c:if test="${param.success == 'delete'}">
            <div class="alert alert-success">✓ Client supprimé avec succès !</div>
        </c:if>
        <c:if test="${param.success == 'deleteMultiple'}">
            <div class="alert alert-success">✓ Clients supprimés avec succès !</div>
        </c:if>
        <c:if test="${param.error != null}">
            <div class="alert alert-error">✗ Une erreur est survenue !</div>
        </c:if>

        <div class="page-header">
            <h2>👥 Gestion des Clients</h2>
            <p>Gérez tous vos clients en un seul endroit</p>
        </div>

        <div class="stats">
            <div class="stat-card">
                <h4>Total Clients</h4>
                <p>${totalClients}</p>
            </div>
        </div>

        <div class="toolbar">
            <form action="clients" method="get" class="search-box">
                <input type="hidden" name="action" value="search">
                <input type="text" name="searchTerm" placeholder="Rechercher un client..." value="${searchTerm}">
                <button type="submit" class="btn btn-primary">🔍 Rechercher</button>
            </form>
            <div style="display: flex; gap: 1rem;">
                <button class="btn btn-primary" onclick="openAddModal()">➕ Ajouter Client</button>
                <button class="btn btn-danger" onclick="deleteSelected()">🗑️ Supprimer Sélection</button>
            </div>
        </div>

        <div class="table-container">
            <form id="deleteForm" action="clients" method="get">
                <input type="hidden" name="action" value="deleteMultiple">
                <table>
                    <thead>
                        <tr>
                            <th><input type="checkbox" id="selectAll" onchange="toggleSelectAll()"></th>
                            <th>ID</th>
                            <th>Nom</th>
                            <th>Entreprise</th>
                            <th>Email</th>
                            <th>Téléphone</th>
                            <th>Adresse</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="client" items="${clients}">
                            <tr>
                                <td><input type="checkbox" name="ids" value="${client.idClient}" class="client-checkbox"></td>
                                <td>${client.idClient}</td>
                                <td>${client.nom}</td>
                                <td>${client.nomEntreprise}</td>
                                <td>${client.adresseEmail}</td>
                                <td>0${client.numTelephone}</td>
                                <td>${client.adressePostale}</td>
                                <td>
                                    <div class="actions">
                                        <button type="button" class="btn btn-edit" onclick="editClient(${client.idClient}, '${client.nom}', '${client.nomEntreprise}', '${client.adressePostale}', '${client.adresseEmail}', ${client.numTelephone}, '${client.notes}')">✏️</button>
                                        <a href="clients?action=delete&id=${client.idClient}" class="btn btn-delete" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce client ?')">🗑️</a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </form>
        </div>
    </div>

    <!-- Modal Ajout -->
    <div class="modal" id="addModal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Ajouter un Client</h3>
                <button class="close-btn" onclick="closeModal('addModal')">&times;</button>
            </div>
            <form action="clients" method="post">
                <input type="hidden" name="action" value="add">
                
                <div class="form-group">
                    <label for="nom">Nom complet *</label>
                    <input type="text" id="nom" name="nom" required>
                </div>

                <div class="form-group">
                    <label for="nomEntreprise">Nom Entreprise</label>
                    <input type="text" id="nomEntreprise" name="nomEntreprise">
                </div>

                <div class="form-group">
                    <label for="adresseEmail">Email *</label>
                    <input type="email" id="adresseEmail" name="adresseEmail" required>
                </div>

                <div class="form-group">
                    <label for="numTelephone">Téléphone *</label>
                    <input type="number" id="numTelephone" name="numTelephone" required>
                </div>

                <div class="form-group">
                    <label for="adressePostale">Adresse</label>
                    <textarea id="adressePostale" name="adressePostale" rows="3"></textarea>
                </div>

                <div class="form-group">
                    <label for="notes">Notes</label>
                    <textarea id="notes" name="notes" rows="3"></textarea>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn" onclick="closeModal('addModal')">Annuler</button>
                    <button type="submit" class="btn btn-primary">Enregistrer</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Modal Modification -->
    <div class="modal" id="editModal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Modifier le Client</h3>
                <button class="close-btn" onclick="closeModal('editModal')">&times;</button>
            </div>
            <form action="clients" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" id="editId" name="id">
                
                <div class="form-group">
                    <label for="editNom">Nom complet *</label>
                    <input type="text" id="editNom" name="nom" required>
                </div>

                <div class="form-group">
                    <label for="editNomEntreprise">Nom Entreprise</label>
                    <input type="text" id="editNomEntreprise" name="nomEntreprise">
                </div>

                <div class="form-group">
                    <label for="editAdresseEmail">Email *</label>
                    <input type="email" id="editAdresseEmail" name="adresseEmail" required>
                </div>

                <div class="form-group">
                    <label for="editNumTelephone">Téléphone *</label>
                    <input type="number" id="editNumTelephone" name="numTelephone" required>
                </div>

                <div class="form-group">
                    <label for="editAdressePostale">Adresse</label>
                    <textarea id="editAdressePostale" name="adressePostale" rows="3"></textarea>
                </div>

                <div class="form-group">
                    <label for="editNotes">Notes</label>
                    <textarea id="editNotes" name="notes" rows="3"></textarea>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn" onclick="closeModal('editModal')">Annuler</button>
                    <button type="submit" class="btn btn-primary">Enregistrer</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openAddModal() {
            document.getElementById('addModal').classList.add('active');
        }

        function closeModal(modalId) {
            document.getElementById(modalId).classList.remove('active');
        }

        function editClient(id, nom, nomEntreprise, adressePostale, adresseEmail, numTelephone, notes) {
            document.getElementById('editId').value = id;
            document.getElementById('editNom').value = nom;
            document.getElementById('editNomEntreprise').value = nomEntreprise || '';
            document.getElementById('editAdressePostale').value = adressePostale || '';
            document.getElementById('editAdresseEmail').value = adresseEmail;
            document.getElementById('editNumTelephone').value = numTelephone;
            document.getElementById('editNotes').value = notes || '';
            document.getElementById('editModal').classList.add('active');
        }

        function toggleSelectAll() {
            const selectAll = document.getElementById('selectAll');
            const checkboxes = document.querySelectorAll('.client-checkbox');
            checkboxes.forEach(cb => cb.checked = selectAll.checked);
        }

        function deleteSelected() {
            const checkboxes = document.querySelectorAll('.client-checkbox:checked');
            if (checkboxes.length === 0) {
                alert('Veuillez sélectionner au moins un client à supprimer');
                return;
            }
            if (confirm(`Êtes-vous sûr de vouloir supprimer ${checkboxes.length} client(s) ?`)) {
                document.getElementById('deleteForm').submit();
            }
        }
    </script>
</body>
</html>