// Capa de servicios: la única que habla con el backend (HTTP + JSON)

const BASE = '/api/v1'

function getToken(): string | null {
  return localStorage.getItem('edufast_token')
}

function authHeaders(): Record<string, string> {
  return { Authorization: `Bearer ${getToken()}` }
}

// Si el backend responde 401 (token vencido o inválido), la sesión ya no sirve:
// se limpia y se vuelve al login en vez de mostrar errores genéricos.
function handleUnauthorized(res: Response): void {
  if (res.status === 401) {
    logout()
    window.location.reload()
  }
}

export interface LoginResponse {
  token: string
  userId: number
  name: string
  email: string
  role: string
  roleScope?: string | null
  educationLevelId?: number | null
  supervisorUserId?: number | null
}

export interface Section {
  id: number
  nombre: string
  grado: string
  descripcion: string
}

export interface Student {
  id: number
  name: string
  code: string
}

export interface AttendanceEntry {
  studentId: number
  present: boolean
}

export interface AttendanceRecord {
  studentId: number
  studentName: string
  date: string
  present: boolean
}

export async function login(email: string, password: string): Promise<LoginResponse> {
  const res = await fetch(`${BASE}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  })
  if (!res.ok) throw new Error('Credenciales inválidas')
  const data = (await res.json()) as LoginResponse
  localStorage.setItem('edufast_token', data.token)
  localStorage.setItem('edufast_name', data.name)
  return data
}

export function logout(): void {
  localStorage.removeItem('edufast_token')
  localStorage.removeItem('edufast_name')
}

export function loggedName(): string {
  return localStorage.getItem('edufast_name') ?? ''
}

export async function getMySections(): Promise<Section[]> {
  const res = await fetch(`${BASE}/sections/me`, { headers: authHeaders() })
  handleUnauthorized(res)
  if (!res.ok) throw new Error('Error al cargar secciones')
  return res.json()
}

export async function getSectionStudents(sectionId: number): Promise<Student[]> {
  const res = await fetch(`${BASE}/sections/${sectionId}/students`, { headers: authHeaders() })
  handleUnauthorized(res)
  if (!res.ok) throw new Error('Error al cargar alumnos')
  return res.json()
}

export async function saveAttendance(
  sectionId: number,
  date: string,
  attendance: AttendanceEntry[],
): Promise<AttendanceRecord[]> {
  const res = await fetch(`${BASE}/sections/${sectionId}/attendance`, {
    method: 'POST',
    headers: { ...authHeaders(), 'Content-Type': 'application/json' },
    body: JSON.stringify({ date, attendance }),
  })
  handleUnauthorized(res)
  if (!res.ok) throw new Error('Error al guardar asistencia')
  return res.json()
}

export async function getAttendance(sectionId: number, date: string): Promise<AttendanceRecord[]> {
  const res = await fetch(`${BASE}/sections/${sectionId}/attendance?date=${date}`, {
    headers: authHeaders(),
  })
  handleUnauthorized(res)
  if (!res.ok) throw new Error('Error al consultar asistencia')
  return res.json()
}
