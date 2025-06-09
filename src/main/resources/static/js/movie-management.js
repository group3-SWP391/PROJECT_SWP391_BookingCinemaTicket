// Movie Management JavaScript
let editingMovieId = null;
let actorRowCount = 0;
let actorOptions = [];

// Initialize when DOM is ready
function initializeMovieManagement() {
    // Get actor options from the page (passed from Thymeleaf)
    const actorOptionsElement = document.getElementById('actorOptionsData');
    if (actorOptionsElement) {
        try {
            actorOptions = JSON.parse(actorOptionsElement.textContent);
            console.log('Actor options loaded:', actorOptions);
        } catch (e) {
            console.error('Error parsing actor options:', e);
            actorOptions = [];
        }
    }
    
    // Bind Add Movie Button
    const addMovieBtn = document.getElementById('addMovieBtn');
    if (addMovieBtn) {
        addMovieBtn.addEventListener('click', openAddMovieModal);
        console.log('Add Movie button bound successfully');
    } else {
        console.warn('Add Movie button not found');
    }

    // Make functions globally available
    window.editMovie = editMovie;
    window.deleteMovie = deleteMovie;
    window.openAddMovieModal = openAddMovieModal;
    window.addActorRow = addActorRow;
    window.removeActorRow = removeActorRow;
    window.saveMovie = saveMovie;
    window.openAddDirectorModal = openAddDirectorModal;
    window.saveDirector = saveDirector;
    window.saveActor = saveActor;
    
    console.log('Movie management initialized');
}

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', initializeMovieManagement);

// Also initialize when window loads (for dynamic content)
window.addEventListener('load', function() {
    if (document.getElementById('addMovieBtn') && !document.getElementById('addMovieBtn').hasEventListener) {
        initializeMovieManagement();
        document.getElementById('addMovieBtn').hasEventListener = true;
    }
});

// Retry mechanism for dynamic content loading
let retryCount = 0;
const maxRetries = 10;

function waitForElementsAndInitialize() {
    const addMovieBtn = document.getElementById('addMovieBtn');
    const movieModal = document.getElementById('movieModal');
    
    if (addMovieBtn && movieModal) {
        if (!addMovieBtn.hasEventListener) {
            initializeMovieManagement();
            addMovieBtn.hasEventListener = true;
        }
        return;
    }
    
    retryCount++;
    if (retryCount < maxRetries) {
        console.log(`Waiting for movie management elements... attempt ${retryCount}`);
        setTimeout(waitForElementsAndInitialize, 500);
    } else {
        console.warn('Movie management elements not found after maximum retries');
    }
}

// Start the retry mechanism
setTimeout(waitForElementsAndInitialize, 100);

// Open Add Movie Modal
function openAddMovieModal() {
    editingMovieId = null;
    
    const modalLabel = document.getElementById('movieModalLabel');
    const movieForm = document.getElementById('movieForm');
    const movieId = document.getElementById('movieId');
    const actorsContainer = document.getElementById('actorsContainer');
    
    if (!modalLabel || !movieForm || !movieId || !actorsContainer) {
        console.error('Modal elements not found. Modal HTML may not be loaded.');
        alert('Error: Modal components are not ready. Please refresh the page.');
        return;
    }
    
    modalLabel.innerHTML = '<i class="fa fa-film"></i> Add New Movie';
    movieForm.reset();
    movieId.value = '';
    actorsContainer.innerHTML = '';
    actorRowCount = 0;
    addActorRow(); // Add one default actor row
}

// Add Actor Row
function addActorRow(selectedActorId = '', characterName = '') {
    actorRowCount++;
    const container = document.getElementById('actorsContainer');
    const rowDiv = document.createElement('div');
    rowDiv.className = 'actor-row';
    rowDiv.id = `actorRow${actorRowCount}`;
    
    let actorOptionsHtml = '<option value="">Select Actor</option>';
    actorOptions.forEach(actor => {
        const selected = actor.id == selectedActorId ? 'selected' : '';
        actorOptionsHtml += `<option value="${actor.id}" ${selected}>${actor.name}</option>`;
    });
    
    rowDiv.innerHTML = `
        <div class="actor-controls">
            <div class="form-group">
                <label>Actor</label>
                <select class="form-control" name="actorIds" required>
                    ${actorOptionsHtml}
                </select>
            </div>
            <div class="form-group">
                <label>Character Name</label>
                <input type="text" class="form-control" name="characterNames" 
                       value="${characterName}" placeholder="Character name in movie" required>
            </div>
            <button type="button" class="remove-actor" onclick="removeActorRow('actorRow${actorRowCount}')">
                <i class="fa fa-times"></i>
            </button>
        </div>
    `;
    
    container.appendChild(rowDiv);
}

