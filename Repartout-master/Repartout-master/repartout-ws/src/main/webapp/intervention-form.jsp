<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle Intervention - RéparTout</title>
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
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 2rem;
        }

        .form-card {
            background: white;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }

        .form-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .form-header h2 {
            color: #333;
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .form-header p {
            color: #666;
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
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 0.8rem;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1rem;
            font-family: inherit;
            transition: border-color 0.3s;
        }

        .form-group input:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            border-color: #667eea;
            outline: none;
        }

        .readonly-field {
            background-color: #f8f9fa;
            color: #6c757d;
            cursor: not-allowed;
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

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .form-actions {
            display: flex;
            gap: 1rem;
            justify-content: flex-end;
            margin-top: 2rem;
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

        .required::after {
            content: " *";
            color: #dc3545;
        }
    </style>
</head>
<body>
    <header>
        <div class="header-content">
            <div class="logo">
                <span class="logo-icon">🔧</span>
                <h1>RéparTout - Nouvelle Intervention</h1>
            </div>
            <nav>
                <a href="index.html">Accueil</a>
                <a href="interventions">Liste des Interventions</a>
                <a href="machines">Machines</a>
                <a href="clients">Clients</a>
            </nav>
        </div>
    </header>

 

    <div class="container">
        <!-- Messages -->
        <c:if test="${param.success == 'created'}">
            <div class="alert alert-success">✓ Intervention créée avec succès !</div>
        </c:if>
        <c:if test="${param.error == 'create_failed'}">
            <div class="alert alert-error">✗ Erreur lors de la création de l'intervention !</div>
        </c:if>

        <div class="form-card">
            <div class="form-header">
                <h2>📝 Nouvelle Demande d'Intervention</h2>
                <p>Remplissez le formulaire pour créer une nouvelle demande</p>
            </div>

            <form action="interventions" method="post">
                <input type="hidden" name="action" value="insert">
                
                <div class="form-group">
                    <label for="numeroIntervention">Numéro d'Intervention</label>
                    <input type="text" id="numeroIntervention" name="numeroIntervention" 
                           value="${numeroIntervention}" readonly class="readonly-field">
                </div>

                <div class="form-group">
                    <label for="machineId" class="required">Machine concernée</label>
                    <select id="machineId" name="machineId" required>
                        <option value="">Sélectionnez une machine</option>
                        <c:forEach var="machine" items="${machines}">
                            <option value="${machine.idMachine}">
                                ${machine.idMachine} - ${machine.marque} ${machine.modele}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="descriptionProbleme" class="required">Description du problème</label>
                    <textarea id="descriptionProbleme" name="descriptionProbleme" rows="4" 
                              placeholder="Décrivez le problème rencontré..." required></textarea>
                </div>

                <div class="form-group">
                    <label for="dateIntervention" class="required">Date d'intervention prévisionnelle</label>
                    <input type="date" id="dateIntervention" name="dateIntervention" required>
                </div>

                <div class="form-group">
                    <label for="technicien" class="required">Technicien assigné</label>
                    <input type="text" id="technicien" name="technicien" 
                           placeholder="Nom du technicien" required>
                </div>

                <!-- CHAMP NOTES AJOUTÉ ICI -->
                <div class="form-group">
                    <label for="notes">Notes supplémentaires</label>
                    <textarea id="notes" name="notes" rows="3" 
                              placeholder="Informations complémentaires, remarques..."></textarea>
                </div>

                <div class="form-actions">
                    <a href="interventions" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn btn-primary">Créer l'Intervention</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Définir la date minimale à aujourd'hui
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('dateIntervention').min = today;
        
        // Auto-remplir avec une date dans 2 jours par défaut
        const inTwoDays = new Date();
        inTwoDays.setDate(inTwoDays.getDate() + 2);
        const defaultDate = inTwoDays.toISOString().split('T')[0];
        document.getElementById('dateIntervention').value = defaultDate;
    </script>
</body>
</html>