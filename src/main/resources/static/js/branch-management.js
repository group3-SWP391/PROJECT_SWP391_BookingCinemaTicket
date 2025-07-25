// Quản lý Chi nhánh (Branch Management)
let currentBranchId = null;
let currentRoomId = null;

function initializeBranchManagement() {
    // Kiểm tra xem có đang ở trang quản lý chi nhánh hay không bằng cách tìm modal chi nhánh
    const branchModal = document.getElementById('branchModal');
    if (!branchModal) {
        // Không phải trang chi nhánh, bỏ qua khởi tạo
        return;
    }

    // Các khởi tạo riêng cho quản lý chi nhánh có thể được thêm tại đây nếu cần
}

// Khởi tạo khi DOM đã tải xong
document.addEventListener('DOMContentLoaded', initializeBranchManagement);

// Các hàm quản lý chi nhánh
function editBranch(branchId) {
    fetch(`/manager/branchs/${branchId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const branch = data.Branch;
                document.getElementById('branchId').value = branch.id;
                document.getElementById('branchName').value = branch.name || '';
                document.getElementById('branchLocation').value = branch.location || '';
                document.getElementById('branchLocationDetail').value = branch.locationDetail || '';
                document.getElementById('branchPhone').value = branch.phoneNo || '';
                document.getElementById('branchDescription').value = branch.description || '';

                const imageUrlInput = document.querySelector('input[name="imgUrl"]');
                if (imageUrlInput) {
                    imageUrlInput.value = branch.imgUrl || '';
                }

                if (branch.imgUrl) {
                    showBranchCurrentFileInfo(branch.imgUrl, 'image');
                }

                document.getElementById('branchModalLabel').innerHTML = '<i class="fa fa-edit"></i> Sửa chi nhánh';
                $('#branchModal').modal('show');
            } else {
                alert('Lỗi tải chi nhánh: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Lỗi:', error);
            alert('Lỗi khi tải chi nhánh');
        });
}

function checkBranchNameDuplicate() {
    const branchName = document.getElementById('branchName').value.trim();
    const branchId = document.getElementById('branchId').value; // For edit mode
    const errorDiv = document.getElementById('branchNameError');
    
    if (!branchName) {
        errorDiv.style.display = 'none';
        return;
    }
    
    // Check for duplicate name via API
    fetch('/manager/api/branchs/check-name', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            name: branchName,
            excludeId: branchId || null
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.exists) {
            errorDiv.textContent = 'Tên chi nhánh đã tồn tại. Vui lòng chọn tên khác.';
            errorDiv.style.display = 'block';
            document.getElementById('branchName').classList.add('is-invalid');
        } else {
            errorDiv.style.display = 'none';
            document.getElementById('branchName').classList.remove('is-invalid');
        }
    })
    .catch(error => {
        console.error('Lỗi kiểm tra tên chi nhánh:', error);
    });
}

function saveBranch() {
    // Check for duplicate name error before submitting
    const errorDiv = document.getElementById('branchNameError');
    if (errorDiv.style.display !== 'none') {
        alert('Vui lòng sửa lỗi trước khi lưu.');
        return;
    }
    
    const form = document.getElementById('branchForm');
    const formData = new FormData(form);
    const branchId = document.getElementById('branchId').value;

    const url = branchId ? `/manager/branchs/${branchId}` : '/manager/branchs';
    const method = branchId ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                $('#branchModal').modal('hide');
                location.reload();
            } else {
                alert('Lỗi: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Lỗi:', error);
            alert('Lỗi khi lưu chi nhánh');
        });
}

function deleteBranch(branchId, branchName) {
    if (confirm(`Bạn có chắc chắn muốn xóa chi nhánh "${branchName}" không? Hành động này cũng sẽ xóa tất cả các phòng liên quan.`)) {
        fetch(`/manager/branchs/${branchId}`, {
            method: 'DELETE'
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.message);
                    location.reload();
                } else {
                    alert('Lỗi: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Lỗi:', error);
                alert('Lỗi khi xóa chi nhánh');
            });
    }
}

// Các hàm quản lý phòng chiếu
function manageRooms(branchId) {
    currentBranchId = branchId;

    // Lấy tên chi nhánh để hiển thị trong tiêu đề modal
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
                        <td><span class="badge badge-info">${room.roomType || 'Không có'}</span></td>
                        <td>${room.capacity} ghế</td>
                        <td>${room.rowCount && room.seatsPerRow ? `${room.rowCount}x${room.seatsPerRow}` : 'Không có'}</td>
                        <td>
                            <span class="status-badge ${room.isActive == 1 ? 'status-showing' : 'status-not-showing'}">
                                ${room.isActive == 1 ? 'Đang hoạt động' : 'Không hoạt động'}
                            </span>
                        </td>
                        <td>
                            <button class="btn btn-sm btn-primary" onclick="editRoom(${room.id})">
                                <i class="fa fa-edit"></i> Sửa
                            </button>
                            <button class="btn btn-sm btn-danger" onclick="deleteRoom(${room.id}, '${room.name}')">
                                <i class="fa fa-trash"></i> Xóa
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
    document.getElementById('roomFormTitle').innerHTML = '<i class="fa fa-plus"></i> Thêm phòng mới';
    document.getElementById('roomFormContainer').style.display = 'block';

    window.currentVipSeats = '';
    hideLayoutPreview();
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
                document.getElementById('roomDescription').value = room.description || '';
                document.getElementById('roomStatus').value = room.isActive;
                document.getElementById('roomVipSeats').value = room.vipSeats || '';

                // Set hidden input value for existing image
                const imageUrlInput = document.querySelector('input[name="imgUrl"][data-current-image="room"]');
                if (imageUrlInput) {
                    imageUrlInput.value = room.imgUrl || '';
                }
                
                // Show current image if it exists
                if (room.imgUrl) {
                    showRoomCurrentFileInfo(room.imgUrl, 'image');
                }

                document.getElementById('roomFormTitle').innerHTML = '<i class="fa fa-edit"></i> Sửa phòng';
                document.getElementById('roomFormContainer').style.display = 'block';
                currentRoomId = roomId;
            } else {
                alert('Lỗi khi tải phòng: ' + data.message);
            }
        });
}

function saveRoom() {
    const form = document.getElementById('roomForm');
    const formData = new FormData(form);
    const roomId = document.getElementById('roomId').value;

    const url = roomId ? `/manager/rooms/${roomId}` : `/manager/branchs/${currentBranchId}/rooms`;
    const method = roomId ? 'PUT' : 'POST';

    // Send FormData directly for file uploads (no JSON conversion)
    fetch(url, {
        method: method,
        body: formData  // Send FormData directly
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                cancelRoomForm();
                loadRooms(currentBranchId);
                
                // Refresh the main page to update branch statistics (room count, capacity)
                setTimeout(() => {
                    location.reload();
                }, 1000); // Small delay to allow user to see the success message
            } else {
                alert('Lỗi: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Lỗi:', error);
            alert('Lỗi khi lưu phòng');
        });
}

function deleteRoom(roomId, roomName) {
    if (confirm(`Bạn có chắc chắn muốn xóa phòng "${roomName}" không?`)) {
        fetch(`/manager/rooms/${roomId}`, {
            method: 'DELETE'
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.message);
                    loadRooms(currentBranchId);
                    
                    // Refresh the main page to update branch statistics (room count, capacity)
                    setTimeout(() => {
                        location.reload();
                    }, 1000); // Small delay to allow user to see the success message
                } else {
                    alert('Lỗi: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Lỗi:', error);
                alert('Lỗi khi xóa phòng');
            });
    }
}


function showAddRoomForm() {
    document.getElementById('roomForm').reset();
    document.getElementById('roomId').value = '';
    document.getElementById('roomBranchId').value = currentBranchId;
    document.getElementById('roomFormTitle').innerHTML = '<i class="fa fa-plus"></i> Thêm phòng mới';
    document.getElementById('roomFormContainer').style.display = 'block';
    
    // Clear VIP seats data
    window.currentVipSeats = '';
    hideLayoutPreview(); // Hide layout preview for new room
    currentRoomId = null;
}



function deleteRoom(roomId, roomName) {
    if (confirm(`Bạn có chắc muốn xóa phòng "${roomName}"?`)) {
        fetch(`/manager/rooms/${roomId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                loadRooms(currentBranchId);
            } else {
                alert('Lỗi: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Lỗi:', error);
            alert('Lỗi xóa phòng');
        });
    }
}

