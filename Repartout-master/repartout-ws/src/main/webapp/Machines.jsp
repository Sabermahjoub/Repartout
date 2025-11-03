<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="models.Machine" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des machines - RéparTout</title>
    <style>
    
        *{margin:0;padding:0;box-sizing:border-box}
        body{font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);min-height:100vh}
        header{background:rgba(255,255,255,0.95);padding:1.5rem 2rem;box-shadow:0 2px 10px rgba(0,0,0,0.1)}
        .header-content{max-width:1400px;margin:0 auto;display:flex;justify-content:space-between;align-items:center}
        .logo{display:flex;align-items:center;gap:1rem}
        .logo h1{color:#667eea;font-size:1.5rem}
        nav a{color:#333;text-decoration:none;font-weight:500;margin-left:2rem;transition:color .3s}
        nav a:hover{color:#667eea}
        .container{max-width:1400px;margin:2rem auto;padding:0 2rem}
        .alert{padding:1rem;border-radius:8px;margin-bottom:1rem}
        .alert-success{background:#d4edda;color:#155724;border:1px solid #c3e6cb}
        .alert-error{background:#f8d7da;color:#721c24;border:1px solid #f5c6cb}
        .page-header{background:white;padding:2rem;border-radius:15px;box-shadow:0 5px 20px rgba(0,0,0,0.1);margin-bottom:2rem}
        .page-header h2{color:#333;font-size:2rem;margin-bottom:.5rem}
        .toolbar{background:white;padding:1.5rem;border-radius:15px;box-shadow:0 5px 20px rgba(0,0,0,0.1);margin-bottom:2rem;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:1rem}
        .search-box{display:flex;gap:1rem;flex:1;max-width:500px}
        .search-box input{flex:1;padding:.8rem;border:2px solid #e0e0e0;border-radius:8px;font-size:1rem}
        .btn{padding:.8rem 1.5rem;border:none;border-radius:8px;font-size:1rem;font-weight:600;cursor:pointer;transition:all .3s;text-decoration:none;display:inline-block}
        .btn-primary{background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:white}
        .btn-danger{background:#dc3545;color:white}
        .btn-edit{background:#28a745;color:white;padding:.5rem 1rem;font-size:.9rem}
        .btn-delete{background:#dc3545;color:white;padding:.5rem 1rem;font-size:.9rem}
        .table-container{background:white;padding:2rem;border-radius:15px;box-shadow:0 5px 20px rgba(0,0,0,0.1);overflow-x:auto}
        table{width:100%;border-collapse:collapse}
        thead{background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:white}
        th,td{padding:1rem;text-align:left;border-bottom:1px solid #e0e0e0}
        tbody tr:hover{background:#f8f9ff}
        .actions{display:flex;gap:.5rem}
        .modal{display:none;position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.5);justify-content:center;align-items:center;z-index:1000}
        .modal.active{display:flex}
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
        .modal-content{background:white;padding:2rem;border-radius:15px;max-width:600px;width:90%;max-height:90vh;overflow-y:auto}
        .form-group{margin-bottom:1.5rem}
        .form-group label{display:block;margin-bottom:.5rem;color:#333;font-weight:600}
        .form-group input,.form-group textarea{width:100%;padding:.8rem;border:2px solid #e0e0e0;border-radius:8px;font-size:1rem;font-family:inherit}
        .form-actions{display:flex;gap:1rem;justify-content:flex-end;margin-top:2rem}
    </style>
</head>
<body>
<header>
    <div class="header-content">
        <div class="logo">
            <span class="logo-icon">🔧</span>
            <h1>RéparTout - Gestion des machines</h1>
        </div>
        <nav>
            <a href="index.html">Accueil</a>
            <a href="#">Demandes</a>
            <a style="color:blue;" href="machines">Machines</a>
        </nav>
    </div>
</header>

<div class="container">
    <!-- messages -->
    <c:if test="${param.success == 'add'}"><div class="alert alert-success">✓ Machine ajoutée avec succès !</div></c:if>
    <c:if test="${param.success == 'update'}"><div class="alert alert-success">✓ Machine modifiée avec succès !</div></c:if>
    <c:if test="${param.success == 'delete'}"><div class="alert alert-success">✓ Machine supprimée avec succès !</div></c:if>
    <c:if test="${param.error != null}"><div class="alert alert-error">✗ Une erreur est survenue !</div></c:if>

    <div class="page-header">
        <h2>🧰 Gestion des machines</h2>
        <p>Gérez toutes les machines en un seul endroit</p>
    </div>

    <div class="toolbar">
        <form action="machines" method="get" class="search-box">
            <input type="hidden" name="action" value="search">
            <input type="text" name="searchTerm" placeholder="Rechercher une machine" value="${searchTerm}">
            <button type="submit" class="btn btn-primary">🔍 Rechercher</button>
        </form>
        <div style="display:flex;gap:1rem;">
            <button class="btn btn-primary" onclick="openAddModal()">➕ Ajouter</button>
            <button class="btn btn-danger" onclick="deleteSelected()">🗑️ Supprimer sélection</button>
        </div>
    </div>

    <div class="table-container">
        <form id="deleteForm" action="machines" method="get">
            <input type="hidden" name="action" value="deleteMultiple">
            <table>
                <thead>
                <tr>
                    <th><input type="checkbox" id="selectAll" onchange="toggleSelectAll()"></th>
                    <th>ID</th>
                    <th>Marque</th>
                    <th>Modèle</th>
                    <th>Description</th>
                    <th>Client</th>
                    <th>Date Fabrication</th>
                    <th>Date Fin Fabrication</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="machine" items="${machines}">
                    <tr>
                        <td><input type="checkbox" name="ids" value="${machine.idMachine}" class="machine-checkbox"></td>
                        <td>${machine.idMachine}</td>
                        <td>${machine.marque}</td>
                        <td>${machine.modele}</td>
                        <td>${machine.description}</td>
                        <td>${machine.client}</td>
                        <td>${machine.dateFabrication}</td>
                        <td>${machine.dateFinFabrication}</td>
                        <td>
                            <div class="actions">
                                <button type="button" class="btn btn-edit"
                                        onclick="editMachine('${machine.idMachine}','${machine.marque}','${machine.modele}','${machine.description}','${machine.client}','${machine.dateFabrication}','${machine.dateFinFabrication}')">
                                    ✏️
                                </button>
                                <a href="machines?action=delete&idMachine=${machine.idMachine}"
                                   class="btn btn-delete"
                                   onclick="return confirm('Supprimer cette machine ?')">🗑️</a>
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
            <h3>Ajouter une machine</h3>
            <button class="close-btn" onclick="closeModal('addModal')">&times;</button>
        </div>
        <form action="machines" method="post">
            <input type="hidden" name="action" value="add">
            
            <div class="form-group">
                <label>Série *</label>
                <input type="text" name="idMachine" required>
            </div>

            <div class="form-group">
                <label>Marque *</label>
                <input type="text" name="marque" required>
            </div>

            <div class="form-group">
                <label>Modèle *</label>
                <input type="text" name="modele" required>
            </div>

            <div class="form-group">
                <label>Description</label>
                <textarea name="description" rows="3"></textarea>
            </div>

            <div class="form-group">
                <label>Client</label>
                <input type="number" name="client">
            </div>

            <div class="form-group">
                <label>Date de fabrication</label>
                <input type="date" name="dateFabrication">
            </div>

            <div class="form-group">
                <label>Date fin de garantie</label>
                <input type="date" name="dateFinFabrication">
            </div>
            
            <div class="form-group">
                <label>Notes</label>
                <textarea name="notes" rows="3"></textarea>
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
            <h3>Modifier une machine</h3>
            <button class="close-btn" onclick="closeModal('editModal')">&times;</button>
        </div>
        <form action="machines" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" id="editId" name="idMachine">

            <div class="form-group">
                <label>Marque *</label>
                <input type="text" id="editMarque" name="marque" required>
            </div>

            <div class="form-group">
                <label>Modèle *</label>
                <input type="text" id="editModele" name="modele" required>
            </div>

            <div class="form-group">
                <label>Description</label>
                <textarea id="editDescription" name="description" rows="3"></textarea>
            </div>

            <div class="form-group">
                <label>Client</label>
                <input type="text" id="editClient" name="client">
            </div>

            <div class="form-group">
                <label>Date de fabrication</label>
                <input type="date" id="editDateFabrication" name="dateFabrication">
            </div>

            <div class="form-group">
                <label>Date fin de fabrication</label>
                <input type="date" id="editDateFinFabrication" name="dateFinFabrication">
            </div>

            <div class="form-actions">
                <button type="button" class="btn" onclick="closeModal('editModal')">Annuler</button>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openAddModal() { document.getElementById('addModal').classList.add('active'); }
    function closeModal(id) { document.getElementById(id).classList.remove('active'); }

    function editMachine(id, marque, modele, description, client, dateFab, dateFin) {
        document.getElementById('editId').value = id;
        document.getElementById('editMarque').value = marque;
        document.getElementById('editModele').value = modele;
        document.getElementById('editDescription').value = description;
        document.getElementById('editClient').value = client;
        document.getElementById('editDateFabrication').value = dateFab;
        document.getElementById('editDateFinFabrication').value = dateFin;
        document.getElementById('editModal').classList.add('active');
    }

    function toggleSelectAll() {
        const selectAll = document.getElementById('selectAll');
        document.querySelectorAll('.machine-checkbox').forEach(cb => cb.checked = selectAll.checked);
    }

    function deleteSelected() {
        const checked = document.querySelectorAll('.machine-checkbox:checked');
        if (checked.length === 0) { alert('Sélectionnez au moins une machine'); return; }
        if (confirm(`Supprimer ${checked.length} machine(s) ?`)) {
            document.getElementById('deleteForm').submit();
        }
    }
</script>
</body>
</html>
