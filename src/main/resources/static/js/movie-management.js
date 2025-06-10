// Movie Management JavaScript
let editingMovieId = null;
let actorRowCount = 0;
let actorOptions = [];

function initializeMovieManagement() {
    const actorOptionsElement = document.getElementById('actorOptionsData');
    if (actorOptionsElement) {
        try {
            const rawText = actorOptionsElement.textContent || actorOptionsElement.innerHTML;
            actorOptions = JSON.parse(rawText);
        } catch (e) {
            console.error('Error parsing actor options:', e);
            console.error('Raw content was:', actorOptionsElement.textContent);
            actorOptions = [];
        }
    } else {
        console.warn('Actor options element not found');
        actorOptions = [];
    }
    if (!actorOptions || actorOptions.length === 0) {
        fetchActorsFromAPI();
    }
    const addMovieBtn = document.getElementById('addMovieBtn');
    if (addMovieBtn) {
        addMovieBtn.addEventListener('click', openAddMovieModal);
    } else {
        console.warn('Add Movie button not found');
    }

    window.editMovie = editMovie;
    window.deleteMovie = deleteMovie;
    window.openAddMovieModal = openAddMovieModal;
    window.addActorRow = addActorRow;
    window.removeActorRow = removeActorRow;
    window.saveMovie = saveMovie;
    window.openAddDirectorModal = openAddDirectorModal;
    window.saveDirector = saveDirector;
    window.saveActor = saveActor;
    window.removeFilePreview = removeFilePreview;
    setupFileUpload();
}
document.addEventListener('DOMContentLoaded', initializeMovieManagement);
window.addEventListener('load', function() {
    if (document.getElementById('addMovieBtn') && !document.getElementById('addMovieBtn').hasEventListener) {
        initializeMovieManagement();
        document.getElementById('addMovieBtn').hasEventListener = true;
    }
});
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
        setTimeout(waitForElementsAndInitialize, 500);
    } else {
        console.warn('Movie management elements not found after maximum retries');
    }
}

setTimeout(waitForElementsAndInitialize, 100);
function fetchActorsFromAPI() {
    fetch('/manager/actors')
        .then(response => response.json())
        .then(actors => {
            actorOptions = actors;
        })
        .catch(error => {
            console.error('Error fetching actors from API:', error);
        });
}
function openAddMovieModal() {
    editingMovieId = null;
    
    const modalLabel = document.getElementById('movieModalLabel');
    const movieForm = document.getElementById('movieForm');
    const movieId = document.getElementById('movieId');
    const actorsContainer = document.getElementById('actorsContainer');
    
    if (!modalLabel || !movieForm || !movieId || !actorsContainer) {
        alert('Error: Modal components are not ready. Please refresh the page.');
        return;
    }
    
    modalLabel.innerHTML = '<i class="fa fa-film"></i> Add New Movie';
    movieForm.reset();
    movieId.value = '';
    actorsContainer.innerHTML = '';
    actorRowCount = 0;
    
    // Reset file upload areas
    resetFileUploadAreas();
    
    addActorRow();
}