function cancelRoomForm() {
    document.getElementById('roomFormContainer').style.display = 'none';
    document.getElementById('roomForm').reset();
    currentRoomId = null;
}

// Generate seat layout preview
function generateSeatLayout() {
    const capacity = document.getElementById('roomCapacity').value;
    const rowCount = document.getElementById('roomRowCount').value;
    
    if (!capacity || !rowCount || capacity <= 0 || rowCount <= 0) {
        hideLayoutPreview();
        return;
    }
    
    if (parseInt(capacity) < parseInt(rowCount)) {
        hideLayoutPreview();
        return;
    }
    
    // Calculate seats per row
    const seatsPerRow = Math.floor(parseInt(capacity) / parseInt(rowCount));
    const extraSeats = parseInt(capacity) % parseInt(rowCount);
    
    // Update calculated values display
    updateCalculatedValues(seatsPerRow, extraSeats);
    
    // Generate layout preview via API
    fetch('/manager/api/rooms/preview-layout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            capacity: parseInt(capacity),
            rowCount: parseInt(rowCount)
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            displaySeatLayout(data.layout);
        } else {
            console.error('Lỗi khi tạo bố cục:', data.message);
            hideLayoutPreview();
        }
    })
    .catch(error => {
        console.error('Lỗi:', error);
        hideLayoutPreview();
    });
}

