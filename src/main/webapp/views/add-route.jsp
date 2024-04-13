<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Route</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
        }

        .container {
            max-width: 800px;
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        #header {
            color: #333;
            border-bottom: 2px solid #28a745;
            padding-bottom: 15px;
        }

        .form-control {
            border-radius: 4px;
        }

        .btn-custom {
            background-color: #28a745;
            border-color: #28a745;
            width: 100%;
            padding: 10px 20px;
            margin-top: 10px;
            display: block;
            text-align: center;
            transition: background-color 0.3s ease, border-color 0.3s ease; /* Smooth transition */
        }

        .btn-custom:hover {
            background-color: #218838;
            border-color: #218838;
        }

        .custom-suggestions {
            position: absolute;
            width: 200px;
            max-height: 250px;
            overflow-y: auto;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            background-color: #fff;
            z-index: 1000;
        }

        .custom-suggestions ul {
            list-style-type: none;
            margin: 0;
            padding: 0;
        }

        .custom-suggestions li {
            padding: 10px;
            cursor: pointer;
        }

        .custom-suggestions li:hover {
            background-color: #f5f5f5;
        }
    </style>
</head>
<body>

<div class="container mt-5">
    <div id="header">
        <h2 style="display: inline">Add Route</h2>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary float-end">Search Route</a>
    </div>
    <form id="addRouteForm" class="mt-4">
        <div class="row">
            <div class="col-md-4">
                <div class="mb-3">
                    <label for="source" class="form-label">Source:</label>
                    <input type="text" class="form-control" id="source" name="source" placeholder="Enter source"
                           required>
                    <div id="customSuggestionsSource" class="custom-suggestions" style="display: none;">
                        <ul id="sourceSuggestions"></ul>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="mb-3">
                    <label for="destination" class="form-label">Destination:</label>
                    <input type="text" class="form-control" id="destination" name="destination"
                           placeholder="Enter destination" required>
                    <div id="customSuggestionsDestination" class="custom-suggestions" style="display: none;">
                        <ul id="destinationSuggestions"></ul>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="mb-3">
                    <label for="distance" class="form-label">Distance (km):</label>
                    <input type="number" class="form-control" id="distance" name="distance" placeholder="Enter distance"
                           min="0" step="0.1" required>
                </div>
            </div>
            <div class="col-md-12 align-self-end mb-3">
                <button type="button" id="addRouteBtn" class="btn btn-custom btn-block">Add Route</button>
            </div>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('addRouteBtn').addEventListener('click', function () {
            let source = document.getElementById('source').value;
            let destination = document.getElementById('destination').value;
            let distance = document.getElementById('distance').value;

            if (!source.trim() || !destination.trim() || distance <= 0) {
                alert('Please enter valid source, destination, and positive distance.');
                return;
            }

            fetch('/api/add-route', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({source: source, destination: destination, distance: distance})
            })
                .then(response => {
                    if (response.status === 200) {
                        alert('Route added successfully!');
                    } else {
                        alert('Error adding route.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });

        const sourceInput = document.getElementById('source');
        const destinationInput = document.getElementById('destination');
        const sourceSuggestions = document.getElementById('sourceSuggestions');
        const destinationSuggestions = document.getElementById('destinationSuggestions');
        const customSuggestionsSource = document.getElementById('customSuggestionsSource');
        const customSuggestionsDestination = document.getElementById('customSuggestionsDestination');

        sourceInput.addEventListener('input', function () {
            showSuggestions(this.value, sourceSuggestions);
        });

        destinationInput.addEventListener('input', function () {
            showSuggestions(this.value, destinationSuggestions);
        });

        function showSuggestions(value, suggestionsList) {
            suggestionsList.innerHTML = '';

            <% String[] suggestions = (String[]) request.getAttribute("suggestions");
               if (suggestions != null) {
                   for (String suggestion : suggestions) {
            %>
            if ("<%= suggestion.trim() %>".toLowerCase().includes(value.toLowerCase())) {
                const li = document.createElement('li');
                li.textContent = '<%= suggestion.trim() %>';
                li.addEventListener('click', function () {
                    if (suggestionsList === sourceSuggestions) {
                        sourceInput.value = this.textContent;
                    } else if (suggestionsList === destinationSuggestions) {
                        destinationInput.value = this.textContent;
                    }
                    customSuggestionsSource.style.display = 'none';
                    customSuggestionsDestination.style.display = 'none';
                });
                suggestionsList.appendChild(li);
            }
            <%
                   }
               }
            %>

            if (suggestionsList.children.length > 0) {
                if (suggestionsList.id === 'sourceSuggestions') {
                    customSuggestionsDestination.style.display = 'none';
                    customSuggestionsSource.style.display = 'block';
                } else if (suggestionsList.id === 'destinationSuggestions') {
                    customSuggestionsSource.style.display = 'none';
                    customSuggestionsDestination.style.display = 'block';
                }
            } else {
                customSuggestionsSource.style.display = 'none';
                customSuggestionsDestination.style.display = 'none';
            }
        }
    });
</script>

</body>
</html>
