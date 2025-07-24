// Schedule Management JavaScript
let editingScheduleId = null;

function initializeScheduleManagement() {
    // Check if we're on the schedules page by looking for the schedule modal
    const scheduleModal = document.getElementById('scheduleModal');
    if (!scheduleModal) {
        // Not on the schedules page, skip initialization
        return;
    }
    
    const addScheduleBtn = document.getElementById('addScheduleBtn');
    if (addScheduleBtn) {
        // Remove any existing event listeners
        addScheduleBtn.removeEventListener('click', openAddScheduleModal);
        
        addScheduleBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log('Add Schedule button clicked');
            
            // Small delay to ensure all elements are ready
            setTimeout(() => {
                openAddScheduleModal();
            }, 50);
        });
        
        console.log('Schedule management initialized successfully');
    } else {
        console.error('Add Schedule button not found during initialization');
    }

    // Initialize form validation
    setupFormValidation();
}

function openAddScheduleModal() {
    editingScheduleId = null;
    
    // Reset modal content
    const modalLabel = document.getElementById('scheduleModalLabel');
    const scheduleForm = document.getElementById('scheduleForm');
    const scheduleId = document.getElementById('scheduleId');
    const scheduleRoom = document.getElementById('scheduleRoom');
    
    if (!modalLabel || !scheduleForm || !scheduleId || !scheduleRoom) {
        console.error('Modal elements not found');
        return;
    }
    
    modalLabel.innerHTML = '<i class="fa fa-calendar"></i> Add New Schedule';
    scheduleForm.reset();
    scheduleId.value = '';
    scheduleRoom.innerHTML = '<option value="">Select Room (Choose branch first)</option>';
    
    // Set minimum date to today
    const today = new Date().toISOString().split('T')[0];
    const dateInput = document.getElementById('scheduleDate');
    if (dateInput) {
        dateInput.setAttribute('min', today);
    }
    
    // Clear any validation errors
    const inputs = scheduleForm.querySelectorAll('.is-invalid');
    inputs.forEach(input => input.classList.remove('is-invalid'));
    
    // Show modal using Bootstrap
    console.log('Opening schedule modal...');
    
    // Try multiple approaches to open the modal
    const modalElement = document.getElementById('scheduleModal');
    
    // Method 1: Using jQuery (if available)
    if (typeof $ !== 'undefined' && $.fn.modal) {
        console.log('Using jQuery modal');
        $('#scheduleModal').modal('show');
        
        // Add event listeners to debug modal events
        $('#scheduleModal').off('shown.bs.modal hidden.bs.modal');
        $('#scheduleModal').on('shown.bs.modal', function() {
            console.log('Schedule modal opened successfully');
        });
        $('#scheduleModal').on('hidden.bs.modal', function() {
            console.log('Schedule modal was closed');
        });
    } 
    // Method 2: Using Bootstrap 4+ API (if available)
    else if (typeof bootstrap !== 'undefined' && bootstrap.Modal) {
        console.log('Using Bootstrap 4+ modal API');
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
    }
    // Method 3: Fallback - manually show modal
    else {
        console.log('Using manual modal display');
        modalElement.style.display = 'block';
        modalElement.classList.add('show');
        modalElement.setAttribute('aria-hidden', 'false');
        
        // Add backdrop
        const backdrop = document.createElement('div');
        backdrop.className = 'modal-backdrop fade show';
        backdrop.id = 'scheduleModalBackdrop';
        document.body.appendChild(backdrop);
        
        // Prevent body scroll
        document.body.classList.add('modal-open');
    }
}

