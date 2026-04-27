/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  api.js  —  MediCore HMS Frontend API Client
 *
 *  Single source of truth for all backend calls.
 *  Every function returns a Promise that resolves to the `data` field
 *  from the ApiResponse envelope:  { success, message, data }
 *
 *  Usage in any page:
 *    import { PatientAPI } from './api.js';
 *    const patients = await PatientAPI.getAll();
 * ─────────────────────────────────────────────────────────────────────────────
 */

const BASE_URL = 'http://localhost:8080/api';

// ── Core fetch wrapper ────────────────────────────────────────────────────────
async function request(method, path, body = null) {
  const options = {
    method,
    headers: { 'Content-Type': 'application/json' },
  };
  if (body) options.body = JSON.stringify(body);

  const res = await fetch(`${BASE_URL}${path}`, options);
  const json = await res.json();

  if (!res.ok) {
    // json is an ApiError { status, error, message, fieldErrors }
    const msg = json.fieldErrors
      ? Object.values(json.fieldErrors).join(' · ')
      : json.message || 'Something went wrong.';
    throw new Error(msg);
  }

  return json.data;   // unwrap the ApiResponse envelope
}

// ── Patient API ───────────────────────────────────────────────────────────────
export const PatientAPI = {
  getAll:            ()       => request('GET',  '/patients'),
  getById:           (id)     => request('GET',  `/patients/${id}`),
  search:            (q)      => request('GET',  `/patients/search?q=${encodeURIComponent(q)}`),
  getPendingBills:   ()       => request('GET',  '/patients/pending-bills'),
  create:            (data)   => request('POST', '/patients', data),
  update:            (id, data)=> request('PUT', `/patients/${id}`, data),
  delete:            (id)     => request('DELETE',`/patients/${id}`),
};

// ── Doctor API ────────────────────────────────────────────────────────────────
export const DoctorAPI = {
  getAll:            ()       => request('GET',  '/doctors'),
  getById:           (id)     => request('GET',  `/doctors/${id}`),
  search:            (name)   => request('GET',  `/doctors/search?name=${encodeURIComponent(name)}`),
  getByDepartment:   (deptId) => request('GET',  `/doctors/department/${deptId}`),
  getTopSalary:      ()       => request('GET',  '/doctors/top-salary'),
  create:            (data)   => request('POST', '/doctors', data),
  update:            (id, data)=> request('PUT', `/doctors/${id}`, data),
  delete:            (id)     => request('DELETE',`/doctors/${id}`),
};

// ── Department API ────────────────────────────────────────────────────────────
export const DepartmentAPI = {
  getAll:            ()       => request('GET',  '/departments'),
  getById:           (id)     => request('GET',  `/departments/${id}`),
  create:            (data)   => request('POST', '/departments', data),
  update:            (id, data)=> request('PUT', `/departments/${id}`, data),
  delete:            (id)     => request('DELETE',`/departments/${id}`),
};

// ── Appointment API ───────────────────────────────────────────────────────────
export const AppointmentAPI = {
  getAll:            ()       => request('GET',  '/appointments'),
  getById:           (id)     => request('GET',  `/appointments/${id}`),
  getToday:          ()       => request('GET',  '/appointments/today'),
  getByDate:         (date)   => request('GET',  `/appointments/date?date=${date}`),
  getByPatient:      (pid)    => request('GET',  `/appointments/patient/${pid}`),
  getByDoctor:       (did)    => request('GET',  `/appointments/doctor/${did}`),
  create:            (data)   => request('POST', '/appointments', data),
  updateStatus:      (id, status)=> request('PATCH',`/appointments/${id}/status?status=${status}`),
  cancel:            (id)     => request('DELETE',`/appointments/${id}/cancel`),
};

// ── Admission API ─────────────────────────────────────────────────────────────
export const AdmissionAPI = {
  getAll:            ()       => request('GET',  '/admissions'),
  getById:           (id)     => request('GET',  `/admissions/${id}`),
  getCurrent:        ()       => request('GET',  '/admissions/current'),
  getByPatient:      (pid)    => request('GET',  `/admissions/patient/${pid}`),
  admit:             (data)   => request('POST', '/admissions', data),
  discharge:         (id, date, charges) =>
                               request('PATCH', `/admissions/${id}/discharge?dischargeDate=${date}&totalCharges=${charges}`),
};

