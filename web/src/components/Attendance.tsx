import { useEffect, useMemo, useState } from 'react'
import { getAttendance, getCourseStudents, saveAttendance } from '../services/api'
import type {
  AttendanceEntry,
  AttendanceRecord,
  Course,
  Student,
} from '../services/api'

interface Props {
  course: Course
  onBack: () => void
}

// Fecha de hoy en hora LOCAL (no UTC): evita que de noche aparezca el día equivocado
function today(): string {
  const d = new Date()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${d.getFullYear()}-${month}-${day}`
}

export default function Attendance({ course, onBack }: Props) {
  const [students, setStudents] = useState<Student[]>([])
  const [date, setDate] = useState(today())
  const [present, setPresent] = useState<Record<number, boolean>>({})
  const [saved, setSaved] = useState<AttendanceRecord[]>([])
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)

  // Carga los alumnos del curso una sola vez
  useEffect(() => {
    let cancelled = false
    getCourseStudents(course.id)
      .then((list) => {
        if (!cancelled) setStudents(list)
      })
      .catch(() => {
        if (!cancelled) setError('No se pudieron cargar los alumnos')
      })
    return () => {
      cancelled = true
    }
  }, [course.id])

  // Cada vez que cambian los alumnos o la fecha, carga la asistencia de ese día.
  // El flag "cancelled" evita que una respuesta lenta pise los cambios del usuario.
  useEffect(() => {
    if (students.length === 0) return
    let cancelled = false

    const allPresent = Object.fromEntries(students.map((s) => [s.id, true]))

    getAttendance(course.id, date)
      .then((records) => {
        if (cancelled) return
        setSaved(records)
        setPresent(
          records.length > 0
            ? Object.fromEntries(records.map((r) => [r.studentId, r.present]))
            : allPresent,
        )
      })
      .catch(() => {
        if (cancelled) return
        setSaved([])
        setPresent(allPresent)
      })
      .finally(() => {
        if (!cancelled) setLoading(false)
      })

    return () => {
      cancelled = true
    }
  }, [course.id, date, students])

  const allPresent = useMemo(
    () => students.length > 0 && students.every((s) => present[s.id]),
    [students, present],
  )

  function toggleAll(value: boolean) {
    setPresent(Object.fromEntries(students.map((s) => [s.id, value])))
  }

  async function handleSave() {
    setSaving(true)
    setError('')
    setMessage('')
    const attendance: AttendanceEntry[] = students.map((s) => ({
      studentId: s.id,
      present: present[s.id] ?? true,
    }))
    try {
      const records = await saveAttendance(course.id, date, attendance)
      setSaved(records)
      setMessage(`Asistencia del ${date} guardada (${records.filter((r) => r.present).length} presentes)`)
    } catch {
      setError('No se pudo guardar la asistencia')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div>
      <div className="topbar">
        <button className="link" onClick={onBack}>
          ← Mis cursos
        </button>
        <h2>{course.name}</h2>
      </div>

      <div className="controls">
        <label>
          Fecha:
          <input
            type="date"
            value={date}
            onChange={(e) => {
              setDate(e.target.value)
              setLoading(true)
            }}
          />
        </label>
        <button onClick={() => toggleAll(true)} disabled={loading}>
          Todos presentes
        </button>
        <button onClick={() => toggleAll(false)} disabled={loading}>
          Todos ausentes
        </button>
      </div>

      <table className="attendance-table">
        <thead>
          <tr>
            <th>Alumno</th>
            <th>Presente</th>
          </tr>
        </thead>
        <tbody>
          {students.map((s) => (
            <tr key={s.id}>
              <td>
                {s.name} <small>{s.code}</small>
              </td>
              <td>
                <input
                  type="checkbox"
                  checked={present[s.id] ?? true}
                  disabled={loading}
                  onChange={(e) => setPresent((p) => ({ ...p, [s.id]: e.target.checked }))}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {message && <p className="ok">{message}</p>}
      {error && <p className="error">{error}</p>}

      <div className="actions">
        <button className="primary" onClick={handleSave} disabled={loading || saving}>
          {saving ? 'Guardando...' : 'Guardar asistencia'}
        </button>
      </div>

      {saved.length > 0 && (
        <div className="summary">
          <h3>Resumen del {date}</h3>
          <p>
            Presentes: <strong>{saved.filter((r) => r.present).length}</strong> · Ausentes:{' '}
            <strong>{saved.filter((r) => !r.present).length}</strong> {allPresent && '(todos)'}
          </p>
        </div>
      )}
    </div>
  )
}