// Remove Actor Row
function removeActorRow(rowId) {
    const row = document.getElementById(rowId);
    if (row) {
        row.remove();
    }
}

// Save Movie
function saveMovie() {
    const form = document.getElementById('movieForm');
    const formData = new FormData(form);
    
    // Convert FormData to JSON for easier handling
    const movieData = {};
    for (let [key, value] of formData.entries()) {
        if (movieData[key]) {
            if (Array.isArray(movieData[key])) {
                movieData[key].push(value);
            } else {
                movieData[key] = [movieData[key], value];
            }
        } else {
            movieData[key] = value;
        }
    }
    
    const url = editingMovieId ? `/manager/movies/${editingMovieId}` : '/manager/movies';
    const method = editingMovieId ? 'PUT' : 'POST';
    
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(movieData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            $('#movieModal').modal('hide');
            location.reload();
        } else {
            alert('Error saving movie: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error saving movie');
    });
}

// Edit Movie
function editMovie(movieId) {
    editingMovieId = movieId;
    document.getElementById('movieModalLabel').innerHTML = '<i class="fa fa-edit"></i> Edit Movie';
    
    // Fetch movie data
    fetch(`/manager/movies/${movieId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const movie = data.movie;
                document.getElementById('movieId').value = movie.id;
                document.getElementById('movieName').value = movie.name || '';
                document.getElementById('movieDuration').value = movie.duration || '';
                document.getElementById('movieCategories').value = movie.categories || '';
                document.getElementById('movieLanguage').value = movie.language || '';
                document.getElementById('movieRated').value = movie.rated || '';
                document.getElementById('movieFormat').value = movie.format || '';
                document.getElementById('movieStatus').value = movie.isShowing || 0;
                document.getElementById('movieDirector').value = movie.director ? movie.director.id : '';
                document.getElementById('movieSmallImage').value = movie.smallImageUrl || '';
                document.getElementById('movieLargeImage').value = movie.largeImageUrl || '';
                document.getElementById('movieTrailer').value = movie.trailerUrlWatchLink || '';
                document.getElementById('movieShortDescription').value = movie.shortDescription || '';
                document.getElementById('movieLongDescription').value = movie.longDescription || '';
                
                // Clear and populate actors
                document.getElementById('actorsContainer').innerHTML = '';
                actorRowCount = 0;
                
                if (data.movieActors && data.movieActors.length > 0) {
                    data.movieActors.forEach(movieActor => {
                        addActorRow(movieActor.actor.id, movieActor.nameInMovie);
                    });
                } else {
                    addActorRow(); // Add empty row if no actors
                }
                
                $('#movieModal').modal('show');
            } else {
                alert('Error loading movie: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error loading movie data');
        });
}

// Delete Movie
function deleteMovie(movieId, movieName) {
    if (confirm(`Are you sure you want to delete "${movieName}"?`)) {
        fetch(`/manager/movies/${movieId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload();
            } else {
                alert('Error deleting movie: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error deleting movie');
        });
    }
}

// Open Add Director Modal
function openAddDirectorModal() {
    document.getElementById('directorModalLabel').innerHTML = '<i class="fa fa-user-plus"></i> Add New Director';
    document.getElementById('directorForm').reset();
    $('#directorModal').modal('show');
}

// Save Director
function saveDirector() {
    const form = document.getElementById('directorForm');
    const formData = new FormData(form);
    
    const directorData = {};
    for (let [key, value] of formData.entries()) {
        directorData[key] = value;
    }
    
    fetch('/manager/directors', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(directorData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Add new director to select
            const select = document.getElementById('movieDirector');
            const option = document.createElement('option');
            option.value = data.director.id;
            option.text = data.director.name;
            option.selected = true;
            select.appendChild(option);
            
            $('#directorModal').modal('hide');
        } else {
            alert('Error saving director: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error saving director');
    });
}

// Save Actor
function saveActor() {
    const form = document.getElementById('actorForm');
    const formData = new FormData(form);
    
    const actorData = {};
    for (let [key, value] of formData.entries()) {
        actorData[key] = value;
    }
    
    fetch('/manager/actors', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(actorData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Add new actor to the global array and refresh current actor rows
            actorOptions.push(data.actor);
            
            // Refresh all actor select elements
            const actorSelects = document.querySelectorAll('select[name="actorIds"]');
            actorSelects.forEach(select => {
                const currentValue = select.value;
                let optionsHtml = '<option value="">Select Actor</option>';
                actorOptions.forEach(actor => {
                    const selected = actor.id == currentValue ? 'selected' : '';
                    optionsHtml += `<option value="${actor.id}" ${selected}>${actor.name}</option>`;
                });
                select.innerHTML = optionsHtml;
            });
            
            $('#actorModal').modal('hide');
        } else {
            alert('Error saving actor: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error saving actor');
    });
} 