function loadRoomsByBranch() {
    const branchSelect = document.getElementById('scheduleBranch');
    const roomSelect = document.getElementById('scheduleRoom');
    
    if (!branchSelect || !roomSelect) {
        console.error('Branch or room select elements not found');
        return;
    }
    
    const branchId = branchSelect.value;
    
    if (!branchId) {
        roomSelect.innerHTML = '<option value="">Select Room (Choose branch first)</option>';
        return;
    }

    // Show loading state
    roomSelect.innerHTML = '<option value="">Loading rooms...</option>';
    roomSelect.disabled = true;

    fetch(`/manager/api/rooms/branch/${branchId}`)
        .then(response => response.json())
        .then(rooms => {
            roomSelect.innerHTML = '<option value="">Select Room</option>';
            rooms.forEach(room => {
                let roomInfo = `${room.name} (${room.capacity} seats)`;
                if (room.roomType) {
                    roomInfo += ` - ${room.roomType}`;
                }
                roomSelect.innerHTML += `<option value="${room.id}">${roomInfo}</option>`;
            });
            roomSelect.disabled = false;
        })
        .catch(error => {
            console.error('Error loading rooms:', error);
            roomSelect.innerHTML = '<option value="">Error loading rooms</option>';
            roomSelect.disabled = false;
            alert('Error loading rooms. Please try again.');
        });
}

function saveSchedule() {
    if (!validateScheduleForm()) {
        return;
    }

    const form = document.getElementById('scheduleForm');
    const formData = new FormData(form);
    
    const scheduleData = {};
    for (let [key, value] of formData.entries()) {
        scheduleData[key] = value;
    }

    // Show loading state
    const saveBtn = document.querySelector('#scheduleModal .btn-save-movie');
    const originalText = saveBtn.textContent;
    saveBtn.textContent = 'Saving...';
    saveBtn.disabled = true;

    const url = editingScheduleId ? `/manager/schedules/${editingScheduleId}` : '/manager/schedules';
    const method = editingScheduleId ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(scheduleData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            $('#scheduleModal').modal('hide');
            showSuccessMessage(data.message);
            setTimeout(() => location.reload(), 1000);
        } else {
            alert('Error saving schedule: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error saving schedule. Please try again.');
    })
    .finally(() => {
        saveBtn.textContent = originalText;
        saveBtn.disabled = false;
    });
}

function editSchedule(scheduleId) {
    editingScheduleId = scheduleId;
    document.getElementById('scheduleModalLabel').innerHTML = '<i class="fa fa-edit"></i> Edit Schedule';
    
    fetch(`/manager/schedules/${scheduleId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const schedule = data.schedule;
                document.getElementById('scheduleId').value = schedule.id;
                document.getElementById('scheduleBranch').value = schedule.branch.id;
                document.getElementById('scheduleMovie').value = schedule.movie.id;
                document.getElementById('scheduleDate').value = schedule.startDate;
                document.getElementById('scheduleStartTime').value = schedule.startTime;
                document.getElementById('scheduleEndTime').value = schedule.endTime;
                document.getElementById('schedulePrice').value = schedule.price;

                // Load rooms for the selected branch, then select the room
                loadRoomsByBranch();
                setTimeout(() => {
                    document.getElementById('scheduleRoom').value = schedule.room.id;
                }, 500);

                $('#scheduleModal').modal('show');
            } else {
                alert('Error loading schedule data: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error loading schedule data. Please try again.');
        });
}

function deleteSchedule(scheduleId, scheduleName) {
    if (confirm(`Are you sure you want to delete "${scheduleName}"?\n\nThis action cannot be undone.`)) {
        fetch(`/manager/schedules/${scheduleId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showSuccessMessage(data.message);
                setTimeout(() => location.reload(), 1000);
            } else {
                alert('Error deleting schedule: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error deleting schedule. Please try again.');
        });
    }
}

