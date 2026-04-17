/* ── HMS Shared Script ── */

/* ─── Toast notifications ─── */
function showToast(message, type = 'info', duration = 3200) {
  let container = document.querySelector('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }
  const icons = { success: '✅', error: '❌', info: 'ℹ️' };
  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `<span>${icons[type]}</span><span>${message}</span>`;
  container.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(20px)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, duration);
}

/* ─── Modal helpers ─── */
function openModal(id) {
  const overlay = document.getElementById(id);
  if (overlay) overlay.classList.add('open');
}
function closeModal(id) {
  const overlay = document.getElementById(id);
  if (overlay) overlay.classList.remove('open');
}
// Close on backdrop click
document.addEventListener('click', (e) => {
  if (e.target.classList.contains('modal-overlay')) {
    e.target.classList.remove('open');
  }
});
// Close on Escape
document.addEventListener('keydown', (e) => {
  if (e.key === 'Escape') {
    document.querySelectorAll('.modal-overlay.open').forEach(m => m.classList.remove('open'));
  }
});

/* ─── Sidebar mobile toggle ─── */
function toggleSidebar() {
  document.querySelector('.sidebar')?.classList.toggle('open');
}

/* ─── Filter buttons ─── */
document.addEventListener('DOMContentLoaded', () => {
  // Filter buttons
  document.querySelectorAll('.filter-btn[data-filter]').forEach(btn => {
    btn.addEventListener('click', () => {
      const group = btn.closest('.filter-bar');
      group.querySelectorAll('.filter-btn[data-filter]').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      filterTable(btn.dataset.filter);
    });
  });

  // Live search
  document.querySelectorAll('.filter-search input').forEach(input => {
    input.addEventListener('input', () => searchTable(input.value));
  });

  // Animate stat values
  document.querySelectorAll('[data-count]').forEach(el => {
    const target = parseInt(el.dataset.count);
    animateCount(el, target);
  });

  // Stagger card animations
  document.querySelectorAll('.stat-card, .doctor-card').forEach((el, i) => {
    el.style.animationDelay = `${i * 0.07}s`;
  });
});

/* ─── Count-up animation ─── */
function animateCount(el, target, duration = 1200) {
  const start = Date.now();
  const tick = () => {
    const elapsed = Date.now() - start;
    const progress = Math.min(elapsed / duration, 1);
    const ease = 1 - Math.pow(1 - progress, 3);
    el.textContent = Math.round(ease * target).toLocaleString();
    if (progress < 1) requestAnimationFrame(tick);
  };
  requestAnimationFrame(tick);
}

/* ─── Table filter ─── */
function filterTable(filter) {
  const rows = document.querySelectorAll('tbody tr[data-category]');
  rows.forEach(row => {
    if (filter === 'all' || row.dataset.category === filter) {
      row.style.display = '';
    } else {
      row.style.display = 'none';
    }
  });
  updateRowCount();
}

function searchTable(query) {
  const q = query.toLowerCase();
  const rows = document.querySelectorAll('tbody tr');
  rows.forEach(row => {
    row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
  });
  updateRowCount();
}

function updateRowCount() {
  const countEl = document.querySelector('[data-row-count]');
  if (!countEl) return;
  const visible = document.querySelectorAll('tbody tr:not([style*="none"])').length;
  countEl.textContent = visible;
}

/* ─── Form validation helper ─── */
function validateForm(formId) {
  const form = document.getElementById(formId);
  if (!form) return true;
  let valid = true;
  form.querySelectorAll('[required]').forEach(field => {
    if (!field.value.trim()) {
      field.style.borderColor = 'var(--rose)';
      valid = false;
      field.addEventListener('input', () => {
        field.style.borderColor = '';
      }, { once: true });
    }
  });
  if (!valid) showToast('Please fill in all required fields.', 'error');
  return valid;
}

/* ─── Ripple effect on buttons ─── */
document.addEventListener('click', (e) => {
  const btn = e.target.closest('.btn');
  if (!btn) return;
  const ripple = document.createElement('span');
  const rect = btn.getBoundingClientRect();
  const size = Math.max(rect.width, rect.height);
  ripple.style.cssText = `
    position:absolute; border-radius:50%;
    width:${size}px; height:${size}px;
    left:${e.clientX - rect.left - size/2}px;
    top:${e.clientY - rect.top - size/2}px;
    background:rgba(255,255,255,0.15);
    transform:scale(0); animation:ripple 0.5s ease;
    pointer-events:none;
  `;
  if (!btn.style.position || btn.style.position === 'static') btn.style.position = 'relative';
  btn.style.overflow = 'hidden';
  btn.appendChild(ripple);
  setTimeout(() => ripple.remove(), 500);
});
const rippleStyle = document.createElement('style');
rippleStyle.textContent = '@keyframes ripple { to { transform: scale(2.5); opacity: 0; } }';
document.head.appendChild(rippleStyle);

/* ─── Active nav link highlight ─── */
const currentPage = window.location.pathname.split('/').pop() || 'index.html';
document.querySelectorAll('.nav-link').forEach(link => {
  if (link.getAttribute('href') === currentPage) {
    link.classList.add('active');
  }
});

/* ─── Confirm delete dialog ─── */
function confirmDelete(name, callback) {
  if (confirm(`Delete "${name}"? This action cannot be undone.`)) {
    callback();
  }
}