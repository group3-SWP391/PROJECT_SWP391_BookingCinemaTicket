// Branch Management JavaScript
let currentBranchId = null;
let currentRoomId = null;

// Branch Management Functions
function editBranch(branchId) {
    fetch(`/manager/branchs/${branchId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const branch = data.Branch;
                document.getElementById('branchId').value = branch.id;
                document.getElementById('branchName').value = branch.name || '';
                document.getElementById('branchLocation').value = branch.location || '';
                document.getElementById('branchPhone').value = branch.phoneNo || '';
                document.getElementById('branchDescription').value = branch.description || '';
                document.getElementById('branchModalLabel').innerHTML = '<i class="fa fa-edit"></i> Edit Branch';
                $('#branchModal').modal('show');
            } else {
                alert('Error loading branch: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error loading branch');
        });
}

function saveBranch() {
    const form = document.getElementById('branchForm');
    const formData = new FormData(form);
    const branchId = document.getElementById('branchId').value;
    
    const url = branchId ? `/manager/branchs/${branchId}` : '/manager/branchs';
    const method = branchId ? 'PUT' : 'POST';
    
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
            $('#branchModal').modal('hide');
            location.reload();
        } else {
            alert('Error: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error saving branch');
    });
}

function deleteBranch(branchId, branchName) {
    if (confirm(`Are you sure you want to delete branch "${branchName}"? This will also delete all associated rooms.`)) {
        fetch(`/manager/branchs/${branchId}`, {
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
            alert('Error deleting branch');
        });
    }
}

// Room Management Functions
function manageRooms(branchId) {
    currentBranchId = branchId;
    
    // Get branch name for modal title
    fetch(`/manager/branchs/${branchId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('roomModalBranchName').textContent = data.Branch?.name;
                document.getElementById('roomBranchId').value = branchId;
                loadRooms(branchId);
                $('#roomModal').modal('show');
            }
        });
}

function viewRooms(branchId) {
    manageRooms(branchId);
}

function loadRooms(branchId) {
    fetch(`/manager/branchs/${branchId}/rooms`)
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
    document.getElementById('roomBranchId').value = currentBranchId;
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
    
    const url = roomId ? `/manager/rooms/${roomId}` : `/manager/branchs/${currentBranchId}/rooms`;
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
            loadRooms(currentBranchId);
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
                loadRooms(currentBranchId);
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
    // Reset form when branch modal is closed
    $('#branchModal').on('hidden.bs.modal', function () {
        document.getElementById('branchForm').reset();
        document.getElementById('branchId').value = '';
        document.getElementById('branchModalLabel').innerHTML = '<i class="fa fa-building"></i> Add New Branch';
    });

    // Reset room form when room modal is closed
    $('#roomModal').on('hidden.bs.modal', function () {
        cancelRoomForm();
        currentBranchId = null;
    });
}); 