function updateCalculatedValues(seatsPerRow, extraSeats) {
    const calculatedValues = document.getElementById('calculatedValues');
    const seatsPerRowDisplay = document.getElementById('seatsPerRowDisplay');
    const extraSeatsDisplay = document.getElementById('extraSeatsDisplay');
    const extraSeatsCount = document.getElementById('extraSeatsCount');
    
    if (calculatedValues && seatsPerRowDisplay) {
        seatsPerRowDisplay.textContent = seatsPerRow;
        
        if (extraSeats > 0) {
            extraSeatsCount.textContent = extraSeats;
            extraSeatsDisplay.style.display = 'inline';
        } else {
            extraSeatsDisplay.style.display = 'none';
        }
        
        calculatedValues.style.display = 'block';
    }
}

function displaySeatLayout(layoutData) {
    const layoutPreview = document.getElementById('seatLayoutPreview');
    const layoutGrid = document.getElementById('seatLayoutGrid');
    
    if (!layoutPreview || !layoutGrid) return;
    
    // Clear existing layout
    layoutGrid.innerHTML = '';
    
    // Generate seat layout HTML
    layoutData.layout.forEach(rowData => {
        const rowDiv = document.createElement('div');
        rowDiv.className = 'seat-row';
        
        // Row label
        const rowLabel = document.createElement('div');
        rowLabel.className = 'row-label';
        rowLabel.textContent = rowData.rowId;
        rowDiv.appendChild(rowLabel);
        
        // Seats in this row
        rowData.seats.forEach(seat => {
            const seatDiv = document.createElement('div');
            seatDiv.className = `seat ${seat.type.toLowerCase()}`;
            seatDiv.textContent = seat.number;
            seatDiv.title = `Seat ${seat.id}`;
            seatDiv.setAttribute('data-seat-id', seat.id);
            seatDiv.onclick = () => toggleSeatType(seatDiv, seat.id);
            rowDiv.appendChild(seatDiv);
        });
        
        layoutGrid.appendChild(rowDiv);
    });
    
    // Show the layout preview
    layoutPreview.style.display = 'block';
}

function hideLayoutPreview() {
    const layoutPreview = document.getElementById('seatLayoutPreview');
    const calculatedValues = document.getElementById('calculatedValues');
    
    if (layoutPreview) {
        layoutPreview.style.display = 'none';
    }
    if (calculatedValues) {
        calculatedValues.style.display = 'none';
    }
}

