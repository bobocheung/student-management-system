// 學生信息管理系統 - 主要JavaScript文件

document.addEventListener('DOMContentLoaded', function() {
    // 初始化所有功能
    initializeTooltips();
    initializeConfirmDialogs();
    initializeFormValidation();
    initializeTableFeatures();
    initializeNotifications();
});

// 初始化Bootstrap工具提示
function initializeTooltips() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// 初始化確認對話框
function initializeConfirmDialogs() {
    document.querySelectorAll('.confirm-delete').forEach(function(element) {
        element.addEventListener('click', function(e) {
            e.preventDefault();
            const url = this.href;
            const itemName = this.dataset.itemName || '此項目';
            
            if (confirm(`確定要刪除 ${itemName} 嗎？此操作無法撤銷。`)) {
                window.location.href = url;
            }
        });
    });
}

// 初始化表單驗證
function initializeFormValidation() {
    // Bootstrap表單驗證
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
    
    // 自定義驗證規則
    const phoneInputs = document.querySelectorAll('input[type="tel"]');
    phoneInputs.forEach(input => {
        input.addEventListener('input', function() {
            const phoneRegex = /^[0-9]{10,11}$/;
            if (this.value && !phoneRegex.test(this.value)) {
                this.setCustomValidity('請輸入有效的電話號碼');
            } else {
                this.setCustomValidity('');
            }
        });
    });
    
    // 郵箱驗證
    const emailInputs = document.querySelectorAll('input[type="email"]');
    emailInputs.forEach(input => {
        input.addEventListener('input', function() {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (this.value && !emailRegex.test(this.value)) {
                this.setCustomValidity('請輸入有效的郵箱地址');
            } else {
                this.setCustomValidity('');
            }
        });
    });
}

// 初始化表格功能
function initializeTableFeatures() {
    // 表格搜索功能
    const searchInputs = document.querySelectorAll('.table-search');
    searchInputs.forEach(input => {
        input.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const table = document.querySelector(this.dataset.target);
            const rows = table.querySelectorAll('tbody tr');
            
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(searchTerm) ? '' : 'none';
            });
        });
    });
    
    // 表格排序功能
    const sortableHeaders = document.querySelectorAll('.sortable');
    sortableHeaders.forEach(header => {
        header.addEventListener('click', function() {
            const table = this.closest('table');
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.querySelectorAll('tr'));
            const column = Array.from(this.parentNode.children).indexOf(this);
            const isAscending = this.classList.contains('asc');
            
            // 清除其他列的排序標記
            this.parentNode.querySelectorAll('.sortable').forEach(h => {
                h.classList.remove('asc', 'desc');
            });
            
            // 設置當前列的排序標記
            this.classList.add(isAscending ? 'desc' : 'asc');
            
            // 排序行
            rows.sort((a, b) => {
                const aValue = a.children[column].textContent.trim();
                const bValue = b.children[column].textContent.trim();
                
                // 數字排序
                if (!isNaN(aValue) && !isNaN(bValue)) {
                    return isAscending ? bValue - aValue : aValue - bValue;
                }
                
                // 文本排序
                return isAscending ? 
                    bValue.localeCompare(aValue) : 
                    aValue.localeCompare(bValue);
            });
            
            // 重新插入排序後的行
            rows.forEach(row => tbody.appendChild(row));
        });
    });
}

// 初始化通知系統
function initializeNotifications() {
    // 自動隱藏警告框
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });
}

// 顯示加載動畫
function showLoading(element) {
    const originalText = element.textContent;
    element.innerHTML = '<span class="loading-spinner"></span> 處理中...';
    element.disabled = true;
    
    return function hideLoading() {
        element.textContent = originalText;
        element.disabled = false;
    };
}

// 顯示成功消息
function showSuccessMessage(message) {
    showNotification(message, 'success');
}

// 顯示錯誤消息
function showErrorMessage(message) {
    showNotification(message, 'danger');
}

// 顯示通知
function showNotification(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(alertDiv);
    
    // 自動移除
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

// 文件上傳預覽
function previewImage(input, previewId) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const preview = document.getElementById(previewId);
            if (preview) {
                preview.src = e.target.result;
                preview.style.display = 'block';
            }
        };
        reader.readAsDataURL(input.files[0]);
    }
}

// 格式化日期
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
}

// 格式化時間
function formatTime(timeString) {
    const time = new Date(`2000-01-01T${timeString}`);
    return time.toLocaleTimeString('zh-TW', {
        hour: '2-digit',
        minute: '2-digit'
    });
}

// 導出表格為CSV
function exportTableToCSV(tableId, filename = 'export.csv') {
    const table = document.getElementById(tableId);
    const rows = table.querySelectorAll('tr');
    const csvContent = [];
    
    rows.forEach(row => {
        const cols = row.querySelectorAll('td, th');
        const rowData = Array.from(cols).map(col => {
            return '"' + col.textContent.replace(/"/g, '""') + '"';
        });
        csvContent.push(rowData.join(','));
    });
    
    const blob = new Blob([csvContent.join('\n')], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
}

// 打印頁面
function printPage() {
    window.print();
}

// AJAX請求輔助函數
async function makeRequest(url, options = {}) {
    try {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Request failed:', error);
        showErrorMessage('請求失敗，請稍後重試');
        throw error;
    }
}

// 工具函數：防抖
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// 工具函數：節流
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}