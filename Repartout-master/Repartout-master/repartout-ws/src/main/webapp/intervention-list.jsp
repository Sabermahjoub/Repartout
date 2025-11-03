<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Interventions - RéparTout</title>
    <style>
        /* Votre CSS existant */
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
            transition: opacity 0.5s ease-in-out;
        }

        .alert-auto-hide {
            animation: fadeOut 0.5s ease-in-out 2s forwards;
        }

        @keyframes fadeOut {
            from { opacity: 1; }
            to { opacity: 0; display: none; }
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

        .btn-warning {
            background: #ffc107;
            color: black;
        }

        .btn-danger {
            background: #dc3545;
            color: white;
        }

        .btn-success {
            background: #28a745;
            color: white;
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

        .status-badge {
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 600;
        }

        .status-en-attente {
            background: #fff3cd;
            color: #856404;
        }

        .status-en-cours {
            background: #cce7ff;
            color: #004085;
        }

        .status-realisee {
            background: #d4edda;
            color: #155724;
        }

        .status-annulee {
            background: #f8d7da;
            color: #721c24;
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
            text-align: center;
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
                <h1>RéparTout - Interventions</h1>
            </div>
            <nav>
                <a href="index.html">Accueil</a>
                <a href="machines">Machines</a>
                <a href="clients">Clients</a>
            </nav>
        </div>
    </header>

    <div class="container">
        <!-- Messages avec auto-hide -->
        <c:if test="${param.success == 'created'}">
            <div class="alert alert-success alert-auto-hide" id="alert-created">✓ Intervention créée avec succès !</div>
        </c:if>
        <c:if test="${param.success == 'updated'}">
            <div class="alert alert-success alert-auto-hide" id="alert-updated">✓ Intervention modifiée avec succès !</div>
        </c:if>
        <c:if test="${param.success == 'deleted'}">
            <div class="alert alert-success alert-auto-hide" id="alert-deleted">✓ Intervention supprimée avec succès !</div>
        </c:if>
        <c:if test="${param.success == 'cancelled'}">
            <div class="alert alert-success alert-auto-hide" id="alert-cancelled">✓ Intervention annulée avec succès !</div>
        </c:if>
        <c:if test="${param.error != null}">
            <div class="alert alert-error alert-auto-hide" id="alert-error">✗ Une erreur est survenue !</div>
        </c:if>

        <div class="page-header">
            <h2>📋 Liste des Interventions</h2>
            <p>Gérez toutes les demandes d'intervention</p>
        </div>

<div class="toolbar">
    <!-- Solution temporaire - lien direct -->
    <a href="/repartout-ws/interventions?action=new" class="btn btn-primary">➕ Nouvelle Intervention</a>
</div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>Numéro</th>
                        <th>Date Demande</th>
                        <th>Machine</th>
                        <th>Problème</th>
                        <th>Technicien</th>
                        <th>Date Intervention</th>
                        <th>État</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="intervention" items="${interventions}">
                        <tr>
                            <td><strong>${intervention.numeroIntervention}</strong></td>
                            <td>
                                <fmt:formatDate value="${intervention.dateDemande}" pattern="dd/MM/yyyy HH:mm" />
                            </td>
                            <td>${intervention.machineId}</td>
                            <td>
                                <div style="max-width: 200px; overflow: hidden; text-overflow: ellipsis;">
                                    ${intervention.descriptionProbleme}
                                </div>
                            </td>
                            <td>${intervention.technicien}</td>
                            <td>
                                <fmt:formatDate value="${intervention.dateInterventionPrevisionnelle}" pattern="dd/MM/yyyy" />
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${intervention.etat == 'en attente'}">
                                        <span class="status-badge status-en-attente">⏳ En attente</span>
                                    </c:when>
                                    <c:when test="${intervention.etat == 'en cours'}">
                                        <span class="status-badge status-en-cours">🔧 En cours</span>
                                    </c:when>
                                    <c:when test="${intervention.etat == 'réalisée'}">
                                        <span class="status-badge status-realisee">✅ Réalisée</span>
                                    </c:when>
                                    <c:when test="${intervention.etat == 'annulée'}">
                                        <span class="status-badge status-annulee">❌ Annulée</span>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>
                                <div class="actions">
                                    <c:if test="${intervention.etat != 'annulée'}">
                                        <a href="interventions?action=annuler&numero=${intervention.numeroIntervention}" 
                                           class="btn btn-danger" 
                                           onclick="return confirm('Annuler cette intervention ?')">❌</a>
                                    </c:if>
                                    <a href="interventions?action=delete&numero=${intervention.numeroIntervention}" 
                                       class="btn btn-danger" 
                                       onclick="return confirm('Supprimer cette intervention ?')">🗑️</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script>
        // Fonction pour supprimer les paramètres d'URL après affichage des messages
        document.addEventListener('DOMContentLoaded', function() {
            // Supprimer les paramètres d'URL après 3 secondes (après la disparition du message)
            setTimeout(function() {
                if (window.location.search.includes('success=') || window.location.search.includes('error=')) {
                    // Créer une nouvelle URL sans les paramètres
                    const newUrl = window.location.pathname;
                    window.history.replaceState({}, document.title, newUrl);
                }
            }, 3000); // 3 secondes pour laisser le temps à l'animation de finir
        });
    </script>
</body>
</html>