function toggleSeatType(seatDiv, seatId) {
    if (seatDiv.classList.contains('regular')) {
        seatDiv.classList.remove('regular');
        seatDiv.classList.add('vip');
    } else {
        seatDiv.classList.remove('vip');
        seatDiv.classList.add('regular');
    }
}

// Get VIP seats as comma-separated string
function getVipSeatsString() {
    const vipSeats = [];
    const seatElements = document.querySelectorAll('.seat.vip');
    
    seatElements.forEach(seat => {
        const seatId = seat.getAttribute('data-seat-id');
        if (seatId) {
            vipSeats.push(seatId);
        }
    });
    
    return vipSeats.join(',');
}

// Apply VIP seats from saved data
function applyVipSeats(vipSeatsString) {
    if (!vipSeatsString) return;
    
    const vipSeats = vipSeatsString.split(',').map(s => s.trim()).filter(s => s);
    
    vipSeats.forEach(seatId => {
        const seatElement = document.querySelector(`[data-seat-id="${seatId}"]`);
        if (seatElement) {
            seatElement.classList.remove('regular');
            seatElement.classList.add('vip');
        }
    });
}

// Legacy function for backward compatibility
function calculateCapacity() {
    // This function is no longer needed as we auto-calculate
    generateSeatLayout();
}

// Branch file upload handling
function setupBranchFileUpload() {
    const fileInputs = [
        { id: 'branchImage', type: 'image', preview: 'branchImagePreview', area: 'branchImageUploadArea' }
    ];

    fileInputs.forEach(config => {
        const input = document.getElementById(config.id);
        const uploadArea = document.getElementById(config.area);
        const preview = document.getElementById(config.preview);

        if (!input || !uploadArea || !preview) return;
        
        input.addEventListener('change', function(e) {
            handleBranchFileSelect(e.target.files[0], config);
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
                handleBranchFileSelect(files[0], config);
                const dt = new DataTransfer();
                dt.items.add(files[0]);
                input.files = dt.files;
            }
        });
    });
}

function handleBranchFileSelect(file, config) {
    if (!file) return;
    
    if (!file.type.startsWith('image/')) {
        alert('Vui lòng chọn một tệp hình ảnh hợp lệ.');
        return;
    }
    
    const maxSize = 10 * 1024 * 1024; // 10MB
    if (file.size > maxSize) {
        alert('Kích thước tệp phải nhỏ hơn 10MB.');
        return;
    }
    
    showBranchFilePreview(file, config);
}

function showBranchFilePreview(file, config) {
    const uploadArea = document.getElementById(config.area);
    const preview = document.getElementById(config.preview);
    
    if (!uploadArea || !preview) return;

    const reader = new FileReader();
    
    reader.onload = function(e) {
        const img = preview.querySelector('.preview-image');
        img.src = e.target.result;

        const fileName = preview.querySelector('.file-name');
        const fileSize = preview.querySelector('.file-size');
        
        if (fileName) fileName.textContent = file.name;
        if (fileSize) fileSize.textContent = formatFileSize(file.size);
        
        uploadArea.style.display = 'none';
        preview.style.display = 'block';
    };

    reader.readAsDataURL(file);
}

function removeBranchFilePreview(type) {
    const configs = {
        'image': { id: 'branchImage', preview: 'branchImagePreview', area: 'branchImageUploadArea' }
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
        if (img) img.src = '';
    }
}

function showBranchCurrentFileInfo(fileUrl, type) {
    if (!fileUrl) return;

    const configs = {
        'image': { preview: 'branchImagePreview', area: 'branchImageUploadArea' }
    };

    const config = configs[type];
    if (!config) return;

    const uploadArea = document.getElementById(config.area);
    const preview = document.getElementById(config.preview);

    if (!uploadArea || !preview) return;

    const img = preview.querySelector('.preview-image');
    if (img) {
        img.src = fileUrl;
    }
    
    const fileName = preview.querySelector('.file-name');
    if (fileName) {
        fileName.textContent = fileUrl.split('/').pop() || 'Tệp hiện tại';
    }

    uploadArea.style.display = 'none';
    preview.style.display = 'block';
}

