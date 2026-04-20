/* ═══════════════════════════════════════════════════════════════════════════
 *  MediCore API Client  —  api.js
 *  All fetch() calls to the Spring Boot backend (localhost:8080)
 *  Responses always follow:  { success, message, data }
 * ═══════════════════════════════════════════════════════════════════════════ */

const API_BASE = 'http://localhost:8080/api';

/* ─── Internal helper ────────────────────────────────────────────────────── */
async function _request(method, path, body = null) {
  const opts = {
    method,
    headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
  };
  if (body) opts.body = JSON.stringify(body);

  try {
    const res = await fetch(`${API_BASE}${path}`, opts);
    const json = await res.json();

    if (!res.ok) {
      // Use server error message if present
      const msg = json?.message || `Server error ${res.status}`;
      throw new Error(msg);
    }
    return json.data;          // unwrap the ApiResponse wrapper
  } catch (err) {
    if (err.message === 'Failed to fetch') {
      showToast('⚠️ Cannot reach server — is the backend running?', 'error', 5000);
    } else {
      showToast(err.message, 'error');
    }
    throw err;
  }
}

const get    = (path)         => _request('GET',    path);
const post   = (path, body)   => _request('POST',   path, body);
const put    = (path, body)   => _request('PUT',    path, body);
const patch  = (path, body)   => _request('PATCH',  path, body);
const del    = (path)         => _request('DELETE', path);


/* ═══════════════════════════════════════════════════════════════════════════
 *  PATIENTS    /api/patients
 * ═══════════════════════════════════════════════════════════════════════════ */
const Patients = {
  getAll:           ()          => get('/patients'),
  getById:          (id)        => get(`/patients/${id}`),
  search:           (q)         => get(`/patients/search?q=${encodeURIComponent(q)}`),
  pendingBills:     ()          => get('/patients/pending-bills'),
  create:           (data)      => post('/patients', data),
  update:           (id, data)  => put(`/patients/${id}`, data),
  delete:           (id)        => del(`/patients/${id}`),
};

/* ═══════════════════════════════════════════════════════════════════════════
 *  DOCTORS     /api/doctors
 * ═══════════════════════════════════════════════════════════════════════════ */
const Doctors = {
  getAll:           ()          => get('/doctors'),
  getById:          (id)        => get(`/doctors/${id}`),
  search:           (name)      => get(`/doctors/search?name=${encodeURIComponent(name)}`),
  byDepartment:     (deptId)    => get(`/doctors/department/${deptId}`),
  topSalary:        ()          => get('/doctors/top-salary'),
  create:           (data)      => post('/doctors', data),
  update:           (id, data)  => put(`/doctors/${id}`, data),
  delete:           (id)        => del(`/doctors/${id}`),
};

/* ═══════════════════════════════════════════════════════════════════════════
 *  APPOINTMENTS  /api/appointments
 * ═══════════════════════════════════════════════════════════════════════════ */
const Appointments = {
  getAll:           ()          => get('/appointments'),
  getById:          (id)        => get(`/appointments/${id}`),
  today:            ()          => get('/appointments/today'),
  byDate:           (date)      => get(`/appointments/date?date=${date}`),
  byPatient:        (pid)       => get(`/appointments/patient/${pid}`),
  byDoctor:         (did)       => get(`/appointments/doctor/${did}`),
  book:             (data)      => post('/appointments', data),
  updateStatus:     (id, status)=> patch(`/appointments/${id}/status?status=${encodeURIComponent(status)}`),
  cancel:           (id)        => del(`/appointments/${id}/cancel`),
};

/* ═══════════════════════════════════════════════════════════════════════════
 *  BILLING     /api/billing
 * ═══════════════════════════════════════════════════════════════════════════ */
const Billing = {
  getAll:           ()          => get('/billing'),
  getById:          (id)        => get(`/billing/${id}`),
  byPatient:        (pid)       => get(`/billing/patient/${pid}`),
  pending:          ()          => get('/billing/pending'),
  generate:         (data)      => post('/billing', data),
  recordPayment:    (id, amount)=> patch(`/billing/${id}/payment?amount=${amount}`),
};

/* ═══════════════════════════════════════════════════════════════════════════
 *  DOM Rendering helpers — used by each page
 * ═══════════════════════════════════════════════════════════════════════════ */

/** Renders a "Loading…" skeleton row into any <tbody> */
function renderLoading(tbodyId, colSpan = 8) {
  const tbody = document.getElementById(tbodyId);
  if (!tbody) return;
  tbody.innerHTML = `
    <tr><td colspan="${colSpan}" style="text-align:center; padding:40px; color:var(--text-3);">
      <div style="font-size:28px; margin-bottom:8px;">⏳</div>
      <div style="font-size:14px;">Loading from server…</div>
    </td></tr>`;
}

/** Renders an error row when the API is unreachable */
function renderError(tbodyId, colSpan = 8) {
  const tbody = document.getElementById(tbodyId);
  if (!tbody) return;
  tbody.innerHTML = `
    <tr><td colspan="${colSpan}" style="text-align:center; padding:40px; color:var(--rose);">
      <div style="font-size:28px; margin-bottom:8px;">🔌</div>
      <div style="font-size:14px; font-weight:600;">Backend offline</div>
      <div style="font-size:12px; color:var(--text-3); margin-top:4px;">
        Run <code style="background:var(--bg-2); padding:2px 6px; border-radius:4px;">mvn spring-boot:run</code> then refresh.
      </div>
    </td></tr>`;
}

/** Empty state helper */
function renderEmpty(tbodyId, colSpan = 8, label = 'No records found') {
  const tbody = document.getElementById(tbodyId);
  if (!tbody) return;
  tbody.innerHTML = `
    <tr><td colspan="${colSpan}" style="text-align:center; padding:40px; color:var(--text-3);">
      <div style="font-size:32px; margin-bottom:8px;">📭</div>
      <div style="font-size:14px; font-weight:500;">${label}</div>
    </td></tr>`;
}

/** Badge helper */
function statusBadge(status) {
  const map = {
    'Admitted':    'badge-blue',
    'Discharged':  'badge-green',
    'Outpatient':  'badge-amber',
    'Completed':   'badge-teal',
    'Scheduled':   'badge-amber',
    'Cancelled':   'badge-rose',
    'Paid':        'badge-green',
    'Pending':     'badge-amber',
    'Partial':     'badge-blue',
  };
  return `<span class="badge ${map[status] || 'badge-muted'}">${status}</span>`;
}
