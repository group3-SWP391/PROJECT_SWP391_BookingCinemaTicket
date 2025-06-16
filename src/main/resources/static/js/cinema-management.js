// Cinema Management JavaScript
let currentCinemaId = null;
let currentRoomId = null;

// Cinema Management Functions
function editCinema(cinemaId) {
    fetch(`/manager/cinemas/${cinemaId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const cinema = data.cinema;
                document.getElementById('cinemaId').value = cinema.id;
                document.getElementById('cinemaName').value = cinema.name || '';
                document.getElementById('cinemaAddress').value = cinema.address || '';
                document.getElementById('cinemaPhone').value = cinema.phone || '';
                document.getElementById('cinemaEmail').value = cinema.email || '';
                document.getElementById('cinemaDescription').value = cinema.description || '';
                document.getElementById('cinemaStatus').value = cinema.isActive;
                
                document.getElementById('cinemaModalLabel').innerHTML = '<i class="fa fa-edit"></i> Edit Cinema';
                $('#cinemaModal').modal('show');
            } else {
                alert('Error loading cinema: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error loading cinema');
        });
}

function saveCinema() {
    const form = document.getElementById('cinemaForm');
    const formData = new FormData(form);
    const cinemaId = document.getElementById('cinemaId').value;
    
    const url = cinemaId ? `/manager/cinemas/${cinemaId}` : '/manager/cinemas';
    const method = cinemaId ? 'PUT' : 'POST';
    
    const data = {};
    formData.forEach((value, key) => {
        data[key] = value;
    });

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(data.message);
            $('#cinemaModal').modal('hide');
            location.reload();
        } else {
            alert('Error: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error saving cinema');
    });
}

function deleteCinema(cinemaId, cinemaName) {
    if (confirm(`Are you sure you want to delete cinema "${cinemaName}"? This will also delete all associated rooms.`)) {
        fetch(`/manager/cinemas/${cinemaId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                location.reload();
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error deleting cinema');
        });
    }
}

// Room Management Functions
function manageRooms(cinemaId) {
    currentCinemaId = cinemaId;
    
    // Get cinema name for modal title
    fetch(`/manager/cinemas/${cinemaId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('roomModalCinemaName').textContent = data.cinema.name;
                document.getElementById('roomCinemaId').value = cinemaId;
                loadRooms(cinemaId);
                $('#roomModal').modal('show');
            }
        });
}

function viewRooms(cinemaId) {
    manageRooms(cinemaId);
}

function loadRooms(cinemaId) {
    fetch(`/manager/cinemas/${cinemaId}/rooms`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const tbody = document.getElementById('roomsTableBody');
                tbody.innerHTML = '';
                
                data.rooms.forEach(room => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td><strong>${room.name}</strong></td>
                        <td><span class="badge badge-info">${room.roomType || 'N/A'}</span></td>
                        <td>${room.capacity} seats</td>
                        <td>${room.rowCount && room.seatsPerRow ? `${room.rowCount}x${room.seatsPerRow}` : 'N/A'}</td>
                        <td>
                            <span class="status-badge ${room.isActive == 1 ? 'status-showing' : 'status-not-showing'}">
                                ${room.isActive == 1 ? 'Active' : 'Inactive'}
                            </span>
                        </td>
                        <td>
                            <button class="btn btn-sm btn-primary" onclick="editRoom(${room.id})">
                                <i class="fa fa-edit"></i> Edit
                            </button>
                            <button class="btn btn-sm btn-danger" onclick="deleteRoom(${room.id}, '${room.name}')">
                                <i class="fa fa-trash"></i> Delete
                            </button>
                        </td>
                    `;
                    tbody.appendChild(row);
                });
            }
        });
}

function showAddRoomForm() {
    document.getElementById('roomForm').reset();
    document.getElementById('roomId').value = '';
    document.getElementById('roomCinemaId').value = currentCinemaId;
    document.getElementById('roomFormTitle').innerHTML = '<i class="fa fa-plus"></i> Add New Room';
    document.getElementById('roomFormContainer').style.display = 'block';
    currentRoomId = null;
}

function editRoom(roomId) {
    fetch(`/manager/rooms/${roomId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const room = data.room;
                document.getElementById('roomId').value = room.id;
                document.getElementById('roomName').value = room.name || '';
                document.getElementById('roomType').value = room.roomType || '';
                document.getElementById('roomCapacity').value = room.capacity || '';
                document.getElementById('roomRowCount').value = room.rowCount || '';
                document.getElementById('roomSeatsPerRow').value = room.seatsPerRow || '';
                document.getElementById('roomDescription').value = room.description || '';
                document.getElementById('roomStatus').value = room.isActive;
                
                document.getElementById('roomFormTitle').innerHTML = '<i class="fa fa-edit"></i> Edit Room';
                document.getElementById('roomFormContainer').style.display = 'block';
                currentRoomId = roomId;
            } else {
                alert('Error loading room: ' + data.message);
            }
        });
}

function saveRoom() {
    const form = document.getElementById('roomForm');
    const formData = new FormData(form);
    const roomId = document.getElementById('roomId').value;
    
    const url = roomId ? `/manager/rooms/${roomId}` : `/manager/cinemas/${currentCinemaId}/rooms`;
    const method = roomId ? 'PUT' : 'POST';
    
    const data = {};
    formData.forEach((value, key) => {
        data[key] = value;
    });

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(data.message);
            cancelRoomForm();
            loadRooms(currentCinemaId);
        } else {
            alert('Error: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error saving room');
    });
}

function deleteRoom(roomId, roomName) {
    if (confirm(`Are you sure you want to delete room "${roomName}"?`)) {
        fetch(`/manager/rooms/${roomId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                loadRooms(currentCinemaId);
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error deleting room');
        });
    }
}

function cancelRoomForm() {
    document.getElementById('roomFormContainer').style.display = 'none';
    document.getElementById('roomForm').reset();
    currentRoomId = null;
}

function calculateCapacity() {
    const rowCount = parseInt(document.getElementById('roomRowCount').value) || 0;
    const seatsPerRow = parseInt(document.getElementById('roomSeatsPerRow').value) || 0;
    
    if (rowCount > 0 && seatsPerRow > 0) {
        document.getElementById('roomCapacity').value = rowCount * seatsPerRow;
    }
}

// Document ready and modal event handlers
$(document).ready(function() {
    // Reset form when cinema modal is closed
    $('#cinemaModal').on('hidden.bs.modal', function () {
        document.getElementById('cinemaForm').reset();
        document.getElementById('cinemaId').value = '';
        document.getElementById('cinemaModalLabel').innerHTML = '<i class="fa fa-building"></i> Add New Cinema';
    });

    // Reset room form when room modal is closed
    $('#roomModal').on('hidden.bs.modal', function () {
        cancelRoomForm();
        currentCinemaId = null;
    });
}); 