function formatFileSize(bytes) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

// Room file upload handling
function setupRoomFileUpload() {
    const fileInputs = [
        { id: 'roomImage', type: 'image', preview: 'roomImagePreview', area: 'roomImageUploadArea' }
    ];

    fileInputs.forEach(config => {
        const input = document.getElementById(config.id);
        const uploadArea = document.getElementById(config.area);
        const preview = document.getElementById(config.preview);

        if (!input || !uploadArea || !preview) return;
        
        input.addEventListener('change', function(e) {
            handleRoomFileSelect(e.target.files[0], config);
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
                handleRoomFileSelect(files[0], config);
                const dt = new DataTransfer();
                dt.items.add(files[0]);
                input.files = dt.files;
            }
        });
    });
}

function handleRoomFileSelect(file, config) {
    if (!file) return;
    
    if (!file.type.startsWith('image/')) {
        alert('Vui lòng chọn một tệp hình ảnh hợp lệ.');
        return;
    }
    
    const maxSize = 10 * 1024 * 1024; // 10MB
    if (file.size > maxSize) {
        alert('Kích thước tệp phải nhỏ hơn 10MB.');
        return;
    }
    
    showRoomFilePreview(file, config);
}

function showRoomFilePreview(file, config) {
    const uploadArea = document.getElementById(config.area);
    const preview = document.getElementById(config.preview);
    
    if (!uploadArea || !preview) return;

    const reader = new FileReader();
    
    reader.onload = function(e) {
        const img = preview.querySelector('.preview-image');
        img.src = e.target.result;

        const fileName = preview.querySelector('.file-name');
        const fileSize = preview.querySelector('.file-size');
        
        if (fileName) fileName.textContent = file.name;
        if (fileSize) fileSize.textContent = formatFileSize(file.size);
        
        uploadArea.style.display = 'none';
        preview.style.display = 'block';
    };

    reader.readAsDataURL(file);
}

function removeRoomFilePreview(type) {
    const configs = {
        'image': { id: 'roomImage', preview: 'roomImagePreview', area: 'roomImageUploadArea' }
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
        if (img) img.src = '';
    }
}

function showRoomCurrentFileInfo(fileUrl, type) {
    if (!fileUrl) return;

    const configs = {
        'image': { preview: 'roomImagePreview', area: 'roomImageUploadArea' }
    };

    const config = configs[type];
    if (!config) return;

    const uploadArea = document.getElementById(config.area);
    const preview = document.getElementById(config.preview);

    if (!uploadArea || !preview) return;

    const img = preview.querySelector('.preview-image');
    if (img) {
        img.src = fileUrl;
    }
    
    const fileName = preview.querySelector('.file-name');
    if (fileName) {
        fileName.textContent = fileUrl.split('/').pop() || 'Tệp hiện tại';
    }

    uploadArea.style.display = 'none';
    preview.style.display = 'block';
}

// Document ready and modal event handlers
$(document).ready(function() {
    // Setup file upload functionality
    setupBranchFileUpload();
    setupRoomFileUpload();
    
    // Reset form when branch modal is closed
    $('#branchModal').on('hidden.bs.modal', function () {
        document.getElementById('branchForm').reset();
        document.getElementById('branchId').value = '';
        document.getElementById('branchModalLabel').innerHTML = '<i class="fa fa-building"></i> Thêm chi nhánh mới';
        
        // Reset file preview
        removeBranchFilePreview('image');
        
        // Clear validation error
        const errorDiv = document.getElementById('branchNameError');
        if (errorDiv) {
            errorDiv.style.display = 'none';
            document.getElementById('branchName').classList.remove('is-invalid');
        }
    });

    // Reset room form when room modal is closed
    $('#roomModal').on('hidden.bs.modal', function () {
        cancelRoomForm();
        currentBranchId = null;
        
        // Reset room file preview
        removeRoomFilePreview('image');
    });
}); 