// ── Medical Record API ────────────────────────────────────────────────────────
export const MedicalRecordAPI = {
  getAll:            ()       => request('GET',  '/medical-records'),
  getById:           (id)     => request('GET',  `/medical-records/${id}`),
  getByPatient:      (pid)    => request('GET',  `/medical-records/patient/${pid}`),
  getByAdmission:    (aid)    => request('GET',  `/medical-records/admission/${aid}`),
  create:            (data)   => request('POST', '/medical-records', data),
  update:            (id, data)=> request('PUT', `/medical-records/${id}`, data),
  delete:            (id)     => request('DELETE',`/medical-records/${id}`),
};

// ── Medicine API ──────────────────────────────────────────────────────────────
export const MedicineAPI = {
  getAll:            ()       => request('GET',  '/medicines'),
  getById:           (id)     => request('GET',  `/medicines/${id}`),
  search:            (q)      => request('GET',  `/medicines/search?q=${encodeURIComponent(q)}`),
  getLowStock:       ()       => request('GET',  '/medicines/low-stock'),
  getExpiringSoon:   ()       => request('GET',  '/medicines/expiring-soon'),
  create:            (data)   => request('POST', '/medicines', data),
  update:            (id, data)=> request('PUT', `/medicines/${id}`, data),
  restock:           (id, qty)=> request('PATCH',`/medicines/${id}/restock?qty=${qty}`),
  delete:            (id)     => request('DELETE',`/medicines/${id}`),
};

// ── Billing API ───────────────────────────────────────────────────────────────
export const BillingAPI = {
  getAll:            ()       => request('GET',  '/billing'),
  getById:           (id)     => request('GET',  `/billing/${id}`),
  getByPatient:      (pid)    => request('GET',  `/billing/patient/${pid}`),
  getPending:        ()       => request('GET',  '/billing/pending'),
  create:            (data)   => request('POST', '/billing', data),
  recordPayment:     (id, amt)=> request('PATCH',`/billing/${id}/payment?amount=${amt}`),
};

// ── Utility helpers used by HTML pages ────────────────────────────────────────

/** Show a toast notification (reused by all pages) */
export function showToast(msg, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'fixed bottom-6 right-6 z-[100] flex flex-col gap-2';
    document.body.appendChild(container);
  }
  const icons   = { success: 'check_circle', error: 'error', info: 'info' };
  const colors  = { success: 'text-secondary', error: 'text-error', info: 'text-primary' };
  const t = document.createElement('div');
  t.className = 'bg-white border border-outline-variant rounded-xl px-4 py-3 flex items-center gap-3 shadow-md min-w-[260px] text-[13px]';
  t.innerHTML = `<span class="material-symbols-outlined icon-fill ${colors[type]} text-[20px]">${icons[type]}</span><span class="text-on-surface font-medium flex-1">${msg}</span><button onclick="this.parentElement.remove()" class="text-outline hover:text-on-surface"><span class="material-symbols-outlined text-[18px]">close</span></button>`;
  container.appendChild(t);
  setTimeout(() => { t.style.opacity = '0'; t.style.transition = 'opacity 0.3s'; setTimeout(() => t.remove(), 300); }, 3500);
}

/** Show an inline loading spinner inside a container */
export function showLoading(containerId) {
  const el = document.getElementById(containerId);
  if (el) el.innerHTML = `
    <tr><td colspan="10" class="py-12 text-center">
      <div class="flex flex-col items-center gap-3">
        <div class="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
        <span class="text-[13px] text-outline">Loading data...</span>
      </div>
    </td></tr>`;
}

/** Show an empty state inside a table body */
export function showEmpty(containerId, message = 'No records found.') {
  const el = document.getElementById(containerId);
  if (el) el.innerHTML = `
    <tr><td colspan="10" class="py-12 text-center">
      <div class="flex flex-col items-center gap-2">
        <span class="material-symbols-outlined text-outline-variant text-[40px]">inbox</span>
        <span class="text-[13px] text-outline">${message}</span>
      </div>
    </td></tr>`;
}

/** Read form field values into a plain object */
export function getFormData(formId) {
  const form = document.getElementById(formId);
  const data = {};
  form.querySelectorAll('[name]').forEach(field => {
    data[field.name] = field.value.trim() || null;
  });
  return data;
}

/** Clear all fields in a form */
export function clearForm(formId) {
  document.getElementById(formId)?.reset();
}
