<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Route</title>
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
            border-bottom: 2px solid #007bff;
            padding-bottom: 15px;
        }

        .form-control {
            border-radius: 4px;
        }

        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
        }

        #result {
            margin-top: 30px;
        }

        .path-container, .distance-container {
            margin-top: 30px;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        }

        .path-container {
            background-color: #f9f9f9;
        }

        .distance-container {
            background-color: #e9e9e9;
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
        <h2 style="display: inline">Search Route</h2>
        <a href="${pageContext.request.contextPath}/add-route" class="btn btn-success float-end mb-3">Add Route</a>
    </div>
    <form id="searchForm" class="mt-4">
        <div class="row">
            <div class="col-md-5">
                <div class="mb-3">
                    <label for="source" class="form-label">Source:</label>
                    <input type="text" class="form-control" id="source" name="source" placeholder="Enter source"
                           required>
                    <div id="customSuggestionsSource" class="custom-suggestions" style="display: none;">
                        <ul id="sourceSuggestions"></ul>
                    </div>
                </div>
            </div>

            <div class="col-md-5">
                <div class="mb-3">
                    <label for="destination" class="form-label">Destination:</label>
                    <input type="text" class="form-control" id="destination" name="destination"
                           placeholder="Enter destination" required>
                    <div id="customSuggestionsDestination" class="custom-suggestions" style="display: none;">
                        <ul id="destinationSuggestions"></ul>
                    </div>
                </div>
            </div>

            <div class="col-md-2 align-self-end mb-3">
                <button type="button" id="searchBtn" class="btn btn-primary btn-block">Search</button>
            </div>
        </div>
    </form>

    <div id="result" class="mt-4">
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('searchBtn').addEventListener('click', function () {
            let source = document.getElementById('source').value;
            let destination = document.getElementById('destination').value;

            if (!isInSuggestions(source) || !isInSuggestions(destination)) {
                alert('Please enter a valid source and destination.');
                return;
            }

            fetch('/api/search?source=' + source + '&destination=' + destination)
                .then(response => response.json())
                .then(data => {
                    displayResult(data);
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

    function displayResult(data) {
        const resultDiv = document.getElementById('result');
        const path = data.path;
        const distance = data.distance;

        let pathHtml = '<div class="path-container"><h5><strong>Path:</strong> ' + path.join(' ➔ ') + '</h5></div>';
        const distanceHtml = '<div class="distance-container"><h3 style="display: inline">Distance: ' + distance + ' km </h3></div>';

        resultDiv.innerHTML = pathHtml + distanceHtml;
    }

    function isInSuggestions(value) {
        <% String[] suggestionsList = (String[]) request.getAttribute("suggestions");
           if (suggestionsList != null) {
               for (String suggestion : suggestionsList) {
        %>
        if ("<%= suggestion.trim() %>".toLowerCase() === value.toLowerCase()) {
            return true;
        }
        <%
               }
           }
        %>
        return false;
    }
</script>

</body>
</html>