function validateScheduleForm() {
    const requiredFields = [
        { id: 'scheduleBranch', name: 'Branch' },
        { id: 'scheduleMovie', name: 'Movie' },
        { id: 'scheduleRoom', name: 'Room' },
        { id: 'scheduleDate', name: 'Date' },
        { id: 'scheduleStartTime', name: 'Start Time' },
        { id: 'scheduleEndTime', name: 'End Time' },
        { id: 'schedulePrice', name: 'Price' }
    ];
    
    let isValid = true;
    const errors = [];

    // Check required fields
    requiredFields.forEach(field => {
        const element = document.getElementById(field.id);
        if (!element.value.trim()) {
            isValid = false;
            errors.push(`${field.name} is required`);
            element.classList.add('is-invalid');
        } else {
            element.classList.remove('is-invalid');
        }
    });

    // Validate date (not in the past)
    const selectedDate = new Date(document.getElementById('scheduleDate').value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    if (selectedDate < today) {
        isValid = false;
        errors.push('Date cannot be in the past');
        document.getElementById('scheduleDate').classList.add('is-invalid');
    }

    // Validate time range
    const startTime = document.getElementById('scheduleStartTime').value;
    const endTime = document.getElementById('scheduleEndTime').value;
    
    if (startTime && endTime) {
        const start = new Date(`2000-01-01T${startTime}:00`);
        const end = new Date(`2000-01-01T${endTime}:00`);
        
        if (end <= start) {
            isValid = false;
            errors.push('End time must be after start time');
            document.getElementById('scheduleEndTime').classList.add('is-invalid');
        } else {
            // Check if duration is reasonable (at least 30 minutes, max 8 hours)
            const durationMs = end - start;
            const durationMinutes = durationMs / (1000 * 60);
            
            if (durationMinutes < 30) {
                isValid = false;
                errors.push('Schedule duration must be at least 30 minutes');
                document.getElementById('scheduleEndTime').classList.add('is-invalid');
            } else if (durationMinutes > 480) { // 8 hours
                isValid = false;
                errors.push('Schedule duration cannot exceed 8 hours');
                document.getElementById('scheduleEndTime').classList.add('is-invalid');
            }
        }
    }

    // Validate price
    const price = parseFloat(document.getElementById('schedulePrice').value);
    if (isNaN(price) || price <= 0) {
        isValid = false;
        errors.push('Price must be a valid number greater than 0');
        document.getElementById('schedulePrice').classList.add('is-invalid');
    } else if (price > 100000) {
        isValid = false;
        errors.push('Price cannot exceed 100000 VND');
        document.getElementById('schedulePrice').classList.add('is-invalid');
    }

    if (!isValid) {
        alert('Please fix the following errors:\n\n' + errors.join('\n'));
    }

    return isValid;
}

function setupFormValidation() {
    // Real-time validation
    const priceInput = document.getElementById('schedulePrice');
    if (priceInput) {
        priceInput.addEventListener('input', function() {
            const value = parseFloat(this.value);
            if (isNaN(value) || value <= 0) {
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    }

    const startTimeInput = document.getElementById('scheduleStartTime');
    const endTimeInput = document.getElementById('scheduleEndTime');
    
    if (startTimeInput && endTimeInput) {
        function validateTimeRange() {
            const startTime = startTimeInput.value;
            const endTime = endTimeInput.value;
            
            if (startTime && endTime) {
                const start = new Date(`2000-01-01T${startTime}:00`);
                const end = new Date(`2000-01-01T${endTime}:00`);
                
                if (end <= start) {
                    endTimeInput.classList.add('is-invalid');
                } else {
                    endTimeInput.classList.remove('is-invalid');
                }
            }
        }
        
        startTimeInput.addEventListener('change', validateTimeRange);
        endTimeInput.addEventListener('change', validateTimeRange);
    }
}

function showSuccessMessage(message) {
    // You can implement a nicer notification system here
    const notification = document.createElement('div');
    notification.className = 'alert alert-success';
    notification.style.position = 'fixed';
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.zIndex = '9999';
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 3000);
}

// Close modal manually (fallback for when Bootstrap is not available)
function closeScheduleModal() {
    const modalElement = document.getElementById('scheduleModal');
    const backdrop = document.getElementById('scheduleModalBackdrop');
    
    if (modalElement) {
        modalElement.style.display = 'none';
        modalElement.classList.remove('show');
        modalElement.setAttribute('aria-hidden', 'true');
    }
    
    if (backdrop) {
        backdrop.remove();
    }
    
    document.body.classList.remove('modal-open');
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', initializeScheduleManagement);

// Global functions for onclick handlers
window.loadRoomsByBranch = loadRoomsByBranch;
window.saveSchedule = saveSchedule;
window.editSchedule = editSchedule;
window.deleteSchedule = deleteSchedule;
window.closeScheduleModal = closeScheduleModal;