function addActorRow(selectedActorId = '', characterName = '') {
    actorRowCount++;
    const container = document.getElementById('actorsContainer');
    const rowDiv = document.createElement('div');
    rowDiv.className = 'actor-row';
    rowDiv.id = `actorRow${actorRowCount}`;
    let actorOptionsHtml = '<option value="">Select Actor</option>';
    if (actorOptions && actorOptions.length > 0) {
        actorOptions.forEach(actor => {
            const selected = actor.id == selectedActorId ? 'selected' : '';
            actorOptionsHtml += `<option value="${actor.id}" ${selected}>${actor.name}</option>`;
        });
    } else {
        actorOptionsHtml += '<option value="" disabled>No actors available - please create some first</option>';
    }
    
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

function removeActorRow(rowId) {
    const row = document.getElementById(rowId);
    if (row) {
        row.remove();
    }
}

function saveMovie() {
    const form = document.getElementById('movieForm');
    const formData = new FormData(form);
    const actorIds = formData.getAll('actorIds');
    const characterNames = formData.getAll('characterNames');
    
    const actors = [];
    for (let i = 0; i < actorIds.length; i++) {
        if (actorIds[i] && characterNames[i]) {
            actors.push({
                actorId: parseInt(actorIds[i]),
                characterName: characterNames[i]
            });
        }
    }
    
    formData.delete('actorIds');
    formData.delete('characterNames');
    formData.append('actors', JSON.stringify(actors));
    if (editingMovieId) {
        formData.append('_method', 'PUT');
        formData.append('movieId', editingMovieId);
    }
    
    const url = '/manager/movies';
    
    fetch(url, {
        method: 'POST',
        body: formData
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
function editMovie(movieId) {
    editingMovieId = movieId;
    document.getElementById('movieModalLabel').innerHTML = '<i class="fa fa-edit"></i> Edit Movie';
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
                const formatDropdown = document.getElementById('movieFormat');
                formatDropdown.value = (movie.format || '').trim();
                document.getElementById('movieStatus').value = movie.isShowing || 0;
                document.getElementById('movieDirector').value = movie.director ? movie.director.id : '';
                resetFileUploadAreas();
                if (movie.smallImageUrl) {
                    showCurrentFileInfo(movie.smallImageUrl, 'smallImage');
                }
                if (movie.largeImageUrl) {
                    showCurrentFileInfo(movie.largeImageUrl, 'largeImage');
                }
                if (movie.trailerUrlWatchLink) {
                    showCurrentFileInfo(movie.trailerUrlWatchLink, 'trailer');
                }
                
                document.getElementById('movieShortDescription').value = movie.shortDescription || '';
                document.getElementById('movieLongDescription').value = movie.longDescription || '';
                document.getElementById('actorsContainer').innerHTML = '';
                actorRowCount = 0;
                
                if (data.movieActors && data.movieActors.length > 0) {
                    data.movieActors.forEach(movieActor => {
                        addActorRow(movieActor.actor.id, movieActor.nameInMovie);
                    });
                } else {
                    addActorRow();
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

function openAddDirectorModal() {
    document.getElementById('directorModalLabel').innerHTML = '<i class="fa fa-user-plus"></i> Add New Director';
    document.getElementById('directorForm').reset();
    $('#directorModal').modal('show');
}
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
            actorOptions.push(data.actor);
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

function setupFileUpload() {
    const fileInputs = [
        { id: 'movieSmallImage', type: 'image', preview: 'smallImagePreview', area: 'smallImageUploadArea' },
        { id: 'movieLargeImage', type: 'image', preview: 'largeImagePreview', area: 'largeImageUploadArea' },
        { id: 'movieTrailer', type: 'video', preview: 'trailerPreview', area: 'trailerUploadArea' }
    ];

    fileInputs.forEach(config => {
        const input = document.getElementById(config.id);
        const uploadArea = document.getElementById(config.area);
        const preview = document.getElementById(config.preview);

        if (!input || !uploadArea || !preview) return;
        input.addEventListener('change', function(e) {
            handleFileSelect(e.target.files[0], config);
        });
        uploadArea.addEventListener('dragover', function(e) {
            e.preventDefault();
            e.stopPropagation();
            uploadArea.classList.add('dragover');
        });
        uploadArea.addEventListener('dragleave', function(e) {
            e.preventDefault();
            e.stopPropagation();
            uploadArea.classList.remove('dragover');
        });

        uploadArea.addEventListener('drop', function(e) {
            e.preventDefault();
            e.stopPropagation();
            uploadArea.classList.remove('dragover');
            
            const files = e.dataTransfer.files;
            if (files.length > 0) {
                handleFileSelect(files[0], config);
                const dt = new DataTransfer();
                dt.items.add(files[0]);
                input.files = dt.files;
            }
        });
    });
}

function handleFileSelect(file, config) {
    if (!file) return;
    const isValidType = config.type === 'image' ?
        file.type.startsWith('image/') : 
        file.type.startsWith('video/');

    if (!isValidType) {
        alert(`Please select a valid ${config.type} file.`);
        return;
    }
    const maxSize = config.type === 'image' ? 10 * 1024 * 1024 : 50 * 1024 * 1024;
    if (file.size > maxSize) {
        alert(`File size must be less than ${config.type === 'image' ? '10MB' : '50MB'}.`);
        return;
    }
    showFilePreview(file, config);
}

function showFilePreview(file, config) {
    const uploadArea = document.getElementById(config.area);
    const preview = document.getElementById(config.preview);
    
    if (!uploadArea || !preview) return;

    const reader = new FileReader();
    
    reader.onload = function(e) {
        if (config.type === 'image') {
            const img = preview.querySelector('.preview-image');
            img.src = e.target.result;
        } else if (config.type === 'video') {
            const video = preview.querySelector('.preview-video');
            const source = video.querySelector('source');
            source.src = e.target.result;
            video.load();
        }

        const fileName = preview.querySelector('.file-name');
        const fileSize = preview.querySelector('.file-size');
        
        if (fileName) fileName.textContent = file.name;
        if (fileSize) fileSize.textContent = formatFileSize(file.size);
        uploadArea.style.display = 'none';
        preview.style.display = 'block';
    };

    reader.readAsDataURL(file);
}

function removeFilePreview(type) {
    const configs = {
        'smallImage': { id: 'movieSmallImage', preview: 'smallImagePreview', area: 'smallImageUploadArea' },
        'largeImage': { id: 'movieLargeImage', preview: 'largeImagePreview', area: 'largeImageUploadArea' },
        'trailer': { id: 'movieTrailer', preview: 'trailerPreview', area: 'trailerUploadArea' }
    };

    const config = configs[type];
    if (!config) return;

    const input = document.getElementById(config.id);
    const uploadArea = document.getElementById(config.area);
    const preview = document.getElementById(config.preview);

    if (input) input.value = '';
    if (uploadArea) uploadArea.style.display = 'block';
    if (preview) {
        preview.style.display = 'none';
        
        // Clear preview content
        const img = preview.querySelector('.preview-image');
        const video = preview.querySelector('.preview-video');
        const source = preview.querySelector('source');
        
        if (img) img.src = '';
        if (video && source) {
            source.src = '';
            video.load();
        }
    }
}

function formatFileSize(bytes) {
    if (bytes === 0) return '0 Bytes';
    
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    
    return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
}

function showCurrentFileInfo(fileUrl, type) {
    if (!fileUrl) return;

    const configs = {
        'smallImage': { preview: 'smallImagePreview', area: 'smallImageUploadArea' },
        'largeImage': { preview: 'largeImagePreview', area: 'largeImageUploadArea' },
        'trailer': { preview: 'trailerPreview', area: 'trailerUploadArea' }
    };

    const config = configs[type];
    if (!config) return;

    const uploadArea = document.getElementById(config.area);
    const preview = document.getElementById(config.preview);

    if (!uploadArea || !preview) return;

    if (type === 'trailer') {
        const video = preview.querySelector('.preview-video');
        const source = video.querySelector('source');
        if (video && source) {
            source.src = fileUrl;
            video.load();
        }
    } else {
        const img = preview.querySelector('.preview-image');
        if (img) {
            img.src = fileUrl;
        }
    }
    const fileName = preview.querySelector('.file-name');
    if (fileName) {
        fileName.textContent = fileUrl.split('/').pop() || 'Current file';
    }

    uploadArea.style.display = 'none';
    preview.style.display = 'block';
}

function resetFileUploadAreas() {
    const types = ['smallImage', 'largeImage', 'trailer'];
    
    types.forEach(type => {
        removeFilePreview(type);
    });
} 